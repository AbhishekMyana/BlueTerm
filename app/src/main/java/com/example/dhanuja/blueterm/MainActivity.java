package com.example.dhanuja.blueterm;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.ParcelUuid;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    private ListView pairedDevicesListView, newDevicesListView;
    private ArrayList<DeviceData> dataList= new ArrayList<DeviceData>();
    private ArrayList<BluetoothDevice> pairedDevices=new ArrayList<BluetoothDevice>();
    private BluetoothAdapter bluetoothAdapter;
    BluetoothDevice device;
    private ArrayAdapter  newDeviceAdapter;
    private DeviceListAdapter pairedDeviceAdapter;
    public static String DEVICE_ADDRESS = "device_address";
    private IntentFilter filter;
    private OutputStream outputStream;
    private InputStream inStream;
    private EditText message;
    private Button send;

    private BroadcastReceiver  broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.e("action", action);
//            Toast.makeText(DeviceListActivity.this, action, Toast.LENGTH_SHORT).show();

            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    newDeviceAdapter.add(device.getName() + "\n" + device.getAddress());
                    pairedDevices.add(device);
                    newDeviceAdapter.notifyDataSetChanged();

                }
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                if (newDeviceAdapter.getCount() == 0) {
                    Toast.makeText(MainActivity.this, "No devices found", Toast.LENGTH_SHORT).show();
                    newDeviceAdapter.add("No new device found");
                    newDeviceAdapter.notifyDataSetChanged();
                }
            }

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        pairedDevicesListView = (ListView) findViewById(R.id.paired_device);
        newDevicesListView=(ListView)findViewById(R.id.active_device);
        message = (EditText)findViewById(R.id.message);
        send=(Button)findViewById(R.id.send);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        pairedDeviceAdapter = new DeviceListAdapter(MainActivity.this,1,dataList);
        pairedDevicesListView.setAdapter(pairedDeviceAdapter);
        pairedDeviceAdapter.notifyDataSetChanged();
        //-----------------------------------------------
        newDeviceAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        newDevicesListView.setAdapter(newDeviceAdapter);
        newDeviceAdapter.notifyDataSetChanged();

        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
        // get paired devices
        Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
        if(pairedDevice.size()>0)
        {
//          pairedDeviceAdapter.clear();
            for(BluetoothDevice device : pairedDevice)
            {
//              pairedDeviceAdapter.add(device.getName()+ "\n" +device.getAddress());
                dataList.add(new DeviceData(device.getName(),device.getAddress()));
                pairedDevices.add(device);
            }
            pairedDeviceAdapter.notifyDataSetChanged();
        }


        // register broadcast receiver
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(broadcastReceiver, filter);



        pairedDevicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bluetoothAdapter.cancelDiscovery();
                String data = ((TextView) view).getText().toString();
                String address = data.substring(data.length() - 17);
                Intent intent = new Intent();
                intent.putExtra("device_address", address);
                intent.putExtra("info", data);
                setResult(Activity.RESULT_OK, intent);
                finish();

                try{

                    //BluetoothDevice device = (BluetoothDevice)pairedDevicesListView[position];
                    ParcelUuid[] uuids = device.getUuids();
                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                    socket.connect();
                    outputStream = socket.getOutputStream();
                    inStream = socket.getInputStream();

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });


        newDevicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bluetoothAdapter.cancelDiscovery();
                Boolean isBonded = false;
                try {
                    isBonded = createBond(device);
                    if(isBonded)
                    {
                        Log.i("Log","Paired");

//                    pairedDeviceAdapter.add(device.getName() + "\n" + device.getAddress());
                        dataList.add(new DeviceData(device.getName(),device.getAddress()));
                        newDeviceAdapter.remove(device.getName() + "\n" + device.getAddress());
                        pairedDeviceAdapter.notifyDataSetChanged();
                        newDeviceAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "paired to:" +device.getName(), Toast.LENGTH_SHORT).show();



                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text= message.getText().toString().trim();
                try{
                    write(text);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }
        this.unregisterReceiver(broadcastReceiver);
    }
    public boolean createBond(BluetoothDevice btDevice)
            throws Exception
    {
        Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
        Method createBondMethod = class1.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }


    /*private void init() throws IOException {
        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (blueAdapter != null) {
            if (blueAdapter.isEnabled()) {
                Set<BluetoothDevice> bondedDevices = blueAdapter.getBondedDevices();

                if(bondedDevices.size() > 0) {
                    Object[] devices = (Object []) bondedDevices.toArray();
                    //BluetoothDevice device = (BluetoothDevice) devices[position];
                    ParcelUuid[] uuids = device.getUuids();
                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                    socket.connect();
                    outputStream = socket.getOutputStream();
                    inStream = socket.getInputStream();
                }

                Log.e("error", "No appropriate paired devices.");
            } else {
                Log.e("error", "Bluetooth is disabled.");
            }
        }
    }*/

    public void write(String s) throws IOException {
        outputStream.write(s.getBytes());
    }

    public void run() {
        final int BUFFER_SIZE = 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytes = 0;
        int b = BUFFER_SIZE;

        while (true) {
            try {
                bytes = inStream.read(buffer, bytes, BUFFER_SIZE - bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
