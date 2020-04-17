package com.snmboy.calling.caller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipProfile;
import com.snmboy.calling.CallUI;
import com.snmboy.calling.utils.Constt;

public class IncomingCallReceiver extends BroadcastReceiver {
    private SipAudioCall incomingCall = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {

            SipAudioCall.Listener listener = new SipAudioCall.Listener() {
                @Override
                public void onRinging(SipAudioCall call, SipProfile caller) {
                    super.onRinging(call, caller);
                }
            };

            CallUI callUI = (CallUI) context;

            incomingCall = callUI.manager.takeAudioCall(intent, listener);
            //Method call which handles incoming call.

            callUI.call = incomingCall;
            showIncomingCall(intent, context);
          //  wtActivity.updateStatus(incomingCall);

        } catch (Exception e) {
            if (incomingCall != null) {
                incomingCall.close();
            }
        }


    }

    private void showIncomingCall (Intent intent, Context context){
        Intent incomingCall = new Intent(context, CallUI.class);
        intent.putExtra(Constt.CALL_TYPE,Constt.CALL_TYPE_INCOMING);
        context.startActivity(incomingCall);
    }



}
