package com.example.sstto.myapplication;

/**
 * The activity is to prompt user to choose a difficulty level
 * The activity consists of three buttons one for each difficulty level
 * On clicking any button, the percentage of mines are set and game activity is started
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {
    Button button1;
    Button button2;
    Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), GameActivity.class);
                intent.putExtra("MINE_PERCENT", "0.1");
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), GameActivity.class);
                intent.putExtra("MINE_PERCENT", "0.3");
                startActivity(intent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), GameActivity.class);
                intent.putExtra("MINE_PERCENT", "0.5");
                startActivity(intent);
            }
        });
    }

}
