package com.cs310.p1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EndgameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.endgame);
        Intent intent = getIntent();
        String time = intent.getStringExtra("seconds");
        String won = intent.getStringExtra("won");
        String message = "You "+won+"! You used "+time+ " seconds!";
        TextView view = (TextView) findViewById(R.id.endMessage);
        view.setText(message);
    }
    public void playAgain(View v){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
