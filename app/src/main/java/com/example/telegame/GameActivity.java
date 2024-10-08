package com.example.telegame;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity{

    private String[] word_list;
    private ImageView[]telito_parts;
    private Random rd;
    private String chosenWord;
    private TextView[] charV;
    private LinearLayout wordLayout;
    //private LetterAdapter adpter;
    private ArrayList<String> correctW = new ArrayList<>();
    private ArrayList<String> clickW = new ArrayList<>();
    private int numChars;
    private int numCharsCorrect;
    private int sizeTelitoParts=6;
    private int numGame;
    private int playTime;
    private long startTime;
    private int trial;
    private boolean isOver=false;
    public String notification;
    public String name;


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

        name = getIntent().getStringExtra("Name");
        if(name != null){
            playGame();
        }
        Button btnPlayNewGame = findViewById(R.id.new_game);
        btnPlayNewGame.setOnClickListener(l->{
            if(!isOver){
                if (numGame == 1) {
                    notification += "Juego " + numGame + " : Canceló";
                } else {
                    notification += "\nJuego " + numGame + " : Canceló";
                }
            }
            playGame();
        });


    }

    //Manejo del juego
    private void playGame(){
        numGame++;

        for (ImageView imgV: telito_parts){
            imgV.setVisibility(View.INVISIBLE);
        }

        String rdWord=word_list[new Random().nextInt(word_list.length)];
        while (rdWord.equals(chosenWord))rdWord = word_list[rd.nextInt(word_list.length)];
        chosenWord = rdWord;
        numChars = chosenWord.length();

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

        configurationGame();
    }

    private void configurationGame(){
        correctW.clear();
        clickW.clear();
        //alfabeto ingles
        List<Character> alfabetoE = Arrays.asList('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');
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

        isOver= false;
        trial= 0;
        numCharsCorrect=0;
        startTime= System.currentTimeMillis();
    }

    public void tapping(View v){
        if(!isOver){
            ((Button) v).setEnabled(false);
            String l = String.valueOf(((Button) v).getText());
            clickW.add(l);

            if(wordIsCorrect(l)){
                letterFound();
            }else{
                letterNotFound();
            }
        }
    }

    private boolean wordIsCorrect(String l){
        boolean found = false;
        LinearLayout lyW = findViewById(R.id.word_game);
        SparseArray<TextView> viewArray = new SparseArray<>();
        //Optimización con SparseArray para evitar que se bloquee el programa
        for (int i = 0; i < numChars; i++) {
            viewArray.put(i, (TextView) lyW.getChildAt(i));
        }
        for (int i = 0; i < numChars; i++) {
            TextView textView = viewArray.get(i);
            if (textView != null && String.valueOf(chosenWord.charAt(i)).equals(l)) {
                textView.setTextColor(Color.BLACK);
                found = true;
                correctW.add(l);
                numCharsCorrect++;
            }
        }
        return found;
    }

    private void letterFound(){
        if(numCharsCorrect==numChars){
            finishGame(true);
        }else{
            updateMsgGame(" ");
        }
    }

    private void finishGame(boolean won){
        playTime = (int) Math.floor((double) (System.currentTimeMillis()-startTime)/1000);
        TextView msgGame = findViewById(R.id.noti_game);

        //Aquí utilice una forma de reducir el código, porque a veces se lagea la app y estoy buscando optimizarlo
        String gameMessage = won ?
                String.format("Ganó / Terminó en %ds", playTime) :
                String.format("Perdió / Terminó en %ds", playTime);
        msgGame.setText(gameMessage);

        updateStadistics(String.format("Terminó en %ds", playTime));
        isOver = true;
    }

    private void letterNotFound(){
        if (trial < telito_parts.length) {
            telito_parts[trial].setVisibility(View.VISIBLE);
            trial++;
        }
        if(trial >= telito_parts.length){
            finishGame(false);
        }else{
            String msg = " ";
            updateMsgGame(msg);
        }
    }
    private void updateStadistics(String result){
        if (numGame == 1) {
            notification = String.format("Juego %d: %s", numGame, result);
        } else {
            notification = String.format("%s\nJuego %d: %s", notification, numGame, result);
        }
    }

    private void updateMsgGame(String msg){
        TextView msgGame = findViewById(R.id.noti_game);
        msgGame.setText(msg);
    }

    public void cleanWords(String chosenWord, ArrayList<String> correctW){

        LinearLayout lyW = findViewById(R.id.word_game);
        lyW.removeAllViews();
        for(int i=0;i<chosenWord.length();i++){
            String l=String.valueOf(chosenWord.charAt(i));
            TextView copyL = new TextView(this);
            copyL.setId(View.generateViewId());
            //con chatgpt solo consulte para poder modificar los view en un layout
            LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            parameters.setMargins(12, 0, 12, 0);
            copyL.setLayoutParams(parameters);
            copyL.setText(l);
            copyL.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
            copyL.setGravity(Gravity.CENTER);
            copyL.setPadding(12,0,12,0);
            copyL.setTextColor(correctW.contains(l) ? Color.GREEN : Color.DKGRAY);
            copyL.setBackgroundResource(R.drawable.line_game);

            copyL.setTextColor(correctW.contains(l) ? Color.BLACK : Color.DKGRAY);

            lyW.addView(copyL);
        }
    }

    //Material diapositiva
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    //Manejo de las estadísticas esto fue con ayuda de la ppt y busqueda en google
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int itemId = item.getItemId();

        if(itemId == R.id.statistics){
            showStatisticsPopup(item);
            return true;
        }else if(itemId == android.R.id.home){
            finish();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    private void showStatisticsPopup(@NonNull MenuItem item){
        View menuItemV = findViewById(item.getItemId());
        PopupMenu popupMenu = new PopupMenu(this, menuItemV);
        popupMenu.getMenuInflater().inflate(R.menu.menu_statistics, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this::handlePopupMenuItemClick);
        popupMenu.show();
    }

    private boolean handlePopupMenuItemClick(@NonNull MenuItem menuItem){
        int itemId = menuItem.getItemId();
        if(itemId == R.id.query_stats){
            openStatisticsActivity();
            return true;
        }else{
            return false;
        }
    }
    private void openStatisticsActivity(){
        /*
        Intent intent = new Intent(GameActivity.this, EstadisticasActivity.class);
        startActivity(intent);
         */
        Intent intent = createStatisticsIntent();
        launcher.launch(intent);
    }
    private Intent createStatisticsIntent(){
        Intent intent = new Intent(GameActivity.this, EstadisticasActivity.class);

        intent.putExtra("startTime",startTime);
        intent.putExtra("name", name);
        intent.putExtra("estadisticas", notification);
        intent.putExtra("numGame", numGame);
        intent.putExtra("intento", trial);
        intent.putExtra("numChars", numChars);
        intent.putExtra("cantCharCorrect", numCharsCorrect);
        intent.putExtra("chosenWord",chosenWord);
        intent.putExtra("correctWords",correctW);
        intent.putExtra("clickWords",clickW);
        intent.putExtra("terminado",isOver);
        intent.putExtra("msgGame",((TextView) findViewById(R.id.noti_game)).getText().toString());
        return intent;
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                //Maneje la lógica para poder manejar el callback solo que salía errores así que decidí consultar con AI para ver cual era el error y la major manera era separando
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        boolean isNewGame = data.getBooleanExtra("NewGame", false);
                        if (isNewGame) {
                            playGame();
                        } else {
                            extractDataFromIntent(data);
                            TextView msgGame = findViewById(R.id.noti_game);
                            String msg = data.getStringExtra("msgGame");
                            msgGame.setText(msg != null ? msg : "");
                            handleGameLogic(data);
                        }
                    }
                }
            }
    );

    private void extractDataFromIntent(Intent data){
        startTime = data.getLongExtra("startTime",0);
        notification = data.getStringExtra("estadisticas");
        numGame = data.getIntExtra("numGame",0);
        trial = data.getIntExtra("intento",0);
        numChars = data.getIntExtra("numChars",0);
        numCharsCorrect = data.getIntExtra("cantCharCorrect",0);
        chosenWord = data.getStringExtra("chosenWord");
        name = data.getStringExtra("Name");
        correctW = data.getStringArrayListExtra("correctWords");
        clickW = data.getStringArrayListExtra("clickWords");
        isOver = data.getBooleanExtra("terminado", false);
    }

    private void handleGameLogic(Intent data){
        if(data.getStringExtra("NewGame") != null){
            cleanWords(chosenWord,correctW);

            for (String l:clickW){
                int rscId = getResources().getIdentifier("b"+l,"id",getPackageName());
                ((Button)findViewById(rscId)).setEnabled(false);
            }

            for(int i=0; i<telito_parts.length;i++){
                if(i<numGame){
                    telito_parts[i].setVisibility(View.VISIBLE);
                }else{
                    telito_parts[i].setVisibility(View.INVISIBLE);
                }
            }

            cleanWords(chosenWord,correctW);
        }else{
            if(!isOver){
                //probando eficiencia código para que la app no muera
                StringBuffer notificationBuffer = new StringBuffer(notification);
                if (trial != 1) {
                    notificationBuffer.append("\n");
                }
                notificationBuffer.append("Juego ").append(numGame).append(": Canceló");
                notification = notificationBuffer.toString();
            }
            playGame();
        }
    }
}
