package com.example.fulanoeciclano.nerdzone.Activits;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.fulanoeciclano.nerdzone.Adapter.AdapterPagInicial.EventoAdapterPagInicial;
import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Model.Evento;
import com.example.fulanoeciclano.nerdzone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class Meus_eventos extends AppCompatActivity {

    private RecyclerView recyclerView_meus_eventos;
    private DatabaseReference database;
    private Query meus_eventosref;
    private String identificadoUsuario;
    private Evento evento;
    private AlertDialog alerta;
    private List<Evento> lista_Meus_Eventos=new ArrayList<>();
    private EventoAdapterPagInicial mAdapter;
    private ChildEventListener childEventListenerMeus_Eventos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_eventos);


        //Configurações Originais
        database = FirebaseDatabase.getInstance().getReference();
        evento = new Evento();

        identificadoUsuario= UsuarioFirebase.getIdentificadorUsuario();
        meus_eventosref=database.child("evento").orderByChild("uid").equalTo(getUid());
        Log.i("putaquepariu",getUid());
         mAdapter = new EventoAdapterPagInicial(lista_Meus_Eventos,Meus_eventos.this);
        recyclerView_meus_eventos = findViewById(R.id.recycler_meus_eventos);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Meus_eventos.this);
       recyclerView_meus_eventos.setLayoutManager(layoutManager);
       recyclerView_meus_eventos.setHasFixedSize(true);
       recyclerView_meus_eventos.setAdapter(mAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        RecuperarMeus_Eventos();
    }

    private void RecuperarMeus_Eventos() {
        //Progress
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Carregando eventos");
        progressDialog.show();
        lista_Meus_Eventos.clear();
        childEventListenerMeus_Eventos = meus_eventosref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Evento evento =dataSnapshot.getValue(Evento.class);
            lista_Meus_Eventos.add(evento);
            mAdapter.notifyDataSetChanged();
            progressDialog.dismiss();
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
    public void onStop() {
        super.onStop();
        meus_eventosref.removeEventListener(childEventListenerMeus_Eventos);
    }
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
