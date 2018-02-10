/*
Homwork 3
MainActivity.java
Hozefa Haveliwala/Nikhil Nagori - Group 29
 */


package com.wordsearch.wordsearch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> searchWords;
    ArrayList<Integer> wordCount;
    ProgressDialog pD1;
    String text;
    EditText editText_add;
    Button button_add;
    CheckBox checkBox_match;
    LinearLayout linearLayout_searchList;
    LinearLayout linearLayout_addWords;
    int async_count = 0;
    int count = 0;
    public final static String WORDS = "words";
    public final static String COUNT = "count";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.activity1);

        editText_add = (EditText) findViewById(R.id.editText_addword);
        button_add = (Button) findViewById(R.id.button_addword);
        checkBox_match = (CheckBox) findViewById(R.id.checkBox_match);
        linearLayout_searchList = (LinearLayout) findViewById(R.id.linearLayout_searchList);
        linearLayout_addWords = (LinearLayout) findViewById(R.id.linearLayout_addWords);

        searchWords = new ArrayList<String>();
        wordCount = new ArrayList<Integer>();

        try {
            InputStream iS = getAssets().open("textfile.txt");
            int size = iS.available();
            byte[] buffer = new byte[size];
            iS.read(buffer);
            iS.close();
            text = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText_add.length() == 0) {
                    Toast.makeText(MainActivity.this, "Enter text to Add", Toast.LENGTH_SHORT).show();
                } else {
                    count++;
                    searchWords.add(editText_add.getText().toString());

                    //Initializing layoutInflater
                    LayoutInflater layoutInflater =
                            (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    //adding the view from xml to inflater
                    final View addView = layoutInflater.inflate(R.layout.add_keyword, null);

                    //Initializing the widgets in the newly adding view
                    final EditText editText_searchWord = (EditText) addView.findViewById(R.id.editText_searchword);
                    Button buttonRemove = (Button) addView.findViewById(R.id.button_remove);

                    editText_searchWord.setText(editText_add.getText().toString());

                    //add this new view to linearlayout
                    linearLayout_searchList.addView(addView);

                    editText_add.setText("");
                    if (count == 20) {
                        linearLayout_addWords.setVisibility(View.GONE);
                        editText_add.clearFocus();
                        editText_add.setEnabled(false);
                    }

                    buttonRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Log.d("word:",((EditText)addView.findViewById(R.id.editText_searchword)).getText().toString() );
                            searchWords.remove(editText_searchWord.getText().toString());
                            ((LinearLayout) addView.getParent()).removeView(addView);

                            if (count == 20) {
                                linearLayout_addWords.setVisibility(View.VISIBLE);
                                editText_add.setEnabled(true);
                            }
                            count--;
                        }
                    });


                }

            }
        });

        findViewById(R.id.button_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new searchTask().execute(searchWords, wordCount);
                //***new addition
                if (linearLayout_addWords.getVisibility() == LinearLayout.VISIBLE && editText_add.length() > 0) {
                    searchWords.add(editText_add.getText().toString());
                }
                //***new addition

                if (searchWords.size() > 0) {
                    pD1 = new ProgressDialog(MainActivity.this);
                    pD1.setCancelable(false);
                    // pD1.getWindow().setGravity(Gravity.CENTER);
                    pD1.show();

                    for (int i = 1; i <= searchWords.size(); i++) {
                        new searchTask().execute(searchWords.get(i - 1).toString());
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Add atleast one word", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private class searchTask extends AsyncTask<String, Void, Void> {
        boolean check = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (checkBox_match.isChecked() == true)
                check = true;

        }

        @Override
        protected Void doInBackground(String... params) {
            Pattern p;
            Matcher m;
            int wCount = 0;
            if (check == true) {
                p = Pattern.compile(params[0]);
                m = p.matcher(text);
                while (m.find()) {
                    wCount++;
                }
                wordCount.add((int) wCount);
            } else {
                p = Pattern.compile(params[0], Pattern.CASE_INSENSITIVE);
                m = p.matcher(text);
                while (m.find()) {
                    wCount++;
                }
                wordCount.add((int) wCount);
            }
            async_count++;
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (async_count == searchWords.size()) {
                pD1.dismiss();
                Intent i = new Intent(MainActivity.this, ResultActivity.class);
                i.putStringArrayListExtra(WORDS, searchWords);
                i.putIntegerArrayListExtra(COUNT, wordCount);
                startActivity(i);
                finish();
            }
        }
    }

}
