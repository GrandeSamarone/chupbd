package com.example.fulanoeciclano.nerdzone.Evento;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Model.Comentario;
import com.example.fulanoeciclano.nerdzone.Model.Evento;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiInformation;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.EmojiTextView;
import com.vanniktech.emoji.EmojiUtils;
import com.vanniktech.emoji.google.GoogleEmojiProvider;
import com.vanniktech.emoji.listeners.OnEmojiBackspaceClickListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupDismissListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupShownListener;
import com.vanniktech.emoji.listeners.OnSoftKeyboardCloseListener;
import com.vanniktech.emoji.listeners.OnSoftKeyboardOpenListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.fulanoeciclano.nerdzone.Helper.App.getUid;

public class EventoDetailActivity extends AppCompatActivity {

    private static final String TAG = "EventoDetailActivity";

    public static final String EXTRA_POST_KEY = "post_key";

    private DatabaseReference mPostReference;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Context context;
    private Toolbar toolbar;
    private DatabaseReference mCommentsReference;
    private ValueEventListener mPostListener;
    private String mPostKey;
    private CommentViewHolder.CommentAdapter mAdapter;
    private String identificadorUsuario;
    private TextView mAuthorView,toolbarnomeevento;
    private SimpleDraweeView mAuthorFotoView,eventobanner;
    private TextView mTitleView,datainicial,datafinal;
    private TextView mBodyView;
    private EmojiEditText mCommentField;
    private ImageView botao_icone;
    private  android.support.v7.widget.AppCompatButton botao_env_msg;
    private DatabaseReference mDatabase;
    private static RecyclerView mCommentsRecycler;
    private Usuario usuarioLogado;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener valueEventListenerIcone;
    private DatabaseReference Recu_icone;
    private DatabaseReference ref;
    private String mCommentIds;
    private List<Comentario> listcomentario = new ArrayList<>();

    private View root_view;
    private EmojiPopup emojiPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_post_detail);
           toolbar = findViewById(R.id.toolbarlayout);
           toolbar.setTitle("");
           setSupportActionBar(toolbar);
           collapsingToolbarLayout=findViewById(R.id.collapseLayout);
        // Esta linha precisa ser executada antes de qualquer uso de EmojiTextView, EmojiEditText ou EmojiButton.
        EmojiManager.install(new GoogleEmojiProvider());
        // Get post key from intent
        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }
        //Configuracoes
        mDatabase = FirebaseDatabase.getInstance().getReference();
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
        // Initialize Database
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("evento").child(mPostKey);
        mCommentsReference = FirebaseDatabase.getInstance().getReference()
                .child("evento-comments").child(mPostKey);


        // Initialize Views
        mAuthorView = findViewById(R.id.post_author);
        datafinal = findViewById(R.id.datafinal);
        datainicial = findViewById(R.id.datainicial);
        toolbarnomeevento= findViewById(R.id.nomeevento_toolbaar);
        mTitleView = findViewById(R.id.post_title);
        botao_env_msg = findViewById(R.id.button_post_comment);
        mBodyView = findViewById(R.id.post_body);
        mAuthorFotoView=findViewById(R.id.post_author_photo);
        eventobanner = findViewById(R.id.bannerevento);
        mCommentField = findViewById(R.id.field_comment_text);
        botao_icone = findViewById(R.id.button_post_icone);
        mCommentsRecycler = findViewById(R.id.recycler_comments);
        Recu_icone = ConfiguracaoFirebase.getFirebaseDatabase().child("usuario-evento");

        //emotion
        root_view=findViewById(R.id.root_view);
        emojiPopup = EmojiPopup.Builder.fromRootView(root_view).build(mCommentField);
        setUpEmojiPopup();
        botao_icone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emojiPopup.toggle();

            }
        });




 botao_env_msg.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         postComment();
     }
 });


        mAdapter = new CommentViewHolder.CommentAdapter(this, mCommentsReference);
        mCommentsRecycler.setAdapter(mAdapter);
        LinearLayoutManager layoutManager  =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        mCommentsRecycler.setLayoutManager(layoutManager );

      collapsingToolbarLayout.setTitle("Marlos");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.preto));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.primary));

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
                        botao_icone.setImageResource(R.drawable.teclado);
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
                .build(mCommentField);
    }
    private void postComment() {

        final String uid = getUid();
        mDatabase.child("usuarios").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        String authorName =usuarioLogado.getNome();
                        String authorFoto= String.valueOf(usuarioLogado.getFoto());

                        // Novo Comentario

                        String commentText = mCommentField.getText().toString();
                        int totalcomentario = listcomentario.size();
                        Log.i("total", String.valueOf(totalcomentario));


                        Comentario comment = new Comentario(uid, authorName, commentText,authorFoto,totalcomentario);
                        if(!commentText.isEmpty()){


                        // Push the comment, it will appear in the list
                        mCommentsReference.push().setValue(comment);

                        // Clear the field
                        mCommentField.setText(null);
                        }else{
                            Toast.makeText(EventoDetailActivity.this, "escreva seu bosta", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private static class CommentViewHolder extends RecyclerView.ViewHolder {

        private TextView authorView;
        private EmojiTextView mensagemView;
        private SimpleDraweeView fotoAuthorView;

        private CommentViewHolder(View itemView) {
            super(itemView);

            authorView = itemView.findViewById(R.id.comment_author);
            mensagemView = itemView.findViewById(R.id.comment_body);
            fotoAuthorView = itemView.findViewById(R.id.comment_photo);
        }


        private static class CommentAdapter extends RecyclerView.Adapter<EventoDetailActivity.CommentViewHolder> {
            private Context mContext;
            private DatabaseReference mDatabaseReference;
            private ChildEventListener mChildEventListener;

            private List<String> mCommentIds = new ArrayList<>();
            private List<Comentario> mComments = new ArrayList<>();

            public CommentAdapter(final Context context, DatabaseReference ref) {
                mContext = context;
                mDatabaseReference = ref;

                //mDatabaseReference = ref;

                ChildEventListener childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                        // A new comment has been added, add it to the displayed list
                        Comentario comment = dataSnapshot.getValue(Comentario.class);

                        // [START_EXCLUDE]
                        // Update RecyclerView
                        mCommentIds.add(dataSnapshot.getKey());
                        mComments.add(comment);
                        mCommentsRecycler.scrollToPosition(mComments.size() - 1);
                        notifyItemInserted(mComments.size() - 1);
                        // [END_EXCLUDE]

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                        Comentario comentario =dataSnapshot.getValue(Comentario.class);
                        String key =dataSnapshot.getKey();
                        for(Comentario cm:mComments){
                            if(cm.getKey().equals(key)){
                                cm.setValues(comentario);
                                break;
                            }
                        }
                        notifyDataSetChanged();

                        // A comment has changed, use the key to determine if we are displaying this
                        // comment and if so displayed the changed comment.
                        Comentario newComment = dataSnapshot.getValue(Comentario.class);
                        String commentKey = dataSnapshot.getKey();

                        // [START_EXCLUDE]
                        int commentIndex = mCommentIds.indexOf(commentKey);
                        if (commentIndex > -1) {
                            // Replace with the new data
                            mComments.set(commentIndex, newComment);

                            // Update the RecyclerView
                            notifyItemChanged(commentIndex);
                        } else {
                            Log.w(TAG, "onChildChanged:unknown_child:" + commentKey);
                        }
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                        // A comment has changed, use the key to determine if we are displaying this
                        // comment and if so remove it.
                        String commentKey = dataSnapshot.getKey();

                        // [START_EXCLUDE]
                        int commentIndex = mCommentIds.indexOf(commentKey);
                        if (commentIndex > -1) {
                            // Remove data from the list
                            mCommentIds.remove(commentIndex);
                            mComments.remove(commentIndex);

                            // Update the RecyclerView
                            notifyItemRemoved(commentIndex);
                        } else {
                            Log.w(TAG, "onChildRemoved:unknown_child:" + commentKey);
                        }
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                        // A comment has changed position, use the key to determine if we are
                        // displaying this comment and if so move it.
                        Comentario movedComment = dataSnapshot.getValue(Comentario.class);
                        String commentKey = dataSnapshot.getKey();

                        // ...
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                        Toast.makeText(mContext, "Failed to load comments.",
                                Toast.LENGTH_SHORT).show();
                    }
                };


                ref.addChildEventListener(childEventListener);
                // [END child_event_listener_recycler]

                // Store reference to listener so it can be removed on app stop
                mChildEventListener = childEventListener;
            }

            @Override
            public EventoDetailActivity.CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View view = inflater.inflate(R.layout.item_comment, parent, false);
                return new EventoDetailActivity.CommentViewHolder(view);
            }

            @Override
            public void onBindViewHolder(EventoDetailActivity.CommentViewHolder holder, int position) {
                final Comentario text = (mComments.get(position));

                final EmojiInformation emojiInformation = EmojiUtils.emojiInformation("");
                final int res;
               if(emojiInformation.isOnlyEmojis && emojiInformation.emojis.size() == 1) {
                    res = R.dimen.emoji_size_single_emoji;
                } else if (emojiInformation.isOnlyEmojis && emojiInformation.emojis.size() > 1) {
                    res = R.dimen.emoji_size_only_emojis;
                } else {
                    res = R.dimen.emoji_size_only_emojis;
                }
                holder.mensagemView.setEmojiSizeRes(res, false);


                Comentario comentario = mComments.get(position);
                if (comentario.author != null) {
                    holder.authorView.setText(comentario.author);
                }

                holder.mensagemView.setText(comentario.text);
                if (comentario.foto != null) {
                    Uri uri = Uri.parse(comentario.foto);
                    Log.i("url", String.valueOf(uri));
                    DraweeController controllerOne = Fresco.newDraweeControllerBuilder()
                            .setUri(uri)
                            .setAutoPlayAnimations(true)
                            .build();

                    holder.fotoAuthorView.setController(controllerOne);
                } else {
                    holder.fotoAuthorView.setImageResource(R.drawable.carregando);
                }


            }

            @Override
            public int getItemCount() {
                return mComments.size();

            }


            public void cleanupListener() {
                if (mChildEventListener != null) {
                    mDatabaseReference.removeEventListener(mChildEventListener);
                }
            }
        }
    }









    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Noticia object and use the values to update the UI
                Evento evento = dataSnapshot.getValue(Evento.class);
                // [START_EXCLUDE]
                mAuthorView.setText(evento.author);
                mTitleView.setText(evento.titulo);
                datainicial.setText(evento.datainicio);
                datafinal.setText(evento.datafim);
                mBodyView.setText(evento.mensagem);
                toolbarnomeevento.setText(evento.titulo);


               /* if(evento.imgperfilusuario!=null) {
                    Uri uri = Uri.parse(evento.imgperfilusuario);
                    DraweeController controllerOne = Fresco.newDraweeControllerBuilder()
                            .setUri(uri)
                            .setAutoPlayAnimations(true)
                            .build();

                   // mAuthorFotoView.setController(controllerOne);

                }
                */
                if(evento.fotoevento!=null) {
                    Uri uri = Uri.parse(evento.fotoevento);
                    DraweeController controllerOne = Fresco.newDraweeControllerBuilder()
                            .setUri(uri)
                            .setAutoPlayAnimations(true)
                            .build();

                    eventobanner.setController(controllerOne);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(EventoDetailActivity.this, "Erro ao Carregar Noticia,Verifique a Internet",
                        Toast.LENGTH_SHORT).show();

            }
        };
        mPostReference.addValueEventListener(postListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        mPostListener = postListener;


    }




    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mPostListener != null) {
            mPostReference.removeEventListener(mPostListener);
        }

        // Clean up comments listener
        mAdapter.cleanupListener();
    }





    }

