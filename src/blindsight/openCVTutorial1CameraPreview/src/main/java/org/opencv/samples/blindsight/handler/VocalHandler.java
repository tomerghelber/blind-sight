package org.opencv.samples.blindsight.handler;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

public class VocalHandler implements StateHandler, TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    private Context context;

    public VocalHandler(Context context) {
        this.context = context;
        this.tts = new TextToSpeech(context, this);
    }

    @Override
    public void handlegreen() {
        speakOut("Green!");

    }

    @Override
    public void handleRed() {

        speakOut("Red!");
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
