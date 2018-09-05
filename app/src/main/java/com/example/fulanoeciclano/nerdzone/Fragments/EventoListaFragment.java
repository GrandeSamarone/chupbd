package com.example.fulanoeciclano.nerdzone.Fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Evento.Evento_Adapter;
import com.example.fulanoeciclano.nerdzone.Model.Evento;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventoListaFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{

    private DatabaseReference mDatabaseevento;
    private SwipeRefreshLayout swipeatualizar;
    private FirebaseAuth autenticacao;
    private FirebaseAuth mFirebaseAuth;
    private FloatingActionButton Novo_Evento;
    private RecyclerView recyclerEvento;
    private Evento_Adapter adapterevento;
    private ArrayList<Evento> ListaEvento = new ArrayList<>();
    private ChildEventListener valueEventListenerEvento;
    private LinearLayoutManager mManager;
    private Usuario user;

    public EventoListaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Fresco.initialize(getContext());
           View view=  inflater.inflate(R.layout.fragment_evento_lista, container, false);

        //ROLAR PARA ATUALIZAR
        swipeatualizar= view.findViewById(R.id.swipe_list_evento);
        swipeatualizar.setOnRefreshListener(EventoListaFragment.this);

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
        recyclerEvento = view.findViewById(R.id.lista_evento);
        mDatabaseevento = ConfiguracaoFirebase.getFirebaseDatabase().child("evento");

        //Configura Adapter
        adapterevento = new Evento_Adapter(ListaEvento,getActivity());

        //Adapter
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        recyclerEvento.setLayoutManager(layoutManager);
        recyclerEvento.setHasFixedSize(true);
        recyclerEvento.setAdapter(adapterevento);

        return view;
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
