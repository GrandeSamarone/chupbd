package com.example.fulanoeciclano.nerdzone.Activits;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.fulanoeciclano.nerdzone.R;

public class teste extends AppCompatActivity {

    private TextView id;
    private String id_do_usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste);
        id_do_usuario=getIntent().getStringExtra("id");
        id = findViewById(R.id.id);
        id.setText(id_do_usuario);
    }
}
