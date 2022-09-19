package com.cs310.p1;

import androidx.appcompat.app.AppCompatActivity;

import androidx.gridlayout.widget.GridLayout;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.TextView;



import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    boolean running = false;
    boolean flagging = false;
    private int numRevealed = 0;
    private static  final int numBombs = 4;
    private int numflags = numBombs;
    private static final int R_COUNT = 8;
    private static final int C_COUNT = 10;
    int clock = 0;
    private final TextView[][] cell_tvs = new TextView[C_COUNT][R_COUNT];
    private final int[][] bombs = new int[C_COUNT][R_COUNT];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridLayout grid = findViewById(R.id.gridLayout);
        LayoutInflater li = LayoutInflater.from(this);
        generateBombs();

        for(int i = 0;i< R_COUNT;i++){
            for(int j = 0;j<C_COUNT;j++){
                TextView view = (TextView) li.inflate(R.layout.cells,grid,false);
                view.setBackgroundColor(Color.GREEN);
                if(bombs[j][i]!=0){
                    view.setText(this.getString(R.string.bomb));
                }

                view.setTextColor(Color.TRANSPARENT);
                view.setOnClickListener(this::OnClick);

                //GridLayout.LayoutParams layout = new GridLayout.LayoutParams();
                GridLayout.LayoutParams layout = (GridLayout.LayoutParams) view.getLayoutParams();
                layout.rowSpec = GridLayout.spec(j);
                layout.columnSpec = GridLayout.spec(i);

                grid.addView(view,layout);
                cell_tvs[j][i] = view;
            }
        }
        runTimer();
    }
public void OnClick(View v){
        if(!running){
            running = true;
            runTimer();
        }
        TextView tv = (TextView) v;

        Pair<Integer,Integer> index = getIndex(tv);
        int x = index.first;
        int y = index.second;
        if(flagging){
            tv.setText(this.getString(R.string.flag));
            tv.setBackgroundColor(Color.LTGRAY);
            tv.setTextColor(Color.RED);
            numflags--;
            TextView flags = (TextView) findViewById(R.id.numFlags);
            flags.setText(String.valueOf(numflags));
        }
        else{
            int adj = getNumBombs(x,y);
            if(bombs[x][y] != 0){
//                tv.setTextColor(Color.RED);
//                tv.setBackgroundColor(Color.RED);
                revealBombs();
                running = false;
//                do some things to end game
            }
            else if(adj > 0){
                if(tv.getCurrentTextColor()!=Color.RED){
                    tv.setText(String.valueOf(adj));
                    tv.setTextColor(Color.RED);
                    tv.setBackgroundColor(Color.LTGRAY);
                    numRevealed++;
                    System.out.println(numRevealed);
                    if(numRevealed == (C_COUNT*R_COUNT)-numBombs){
                        System.out.println("WON!");
                    }
                }

            }
            else{
                DFS(tv);
            }
        }


//        TextView topleft = cell_tvs[y-1][x-1];
//        topleft.setBackgroundColor(Color.RED);

}
public void DFS(TextView v){
    Set<TextView> visited = new HashSet<>();
    Stack<TextView> stack = new Stack<>();
    stack.add(v);
    visited.add(v);
    while(!stack.empty()){
        TextView current = stack.pop();
        visited.add(current);
        if(current.getCurrentTextColor() != Color.RED){
            numRevealed++;
        }



        if(current.getText().equals(this.getString(R.string.flag))){
            numflags++;
            TextView flags = (TextView) findViewById(R.id.numFlags);
            flags.setText(String.valueOf(numflags));
            current.setText("");
        }

        Pair<Integer,Integer> index = getIndex(current);
//        System.out.println(numRevealed);
//        System.out.println("coords " +index);
        int xnew = index.first;
        int ynew = index.second;
        int adj = getNumBombs(xnew,ynew);
//        System.out.println(adj);
//        no bombs near => continue dfs
        if(adj == 0){
            //topleft
            current.setBackgroundColor(Color.LTGRAY);
            if(ynew-1 >=0 && xnew-1>=0){
                TextView tleft = cell_tvs[xnew-1][ynew-1];
                if(!visited.contains(tleft)){
                    stack.add(tleft);
                    visited.add(tleft);
                }
            }
//            left
            if(xnew - 1 >= 0){
                TextView left = cell_tvs[xnew-1][ynew];
                if(!visited.contains(left)){
                    stack.add(left);
                    visited.add(left);
                }
            }
//            botleft
            if(ynew+1<R_COUNT && xnew-1>=0){
                TextView bleft = cell_tvs[xnew-1][ynew+1];
                if(!visited.contains(bleft)){
                    stack.add(bleft);
                    visited.add(bleft);
                }
            }

//            topright
            if(ynew-1 >=0 && xnew+1 < C_COUNT){
                TextView topright = cell_tvs[xnew+1][ynew-1];
                if(!visited.contains(topright)){
                    stack.add(topright);
                    visited.add(topright);
                }
            }
    //      right
            if(xnew + 1 < C_COUNT){
                TextView right = cell_tvs[xnew+1][ynew];
                if(!visited.contains(right)){
                    stack.add(right);
                    visited.add(right);
                }
            }
            if(ynew+1<R_COUNT && xnew+1 < C_COUNT){
                TextView botright = cell_tvs[xnew+1][ynew+1];
                if(!visited.contains(botright)){
                    stack.add(botright);
                    visited.add(botright);
                }
            }
    //      top
            if(ynew - 1 >= 0){
                TextView top = cell_tvs[xnew][ynew-1];
                if(!visited.contains(top)){
                    stack.add(top);
                    visited.add(top);
                }
            }
//            bot
            if(ynew + 1 < R_COUNT){
                TextView bottom = cell_tvs[xnew][ynew+1];
                if(!visited.contains(bottom)){
                    stack.add(bottom);
                    visited.add(bottom);

                }
            }
        }
        else{
            current.setText(String.valueOf(adj));
            current.setTextColor(Color.RED);
            current.setBackgroundColor(Color.LTGRAY);
            visited.add(current);
        }
    }
    System.out.println(numRevealed);
    if(numRevealed == R_COUNT*C_COUNT - numBombs){
        System.out.println("WON!");
    }
}

private Pair<Integer,Integer> getIndex(TextView v){
    for(int i=0;i< C_COUNT;i++){
        for(int j=0;j< R_COUNT;j++){
            if(cell_tvs[i][j]==v){
                return new Pair<>(i,j);
            }
        }
    }
    return new Pair<>(-1,-1);
}

private int getNumBombs(int x,int y){
//        System.out.println("coords " + "x " + x + " y "+ y);
//        System.out.println(bombs[x-1][y-1] + " " +  bombs[x][y-1] + " " + bombs[x+1][y-1]);
//        System.out.println(bombs[x-1][y] +" x "+bombs[x+1][y]);
//        System.out.println(bombs[x-1][y+1] + " " + bombs[x][y+1] + " " + bombs[x+1][y+1]);
        int numbombs = 0;
//        leftbound
        if(x-1 >= 0){
    //           left
            if(bombs[x-1][y]!=0){
                numbombs++;
            }
        }
    //            topleft
        if(y-1 >=0 && x-1>=0){
            if(bombs[x-1][y-1]!=0){
                numbombs++;
            }
        }
    //            botleft
        if(y+1<R_COUNT && x-1>=0){
            if(bombs[x-1][y+1]!=0){
                numbombs++;
            }
        }

        if(x+1 < C_COUNT){
    //            right
            if(bombs[x+1][y]!=0){
                numbombs++;
            }
        }
    //            topright
        if(y-1 >=0 && x+1 < C_COUNT){
            if(bombs[x+1][y-1]!=0){
                numbombs++;
            }
        }
//        bot right
        if(y+1<R_COUNT && x+1 < C_COUNT){
            if(bombs[x+1][y+1]!=0){
                numbombs++;
            }
        }

    //        top
        if(y-1>=0){
            if(bombs[x][y-1]!=0){
                numbombs++;
            }
        }
    //        bottom
        if(y+1< R_COUNT){
            if(bombs[x][y+1]!=0){
                numbombs++;
            }
        }
        return numbombs;
    }
    public void runTimer(){
        final TextView timeView = (TextView) findViewById(R.id.timeView);
        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                int seconds = clock%60;
                String time = String.format("%02d", seconds/2);
                timeView.setText(time);

                if (running) {
                    clock++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    public void generateBombs(){
        for(int i =0;i<numBombs;i++){
            double randomx = Math.random() * R_COUNT;
            double randomy = Math.random()* C_COUNT;
            int x = (int) Math.floor(randomx);
            int y = (int) Math.floor(randomy);
            bombs[y][x] = 1;
            System.out.println("RANDOM " +x + " " + y);
        }
    }
    public void toggleClick(View view){
        TextView iconView = (TextView) view;
        if(flagging){
            flagging = false;
            iconView.setText(this.getString(R.string.pick));
        }
        else{
            flagging = true;
            iconView.setText(this.getString(R.string.flag));
        }
    }
    public void revealBombs(){
        for(int i = 0;i< R_COUNT;i++){
            for(int j = 0;j<C_COUNT;j++){
                if(bombs[j][i] != 0){
                    cell_tvs[j][i].setBackgroundColor(Color.RED);
                    cell_tvs[j][i].setTextColor(Color.RED);

                }
            }
        }
    }
}

