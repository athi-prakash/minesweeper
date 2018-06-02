package com.example.sstto.myapplication;
/**
 * Game is play & win/lose
 * Event handlers for the game grid
 * Game mechanism and timer
 * Reset & back option and respective handlers
 * Created by sstto on 15-10-2017.
 */
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GameActivity extends AppCompatActivity {

    public GridView gv;
    public CustomGridAdapter gridAdapter;
    protected ArrayList<Integer> mine = new ArrayList<Integer>();
    protected int size = 81, col = 9;
    protected String[] items;
    protected int bomb_count = 0, open_count = 0, time_inc=1;
    protected AlertDialog.Builder builder;
    Button reset;
    TextView time;
    int time_count;

    /**
     * Open cells on clicking
     * Recursive function to implement ripple effect on opening empty cell.
     */
    protected void open(int xpos, int ypos, View view, ViewGroup parent) {
        /*Validate edges or end of ripple*/
        if (xpos < 0 || ypos < 0 || xpos >= col || ypos >= col || !gridAdapter.items[ypos * col + xpos].equals(""))
            return;
        /*Set appropriate open image on the cell*/
        gridAdapter.items[(ypos * col + xpos)] = items[(ypos * col + xpos)];
        gridAdapter.getView((ypos * col + xpos), gv.getChildAt(ypos * col + xpos), gv);
        open_count++;
        /*Recursion*/
        if (gridAdapter.items[ypos * col + xpos].equals("0")) {
            open(xpos - 1, ypos - 1, view, parent);
            open(xpos, ypos - 1, view, parent);
            open(xpos + 1, ypos - 1, view, parent);
            open(xpos - 1, ypos, view, parent);
            open(xpos + 1, ypos, view, parent);
            open(xpos - 1, ypos + 1, view, parent);
            open(xpos, ypos + 1, view, parent);
            open(xpos + 1, ypos + 1, view, parent);
        }
        return;
    }

    /**
     * Random mine population and neighbour calculation
     * Set event handlers
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Toast t= Toast.makeText(GameActivity.this,"SELECT VALUE & CLICK CELLS",Toast.LENGTH_SHORT);
        //t.show();
        builder = new AlertDialog.Builder(GameActivity.this, R.style.Alert);
        gridAdapter = new CustomGridAdapter(GameActivity.this);
        gv = (GridView) this.findViewById(R.id.mygrid);
        gridAdapter.items = new String[size];
        Arrays.fill(gridAdapter.items, "");
        items = new String[size];
        Arrays.fill(items, "");

        /*Random mine populator*/
        for (int i = 0; i < size; i++) {
            mine.add(new Integer(i));
        }
        Collections.shuffle(mine);
        bomb_count = (int) (size * Float.parseFloat(getIntent().getStringExtra("MINE_PERCENT")));
        for (int i = 0; i < bomb_count; i++) {
            items[mine.get(i)] = "b";
        }

        /*Neighbour calculation*/
        int count, xpos, ypos;
        for (int i = 0; i < size; i++) {
            if (items[i].equals("b"))
                continue;
            count = 0;
            xpos = i % col;
            ypos = i / col;
            if (xpos - 1 >= 0 && ypos - 1 >= 0 && items[i - col - 1].equals("b"))
                count++;
            if (ypos - 1 >= 0 && items[i - col].equals("b"))
                count++;
            if (xpos + 1 < col && ypos - 1 >= 0 && items[i - col + 1].equals("b"))
                count++;
            if (xpos - 1 >= 0 && items[i - 1].equals("b"))
                count++;
            if (xpos + 1 < col && items[i + 1].equals("b"))
                count++;
            if (xpos - 1 >= 0 && ypos + 1 < col && items[i + col - 1].equals("b"))
                count++;
            if (ypos + 1 < col && items[i + col].equals("b"))
                count++;
            if (xpos + 1 < col && ypos + 1 < col && items[i + col + 1].equals("b"))
                count++;
            items[i] = "" + count;
        }
        gv.setAdapter(gridAdapter);

        /**
         * Long click listener to flag/unflag cell
         */
        gv.setOnItemLongClickListener(new GridView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (gridAdapter.items[position].equals("")) {
                    gridAdapter.items[position] = "f";
                    gridAdapter.getView(position, gv.getChildAt(position), gv);
                } else if (gridAdapter.items[position].equals("f")) {
                    gridAdapter.items[position] = "";
                    gridAdapter.getView(position, gv.getChildAt(position), gv);
                }
                return true;
            }
        });

        /**
         * Click listener for the cell
         * Handles click on mine, empty cell with empty neighbours and empty cell with non-empty neighbours
         */
        gv.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast t= Toast.makeText(GameActivity.this,position+"P",Toast.LENGTH_SHORT);
                //t.show();
                /*Click on flagged cell, do nothing*/
                if (gridAdapter.items[position].equals("f")) {
                    return;
                }
                /*click on a mine, game lost*/
                else if (items[position].equals("b")) {
                    time_inc=0;
                    /*Open other mines*/
                    for (int i = 0; i < bomb_count; i++) {
                        gridAdapter.items[mine.get(i)] = "b";
                        gridAdapter.getView(mine.get(i), gv.getChildAt(mine.get(i)), gv);
                    }
                    /*Explode current mine*/
                    gridAdapter.items[position] = "e";
                    gridAdapter.getView(position, view, parent);
                    /*Alert user on end of game, with forced option to reset or go to menu*/
                    builder.setTitle("Gameover")
                            .setMessage("Restart game?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    recreate();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getBaseContext(), MenuActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert);
                    AlertDialog dialog = builder.create();
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                }
                /*Click on non-mine, open cell and check winning*/
                else {
                    open(position % col, position / col, view, parent);
                    /*Validate win condition*/
                    if (open_count + bomb_count == size) {
                        time_inc=0;
                        /*Game one, flag all mines*/
                        for (int i = 0; i < bomb_count; i++) {
                            gridAdapter.items[mine.get(i)] = "f";
                            gridAdapter.getView(mine.get(i), gv.getChildAt(mine.get(i)), gv);
                        }
                        /*Alert user on end of game, with forced option to reset or go to menu*/
                        builder.setTitle("MINE DEFUSED in " + String.format("%02d:%02d", time_count / 60, time_count % 60) + "!!!")
                                .setMessage("Play another game?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        recreate();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getBaseContext(), MenuActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .setIcon(android.R.drawable.checkbox_on_background);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                        textView.setTextSize(30);
                    }
                }
            }
        });

        /*Handle reset button to restart game*/
        reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });

        /*Initialize timer*/
        time = (TextView) findViewById(R.id.time);
        new CountDownTimer(999999999, 1000) {
            @Override
            public void onTick(long l) {
                time_count+=time_inc;
                time.setText(String.format("%02d:%02d", time_count / 60, time_count % 60));
            }

            @Override
            public void onFinish() {
                return;
            }
        }.start();
    }
}
