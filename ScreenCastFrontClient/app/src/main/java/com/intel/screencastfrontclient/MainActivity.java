package com.intel.screencastfrontclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.IBinder;
import android.os.RemoteException;
import android.content.Context;
import java.util.regex.Pattern;

// import com.intel.screencastfrontclient.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // private ActivityMainBinding binding;
    private boolean state = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // mContext = getApplicationContext();
        // binding = ActivityMainBinding.inflate(getLayoutInflater());
        // setContentView(binding.getRoot());
        setContentView(R.layout.activity_main);
        // Example of a call to a native method
        // TextView tv = binding.sampleText;
        TextView tv = (TextView)findViewById(R.id.sample_text);
        tv.setText(CastJNILib.stringFromJNI());
        EditText et = (EditText)findViewById(R.id.edit_text_1);
        String[] texts = et.getText().toString().split(":");

        boolean isValid = IPAddressValidator.isValidIPAddress(texts[0]);
        if(! isValid)
            Log.e("IP", "Is not a valid ip address");

        int port = Integer.parseInt(texts[1]);
        if(port < 0 || port > 65535)
            Log.e("Port", "Is not a valid port num");

//        CastJNILib.init(texts[0],port);

        Button connect_btn = (Button) findViewById(R.id.btn_1);
        connect_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CastJNILib.init(texts[0],port);
                Log.d("BUTTONS", "User tapped the Supabutton");
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

                Log.d("BUTTONS", "User tapped the Supabutton");
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

    /**
     * A native method that is implemented by the 'screencastfrontclient' native library,
     * which is packaged with this application.
     */
}