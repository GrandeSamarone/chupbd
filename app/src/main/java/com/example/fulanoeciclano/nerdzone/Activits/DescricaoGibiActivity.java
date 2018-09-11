package com.example.fulanoeciclano.nerdzone.Activits;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fulanoeciclano.nerdzone.Adapter.GibiAdapter;
import com.example.fulanoeciclano.nerdzone.Model.Gibi;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.List;

public class DescricaoGibiActivity extends AppCompatActivity {

   private SimpleDraweeView desc_img_icone;
   private TextView desc_nome_icone;
   private TextView desc_desc_icone;
   private TextView toolbar_nome;
   private TextView Re_saga;
   private Button verOnline;
   private RecyclerView rec_relacionado;
   private DatabaseReference GibisRelacionadoMarvel;
    private GibiAdapter adapterRelacionadoMarvel;
    private ChildEventListener valueEventListenerGibiMarvel;
    private DatabaseReference GibisRelacionadoDC;
    private GibiAdapter adapterRelacionadoDC;
    private ChildEventListener valueEventListenerGibiDC;
    private DatabaseReference GibisRelacionadoOutros;
    private GibiAdapter adapterRelacionadoOutros;
    private ChildEventListener valueEventListenerGibiOutros;
   private List<Gibi> lista_gibi = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descricao_gibi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Configuracao Inicial
        desc_img_icone=findViewById(R.id.descricao_icone_gibi);
        verOnline = findViewById(R.id.descricao_btn_online);
        desc_desc_icone = findViewById(R.id.descricao_descricao_gibi);
        desc_nome_icone = findViewById(R.id.descricao_nome_gibi);
        Re_saga = findViewById(R.id.descricao_saga);
        toolbar_nome = findViewById(R.id.toolbar_gibi_nome);
        rec_relacionado= findViewById(R.id.recycleview_relacionado_gibi);

        //Configuracoes Originais
        GibisRelacionadoMarvel= FirebaseDatabase.getInstance().getReference();
        GibisRelacionadoDC = FirebaseDatabase.getInstance().getReference();
        GibisRelacionadoOutros = FirebaseDatabase.getInstance().getReference();

       //Configurar Adapter
        adapterRelacionadoMarvel=new GibiAdapter(lista_gibi,this);
        adapterRelacionadoDC=new GibiAdapter(lista_gibi,this);
        adapterRelacionadoOutros=new GibiAdapter(lista_gibi,this);

        RecyclerView.LayoutManager layoutManagerMarvel = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        rec_relacionado.setLayoutManager(layoutManagerMarvel);
        rec_relacionado.setHasFixedSize(true);
        rec_relacionado.setAdapter(adapterRelacionadoMarvel);
        rec_relacionado.setAdapter(adapterRelacionadoDC);
        rec_relacionado.setAdapter(adapterRelacionadoOutros);



        ReceberInfoIntent();

    verOnline.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                getIntent().hasExtra("url");
                getIntent().hasExtra("icone");
                String url = getIntent().getStringExtra("url");
                String icone = getIntent().getStringExtra("icone");
                Intent it = new Intent(DescricaoGibiActivity.this,AbrirImagemHQ.class);
                it.putExtra("url",url);
                it.putExtra("icone",icone);
                startActivity(it);
            }else {
                getIntent().hasExtra("url");
                String url = getIntent().getStringExtra("url");
                Intent it = new Intent(DescricaoGibiActivity.this, AbrirImagemHQ.class);
                it.putExtra("url", url);
                startActivity(it);
            }
        }
    });
    }

    //Recebendo Informacoes do Gibi Adapter
    private void ReceberInfoIntent(){

        if(getIntent().hasExtra("imagem_url") && getIntent().hasExtra("nome")
                && getIntent().hasExtra("descricao")){


            String imageUrl = getIntent().getStringExtra("imagem_url");
            String nome = getIntent().getStringExtra("nome");
            String saga = getIntent().getStringExtra("saga");
            String descricao = getIntent().getStringExtra("descricao");
            String url = getIntent().getStringExtra("url");


            setImagem(imageUrl, nome,descricao,saga);

        }
    }
    //Inserindo Informacoes
    private void setImagem(String imagemUrl, String nome, String descricao, String saga){
        desc_nome_icone.setText(nome);
        toolbar_nome.setText(nome);
        Re_saga.setText(saga);
        Log.i("saga", String.valueOf(saga));


        desc_desc_icone.setText(descricao);

       //SimpleDraweeView image = findViewById(R.id.image);
        Glide.with(DescricaoGibiActivity.this)
                .load(imagemUrl)
                .into(desc_img_icone );

    }
    public void onStart() {
        super.onStart();
      RecuperarGibiRelacionadoMarvel();
      RecuperarGibiRelacionadoDC();
        RecuperarGibiRelacionadoOutros();
    }


    @Override
    public void onStop() {
        super.onStop();
       GibisRelacionadoMarvel.removeEventListener(valueEventListenerGibiMarvel);
       GibisRelacionadoDC.removeEventListener(valueEventListenerGibiDC);
        GibisRelacionadoOutros.removeEventListener(valueEventListenerGibiOutros);

    }

    public void RecuperarGibiRelacionadoMarvel(){

        getIntent().hasExtra("saga");

        String sagaSelecionado = getIntent().getStringExtra("saga");
        Log.i("sagaselecionado", String.valueOf(sagaSelecionado));
        lista_gibi.clear();

       // Log.i("getNome", String.valueOf(GibisRelacionado.getRef().child(getPackageName())));

        valueEventListenerGibiMarvel=  GibisRelacionadoMarvel.getRef().child("Marvel").orderByChild("saga").equalTo(sagaSelecionado).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Gibi gibi = dataSnapshot.getValue(Gibi.class );
                gibi.setKey(dataSnapshot.getKey());
              lista_gibi.add(0,gibi);
                adapterRelacionadoMarvel.notifyDataSetChanged();

              //
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

    private void RecuperarGibiRelacionadoDC(){
        getIntent().hasExtra("saga");
        String sagaSelecionado = getIntent().getStringExtra("saga");
        Log.i("sagaselecionado", String.valueOf(sagaSelecionado));
        lista_gibi.clear();

        // Log.i("getNome", String.valueOf(GibisRelacionado.getRef().child(getPackageName())));

        valueEventListenerGibiDC=  GibisRelacionadoDC.getRef().child("DC").orderByChild("saga").equalTo(sagaSelecionado).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Gibi gibi = dataSnapshot.getValue(Gibi.class );
                gibi.setKey(dataSnapshot.getKey());
                lista_gibi.add(0,gibi);
                adapterRelacionadoDC.notifyDataSetChanged();

                //
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

    private void RecuperarGibiRelacionadoOutros(){
        getIntent().hasExtra("saga");
        String sagaSelecionado = getIntent().getStringExtra("saga");
        Log.i("sagaselecionado", String.valueOf(sagaSelecionado));
        lista_gibi.clear();

        // Log.i("getNome", String.valueOf(GibisRelacionado.getRef().child(getPackageName())));

        valueEventListenerGibiOutros=  GibisRelacionadoOutros.getRef().child("Outros").orderByChild("saga").equalTo(sagaSelecionado).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Gibi gibi = dataSnapshot.getValue(Gibi.class );
                gibi.setKey(dataSnapshot.getKey());
                lista_gibi.add(0,gibi);
                adapterRelacionadoOutros.notifyDataSetChanged();

                //
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
}
