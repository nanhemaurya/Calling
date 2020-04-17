package com.snmboy.calling;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ParseException;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.snmboy.calling.caller.IncomingCallReceiver;
import com.snmboy.calling.utils.Constt;
import com.snmboy.calling.utils.SharedPrefs;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_SIP_STATE = 1;
    public String sipAddress = null;
    public SipManager manager = null;
    public SipProfile sipProfile = null;
    public SipAudioCall call = null;
    public IncomingCallReceiver callReceiver;
    private Context context;
    private AppCompatButton dial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        dial = findViewById(R.id.dial);


        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.USE_SIP);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_SIP}, REQUEST_SIP_STATE);
        } else {
            //TODO
        }

        // Set up the intent filter.  This will be used to fire an
        // IncomingCallReceiver11 when someone calls the SIP address used by this
        // application.
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constt.CALL_UI_ACTION);
        callReceiver = new IncomingCallReceiver();
        this.registerReceiver(callReceiver, filter);



        if(SipManager.isVoipSupported(getApplicationContext())){
            System.out.println("VOIP: Supported!");
        }
        else{
            Log.d("VOIP:", "Not Supported");
        }


        if(SipManager.isApiSupported(getApplicationContext())){

            Toast.makeText(context,"API: Supported!",Toast.LENGTH_LONG).show();

        }
        else{
            Toast.makeText(context,"API: NotSupported!",Toast.LENGTH_LONG).show();
        }


        // "Push to talk" can be a serious pain when the screen keeps turning off.
        // Let's prevent that.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initializeManager();

        dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SipManager.isApiSupported(getApplicationContext())){
                        context.startActivity(new Intent(context,Dialer.class));
                }
                else{
                    Toast.makeText(context,"API: NotSupported!",Toast.LENGTH_LONG).show();
                }


            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        // When we get back from the preference setting Activity, assume
        // settings have changed, and re-login with new auth info.
        initializeManager();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (call != null) {
            call.close();
        }
        closeLocalProfile();

        if (callReceiver != null) {
            this.unregisterReceiver(callReceiver);
        }
    }

    public void initializeManager() {
        if(manager == null) {
            manager = SipManager.newInstance(context);
        }

        System.out.println(manager);


        initializeLocalProfile();
    }



    /**
     * Logs you into your SIP provider, registering this device as the location to
     * send SIP calls to for your SIP address.
     */
    public void initializeLocalProfile() {
        if (manager == null) {
            return;
        }
        if (sipProfile != null) {
            closeLocalProfile();
        }

        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        String username = SharedPrefs.getPrefs(context,Constt.CALLER_USERNAME);
        String password = SharedPrefs.getPrefs(context,Constt.CALLER_PASSWORD);



        if ((username == null)  || (password == null)) {
            //showDialog(UPDATE_SETTINGS_DIALOG);
            context.startActivity(new Intent(context,SetProfile.class));

            return;
        }

        try {
            SipProfile.Builder builder = new SipProfile.Builder(username, Constt.SERVER);
            builder.setPassword(password);
            builder.setPort(Constt.PORT);
            sipProfile = builder.build();

            Intent i = new Intent();
            i.setAction(Constt.CALL_UI_ACTION);
            i.putExtra(Constt.CALL_TYPE,Constt.CALL_TYPE_INCOMING);
            PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, Intent.FILL_IN_DATA);
            manager.open(sipProfile, pi, null);


            // This listener must be added AFTER manager.open is called,
            // Otherwise the methods aren't guaranteed to fire.

            manager.setRegistrationListener(sipProfile.getUriString(), new SipRegistrationListener() {
                public void onRegistering(String localProfileUri) {
//                    Toast.makeText(context,"Registering with SIP Server....",Toast.LENGTH_LONG).show();
                    updateStatus("Registering with SIP Server...");

                }

                public void onRegistrationDone(String localProfileUri, long expiryTime) {
//                    Toast.makeText(context,"Ready.",Toast.LENGTH_LONG).show();
                    updateStatus("Ready");

                }

                public void onRegistrationFailed(String localProfileUri, int errorCode,String errorMessage) {
//                    Toast.makeText(context,"Registration failed.  Please check settings.",Toast.LENGTH_LONG).show();
                    updateStatus("Registration failed.  Please check settings.");
                   // context.startActivity(new Intent(context,SetProfile.class));
                }
            });
        } catch (ParseException pe) {
            updateStatus("Connection Error.");
        } catch (SipException se) {
            updateStatus("Connection error.");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
    }





    public void closeLocalProfile() {
        if (manager == null) {
            return;
        }
        try {
            if (sipProfile != null) {
                manager.close(sipProfile.getUriString());
            }
        } catch (Exception ee) {
            System.out.println("Error  ----------- Failed to close local profile.");
            // Log.d("WalkieTalkieActivity/onDestroy", "Failed to close local profile.", ee);
        }
    }





    public void updateStatus(final String status) {
        // Be a good citizen.  Make sure UI changes fire on the UI thread.
        /*this.runOnUiThread(new Runnable() {
            public void run() {
                TextView labelView = (TextView) findViewById(R.id.sipLabel);
                labelView.setText(status);
            }
        });*/

        System.out.println(status);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_SIP_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                }
                break;

            default:
                break;
        }
    }



}
