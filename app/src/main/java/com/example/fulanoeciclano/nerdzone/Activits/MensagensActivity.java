package com.example.fulanoeciclano.nerdzone.Activits;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.example.fulanoeciclano.nerdzone.Adapter.ConversasAdapter;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Helper.RecyclerItemClickListener;
import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Model.Conversa;
import com.example.fulanoeciclano.nerdzone.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MensagensActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton Novo_BatePapo;
    private RecyclerView recyclerViewConversas;
    private List<Conversa> listaConversas = new ArrayList<>();;
    private ConversasAdapter adapter;
    private DatabaseReference database;
    private DatabaseReference conversasRef;
    private ValueEventListener valueEventListenerContatos;
    private ChildEventListener childEventListenerConversas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagens);
        toolbar =findViewById(R.id.toolbar_principal);
        toolbar.setTitle("Caixa de Mensagens");
        setSupportActionBar(toolbar);
        recyclerViewConversas= findViewById(R.id.recyclerviewConversas);
        adapter = new ConversasAdapter(listaConversas,MensagensActivity.this);

        //Configurar RecycleView
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(MensagensActivity.this);
        recyclerViewConversas.setLayoutManager(layoutManager);

        recyclerViewConversas.setHasFixedSize(true);
        recyclerViewConversas.setAdapter(adapter);

        //Configurar Evento de click
        recyclerViewConversas.addOnItemTouchListener(new RecyclerItemClickListener(
                MensagensActivity.this,
                recyclerViewConversas,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        List<Conversa> listaconversaatualizada = adapter.getConversas();
                        if (listaconversaatualizada.size()>0) {
                            Conversa conversaSelecionado = listaconversaatualizada.get(position);

                            if(conversaSelecionado.getIsGroup().equals("true")){
                                Intent it = new Intent(MensagensActivity.this, ChatActivity.class);
                                it.putExtra("chatGrupo", conversaSelecionado.getGrupo());
                                startActivity(it);

                            }else{

                                Intent it = new Intent(MensagensActivity.this, ChatActivity.class);
                                it.putExtra("chatcontato", conversaSelecionado.getUsuarioExibicao());
                                startActivity(it);
                            }

                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));
        String identificadoUsuario= UsuarioFirebase.getIdentificadorUsuario();
        database = ConfiguracaoFirebase.getFirebaseDatabase();
        conversasRef= database.child("Conversas")
                .child(identificadoUsuario);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //Botao Voltar
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                finish();

                break;

            default:break;
        }

        return true;
    }


    @Override
    public void onStart() {
        super.onStart();
        RecuperarConversa();
    }

    @Override
    public void onStop() {
        super.onStop();
        conversasRef.removeEventListener(childEventListenerConversas);
    }

    public void PesquisarConversa(String texto) {

        List<Conversa> listaConversaBusca = new ArrayList<>();
        for (Conversa conversa : listaConversas) {

            if (conversa.getUsuarioExibicao() != null) {

                String nome = conversa.getUsuarioExibicao().getNome().toLowerCase();
                String ultimaMsg = conversa.getUltimaMensagem().toLowerCase();

                if (nome.contains(texto) || ultimaMsg.contains(texto)) {
                    listaConversaBusca.add(conversa);
                }
            } else {
                String nome = conversa.getGrupo().getNome().toLowerCase();
                String ultimaMsg = conversa.getUltimaMensagem().toLowerCase();

                if (nome.contains(texto) || ultimaMsg.contains(texto)) {

                }

            }
            adapter = new ConversasAdapter(listaConversaBusca, MensagensActivity.this);
            recyclerViewConversas.setAdapter(adapter);
            adapter.notifyDataSetChanged();


        }
    }

    public void recarregarConversas(){
        adapter = new ConversasAdapter(listaConversas,MensagensActivity.this);
        recyclerViewConversas.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public void RecuperarConversa(){
        listaConversas.clear();
        childEventListenerConversas =  conversasRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Recuperar Conversa
                Conversa conversa=dataSnapshot.getValue(Conversa.class);
                listaConversas.add(conversa);
                adapter.notifyDataSetChanged();






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