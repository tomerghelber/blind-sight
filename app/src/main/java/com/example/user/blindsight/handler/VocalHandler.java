package com.example.user.blindsight.handler;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

import java.util.Locale;

public class VocalHandler implements StateHandler, OnInitListener {
    private TextToSpeech tts;
    private Context context;
    private long lastGreen;
    private long lastRed;

    public VocalHandler(Context context) {
        this.context = context;
        this.tts = new TextToSpeech(context, this);
    }

    @Override
    public void handlegreen() {
        if (lastGreen == 0) {
            lastGreen = System.currentTimeMillis();
        } else if (System.currentTimeMillis() - lastGreen  > 2000) {
            speakOut("Green!");
            lastGreen = System.currentTimeMillis();
        }

    }

    @Override
    public void handleRed() {
        if (lastRed == 0) {
            lastRed = System.currentTimeMillis();
        } else if (System.currentTimeMillis() - lastRed > 2000) {
            speakOut("Red!");
            lastRed = System.currentTimeMillis();
        }
    }

    @Override
    public void handleNa() {

    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void speakOut(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}
