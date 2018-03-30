package ssn.codebreakers.pecsinstructor.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

public class SpeechHelper
{
    public static final int SPEECH_REQUEST_ID = 100;

    TextToSpeech textToSpeech;
    boolean textToSpeechInitialized = false;
    Context context;
    Activity activity;

    public SpeechHelper(Context context, Activity activity)
    {
        this.context = context;
        this.activity = activity;
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeechInitialized = true;
                }
            }
        });
    }

    public void textToSpeech(String text)
    {
        if(textToSpeechInitialized)
        {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    public void speechToText()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "speak now");
        try {
            activity.startActivityForResult(intent, SPEECH_REQUEST_ID);
        } catch (Exception a) {
            Toast.makeText(context, "Sorry. your device does not support speech recognition.", Toast.LENGTH_SHORT).show();
        }
    }
}
