package com.example.fulanoeciclano.nerdzone.Chat;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Model.Comentario;
import com.example.fulanoeciclano.nerdzone.Model.Noticia;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BatePapoListFragment extends Fragment {
    private static final String TAG = "EventoListFragment";


    private DatabaseReference mDatabase;
    private FirebaseAuth autenticacao;
    private FirebaseAuth mFirebaseAuth;
    private FloatingActionButton Novo_Topico;
    private FirebaseRecyclerAdapter<Noticia, BatePapoViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private Usuario user;
    private List<Comentario> listcomentario = new ArrayList<>();

    public BatePapoListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Fresco.initialize(getContext());
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.batepapo_post_list, container, false);
        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        autenticacao = FirebaseAuth.getInstance();
      //  user = new Usuario();
        user = UsuarioFirebase.getDadosUsuarioLogado();
        Log.i("TipoUsernome", user.getNome());



        Novo_Topico = view.findViewById(R.id.fab_new_batepapo);
        Novo_Topico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(),NewChatActivity.class);
                startActivity(it);
            }
        });


        mRecycler =view.findViewById(R.id.messages_list);
        mRecycler.setHasFixedSize(true);
        verificarTipoUsuario();
        return view;

    }
public  void verificarTipoUsuario() {
    String conta= String.valueOf(user.getTipoconta());

        mDatabase.child("usuarios").orderByChild("tipoconta").equalTo(conta).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

/*                    String TipoUsuario = postSnapshot.child("tipousuario").getValue().toString();
                    Log.i("TipoUser", TipoUsuario);

                    //verificando se o usuario é administrador
                    if (TipoUsuario.equals("adm")) {

                        Novo_Topico.setVisibility(View.VISIBLE);
                    }
*/
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

}



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

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Noticia>()
                .setQuery(postsQuery, Noticia.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Noticia, BatePapoViewHolder>(options) {

            @Override
            public BatePapoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new BatePapoViewHolder(inflater.inflate(R.layout.adapterbatepapo, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(BatePapoViewHolder viewHolder, int position, final Noticia model) {
                final DatabaseReference postRef = getRef(position);



                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch EventoDetailActivity
                        Intent intent = new Intent(getActivity(), ChatDetailActivity.class);
                        intent.putExtra(ChatDetailActivity.EXTRA_POST_KEY, postKey);
                        startActivity(intent);
                    }
                });

                // Determine if the current user has liked this post and set UI accordingly
                if (model.stars.containsKey(getUid())) {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_24);
                } else {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_outline_24);
                }

                // Bind Noticia to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {
                        // Need to write to both places the post is stored
                        DatabaseReference globalPostRef = mDatabase.child("batepapo").child(postRef.getKey());
                        DatabaseReference userPostRef = mDatabase.child("usuario-batepapo").child(model.uid).child(postRef.getKey());

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
                Noticia p = mutableData.getValue(Noticia.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.stars.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    p.starCount = p.starCount - 1;
                    p.stars.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    p.starCount = p.starCount + 1;

                    p.stars.put(getUid(), true);
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
