package com.example.fulanoeciclano.nerdzone.Evento;


import android.content.Intent;
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

import com.example.fulanoeciclano.geek.helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Model.Evento;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class EventoListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
        {
    private static final String TAG = "EventoListFragment";


    private DatabaseReference mDatabase;
    private SwipeRefreshLayout swipeRefresh;
    private DatabaseReference eventoref;
    private FirebaseAuth autenticacao;
    private FirebaseAuth mFirebaseAuth;
    private FloatingActionButton Novo_Evento;
    private FirebaseRecyclerAdapter<Evento, EventoViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private Usuario user;


    public EventoListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Fresco.initialize(getContext());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.evento_post_list, container, false);
        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        eventoref = mDatabase.child("evento");

        //atualiza pagina
        swipeRefresh = view.findViewById(R.id.swipe_evento);
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(false);
                if (mAdapter != null) {
                    mAdapter.startListening();
                }
            }
        });
        swipeRefresh.setColorSchemeResources
                (R.color.colorPrimaryDark, R.color.amareloclaro,
                        R.color.accent);

        autenticacao = FirebaseAuth.getInstance();
        //  user = new Usuario();
        user = UsuarioFirebase.getDadosUsuarioLogado();
        Log.i("TipoUsernome", user.getNome());


        Novo_Evento = view.findViewById(R.id.fab_new_evento);
        Novo_Evento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), NewEventoActivity.class);
                startActivity(it);

            }
        });

//        Verificar_Tempo_Evento();
        mRecycler = view.findViewById(R.id.messages_list);
        mRecycler.setHasFixedSize(true);

        return view;


    }






/*
    public void Verificar_Tempo_Evento() {
        long cutoff = new Date().getTime() - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
        Query oldItems = eventoref.orderByChild("datafim").endAt(cutoff);
        oldItems.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    itemSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
*/


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Evento>()
                .setQuery(postsQuery, Evento.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Evento, EventoViewHolder>(options) {

            @Override
            public EventoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new EventoViewHolder(inflater.inflate(R.layout.item_evento, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(EventoViewHolder viewHolder, int position, final Evento model) {
                final DatabaseReference postRef = getRef(position);



                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch EventoDetailActivity
                        Intent intent = new Intent(getActivity(), EventoDetailActivity.class);
                        intent.putExtra(EventoDetailActivity.EXTRA_POST_KEY, postKey);
                        startActivity(intent);
                    }
                });

                // Determine if the current user has liked this post and set UI accordingly
                if (model.curtida.containsKey(getUid())) {
                    viewHolder.curtirEventoView.setImageResource(R.drawable.likecolorido);
                } else {
                    viewHolder.curtirEventoView.setImageResource(R.drawable.likepreto);
                }

                // Bind Noticia to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {
                        // Need to write to both places the post is stored
                        DatabaseReference globalPostRef = mDatabase.child("evento").child(postRef.getKey());
                        DatabaseReference userPostRef = mDatabase.child("usuario-evento")
                                .child(model.uid).child(postRef.getKey());

                        // Run two transactions
                        onStarClicked(globalPostRef);
                        onStarClicked(userPostRef);
                    }
                });
            }
        };

        mRecycler.setAdapter(mAdapter);


    }


    // [START post_stars_transaction]
    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Evento p = mutableData.getValue(Evento.class);

                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.curtida.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    p.curtirCount = p.curtirCount - 1;
                    p.curtida.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    p.curtirCount = p.curtirCount + 1;
                    p.curtida.put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {

                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }
    // [END post_stars_transaction]

   //refresh
    @Override
    public void onRefresh() {
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();

        }
    }


    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);



}
