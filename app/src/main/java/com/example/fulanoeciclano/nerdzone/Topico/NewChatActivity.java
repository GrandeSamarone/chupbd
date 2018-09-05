package com.example.fulanoeciclano.nerdzone.Topico;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Model.Topico;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static com.example.fulanoeciclano.nerdzone.Helper.App.getUid;

public class NewChatActivity extends AppCompatActivity {

    private static final String TAG = "NewEventoActivity";
    private static final String TITULO = "Obrigatorio";
    private static final String MENSAGEM = "nao pode em branco";


    private Usuario usuarioLogado;


    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private EditText titulo;
    private EditText mensagem;
    private AppCompatButton botaoSalvar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        //Configuraçoes
        mDatabase = FirebaseDatabase.getInstance().getReference();

        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();


       titulo = findViewById(R.id.titulo_topico);
        mensagem= findViewById(R.id.mensagem_topico);
        botaoSalvar = findViewById(R.id.btn_salvar_topico);


        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SalvarPost();
            }
        });
    }

    private void SalvarPost() {
        final String tituloDoPost = titulo.getText().toString();
        final String mensagemDoPost = mensagem.getText().toString();
        final int quantcomentario = 0;

        // Title is required
        if (TextUtils.isEmpty(tituloDoPost)) {
            titulo.setError(TITULO);
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(mensagemDoPost)) {
           mensagem.setError(MENSAGEM);
            return;
        }

        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        Toast.makeText(this, "Postando...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
        final String userId = getUid();
        Log.i("carz",userId);
        mDatabase.child("usuarios").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                    writeNewPost(userId, usuarioLogado.getNome(),usuarioLogado.getFoto(),tituloDoPost, mensagemDoPost,quantcomentario);


                        // Finish this Activity, back to the stream
                        setEditingEnabled(true);
                        finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
        // [END single_value_read]
    }

    private void setEditingEnabled(boolean enabled) {
       titulo.setEnabled(enabled);
        mensagem.setEnabled(enabled);
        if (enabled) {
            botaoSalvar.setVisibility(View.VISIBLE);
        } else {
            botaoSalvar.setVisibility(View.GONE);
        }
    }

    // [START write_fan_out]
    private void writeNewPost(String uid, String nome, String foto, String titulo, String mensagem, int quantidade) {
        // Create new topico at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("batepapo").push().getKey();
        Log.i("key",key);
        Topico topico = new Topico(uid, nome,foto,titulo, mensagem);
        Map<String, Object> postValues = topico.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/batepapo/" + key, postValues);
        childUpdates.put("/usuario-batepapo/" + uid + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }
    // [END write_fan_out]
}
