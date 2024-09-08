package com.example.telegame;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EstadisticasActivity extends AppCompatActivity {
    //Bundle sirve para pasar información entre vistas (GOOGLEADO)
    private Bundle intentExtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        intentExtras = getIntent().getExtras();
        playerStatistics();
        btnNewNewGame();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent resultIntent = new Intent();
            resultIntent.putExtras(intentExtras);
            setResult(RESULT_OK, resultIntent);
            finish();
            return true;
        }else{
            return false;
        }
    }

    private void playerStatistics() {
        String playerName = intentExtras.getString("name");
        ((TextView) findViewById(R.id.name)).setText("Jugador: " + playerName);

        String statistics = intentExtras.getString("estadisticas");
        if (!statistics.isEmpty()) {
            ((TextView) findViewById(R.id.stats)).setText(statistics);
        } else {
            ((TextView) findViewById(R.id.stats)).setText("...");
        }
    }

    private void btnNewNewGame() {
        Button btnNewNewGame = findViewById(R.id.btnNewNewGame);
        btnNewNewGame.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtras(intentExtras);
            resultIntent.putExtra("NuevoJuego", "");
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
