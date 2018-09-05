package com.example.fulanoeciclano.nerdzone.Evento;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fulanoeciclano.nerdzone.Model.Comentario;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.vanniktech.emoji.EmojiEditText;

import java.util.ArrayList;
import java.util.List;

public class DetalheEvento extends AppCompatActivity {

    private DatabaseReference database;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private DatabaseReference mCommentsReference;
    private ChildEventListener childEventListener;
    private String identificadorUsuario;
    private TextView Author_evento_View,toolbarnome_evento;
    private SimpleDraweeView AuthorFoto_evento_View,eventobanner;
    private TextView titutoevento,datainicial,datafinal;
    private TextView mensagem_evento;
    private EmojiEditText edit_chat_emoji;
    private ImageView botao_icone;
    private  android.support.v7.widget.AppCompatButton botao_env_msg;
    private DatabaseReference mDatabase;
    private static RecyclerView recyclerViewcomentarios;
    private Usuario usuarioLogado;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener valueEventListenerIcone;
    private DatabaseReference Recu_icone;
    private DatabaseReference ref;
    private String mCommentIds;
    private List<Comentario> listcomentario = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_evento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //configuracoes iniciais
        collapsingToolbarLayout = findViewById(R.id.root);


    }

}
