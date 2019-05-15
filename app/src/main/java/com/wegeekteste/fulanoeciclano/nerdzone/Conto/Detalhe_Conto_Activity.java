package com.wegeekteste.fulanoeciclano.nerdzone.Conto;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.varunest.sparkbutton.SparkButton;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Conto;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

public class Detalhe_Conto_Activity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView titulo,mensagem,author;
    private SparkButton botao_curtir,botao_add_colecao;
    private DatabaseReference database,database_conto;
    private Conto contoselecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe__conto_);

        toolbar = findViewById(R.id.toolbar_detalhe_conto);
        setSupportActionBar(toolbar);

        titulo= findViewById(R.id.conto_titulo_detalhe);
        mensagem = findViewById(R.id.conto_mensagem_detalhe);
        author = findViewById(R.id.conto_author_detalhe);
        botao_curtir = findViewById(R.id.botaocurtirconto_detalhe);
        botao_add_colecao = findViewById(R.id.botao_add_a_colecao_detalhe);

        database = ConfiguracaoFirebase.getDatabase().getReference().child("usuarios");
        database_conto = ConfiguracaoFirebase.getDatabase().getReference().child("conto");

        contoselecionado = (Conto)  getIntent().getSerializableExtra("contoselecionado");

        if(contoselecionado!=null){
        titulo.setText(contoselecionado.getTitulo());
        mensagem.setText(contoselecionado.getMensagem());

        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
