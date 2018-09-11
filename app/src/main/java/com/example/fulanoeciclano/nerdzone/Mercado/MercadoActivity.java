package com.example.fulanoeciclano.nerdzone.Mercado;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.example.fulanoeciclano.nerdzone.Activits.MainActivity;
import com.example.fulanoeciclano.nerdzone.Adapter.MercadoAdapter;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Helper.RecyclerItemClickListener;
import com.example.fulanoeciclano.nerdzone.Model.Mercado;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MercadoActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private Toolbar toolbar;
    private FloatingActionButton novoMercado;
    private FirebaseAuth autenticacao;
    private SwipeRefreshLayout refresh;
    private RecyclerView recyclerViewMercadoPublico;
    private MercadoAdapter adapter;
    private DatabaseReference mercadopublico;
    private ChildEventListener valueMercadoListener;
    private Mercado mercado;
    private List<Mercado> listamercado = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_mercado);

        toolbar = findViewById(R.id.toolbarprincipal);
        toolbar.setTitle(R.string.mercado);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Configuraçoes iniciais
        mercado = new Mercado();
        refresh = findViewById(R.id.refreshmercado);
        refresh.setOnRefreshListener(this);
        refresh.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(true);
                RecuperarMercadoPublicos();
            }
        });
        refresh.setColorSchemeResources
                (R.color.colorPrimaryDark, R.color.amareloclaro,
                        R.color.accent);

        mercadopublico = ConfiguracaoFirebase.getFirebaseDatabase().child("mercado");
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        recyclerViewMercadoPublico = findViewById(R.id.recycleviewmercado);

        //recycleview
        recyclerViewMercadoPublico.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMercadoPublico.setHasFixedSize(true);
        adapter = new MercadoAdapter(listamercado, this);
        recyclerViewMercadoPublico.setAdapter(adapter);

        novoMercado = findViewById(R.id.buton_novo_mercado);
        novoMercado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MercadoActivity.this, Cadastrar_Novo_MercadoActivity.class);
                startActivity(it);


            }
        });
       //Aplicar Evento click
        recyclerViewMercadoPublico.addOnItemTouchListener(new RecyclerItemClickListener(this,
                recyclerViewMercadoPublico, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Mercado mercadoselecionado = listamercado.get(position);
                Intent it = new Intent(MercadoActivity.this,Detalhe_Mercado.class);
                it.putExtra("mercadoelecionado",mercadoselecionado);
                startActivity(it);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
    }

    //Botao Voltar
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                Intent it = new Intent(MercadoActivity.this, MainActivity.class);
                startActivity(it);

                break;

            default:
                break;
        }

        return true;
    }



    public void onStop() {
        super.onStop();
        mercadopublico.removeEventListener(valueMercadoListener);
    }

    public void RecuperarMercadoPublicos() {

        listamercado.clear();
        refresh.setRefreshing(true);
      valueMercadoListener = mercadopublico.addChildEventListener(new ChildEventListener() {
          @Override
          public void onChildAdded(DataSnapshot dataSnapshot, String s) {
              for(DataSnapshot loja:dataSnapshot.getChildren()){
                  for(DataSnapshot artista:loja.getChildren()){
                      for(DataSnapshot mercados:artista.getChildren()){

                          Mercado mercado = mercados.getValue(Mercado.class);
                          listamercado.add(mercado);

                          Collections.reverse(listamercado);
                          adapter.notifyDataSetChanged();
                          refresh.setRefreshing(false);

                      }
                  }
              }
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
    public void onRefresh() {
        refresh.setRefreshing(true);
        RecuperarMercadoPublicos();
    }
}
