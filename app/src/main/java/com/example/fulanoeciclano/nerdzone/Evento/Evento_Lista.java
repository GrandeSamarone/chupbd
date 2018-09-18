package com.example.fulanoeciclano.nerdzone.Evento;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.example.fulanoeciclano.nerdzone.Activits.MainActivity;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Model.Evento;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Evento_Lista extends AppCompatActivity  implements SwipeRefreshLayout.OnRefreshListener
{

    private DatabaseReference mDatabaseevento;
    private SwipeRefreshLayout swipeatualizar;
    private CircleImageView icone;
    private FirebaseAuth autenticacao;
    private FirebaseAuth mFirebaseAuth;
    private FloatingActionButton Novo_Evento;
    private RecyclerView recyclerEvento;
    private Evento_Adapter adapterevento;
    private ArrayList<Evento> ListaEvento = new ArrayList<>();
    private ChildEventListener valueEventListenerEvento;
    private LinearLayoutManager mManager;
    private Usuario user;
    private Toolbar toolbar;
    private String mPhotoUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento__lista);

       icone = findViewById(R.id.icone_img_toolbar_evento);
        toolbar = findViewById(R.id.toolbarevento);
        setSupportActionBar(toolbar);

        FirebaseUser UsuarioAtual = UsuarioFirebase.getUsuarioAtual();
        mPhotoUrl=UsuarioAtual.getPhotoUrl().toString();

        Glide.with(Evento_Lista.this)
                .load(mPhotoUrl)
                .into(icone);

        //ROLAR PARA ATUALIZAR
        swipeatualizar= findViewById(R.id.swipe_list_evento);
        swipeatualizar.setOnRefreshListener(Evento_Lista.this);

        swipeatualizar.post(new Runnable() {
            @Override
            public void run() {
                swipeatualizar.setRefreshing(true);
                RecuperarEventos();

            }
        });
        swipeatualizar.setColorSchemeResources
                (R.color.colorPrimaryDark, R.color.amareloclaro,
                        R.color.accent);

        //Configuracoes Basicas
        recyclerEvento = findViewById(R.id.lista_evento);
        mDatabaseevento = ConfiguracaoFirebase.getFirebaseDatabase().child("evento");

        //Configura Adapter
        adapterevento = new Evento_Adapter(ListaEvento,this);

        //Adapter
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerEvento.setLayoutManager(layoutManager);
        recyclerEvento.setHasFixedSize(true);
        recyclerEvento.setAdapter(adapterevento);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //Botao Voltar
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
               // NavUtils.navigateUpFromSameTask(this);
                startActivity(new Intent(this, MainActivity.class)); //O efeito ao ser pressionado do botão (no caso abre a activity)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }else{
                    finish();
                }

                break;

            default:break;
        }

        return true;
    }



    @Override
    public void onRefresh() {
        RecuperarEventos();
    }

    @Override
    public void onStop() {
        super.onStop();
        mDatabaseevento.removeEventListener(valueEventListenerEvento);
    }

    //recupera e nao deixa duplicar
    public void RecuperarEventos(){
        ListaEvento.clear();
        swipeatualizar.setRefreshing(true);
        valueEventListenerEvento =mDatabaseevento.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Evento evento = dataSnapshot.getValue(Evento.class );
                ListaEvento.add(0,evento);


                adapterevento.notifyDataSetChanged();
                swipeatualizar.setRefreshing(false);

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
