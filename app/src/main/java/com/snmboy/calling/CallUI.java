package com.snmboy.calling;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.snmboy.calling.caller.IncomingCallReceiver;
import com.snmboy.calling.caller.SipCallMethods;
import com.snmboy.calling.utils.Constt;

public class CallUI extends AppCompatActivity {

    public String sipAddress = null;
    public SipManager manager = null;
    public SipProfile sipProfile = null;
    public SipAudioCall call = null;
    public IncomingCallReceiver callReceiver;

    private LinearLayout callui_ans, callui_reject, callui_end;
    private Activity activity;
    private TextView callui_status;
    boolean incomingCall = false;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_ui);
        context = this;
        activity = ((Activity) context);

        Intent intent = getIntent();
        String callType = intent.getStringExtra(Constt.CALL_TYPE);
        assert callType != null;
        if(callType.equalsIgnoreCase(Constt.CALL_TYPE_INCOMING)){
            incomingCall = true;
        }

        if (manager == null) {
            Toast.makeText(context,"please Check the Settings",Toast.LENGTH_LONG).show();
            activity.finish();
        }

        init();

    }

    private void init() {
        callui_status = activity.findViewById(R.id.callui_status);
        callui_ans = activity.findViewById(R.id.callui_ans);
        callui_end = activity.findViewById(R.id.callui_end);
        callui_reject = activity.findViewById(R.id.callui_reject);
        callui_ans.setVisibility(View.GONE);
        callui_end.setVisibility(View.GONE);
        callui_reject.setVisibility(View.GONE);
        initUI();


        callui_ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SipCallMethods.answerIncomingCall(call);
                onCall();
            }
        });

        callui_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SipCallMethods.rejectIncomingCall(call);
                activity.finish();
            }
        });

        callui_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    call.endCall();
                    activity.finish();
                } catch (SipException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void initUI(){
        if(incomingCall){
            incomingCallUI();
        }else {
            onCall();
        }
    }

    private void incomingCallUI(){
        callui_reject.setVisibility(View.GONE);
        callui_ans.setVisibility(View.VISIBLE);
        callui_end.setVisibility(View.VISIBLE);
        callui_status.setText("Incoming Call");
    }

    private void onCall(){
        callui_reject.setVisibility(View.VISIBLE);
        callui_ans.setVisibility(View.GONE);
        callui_end.setVisibility(View.GONE);

    }


}
