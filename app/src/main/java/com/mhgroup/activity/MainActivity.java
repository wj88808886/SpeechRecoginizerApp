package com.mhgroup.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mhgroup.application.MyApplication;
import com.mhgroup.bean.Record;
import com.mhgroup.bean.IndexMatchpair;
import com.mhgroup.bean.StringMatchpair;
import com.mhgroup.function.MyReader;
import com.mhgroup.function.MyTranslator;
import com.mhgroup.function.Matcher;
import com.mhgroup.util.DatabaseHelper;
import com.mhgroup.util.FileUtils;
import com.mhgroup.util.PromptUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
    private MyReader reader = null;
    private MyTranslator translator = null;
    private Matcher matcher = null;
//    private CallMaker callMaker = null;

    private Button playButton = null;
    private Button recordButton = null;
    private Button settingButton = null;
    private Button reviseButton = null;

    private EditText translatedET = null, originalET = null;
    private String translatedText = null;
    private Handler myHandler = null;

    private DatabaseHelper DH = new DatabaseHelper(MainActivity.this);
    private boolean flag = true;

    private Button.OnClickListener buttonClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.playButton: {
                    String text = translatedET.getText().toString();
                    reader.read(text);
                    break;
                }
                case R.id.recordButton: {
                    startSpeaker();
                    break;
                }
                case R.id.settingButton: {
                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.reviseButton: {
                    if (flag) {
                        translatedET.setEnabled(true);
                        reviseButton.setText("Submit");
                        flag = false;
                    }
                    else{
                        String goalLanguage = new MyApplication().getGoalLanguage();
                        String revisedText = translatedET.getText().toString();
                        ArrayList<IndexMatchpair> Match;
                        ArrayList<StringMatchpair> StringMatch;
                        String firstWord;
                        if (goalLanguage.equals("zh-CN")){
                            String[] translatedArray = new String[translatedText.length()];
                            for (int i = 0; i<translatedText.length();i++){
                                translatedArray[i] = translatedText.charAt(i)+"";
                            }
                            String[] revisedArray = new String[revisedText.length()];
                            for (int i = 0; i<revisedText.length();i++){
                                revisedArray[i] = revisedText.charAt(i)+"";
                            }
                            Match = matcher.Eqindex(translatedArray,revisedArray);
                            StringMatch = matcher.EqString(Match,translatedArray,revisedArray,true);
                            for (int i = 0;i<StringMatch.size();i++) {
                                if (!StringMatch.get(i).Ophrase.isEmpty()) {
                                    firstWord = StringMatch.get(i).Ophrase.charAt(0) + "";
                                    Record record = new Record(firstWord, StringMatch.get(i).Ophrase, StringMatch.get(i).Rphrase);
//                                Log.d("INSERT", record.toString());
                                    DH.addRecord(record);
                                }
                            }
                        }
                        else{
                            String[] translatedArray = translatedText.split(" ");
                            String[] revisedArray = revisedText.split(" ");
                            Log.d("revisedText",revisedText);
                            Match = matcher.Eqindex(translatedArray,revisedArray);
                            StringMatch = matcher.EqString(Match, translatedArray, revisedArray, false);
                            for (int i = 0;i<StringMatch.size();i++) {
                                firstWord = StringMatch.get(i).Ophrase.split(" ")[0];
                                Record record = new Record(firstWord, StringMatch.get(i).Ophrase, StringMatch.get(i).Rphrase);
                                Log.d("INSERT", record.toString());
                                DH.addRecord(record);
                            }
                        }
                        flag = true;
                        reviseButton.setText("Revise");
                        translatedET.setEnabled(false);

                    }
                }
                default: {

                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PromptUtils.currentContext = MainActivity.this;

        // We need to change the locale later.
        reader = new MyReader(MainActivity.this);
        translator = new MyTranslator(MainActivity.this);
        matcher = new Matcher(MainActivity.this);
        myHandler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                translatedET.setText(translatedText);
            }

        };

//        Intent i = getBaseContext().getPackageManager()
//                .getLaunchIntentForPackage(
//                        getBaseContext().getPackageName());
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        callMaker = new CallMaker(MainActivity.this, i);

        translatedText = new String();

        originalET = (EditText) findViewById(R.id.originalEditText);
        translatedET = (EditText) findViewById(R.id.translatedEditText);
        translatedET.setEnabled(false);

        playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(buttonClickListener);

        settingButton = (Button) findViewById(R.id.settingButton);
        settingButton.setOnClickListener(buttonClickListener);

        recordButton = (Button) findViewById(R.id.recordButton);
        recordButton.setOnClickListener(buttonClickListener);

        reviseButton = (Button) findViewById(R.id.reviseButton);
        reviseButton.setOnClickListener(buttonClickListener);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
// Fill the list view with the strings the recognizer thought it could have heard
            ArrayList matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String firstMatch = matches.get(0).toString();
            originalET.setText(firstMatch);
            startTranslate();
        }
        //Result code for various error.
        else if (resultCode == RecognizerIntent.RESULT_AUDIO_ERROR) {
            showToastMessage("Audio Error");
        } else if (resultCode == RecognizerIntent.RESULT_CLIENT_ERROR) {
            showToastMessage("Client Error");
        } else if (resultCode == RecognizerIntent.RESULT_NETWORK_ERROR) {
            showToastMessage("Network Error");
        } else if (resultCode == RecognizerIntent.RESULT_NO_MATCH) {
            showToastMessage("No Match");
        } else if (resultCode == RecognizerIntent.RESULT_SERVER_ERROR) {
            showToastMessage("Server Error");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void startSpeaker() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // Specify the calling package to identify your application
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass()
                .getPackage().getName());


        // Given an hint to the recognizer about what the user is going to say
        //There are two form of language model available
        //1.LANGUAGE_MODEL_WEB_SEARCH : For short phrases
        //2.LANGUAGE_MODEL_FREE_FORM  : If not sure about the words or phrases and its domain.
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        Locale locale = MyApplication.LANG_LOCALE_MAP.get(FileUtils.getOriginalLanguage(MainActivity.this));
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, locale.getLanguage());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "please speak");
        try {
            startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(this, "speech not support",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startTranslate() {
        new Thread(translateRunnable).start();
    }

    public Runnable translateRunnable = new Runnable()
    {

        @Override
        public void run() {
            String originalText = originalET.getText().toString();
            String firstWord;
            ArrayList<String> translatedTextArray = new ArrayList<String>();
            if (!originalText.equals("")) {
                translatedText = translator.translate(originalText);
            }
            else {
                PromptUtils.showMessage("No input.");
            }
            String goalLanguage = new MyApplication().getGoalLanguage();
            if (goalLanguage.equals("zh-CN")) {
                int pointer = 0;
                boolean tempflag = true;
                while (pointer < translatedText.length()){
                        firstWord = translatedText.charAt(pointer)+"";
//                        Log.d("aa", firstWord);
                        List<Record> ListRecords = DH.queryRecords(firstWord);
                        for(Record record : ListRecords)
                        {
                        Log.d("QUERY", record.toString());
                    }
                    while (!ListRecords.isEmpty()){

                        Record temprecord = ListRecords.remove(0);
                        if (translatedText.length()-pointer >= temprecord.getLength()){
                            String wholephrase = temprecord.getWholePhrase();
                            if (translatedText.substring(pointer, pointer+wholephrase.length()).equals(wholephrase)){
                                translatedTextArray.add(temprecord.getChangedPhrase());
                                pointer += wholephrase.length();
                                tempflag = false;
                                break;
                            }
                        }
                    }
                    if (tempflag){
                        translatedTextArray.add(firstWord);
                        pointer ++;
                    }
                    flag = true;
                }
                String[] translatedstringarray = translatedTextArray.toArray(new String[translatedTextArray.size()]);
                translatedText = matcher.join(translatedstringarray,0,translatedstringarray.length,"");
            }
            else{
                int pointer = 0;
                boolean tempflag = true;
                String[] UnrevisedText = translatedText.split(" ");
                while (pointer < UnrevisedText.length){
                    firstWord = UnrevisedText[pointer];
                    List<Record> ListRecords = DH.queryRecords(firstWord);

/*                  for(Record record : ListRecords)
                    {
                        Log.d("QUERY", record.toString());
                    }
*/
                    while (!ListRecords.isEmpty()){
                        Record tempRecord = ListRecords.remove(0);
//                        Log.d("left", UnrevisedText.length-pointer-1+"");
//                        Log.d("right",tempRecord.getLength()+"");
                        if (UnrevisedText.length-pointer >= tempRecord.getLength()){
                                String[] wholephrase = tempRecord.getWholePhrase().split(" ");
                                if (Arrays.equals(Arrays.copyOfRange(UnrevisedText, pointer, pointer + wholephrase.length), wholephrase)){
                                    translatedTextArray.add(tempRecord.getChangedPhrase());
                                    pointer += wholephrase.length;
                                    tempflag = false;
                                    break;
                                }
                        }
                    }
                    if (tempflag){
                        translatedTextArray.add(firstWord);
                        pointer ++;
                    }
                    flag = true;
                }
                String[] translatedstringarray = translatedTextArray.toArray(new String[translatedTextArray.size()]);
//                Log.d("translatedStringArray", Arrays.toString(translatedstringarray));
                translatedText = matcher.join(translatedstringarray,0,translatedstringarray.length," ");
            }
            myHandler.sendEmptyMessage(0);
        }
    };


    @Override
    protected void onDestroy() {
        reader.destroy();
        super.onDestroy();
    }
}
