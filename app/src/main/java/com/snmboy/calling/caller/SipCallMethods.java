package com.snmboy.calling.caller;

import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;

import com.snmboy.calling.CallUI;
import com.snmboy.calling.utils.Constt;

import java.text.ParseException;

public class SipCallMethods {


    public static SipProfile sipProfile(String username, String password) {
        SipProfile sipProfile = null;
        SipProfile.Builder builder = null;
        try {
            builder = new SipProfile.Builder(username, Constt.SERVER);
            builder.setPort(Constt.PORT);
            builder.setPassword(password);
            sipProfile = builder.build();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sipProfile;
    }


    public static String sipAddress(String username) {
        //    "sip:x@y:Port"
        return "sip:" + username + "@" + Constt.SERVER + ":" + Constt.PORT;
    }

    public static String initiateCallSipAddress(String user) {
        SipProfile.Builder builder = null;
        try {
            builder = new SipProfile.Builder(user, Constt.SERVER);
            builder.setPort(Constt.PORT);
            /*builder.setPassword(password);*/

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return builder.build().getUriString();
    }

    public static void answerIncomingCall(SipAudioCall incomingCall) {
        try {
            incomingCall.answerCall(30);
            incomingCall.startAudio();
            if (incomingCall.isMuted()) {
                incomingCall.toggleMute();
            }
        } catch (Exception e) {

            System.out.println(e.toString());
        }
    }


    public static void rejectIncomingCall(SipAudioCall incomingCall) {
        try {
            if (incomingCall != null) {
                incomingCall.endCall();
                incomingCall.close();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


    public static void userSipProfile(){

    }


    /**
     * Make an outgoing call.
     */
    public static void initiateCall(Context context,SipProfile sipProfile, String sipAddress) {

      //  updateStatus(sipAddress);
        //SipAudioCall call;
        CallUI callUI = (CallUI) context.getApplicationContext();
        SipAudioCall call = callUI.call;
        SipManager manager = callUI.manager;

        try {
            SipAudioCall.Listener listener = new SipAudioCall.Listener() {
                // Much of the client's interaction with the SIP Stack will
                // happen via listeners.  Even making an outgoing call, don't
                // forget to set up a listener to set things up once the call is established.
                @Override
                public void onCallEstablished(SipAudioCall call) {
                    call.startAudio();
                    call.setSpeakerMode(true);
                    call.toggleMute();
                    //   updateStatus(call);
                }

                @Override
                public void onCallEnded(SipAudioCall call) {
                //    updateStatus("Ready.");
                }
            };



            call = manager.makeAudioCall(sipProfile.getUriString(), sipAddress, listener, 30);
            callUI.call = call;
            Intent startCall = new Intent(context, CallUI.class);
            startCall.putExtra(Constt.CALL_TYPE,Constt.CALL_TYPE_DIAL);
            context.startActivity(startCall);
        }
        catch (Exception e) {
           // Log.i("WalkieTalkieActivity/InitiateCall", "Error when trying to close manager.", e);

            System.out.println("InitiateCall ---- "+ e.getMessage());

            if (sipProfile != null) {
                try {
                    manager.close(sipProfile.getUriString());
                } catch (Exception ee) {
//                    Log.i("WalkieTalkieActivity/InitiateCall","Error when trying to close manager.", ee);
//                    ee.printStackTrace();
                    System.out.println("InitiateCallxx ---- "+ ee.getMessage());

                }
            }
            if (call != null) {
                call.close();
            }
        }
    }


}
