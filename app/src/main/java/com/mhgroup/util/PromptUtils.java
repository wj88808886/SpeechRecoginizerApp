package com.mhgroup.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by DK Wang on 2015/3/16.
 */
public class PromptUtils {

    public static Context currentContext;

    public static void showMessage(String message)
    {
        Toast.makeText(currentContext, message,
                Toast.LENGTH_SHORT).show();
    }
}
