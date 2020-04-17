package com.snmboy.calling;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.snmboy.calling.utils.Constt;
import com.snmboy.calling.utils.SharedPrefs;

public class SetProfile extends AppCompatActivity {

    private Context context;
    private Activity activity;
    private AppCompatEditText profile_username,profile_password;
    private AppCompatButton profile_continue;
    String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);
        context = this;
        activity = ((Activity) context);

        profile_username = activity.findViewById(R.id.profile_username);
        profile_password = activity.findViewById(R.id.profile_password);
        profile_continue = activity.findViewById(R.id.profile_continue);

        username = profile_username.getText().toString();
        password = profile_password.getText().toString();

        profile_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((!profile_username.getText().toString().isEmpty()) && (!profile_password.getText().toString().isEmpty())){

                    SharedPrefs.setPrefs(context, Constt.CALLER_USERNAME,username);
                    SharedPrefs.setPrefs(context,Constt.CALLER_PASSWORD,password);
                    context.startActivity(new Intent(context,MainActivity.class));

                }else{
                    Toast.makeText(context,"Fields are empty !!",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
