package com.example.fulanoeciclano.nerdzone.Mercado;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fulanoeciclano.nerdzone.Activits.AbrirImagem;
import com.example.fulanoeciclano.nerdzone.Activits.ChatActivity;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Model.Mercado;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.io.Serializable;
import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class Detalhe_Mercado extends AppCompatActivity {

    private CarouselView fotos;
    private FloatingActionButton fabcontato;
    private TextView titulo, legenda, descricao, endereco, categoria, estado, criador, num_rating;
    private Button botaoavaliar;
    private LinearLayout botaovoltar;
    private Mercado mercadoselecionado;
    private MaterialRatingBar ratingBar;
    private Dialog dialog;
    private String identificadorUsuario;
    private DatabaseReference database, databasemercado;
    private com.google.firebase.database.ChildEventListener ChildEventListenermercado;
    private FirebaseDatabase databases;
    private AlertDialog alerta;
    private SharedPreferences preferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_detalhe_mercados);


        //configuracoes iniciais
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        databasemercado = ConfiguracaoFirebase.getDatabase().getReference().child("comercio");
        database = ConfiguracaoFirebase.getDatabase().getReference().child("rating");
        databases = FirebaseDatabase.getInstance();
        num_rating = findViewById(R.id.mercado_num_ratings);
        ratingBar = findViewById(R.id.rate_star);
        fotos = findViewById(R.id.carousel_foto_mercado);
        titulo = findViewById(R.id.detalhe_mercado_titulo);
        legenda = findViewById(R.id.detalhe_mercado_legenda);
        criador = findViewById(R.id.detalhe_mercado_criador);
        descricao = findViewById(R.id.detalhe_mercado_descricao);
        endereco = findViewById(R.id.detalhe_mercado_endereco);
        botaovoltar = findViewById(R.id.mercado_button_back);
        botaovoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent it = new Intent(Detalhe_Mercado.this,MercadoActivity.class);
               startActivity(it);
                finish();
            }
        });



        //recuperar mercado selecionado

        mercadoselecionado = (Mercado) getIntent().getSerializableExtra("mercadoelecionado");

        if (mercadoselecionado != null) {
            titulo.setText(mercadoselecionado.getTitulo());
            legenda.setText(mercadoselecionado.getFraserapida());
            descricao.setText(mercadoselecionado.getDescricao());
            endereco.setText(mercadoselecionado.getEndereco());
            criador.setText(mercadoselecionado.getAutor());
            //   categoria.setText(mercadoselecionado.getCategoria());

            //carregar as imagens

            ImageListener imageListener = new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    String urlstring = mercadoselecionado.getFotos().get(position);

                    Glide.with(getApplicationContext())
                            .load(urlstring)
                            .into(imageView);
                }
            };

            fotos.setPageCount(mercadoselecionado.getFotos().size());
            fotos.setImageListener(imageListener);

            fotos.setImageClickListener(new ImageClickListener() {
                @Override
                public void onClick(int position) {
                    /*Bundle bundle = new Bundle();
                    bundle.putString("id", mercadoselecionado.getIdMercado());
                    Intent it = new Intent(Detalhe_Mercado.this, AbrirImagem.class);
                    it.putExtras(bundle);
                    startActivity(it);
                    */


                    List<String> ff = mercadoselecionado.getFotos();
                    Intent it = new Intent(Detalhe_Mercado.this, AbrirImagem.class);
                    it.putExtra("fotoselecionada", (Serializable) ff);
                    it.putExtra("nome", mercadoselecionado.getTitulo());
                    startActivity(it);

                }
            });

            fabcontato = findViewById(R.id.fab_entrar_em_contato);
            fabcontato.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(Detalhe_Mercado.this, ChatActivity.class);
                    it.putExtra("id", mercadoselecionado.getIdAutor());
                    startActivity(it);
                }
            });

        }

    }

    private void CarregarDados_do_Comercio() {
        ChildEventListenermercado = database.orderByChild("tipoconta").equalTo("").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Usuario perfil = dataSnapshot.getValue(Usuario.class);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        //RatingBar();
        preferences = getSharedPreferences("primeiravezdetalhe", MODE_PRIVATE);
        if (preferences.getBoolean("primeiravezdetalhe", true)) {
            preferences.edit().putBoolean("primeiravezdetalhe", false).apply();
            Dialog_click_foto();
        } else {

        }
    }

    private void Dialog_click_foto() {

        LayoutInflater li = getLayoutInflater();


        View view = li.inflate(R.layout.dialog_informar_click_foto, null);


        view.findViewById(R.id.botaoentendi).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {


                //desfaz o dialog_opcao_foto.
                dialog.dismiss();
            }
        });


        //Dialog de tela
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();

    }


    //Botao Voltar
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                } else {
                    finish();
                }


                break;

            default:
                break;
        }

        return true;
    }
/*
    private void RatingBar() {
        final DatabaseReference ref = databases.getReference("ratingbar").child("rating")
                .child(mercadoselecionado.getIdMercado());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    float rating = Float.parseFloat(dataSnapshot.getValue().toString());
                    // float d= (float) ((number*5) /100);
                    ratingBar.setRating(rating);
                    mercadoselecionado.setTotalrating(rating+1);
                    Toast.makeText(Detalhe_Mercado.this, "total"+mercadoselecionado.getTotalrating(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) ref.setValue(rating);
            }
        });
   }
  */


}