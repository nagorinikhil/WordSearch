/*
Homwork 3
ResultActivity.java
Hozefa Haveliwala/Nikhil Nagori - Group 29
 */
package com.wordsearch.wordsearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setTitle(R.string.activity2);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout_result);
        TextView tv;
        ArrayList<String> keyWords = getIntent().getStringArrayListExtra(MainActivity.WORDS);
        ArrayList<Integer> wordCount = getIntent().getIntegerArrayListExtra(MainActivity.COUNT);

        for (int i = 0; i < keyWords.size(); i++) {
            tv = new TextView(ResultActivity.this);
            tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setPadding(0, 6, 0, 6);
            tv.setTextSize(16);
            tv.setText(keyWords.get(i) + ": " + wordCount.get(i));
            linearLayout.addView(tv);
        }

        findViewById(R.id.button_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
