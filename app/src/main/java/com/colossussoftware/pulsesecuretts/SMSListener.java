package com.colossussoftware.pulsesecuretts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.Locale;

//
// Created by alican.korkmaz on 26.11.2020.
//
public class SMSListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        String body = smsMessage.getMessageBody();
                        if (body.contains("SSL VPN")) {
                            int dotIndex = body.indexOf('.');
                            String code = body.substring(dotIndex - 5, dotIndex);

                            Intent serviceIntent = new Intent();
                            serviceIntent.putExtra("MESSAGE", code);
                            MySpeakService.enqueueWork(context, serviceIntent);
                        }
                    }

                } catch (Exception exception) {
                    Log.e("smsReceiveError", exception.getMessage());
                }
            }
        }
    }

    public void speak(Context context, String text) {

        if (text != null && !text.isEmpty()) {
            TextToSpeech tts = new TextToSpeech(context, null);
            tts.setLanguage(Locale.US);
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}
