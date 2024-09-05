package com.example.telegame;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "oncreate");
        /*
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

         */
        Button buttonStart = findViewById(R.id.buttonStartGame);
        buttonStart.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            launcher.launch(intent);
        });
    }

    //Va al juego
    public void irAlJuego(View view) {

        Intent intent = new Intent(this, GameActivity.class);

        startActivity(intent);

    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    String nombre = data.getStringExtra("nombre");
                    Toast.makeText(MainActivity.this,
                            "El nombre recibido es: " + nombre    ,
                            Toast.LENGTH_LONG).show();
                }
            }
    );
}