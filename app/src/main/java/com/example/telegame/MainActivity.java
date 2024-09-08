package com.example.telegame;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.res.TypedArray;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button startX;
    EditText editX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "oncreate");
        setContentView(R.layout.activity_main);

        startX = (Button) findViewById(R.id.buttonStartGame);
        startX.setOnClickListener(this);
        startX.setEnabled(false);

        this.editX = (EditText) findViewById(R.id.editarNombre);
        //Fuente: YT
        //Permite controlar que la activación del boton de jugar para los casos que se escriba antes, durante y después de ingresar el nombre
        editX.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    startX.setEnabled(false);
                }else{
                    startX.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("")){
                    startX.setEnabled(false);
                }else{
                    startX.setEnabled(true);
                }
            }
        });
        /*

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

         */
        getSupportActionBar().setTitle("APPSIoT - Lab 2");

        registerForContextMenu(findViewById(R.id.title_game));

        Button buttonStart = findViewById(R.id.buttonStartGame);
        buttonStart.setOnClickListener(view -> {
           irAlJuego(view);
        });
    }

    @Override
    public void onClick(View v){
        if(v.getId()==R.id.buttonStartGame){
            editX.setText("");
        }
    }

    //Para el context menu (ppt)
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,v,menuInfo);
        getMenuInflater().inflate(R.menu.menu_title_game,menu);
    }

    //gestión de eventos del context menu (ppt)
    //@Override
    public boolean onContextItemSelected(@NonNull MenuItem item){
        if(item.getItemId() == R.id.opt_green){
            ((TextView) findViewById(R.id.title_game)).setTextColor(getColor(R.color.green));
        }else {
            if (item.getItemId() == R.id.opt_red) {
                ((TextView) findViewById(R.id.title_game)).setTextColor(getColor(R.color.red));
            } else {
                if (item.getItemId() == R.id.opt_purple) {
                    ((TextView) findViewById(R.id.title_game)).setTextColor(getColor(R.color.purple));
                }
            }
        }
        return super.onContextItemSelected(item);
    }

    //Va al juego
    public void irAlJuego(View view){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("Name",((TextView) findViewById(R.id.editarNombre)).getText().toString());
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