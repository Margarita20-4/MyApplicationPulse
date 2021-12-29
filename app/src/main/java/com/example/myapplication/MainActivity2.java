package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
public class MainActivity2 extends AppCompatActivity {
    ImageView rezimage;
    TextView resultText, text1;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        result = getIntent().getExtras().getString("result");
        text1 = findViewById(R.id.rez2);
        text1.setText(R.string.rez2);

        rezimage = findViewById(R.id.icon);
        resultText = findViewById(R.id.resultText);
        switch (result) {
            case "good":
                resultText.setText(R.string.goodres);
                rezimage.setImageResource(R.drawable.good);

                break;
            case "norm":
                resultText.setText(R.string.normres);
                rezimage.setImageResource(R.drawable.norm);

                break;
            case "bad":
                resultText.setText(R.string.badres);
                rezimage.setImageResource(R.drawable.bad);
                break;
            case "error":
                resultText.setText(R.string.errorres);
                rezimage.setImageResource(R.drawable.error);
                break;
        }
    }
}