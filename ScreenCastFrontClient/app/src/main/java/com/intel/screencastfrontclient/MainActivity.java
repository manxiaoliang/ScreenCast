package com.intel.screencastfrontclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.IBinder;
import android.os.ServiceManager;
import android.os.RemoteException;
import android.content.Context;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private boolean state = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        TextView tv = (TextView)findViewById(R.id.sample_text);
        tv.setText(CastJNILib.stringFromJNI());

        Button connect_btn = (Button) findViewById(R.id.btn_1);
        connect_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText et = (EditText)findViewById(R.id.edit_text_1);
                String text = et.getText().toString();
                boolean isValid = IPAddressValidator.isValidIPAddress(text);
                if(! isValid)
                    Log.e("IP", "Is not a valid ip address");
        
                int port = 6999;
                if(port < 0 || port > 65535)
                    Log.e("Port", "Is not a valid port num");
                
                CastJNILib.init(text,port);

            }
        });

        Button control_btn = (Button) findViewById(R.id.btn_2);
        control_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(state){
                    CastJNILib.startCast();
                    control_btn.setText("STOP CAST");
                }
                else{
                    CastJNILib.stopCast();
                    control_btn.setText("START CAST");
                }
                state = !state;
            }
        });
    }

    public static class IPAddressValidator {
        private static final String IP_ADDRESS_PATTERN =
                "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

        public static boolean isValidIPAddress(String ipAddress) {
            Pattern pattern = Pattern.compile(IP_ADDRESS_PATTERN);
            return pattern.matcher(ipAddress).matches();
        }
    }
}