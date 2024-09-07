package com.example.telegame;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private String[] word_list;
    private ImageView[]telito_parts;
    private Random rd;
    private String chosenWord;
    private TextView[] charV;
    private LinearLayout wordLayout;
    //private LetterAdapter adpter;
    private ArrayList<String> correctW = new ArrayList<>();
    private ArrayList<String> incorrectW = new ArrayList<>();
    private int numCorr;
    private int numCharsCorrect;
    private int sizeTelitoParts=6;
    private int currPart;
    private int numGame;
    private int playTime;
    private long startTime;
    private int trial;
    private boolean isOver=false;
    public String notification="";
    public String name="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        word_list = getResources().getStringArray(R.array.words_game);
        wordLayout=findViewById(R.id.word_game);


        telito_parts=new ImageView[sizeTelitoParts];
        telito_parts[0] = findViewById(R.id.head);
        telito_parts[1] = findViewById(R.id.torso);
        telito_parts[2] = findViewById(R.id.rightarm);
        telito_parts[3] = findViewById(R.id.leftarm);
        telito_parts[4] = findViewById(R.id.leftleg);
        telito_parts[5] = findViewById(R.id.rightleg);

        Button btnPlayNewGame = findViewById(R.id.new_game);
        btnPlayNewGame.setOnClickListener(l->{
            if(!isOver){
                notification=notification+(numGame==1?""?:"\n")+"Juego "+numGame+" : Canceló";
            }

            playGame();
        });

    }

    //Manejo del juego
    private void playGame(){
        numGame++;

        String rdWord=word_list[rd.nextInt(word_list.length)];
        while (rdWord.equals(chosenWord))rdWord = word_list[rd.nextInt(word_list.length)];
        chosenWord = rdWord;

        charV = new TextView[chosenWord.length()];
        wordLayout.removeAllViews();
        for (int i=0; i<chosenWord.length();i++){
            charV[i] = new TextView(this);
            charV[i].setText(""+chosenWord.charAt(i));
            charV[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            charV[i].setGravity(Gravity.CENTER);
            charV[i].setTextColor(Color.TRANSPARENT);
            charV[i].setBackgroundResource(R.drawable.line_game);
            wordLayout.addView(charV[i]);
        }

        for (ImageView imgV: telito_parts){
            imgV.setVisibility(View.INVISIBLE);
            //Para que se vea las imagenes de las partes del teleco xd
        }

        ((TextView) findViewById(R.id.noti_game)).setText("Qué comience el juego :D");

        //configuración del juego
        configurationGame();


    }

    private void configurationGame(){
        correctW.clear();
        incorrectW.clear();
        //alfabeto ingles
        List<Character> alfabetoE = Arrays.asList('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','X','Y','Z');
        for (char letra:alfabetoE){
            String btnId = "b"+letra;
            int rscId = getResources().getIdentifier(btnId,"id",getPackageName());

            if(rscId != 0){
                View btn = findViewById(rscId);

                if(btn instanceof Button){
                    ((Button) btn).setEnabled(true);
                }
            }
        }

        startTime= System.currentTimeMillis();

        trial = 0;
        numCharsCorrect=0;
        isOver = false;

        Log.d(TAG,notification);

    }




}
