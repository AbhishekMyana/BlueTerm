package com.example.dhanuja.blueterm;

public class SpareCode {
    /*

        int REQUEST_ENABLE_BT=1;
    private ListView paired_devices,active_devices;
    ArrayList<String> newlist= new ArrayList<String>();
    ArrayList<BluetoothDevice> deviceArrayList = new ArrayList<BluetoothDevice>();
    Set<BluetoothDevice> deviceList;

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e("action", action);

            if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                Toast.makeText(MainActivity.this,"Searching...",Toast.LENGTH_LONG).show();
            }

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Toast.makeText(MainActivity.this,deviceName,Toast.LENGTH_LONG).show();

                newlist.add(deviceName);
                deviceList.add(device);
                deviceArrayList.add(device);
            }
            else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                if (newlist.isEmpty()) {
                    Toast.makeText(MainActivity.this, "No devices found", Toast.LENGTH_SHORT).show();
                    newlist.add("No new device found");
                }
            }

            ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_list_item_1,
                    newlist);
            active_devices.setAdapter(adapter);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(MainActivity.this,"Your Device does not support Bluetooth",Toast.LENGTH_LONG).show();
            MainActivity.this.finish();
        }
        else{
            Toast.makeText(MainActivity.this,"Your Device supports Bluetooth",Toast.LENGTH_LONG).show();
        }


        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);



        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        paired_devices = (ListView)findViewById(R.id.paired_device);
        active_devices = (ListView)findViewById(R.id.active_device);


        ArrayList<String> list= new ArrayList<String>();


        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                list.add(deviceName);
                deviceList.add(device);
            }
        }

        final ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                list);

        paired_devices.setAdapter(adapter);

        int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        bluetoothAdapter.startDiscovery();
        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(receiver, filter);

        bluetoothAdapter.startDiscovery();

        active_devices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selecetedName = (String) adapter.getItem(i);
                int select=0;
                BluetoothDevice selectedDevice;

                for(BluetoothDevice device:deviceList){
                    if(selecetedName.equals(device.getName())){
                        try{
                            createBond(deviceArrayList.get(select));
                            Toast.makeText(MainActivity.this, "paired to:" +device.getName(), Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }


    public boolean createBond(BluetoothDevice btDevice)
            throws Exception
    {
        Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
        Method createBondMethod = class1.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

    int REQUEST_ENABLE_BT=1;
    private ListView paired_devices,active_devices;
    ArrayList<String> newlist= new ArrayList<String>();
    ArrayList<BluetoothDevice> deviceArrayList = new ArrayList<BluetoothDevice>();
    Set<BluetoothDevice> deviceList;

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e("action", action);

            if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                Toast.makeText(MainActivity.this,"Searching...",Toast.LENGTH_LONG).show();
            }

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Toast.makeText(MainActivity.this,deviceName,Toast.LENGTH_LONG).show();

                newlist.add(deviceName);
                deviceList.add(device);
                deviceArrayList.add(device);
            }
            else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                if (newlist.isEmpty()) {
                    Toast.makeText(MainActivity.this, "No devices found", Toast.LENGTH_SHORT).show();
                    newlist.add("No new device found");
                }
            }

            ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_list_item_1,
                    newlist);
            active_devices.setAdapter(adapter);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(MainActivity.this,"Your Device does not support Bluetooth",Toast.LENGTH_LONG).show();
            MainActivity.this.finish();
        }
        else{
            Toast.makeText(MainActivity.this,"Your Device supports Bluetooth",Toast.LENGTH_LONG).show();
        }


        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);



        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        paired_devices = (ListView)findViewById(R.id.paired_device);
        active_devices = (ListView)findViewById(R.id.active_device);


        ArrayList<String> list= new ArrayList<String>();


        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                list.add(deviceName);
                deviceList.add(device);
            }
        }

        final ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                list);

        paired_devices.setAdapter(adapter);

        int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        bluetoothAdapter.startDiscovery();
        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(receiver, filter);

        bluetoothAdapter.startDiscovery();

        active_devices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selecetedName = (String) adapter.getItem(i);
                int select=0;
                BluetoothDevice selectedDevice;

                for(BluetoothDevice device:deviceList){
                    if(selecetedName.equals(device.getName())){
                        try{
                            createBond(deviceArrayList.get(select));
                            Toast.makeText(MainActivity.this, "paired to:" +device.getName(), Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }


    public boolean createBond(BluetoothDevice btDevice)
            throws Exception
    {
        Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
        Method createBondMethod = class1.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }



    */
}
