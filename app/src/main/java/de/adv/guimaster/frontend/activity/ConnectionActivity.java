package de.adv.guimaster.frontend.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import de.adv.guimaster.R;
import de.adv.guimaster.api.SerialPort;
import de.adv.guimaster.frontend.logic.Constants;
import de.adv.guimaster.frontend.logic.DataHolder;
import de.adv.guimaster.frontend.uitools.ConnectionDialog;
import de.adv.guimaster.frontend.uitools.PermissionDialog;


public class ConnectionActivity extends AppCompatActivity {

    public DataHolder holder = DataHolder.getInstance();
    public SerialPort serialPort;
    public final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constants.USB_PERMISSION.equals(action)) {
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                    startApp();
                } else {
                    DialogFragment newFragment = PermissionDialog.newInstance();
                    newFragment.show(getSupportFragmentManager(),"dialog");
                }
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_connection);
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        serialPort = new SerialPort(manager);
        holder.save("SerialPort",serialPort);
        holder.save("ConnectionActivity",this);
        holder.save("BroadcastReceiver",broadcastReceiver);
        attachDevice();
    }


    public void attachDevice(){
        if(serialPort.deviceAttached(this)){
            initConnection();
        } else {
            DialogFragment newFragment = ConnectionDialog.newInstance(this);
            newFragment.show(getSupportFragmentManager(),"dialog");
        }
    }


    public void initConnection(){
        IntentFilter filter = new IntentFilter(Constants.USB_PERMISSION);
        registerReceiver(broadcastReceiver,filter);
        serialPort.requestPermission(this);
    }

    public void startApp(){
        unregisterReceiver(broadcastReceiver);
        Intent i1 = new Intent(ConnectionActivity.this, StartupActivity.class);
        ConnectionActivity.this.startActivity(i1);
    }

}
