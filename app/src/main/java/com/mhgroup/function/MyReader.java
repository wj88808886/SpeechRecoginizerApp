package com.mhgroup.function;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.*;

import com.mhgroup.activity.R;
import com.mhgroup.application.MyApplication;
import com.mhgroup.util.FileUtils;
import com.mhgroup.util.PromptUtils;

import java.util.Locale;

/**
 * Created by DK Wang on 2015/3/16.
 */
public class MyReader implements OnInitListener{

    private TextToSpeech mtts;
    private Context context;

    public MyReader(Context context)
    {
        this.context = context;
        this.mtts = new TextToSpeech(context, this);
    }

    public void destroy()
    {
        if(mtts!=null)
        {
            mtts.stop();
            mtts.shutdown();
        }
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS)
        {
            //
        }
        else
        {
            mtts = null;
            PromptUtils.showMessage("Failed to initialize TTS engine.");
        }
    }

    public void read(String text)
    {
        if(text!=null)
        {
            if(mtts!=null)
            {
                if(!mtts.isSpeaking())
                {
                    mtts.setLanguage(MyApplication.LANG_LOCALE_MAP.get(FileUtils.getGoalLanguage(context)));
                    mtts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }
                else
                {
                    PromptUtils.showMessage("Wait a moment, the engine is busy now.");
                }
            }
            else
            {
                PromptUtils.showMessage("TTS engine hasn't been successfully initialized.");
            }
        }
        else
        {
            PromptUtils.showMessage("There is no content.");
        }
    }

    public void cancel()
    {
        mtts.stop();
    }

}
