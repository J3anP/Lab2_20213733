package com.example.telegame;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class EstadisticasActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        intent = getIntent();
        playerStatistics();
        btnNewNewGame();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent resultIntent = new Intent();
            resultIntent.putExtras(intent.getExtras());
            setResult(RESULT_OK, resultIntent);
            finish();
            return true;
        }else{
            return false;
        }
    }

    private void playerStatistics() {
        String playerName = intent.getStringExtra("name");
        TextView playerNameTV = findViewById(R.id.name);
        playerNameTV.setText("Jugador: " + playerName);

        TextView textVStatistics = findViewById(R.id.stats);
        String playerStats = intent.getStringExtra("estadisticas");

        if(!TextUtils.isEmpty(playerStats)){
            textVStatistics.setText(playerStats);
        }else{
            textVStatistics.setText(R.string.no_stats_available);
        }

    }

    private void btnNewNewGame() {
        Button btnNewNewGame = findViewById(R.id.btnNewNewGame);
        btnNewNewGame.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtras(intent.getExtras());
            resultIntent.putExtra("NewGame", true);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
