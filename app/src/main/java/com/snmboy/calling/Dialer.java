package com.snmboy.calling;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.sip.SipAudioCall;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.snmboy.calling.caller.SipCallMethods;
import com.snmboy.calling.utils.Constt;
import com.snmboy.calling.utils.SharedPrefs;

public class Dialer extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private Activity activity;
    private String digit = null, getNumbers;
    private RelativeLayout number_1, number_2, number_3, number_4, number_5, number_6,
            number_7, number_8, number_9, number_0, number_star, number_hash;
    private EditText number_view;
    private RelativeLayout edit_back;
    private LinearLayout call_btn;
    public SipAudioCall call = null;
    public SipManager manager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_dialer);
        context = this;
        activity = ((Activity) context);

        number_view = activity.findViewById(R.id.number_view);
        edit_back = activity.findViewById(R.id.edit_back);
        call_btn = activity.findViewById(R.id.call_btn);
        getNumbers = number_view.getText().toString();


        initNumberViews();
        initClick();

        edit_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getNumbers != null && !getNumbers.isEmpty()){
                    String str = getNumbers;
                    putNumberToEditText(removeLastCharacter(str));
                }

            }
        });

        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainActivity mainActivity = (MainActivity) context;
                String username = SharedPrefs.getPrefs(context, Constt.CALLER_USERNAME);
                String password = SharedPrefs.getPrefs(context, Constt.CALLER_PASSWORD);
                SipProfile sipProfile = SipCallMethods.sipProfile(username, password);
                String sipAddress = SipCallMethods.sipAddress(getNumber());
                if (!getNumber().isEmpty()) {
                    SipCallMethods.initiateCall(context, sipProfile, sipAddress);
                }

                System.out.println(sipAddress);

            }
        });

    }


    public void initNumberViews() {
        number_0 = activity.findViewById(R.id.number_0);
        number_1 = activity.findViewById(R.id.number_1);
        number_2 = activity.findViewById(R.id.number_2);
        number_3 = activity.findViewById(R.id.number_3);
        number_4 = activity.findViewById(R.id.number_4);
        number_5 = activity.findViewById(R.id.number_5);
        number_6 = activity.findViewById(R.id.number_6);
        number_7 = activity.findViewById(R.id.number_7);
        number_8 = activity.findViewById(R.id.number_8);
        number_9 = activity.findViewById(R.id.number_9);
        number_star = activity.findViewById(R.id.number_star);
        number_hash = activity.findViewById(R.id.number_hash);
    }

    public void initClick() {
        number_0.setOnClickListener(this);
        number_1.setOnClickListener(this);
        number_2.setOnClickListener(this);
        number_3.setOnClickListener(this);
        number_4.setOnClickListener(this);
        number_5.setOnClickListener(this);
        number_6.setOnClickListener(this);
        number_7.setOnClickListener(this);
        number_8.setOnClickListener(this);
        number_9.setOnClickListener(this);
        number_star.setOnClickListener(this);
        number_hash.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.number_0:
                digit = "0";
                break;

            case R.id.number_1:
                digit = "1";
                break;

            case R.id.number_2:
                digit = "2";
                break;

            case R.id.number_3:
                digit = "3";
                break;

            case R.id.number_4:
                digit = "4";
                break;

            case R.id.number_5:
                digit = "5";
                break;

            case R.id.number_6:
                digit = "6";
                break;

            case R.id.number_7:
                digit = "7";
                break;

            case R.id.number_8:
                digit = "8";
                break;

            case R.id.number_9:
                digit = "9";
                break;
            case R.id.number_star:
                digit = "*";
                break;

            case R.id.number_hash:
                digit = "#";
                break;

        }
        putNumberToEditText(digit);

    }


    private void putNumberToEditText(String editNumber) {
        if (getNumbers == null && getNumbers.isEmpty()) {
            getNumbers = editNumber;
        } else {
            getNumbers += editNumber;
        }
        number_view.setText(getNumbers);

    }


    private String removeLastCharacter(String str) {
        return str.substring(0, str.length() - 1);
    }


    private String getNumber() {
        return getNumbers;
    }


}
