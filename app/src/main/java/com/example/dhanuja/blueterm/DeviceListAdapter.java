package com.example.dhanuja.blueterm;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DeviceListAdapter extends ArrayAdapter<DeviceData> {

    Context device;
    int resource;
    List<DeviceData> deviceDataList;

    public DeviceListAdapter(Context device ,int resource,List<DeviceData> deviceDataList){
        super(device ,resource,deviceDataList);

        this.device = device;
        this.resource=resource;
        this.deviceDataList = deviceDataList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(device);

        View view= inflater.inflate(R.layout.list_item,null);

        TextView name = view.findViewById(R.id.name);
        TextView address = view.findViewById(R.id.address);

        DeviceData deviceData= deviceDataList.get(position);

        name.setText(deviceData.getName());
        address.setText(deviceData.getAddress());

/*
        if(chatMessage.getMessageUser().toString()==FirebaseAuth.getInstance().getCurrentUser().getEmail()){
            message_user.setBackgroundResource(R.drawable.button_shape);
            message_user.setTextColor(Color.WHITE);
            message_user.setText("You");
        }
        else if(chatMessage.getMessageUser().toString()!=FirebaseAuth.getInstance().getCurrentUser().getEmail()){
            message_user.setText(chatMessage.getMessageUser());
        }
*/
        /*message_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(chat, "History Page is not yet Created", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(chat,UserProfileActivity.class).putExtra("message_user",chatMessage.getMessageUser());
                chat.startActivity(intent);
            }
        });*/

        return view;
    }

}

