package com.example.telegame;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityOptionsCompat;

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
        Log.d(TAG, "ongame");

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
                notification=notification+(numGame == 1 ? "" : "\n")+"Juego "+numGame+" : Canceló";
            }
            playGame();
        });


    }

    //Manejo del juego
    private void playGame(){
        numGame++;
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

        for (ImageView imgV: telito_parts){
            imgV.setVisibility(View.INVISIBLE);
        }

        ((TextView) findViewById(R.id.noti_game)).setText("Qué comience el juego :D");

        //configuración del juego
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

        startTime= System.currentTimeMillis();
        isOver= false;
        trial= 0;
        numCharsCorrect=0;
        Log.d(TAG,notification);
    }

    public void tapping(View v){
        if(!isOver){
            ((Button) v).setEnabled(false);
            String l = String.valueOf(((Button) v).getText());
            clickW.add(l);

            boolean isCorrect = wordIsCorrect(l);

            if(isCorrect){
                letterFound();
            }else{
                letterNotFound();
            }
        }
    }

    private boolean wordIsCorrect(String l){
        boolean found = false;
        LinearLayout lyW = findViewById(R.id.word_game);

        for(int i= 0; i<numChars;i++) {
            if(String.valueOf(chosenWord.charAt(i)).equals(l)){
                ((TextView)lyW.getChildAt(i)).setTextColor(Color.BLACK);
                found=true;
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
    
        if(won){
            msgGame.setText("Ganó / Terminó en "+playTime+"s");
            updateStadistics("Terminó en "+ playTime+"s");
        }else{
            msgGame.setText("Perdió / Terminó en "+playTime+"s");
            updateStadistics("Terminó en "+ playTime+"s");
        }
        isOver = true;
    }

    private void letterNotFound(){
        telito_parts[trial].setVisibility(View.VISIBLE);
        trial++;
        if(trial == telito_parts.length){
            finishGame(false);
        }else{
            String msg = " ";
            updateMsgGame(msg);
        }
    }
    private void updateStadistics(String result){
        notification = notification + (numGame == 1 ? "" : "\n") + "Juego "+numGame + ": " + result;
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
            parameters.weight = 0;
            copyL.setLayoutParams(parameters);

            copyL.setText(l);
            copyL.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
            copyL.setGravity(Gravity.CENTER);
            copyL.setPadding(12,0,12,0);

            copyL.setTextColor(correctW.contains(l) ? Color.GREEN : Color.DKGRAY);

            copyL.setBackgroundResource(R.drawable.line_game);

            if(correctW.contains(l)){
                copyL.setTextColor(Color.BLACK);
            }else{
                copyL.setTextColor(Color.TRANSPARENT);
            }

            lyW.addView(copyL);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    //Manejo de las estadísticas esto fue con ayuda de YT y busqueda en google
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
                //Maneje la lógica para poder manejar el callback solo que salía errores así que decidí poner el código en blackBox y obtuve esto
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if(data.getStringExtra("NewGame") != null){
                        playGame();
                    }else{
                        extractDataFromIntent(data);
                        updateUI(data);
                        handleGameLogic(data);
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

    private void updateUI(Intent data){
        TextView msgGame = findViewById(R.id.noti_game);
        msgGame.setText(data.getStringExtra("msgGame"));
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
                notification = notification + ((trial == 1) ? "" : "\n") + "Juego "+numGame+": Canceló";
            }
            playGame();
        }
    }
}
