package com.example.androidbluetooth;

import java.util.Timer;
import java.util.TimerTask;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class AndroidBluetooth extends ActionBarActivity {

	private static final int REQUEST_ENABLE_BT = 1;
	 
    ListView listDevicesFound;
 Button btnScanDevice;
 TextView stateBluetooth;
 BluetoothAdapter bluetoothAdapter;
 
 ArrayAdapter<String> btArrayAdapter;
 
 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_bluetooth);
        
        btnScanDevice = (Button)findViewById(R.id.scandevice);
        
        stateBluetooth = (TextView)findViewById(R.id.bluetoothstate);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        
        listDevicesFound = (ListView)findViewById(R.id.devicesfound);
        btArrayAdapter = new ArrayAdapter<String>(AndroidBluetooth.this, android.R.layout.simple_list_item_1);
        listDevicesFound.setAdapter(btArrayAdapter);
        
        CheckBlueToothState();
        
        btnScanDevice.setOnClickListener(btnScanDeviceOnClickListener);

        registerReceiver(ActionFoundReceiver, 
          new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }
    
    @Override
 protected void onDestroy() {
  // TODO Auto-generated method stub
  super.onDestroy();
  unregisterReceiver(ActionFoundReceiver);
 }

 private void CheckBlueToothState(){
     if (bluetoothAdapter == null){
         stateBluetooth.setText("Bluetooth NOT support");
        }else{
         if (bluetoothAdapter.isEnabled()){
          if(bluetoothAdapter.isDiscovering()){
           stateBluetooth.setText("Bluetooth is currently in device discovery process.");
          }else{
           stateBluetooth.setText("Bluetooth is Enabled.");
           btnScanDevice.setEnabled(true);
          }
         }else{
          stateBluetooth.setText("Bluetooth is NOT Enabled!");
          Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
             startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
         }
        }
    }
    
    private Button.OnClickListener btnScanDeviceOnClickListener
    = new Button.OnClickListener(){

  @Override
  public void onClick(View arg0) {
   // TODO Auto-generated method stub

   btArrayAdapter.clear();
   bluetoothAdapter.startDiscovery();
  }};

 @Override
 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  // TODO Auto-generated method stub
  if(requestCode == REQUEST_ENABLE_BT){
   CheckBlueToothState();
  }
 }
    
 private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver(){

  @Override
  public void onReceive(Context context, Intent intent) {
   // TODO Auto-generated method stub
   String action = intent.getAction();
   if(BluetoothDevice.ACTION_FOUND.equals(action)) {
      	   
             BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
             short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
             btArrayAdapter.add("Name: "+device.getName() + "\n" +"Address: "+ device.getAddress() + "\n" +"rssi: "+ rssi +" dBm");
             btArrayAdapter.notifyDataSetChanged();
         }
   
   TimerTask task = new TimerTask(){  
	    public void run(){  
	    //execute the task   
	    	   btArrayAdapter.clear();
	    	   bluetoothAdapter.startDiscovery();
	    }  
	};  
	Timer timer = new Timer();
	timer.schedule(task, 1000);
  }};
  
  

}
