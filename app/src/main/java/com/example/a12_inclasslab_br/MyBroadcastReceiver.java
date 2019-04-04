package com.example.a12_inclasslab_br;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";
    private static final CharSequence SECRETSTRING = "secret";
    /**
     * This method is called when the BroadcastReceiver is receiving an Intent
     * broadcast.  During this time you can use the other methods on
     * BroadcastReceiver to view/modify the current result values.  This method
     * is always called within the main thread of its process, unless you
     * explicitly asked for it to be scheduled on a different thread using
     * {@link Context#registerReceiver(BroadcastReceiver,
     * IntentFilter, String, Handler)}. When it runs on the main
     * thread you should
     * never perform long-running operations in it (there is a timeout of
     * 10 seconds that the system allows before considering the receiver to
     * be blocked and a candidate to be killed). You cannot launch a popup dialog
     * in your implementation of onReceive().
     *
     * <p><b>If this BroadcastReceiver was launched through a &lt;receiver&gt; tag,
     * then the object is no longer alive after returning from this
     * function.</b> This means you should not perform any operations that
     * return a result to you asynchronously. If you need to perform any follow up
     * background work, schedule a {@link JobService} with
     * {@link JobScheduler}.
     * <p>
     * If you wish to interact with a service that is already running and previously
     * bound using {@link Context#bindService(Intent, ServiceConnection, int) bindService()},
     * you can use {@link #peekService}.
     *
     * <p>The Intent filters used in {@link Context#registerReceiver}
     * and in application manifests are <em>not</em> guaranteed to be exclusive. They
     * are hints to the operating system about how to find suitable recipients. It is
     * possible for senders to force delivery to specific recipients, bypassing filter
     * resolution.  For this reason, {@link #onReceive(Context, Intent) onReceive()}
     * implementations should respond only to known actions, ignoring any unexpected
     * Intents that they may receive.
     *
     * @param context The Context in which the receiver is running.
     * @param intent  The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG,"In Broadcast Receiver");

        doStuff(context, intent);


        Intent myIntent = new Intent(context, MyService.class);
        context.startService(myIntent);
    }

    private void doStuff(Context context, Intent intent) {
        //lets see whats inside
        Bundle extras = intent.getExtras();

        if ( extras != null )
        {
            //A PDU is a "protocol description unit", which is the industry format for an SMS message. because SMSMessage reads/writes them you shouldn't need to dissect them.
            //A large message might be broken into many, which is why it is an array of objects.
            Object[] smsextras = (Object[]) extras.get( "pdus" );

            for ( int i = 0; i < smsextras.length; i++ )
            {
                SmsMessage smsmsg = SmsMessage.createFromPdu((byte[])smsextras[i]);

                //see whats in the message
                String strMsgBody = smsmsg.getMessageBody().toString();

                //does it contain our string?
                if (strMsgBody.contains(SECRETSTRING)){
                    Log.i(TAG, "contains secret string");

                    //start the service
                    Intent myIntent = new Intent(context, MyService.class);
                    context.startService(myIntent);
                }
                else
                    Log.i(TAG, "Does Not contain secret string");

                //can also do this by phone number
                //String strMsgSrc = smsmsg.getOriginatingAddress();
            }
        }
    }


}
