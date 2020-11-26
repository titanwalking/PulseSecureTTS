package com.colossussoftware.pulsesecuretts;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

//
// Created by alican.korkmaz on 26.11.2020.
//
public class MySpeakService extends JobIntentService {

    private TextToSpeech mySpeakTextToSpeech = null;
    private boolean isSafeToDestroy = false;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, MySpeakService.class, 1, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        final String message = intent.getStringExtra("MESSAGE");

        mySpeakTextToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                mySpeakTextToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
                while (mySpeakTextToSpeech.isSpeaking()) {

                }
                isSafeToDestroy = true;
            }
        });
    }

    @Override
    public void onDestroy() {
        if (isSafeToDestroy) {
            if (mySpeakTextToSpeech != null) {
                mySpeakTextToSpeech.shutdown();
            }
            super.onDestroy();
        }
    }
}
