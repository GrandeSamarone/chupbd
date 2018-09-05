package com.example.fulanoeciclano.nerdzone.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fulanoeciclano.nerdzone.Adapter.AdapterPagInicial.AdapterMercado;
import com.example.fulanoeciclano.nerdzone.Adapter.EventoAdapter;
import com.example.fulanoeciclano.nerdzone.Adapter.GibiAdapter;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Helper.HeaderDecoration;
import com.example.fulanoeciclano.nerdzone.Model.Evento;
import com.example.fulanoeciclano.nerdzone.Model.Gibi;
import com.example.fulanoeciclano.nerdzone.Model.Mercado;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class InicioFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerViewListaGibiMercado;
    private RecyclerView recyclerViewListaGibiDC;
    private RecyclerView recyclerViewListaGibiOutros;
    private RecyclerView recyclerVieweventos;
    private GibiAdapter adapterDC,adapterOutros;
    private AdapterMercado adapterMercado;
    private EventoAdapter adapterEvento;
    private ArrayList<Mercado> ListaGibiMercado = new ArrayList<>();
    private ArrayList<Gibi> ListaGibiDC = new ArrayList<>();
    private ArrayList<Gibi> ListaGibiOutros = new ArrayList<>();
    private ArrayList<Evento> ListaEvento = new ArrayList<>();
    private ArrayList<String> mKeys = new ArrayList<>();
    private DatabaseReference GibiMercado;
    private DatabaseReference GibiDC;
    private DatabaseReference GibiOutros;
    private DatabaseReference GibiEventos;
    private ChildEventListener valueEventListenerMercado;
    private ChildEventListener valueEventListenerEvento;
    private ChildEventListener valueEventListenerDC;
    private ChildEventListener valueEventListenerOutros;
    private SwipeRefreshLayout swipeatualizar;


    public InicioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Fresco.initialize(getContext());
        View view= inflater.inflate(R.layout.fragment_inicio, container, false);

        //Configuracoes Iniciais
        swipeatualizar= view.findViewById(R.id.swipe_inicial);

        swipeatualizar.setOnRefreshListener(InicioFragment.this);

        swipeatualizar.post(new Runnable() {
            @Override
            public void run() {
          swipeatualizar.setRefreshing(true);
                RecuperarMercado();
                RecuperarDC();
                RecuperarOutros();
                RecuperarEvento();
             }
        });
        swipeatualizar.setColorSchemeResources
                (R.color.colorPrimaryDark, R.color.amareloclaro,
                        R.color.accent);



        recyclerViewListaGibiDC = view.findViewById(R.id.RecycleViewGibiDC);
        recyclerViewListaGibiOutros = view.findViewById(R.id.RecycleViewGibiOutros);
        recyclerViewListaGibiMercado = view.findViewById(R.id.RecycleViewMercado);
        recyclerVieweventos = view.findViewById(R.id.RecycleViewEventos);

        GibiMercado = ConfiguracaoFirebase.getFirebaseDatabase().child("mercado");
        GibiDC = ConfiguracaoFirebase.getFirebaseDatabase().child("DC");
        GibiOutros = ConfiguracaoFirebase.getFirebaseDatabase().child("Outros");
        GibiEventos = ConfiguracaoFirebase.getFirebaseDatabase().child("evento");

        //Configurar Adapter
        adapterMercado=new AdapterMercado(ListaGibiMercado,getActivity());
        adapterDC = new GibiAdapter(ListaGibiDC,getActivity());
        adapterOutros = new GibiAdapter(ListaGibiOutros,getActivity());
        adapterEvento = new EventoAdapter(ListaEvento,getActivity());

        //Configurar recycleView Evento
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getActivity(), LinearLayoutManager.HORIZONTAL,false);
        recyclerVieweventos.setLayoutManager(layoutManager);
        recyclerVieweventos.setHasFixedSize(true);
        recyclerVieweventos.setAdapter(adapterEvento);
        recyclerVieweventos.addItemDecoration(new HeaderDecoration(getContext(),
                recyclerVieweventos,  R.layout.header_evento));





        //Configurar recycleView Marvel
        RecyclerView.LayoutManager layoutManagerMarvel = new LinearLayoutManager
                (getActivity(), LinearLayoutManager.HORIZONTAL,false);
        recyclerViewListaGibiMercado.setLayoutManager(layoutManagerMarvel);
        recyclerViewListaGibiMercado.setHasFixedSize(true);
        recyclerViewListaGibiMercado.setAdapter(adapterMercado);
        recyclerViewListaGibiMercado.addItemDecoration(new HeaderDecoration(getContext(),
                recyclerViewListaGibiMercado,  R.layout.header_evento));


           //Configurar recycleView DC
        RecyclerView.LayoutManager layoutManagerdc = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        recyclerViewListaGibiDC.setLayoutManager(layoutManagerdc);
        recyclerViewListaGibiDC.setHasFixedSize(true);
        recyclerViewListaGibiDC.setAdapter(adapterDC);

        //Configurar recycleView Outros
        RecyclerView.LayoutManager layoutManageroutros = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        recyclerViewListaGibiOutros.setLayoutManager(layoutManageroutros);
        recyclerViewListaGibiOutros.setHasFixedSize(true);
        recyclerViewListaGibiOutros.setAdapter(adapterOutros);




        /*recyclerViewListaGibiMarvel.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(), recyclerViewListaGibiMarvel, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, int position) {

              remove(ListaGibiMarvel.get(position));
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
        */
        return  view;
    }

    //puxar e atualiza
    @Override
    public void onRefresh() {
        RecuperarMercado();
        RecuperarDC();
        RecuperarOutros();
        RecuperarEvento();

    }

    /*@Override
    public void onStart() {
        super.onStart();
        RecuperarMarvel();
        RecuperarDC();
        RecuperarOutros();
        RecuperarEvento();
    }
*/

    @Override
    public void onStop() {
        super.onStop();
        GibiMercado.removeEventListener(valueEventListenerMercado);
        GibiDC.removeEventListener(valueEventListenerDC);
        GibiOutros.removeEventListener(valueEventListenerOutros);
        GibiEventos.removeEventListener(valueEventListenerEvento);
    }


    //recupera e nao deixa duplicar
    public void RecuperarEvento(){
        ListaEvento.clear();
        swipeatualizar.setRefreshing(true);
        valueEventListenerEvento =GibiEventos.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Evento evento = dataSnapshot.getValue(Evento.class );
                ListaEvento.add(0,evento);


                adapterEvento.notifyDataSetChanged();
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



    //recupera e nao deixa duplicar
     public void RecuperarMercado(){
       ListaGibiMercado.clear();
         swipeatualizar.setRefreshing(true);
       valueEventListenerMercado =GibiMercado.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot loja:dataSnapshot.getChildren()){
                    for(DataSnapshot artista:loja.getChildren()){
                        for(DataSnapshot mercados:artista.getChildren()){

                            Mercado mercado = mercados.getValue(Mercado.class);
                            ListaGibiMercado.add(mercado);

                            Collections.reverse(ListaGibiMercado);
                            adapterMercado.notifyDataSetChanged();

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
         swipeatualizar.setRefreshing(false);
    }
    public void remove(Gibi gibi){
      //  GibiMarvel.child(gibi.getKey()).removeValue();
    }
    //recupera e nao deixa duplicar DC
    public void RecuperarDC(){
        ListaGibiDC.clear();
        swipeatualizar.setRefreshing(true);
        valueEventListenerDC =GibiDC.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Gibi gibi = dataSnapshot.getValue(Gibi.class );
                gibi.setKey(dataSnapshot.getKey());
                ListaGibiDC.add(0,gibi);
                adapterDC.notifyDataSetChanged();
                swipeatualizar.setRefreshing(false);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Gibi gibi =dataSnapshot.getValue(Gibi.class);
                String key =dataSnapshot.getKey();
                for(Gibi gb:ListaGibiDC){
                    if(gb.getKey().equals(key)){
                        gb.setValues(gibi);
                        break;
                    }
                }
                adapterDC.notifyDataSetChanged();
                swipeatualizar.setRefreshing(false);
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

    //recupera e nao deixa duplicar Outros
    public void RecuperarOutros(){
        ListaGibiOutros.clear();
        swipeatualizar.setRefreshing(true);
        valueEventListenerOutros =GibiOutros.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Gibi gibi = dataSnapshot.getValue(Gibi.class );
                gibi.setKey(dataSnapshot.getKey());
                ListaGibiOutros.add(0,gibi);
                adapterOutros.notifyDataSetChanged();
                swipeatualizar.setRefreshing(false);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Gibi gibi =dataSnapshot.getValue(Gibi.class);
                String key =dataSnapshot.getKey();
                for(Gibi gb:ListaGibiOutros){
                    if(gb.getKey().equals(key)){
                        gb.setValues(gibi);
                        break;
                    }
                }
                adapterOutros.notifyDataSetChanged();
                swipeatualizar.setRefreshing(false);
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
