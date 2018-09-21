package com.example.fulanoeciclano.nerdzone.Evento;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fulanoeciclano.nerdzone.Adapter.Adapter_comentario;
import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Model.Comentario;
import com.example.fulanoeciclano.nerdzone.Model.Evento;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.google.GoogleEmojiProvider;
import com.vanniktech.emoji.listeners.OnEmojiBackspaceClickListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupDismissListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupShownListener;
import com.vanniktech.emoji.listeners.OnSoftKeyboardCloseListener;
import com.vanniktech.emoji.listeners.OnSoftKeyboardOpenListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.fulanoeciclano.nerdzone.Helper.App.getUid;

public class DetalheEvento extends AppCompatActivity {

    private DatabaseReference database;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private DatabaseReference mCommentsReference;
    private ChildEventListener childEventListener;
    private Evento eventoselecionado;
    private String identificadorUsuario;
    private TextView Author_evento_View,toolbarnome_evento;
    private SimpleDraweeView eventobanner;
    private CircleImageView AuthorFoto_evento_View;
    private TextView titutoevento,datainicial,datafinal;
    private TextView mensagem_evento;
    private EmojiEditText edit_chat_emoji;
    private ImageView botao_icone;
    private  android.support.v7.widget.AppCompatButton botao_env_msg;
    private DatabaseReference mDatabase;
    private static RecyclerView recyclerViewcomentarios;
    private Usuario usuarioLogado;
    private String mCommentIds;
    private Adapter_comentario adapter;
    private Comentario comentar = new Comentario();
    private List<Comentario> listcomentario = new ArrayList<>();

    private View root_view;
    private EmojiPopup emojiPopup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_evento);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarlayout);
        setSupportActionBar(toolbar);


        //configuracoes iniciais
        collapsingToolbarLayout = findViewById(R.id.collapseLayoutevento);
        EmojiManager.install(new GoogleEmojiProvider());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

       /* // Initialize Database
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("evento").child(mPostKey);
        mCommentsReference = FirebaseDatabase.getInstance().getReference()
                .child("evento-comments").child(mPostKey);
*/

        // Initialize Views
        Author_evento_View = findViewById(R.id.post_author_evento);
       // datafinal = findViewById(R.id.datafinal);
      //  datainicial = findViewById(R.id.datainicial);
       // toolbarnomeevento= findViewById(R.id.nomeevento_toolbaar);
        titutoevento = findViewById(R.id.detalhe_evento_titulo);
        botao_env_msg = findViewById(R.id.button_postar_comentario);
        botao_env_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostarComentario();
            }
        });
        mensagem_evento = findViewById(R.id.detalhe_evento_mensagem);
        AuthorFoto_evento_View=findViewById(R.id.icone_author);
        eventobanner = findViewById(R.id.bannereventocapa);
        edit_chat_emoji = findViewById(R.id.caixa_de_texto_comentario);
        botao_icone = findViewById(R.id.botao_post_icone);
        recyclerViewcomentarios = findViewById(R.id.recycler_comentario);
        //configurar adapter
        adapter = new Adapter_comentario(listcomentario, getApplicationContext());

        //configurando recycleview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewcomentarios.setLayoutManager(layoutManager);
        // recyclemensagens.setHasFixedSize(true);
        recyclerViewcomentarios.setAdapter(adapter);


        eventoselecionado = (Evento) getIntent().getSerializableExtra("eventoselecionado");
        if(eventoselecionado!=null){

            titutoevento.setText(eventoselecionado.getTitulo());
            mensagem_evento.setText(eventoselecionado.getMensagem());
            Author_evento_View.setText(eventoselecionado.getAuthor());
            Uri uri = Uri.parse(eventoselecionado.getFotoevento());
            DraweeController controllerOne = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setAutoPlayAnimations(true)
                    .build();

            eventobanner.setController(controllerOne);

        }
        mCommentsReference = FirebaseDatabase.getInstance().getReference()
                .child("evento-comentario").child(getUid());

        Log.i("comentaasd", String.valueOf(mCommentsReference));
        //emotion
        root_view=findViewById(R.id.root_view);
        emojiPopup = EmojiPopup.Builder.fromRootView(root_view).build(edit_chat_emoji);
        setUpEmojiPopup();
        botao_icone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emojiPopup.toggle();

            }
        });


        collapsingToolbarLayout.setTitle("Marlos");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.preto));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.primary));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void PostarComentario() {
        final String uid = getUid();
       // mDatabase.child("usuarios").child(uid)
                /*.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        String authorName =usuarioLogado.getNome();
                        String  authorFoto= String.valueOf(usuarioLogado.getFoto());
                        String mensagem = edit_chat_emoji.getText().toString();


                        comentar.setAuthor(authorName);
                        comentar.setFoto(authorFoto);
                        comentar.setText(mensagem);

                        /*
                        Comentario commentario = new Comentario
                                (uid, authorName, comentariotexto,authorFoto,totalcomentario);
                        if(!comentariotexto.isEmpty()){


                            // Push the comment, it will appear in the list
                            mCommentsReference.push().setValue(commentario);

                            // Clear the field
                            edit_chat_emoji.setText(null);
                        }else{
                            Toast.makeText(DetalheEvento.this, "Deixe algo importante", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    */
    }


    private void setUpEmojiPopup() {
        emojiPopup = EmojiPopup.Builder.fromRootView(root_view)
                .setOnEmojiBackspaceClickListener(new OnEmojiBackspaceClickListener() {
                    @Override
                    public void onEmojiBackspaceClick(View v) {
                        if(emojiPopup.isShowing()){
                            emojiPopup.dismiss();
                        }
                        Log.d("ss","Clicked on Backspace");
                    }
                })
                .setOnEmojiPopupShownListener(new OnEmojiPopupShownListener() {
                    @Override
                    public void onEmojiPopupShown() {
                        botao_icone.setImageResource(R.drawable.ic_teclado);
                    }
                })
                .setOnSoftKeyboardOpenListener(new OnSoftKeyboardOpenListener() {
                    @Override
                    public void onKeyboardOpen(final int keyBoardHeight) {
                        Log.d("ss","Clicked on Backspace");
                    }
                })
                .setOnEmojiPopupDismissListener(new OnEmojiPopupDismissListener() {
                    @Override
                    public void onEmojiPopupDismiss() {
                        botao_icone.setImageResource(R.drawable.ic_emotion_chat);
                    }
                })
                .setOnSoftKeyboardCloseListener(new OnSoftKeyboardCloseListener() {
                    @Override
                    public void onKeyboardClose() {
                        if (emojiPopup.isShowing()){
                            emojiPopup.dismiss();
                        }
                        Log.d("ss","Clicked on Backspace");
                    }
                })
                .build(edit_chat_emoji);
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

}
