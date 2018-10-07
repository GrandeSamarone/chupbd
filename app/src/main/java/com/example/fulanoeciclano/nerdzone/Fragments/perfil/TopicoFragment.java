package com.example.fulanoeciclano.nerdzone.Fragments.perfil;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Model.Topico;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.R;
import com.example.fulanoeciclano.nerdzone.Adapter.Adapter_Topico;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


public class TopicoFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener {

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
