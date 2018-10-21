package com.example.fulanoeciclano.nerdzone.Fragments.fragment_feed;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fulanoeciclano.nerdzone.Adapter.Adapter_Conto;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Model.Conto;
import com.example.fulanoeciclano.nerdzone.Model.Feed_Conto;
import com.example.fulanoeciclano.nerdzone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Contos_feed_Fragment extends Fragment {

    private DatabaseReference mDatabasefeed,database_conto;
    private FirebaseAuth autenticacao;
    private FirebaseAuth mFirebaseAuth;
    private RecyclerView recyclerConto;
    private Adapter_Conto adapter_conto;
    private ArrayList<Conto> ListaConto = new ArrayList<>();
    private ChildEventListener valueEventListenerConto,valueEventListenerFeed;
    public Contos_feed_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_contos_feed_, container, false);

        recyclerConto = view.findViewById(R.id.lista_conto_feed);
        mDatabasefeed = ConfiguracaoFirebase.getFirebaseDatabase().child("feed-conto");
        database_conto = ConfiguracaoFirebase.getDatabase().getReference().child("conto");
        //Configuracao Adapter
        adapter_conto =new Adapter_Conto(ListaConto,getActivity());
        //Adapter
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerConto.setLayoutManager(layoutManager);
        recyclerConto.setHasFixedSize(true);
        recyclerConto.setAdapter(adapter_conto);
        RecuperarFeed();
       return view;
    }

    public void RecuperarFeed(){
        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        valueEventListenerFeed=mDatabasefeed.child(identificadorUsuario)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Feed_Conto feedConto = dataSnapshot.getValue(Feed_Conto.class);


                        RecuperarContos(feedConto.getIdconto());
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
    private void RecuperarContos(String idconto){
        ListaConto.clear();
        valueEventListenerConto = database_conto.orderByChild("uid").equalTo(idconto).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Conto conto = dataSnapshot.getValue(Conto.class);
                ListaConto.add(0, conto);

                adapter_conto.notifyDataSetChanged();


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
