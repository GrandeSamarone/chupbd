package com.example.fulanoeciclano.nerdzone.Fragments.perfil;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fulanoeciclano.nerdzone.Adapter.Adapter_Topico;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Helper.Main;
import com.example.fulanoeciclano.nerdzone.Model.Topico;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


public class TopicoFragment extends Fragment  implements Main, SwipeRefreshLayout.OnRefreshListener {

    private DatabaseReference mDatabasetopico;
    private SwipeRefreshLayout atualizar_topico;
    private FirebaseAuth autenticacao;
    private FirebaseAuth mFirebaseAuth;
    private FloatingActionButton Novo_Evento;
    private RecyclerView recyclerTopico;
    private Adapter_Topico adapter_topico;
    private ArrayList<Topico> ListaTopico = new ArrayList<>();
    private ChildEventListener valueEventListenerTopico;
    private LinearLayoutManager mManager;
    private Usuario user;

    public TopicoFragment() {}


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            Fresco.initialize(getContext());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.noticia_post_list, container, false);

        //Atualizar TOpico
        atualizar_topico =view.findViewById(R.id.swip_atualizar_noticia);
        atualizar_topico.setOnRefreshListener(TopicoFragment.this);

        atualizar_topico.post(new Runnable() {
            @Override
            public void run() {
          atualizar_topico.setRefreshing(true);
                RecuperarTopico();

            }
        });
        //cores atualizar topico
        atualizar_topico.setColorSchemeResources (R.color.colorPrimaryDark, R.color.amareloclaro,
                R.color.accent);
        //COnfiguracoes Basicas
        recyclerTopico = view.findViewById(R.id.lista_topico);
        mDatabasetopico = ConfiguracaoFirebase.getFirebaseDatabase().child("topico");
        //Configuracao Adapter
        adapter_topico =new Adapter_Topico(ListaTopico,getActivity());
        //Adapter
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerTopico.setLayoutManager(layoutManager);
        recyclerTopico.setHasFixedSize(true);
        recyclerTopico.setAdapter(adapter_topico);

        return view;

    }

    @Override
    public void onRefresh() {
        RecuperarTopico();
    }

    public void onStop() {
        super.onStop();
        mDatabasetopico.removeEventListener(valueEventListenerTopico);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        this.unregisterEventBus();

    }

    @Override
    public void onResume() {
        super.onResume();
        this.registerEventBus();
    }

    @Override
    public void registerEventBus() {

        try {
            EventBus.getDefault().register(this);
        }catch (Exception Err){


        }

    }

    @Override
    public void unregisterEventBus() {
        try {
            EventBus.getDefault().unregister(this);
        }catch (Exception e){

        }

    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void setUserOfDrawer(String account) {
        // Toast.makeText(this, account, Toast.LENGTH_SHORT).show();
        Log.i("eeo34",account);
    }


    public void RecuperarTopico(){
        ListaTopico.clear();
        atualizar_topico.setRefreshing(true);
        valueEventListenerTopico=mDatabasetopico.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Topico topico = dataSnapshot.getValue(Topico.class);
                ListaTopico.add(0,topico);

            adapter_topico.notifyDataSetChanged();
            atualizar_topico.setRefreshing(false);
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
