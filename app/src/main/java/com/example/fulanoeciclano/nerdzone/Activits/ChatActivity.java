package com.example.fulanoeciclano.nerdzone.Activits;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fulanoeciclano.nerdzone.Adapter.MensagensAdapter;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Helper.Base64Custom;
import com.example.fulanoeciclano.nerdzone.Helper.RecyclerItemClickListener;
import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Model.Conversa;
import com.example.fulanoeciclano.nerdzone.Model.Grupo;
import com.example.fulanoeciclano.nerdzone.Model.Mensagem;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.google.GoogleEmojiProvider;
import com.vanniktech.emoji.listeners.OnEmojiBackspaceClickListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupDismissListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupShownListener;
import com.vanniktech.emoji.listeners.OnSoftKeyboardCloseListener;
import com.vanniktech.emoji.listeners.OnSoftKeyboardOpenListener;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.photodraweeview.PhotoDraweeView;

public class ChatActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private TextView textViewNome;

    private CircleImageView circleImageViewFoto;
    private SwipeRefreshLayout swipe;
    private Toolbar toolbarDialog;
    private Usuario usuariodestinatario;
    private Usuario usuarioRemetente;
    private PhotoDraweeView dialogimg;
    private Grupo grupo;
    private AlertDialog ImgDialog;
    private ViewPager viewPager;
    private EditText editMensagem;
    private ImageView imageCamera;
    private RecyclerView recyclemensagens;
    private MensagensAdapter adapter;
    private DatabaseReference mensagensRef;
    private ChildEventListener childEventListenermensagens;
    private static final int SELECAO_GALERIA=100;
    private EmojiPopup emojiPopup;
    private ImageView botao_icone;
    private EmojiEditText chatField;
    private View root_view;
    private List<Mensagem> mensagens= new ArrayList<>();

    //identificado  usuario rementente e destinatario
    private String idUsuarioRemetente;
    private String idusuarioDestinatario;
    private DatabaseReference database;
    private StorageReference storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_chat);

        // Esta linha precisa ser executada antes de qualquer uso de EmojiTextView, EmojiEditText ou EmojiButton.
        EmojiManager.install(new GoogleEmojiProvider());
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Configuracoes iniciais
        textViewNome = findViewById(R.id.textViewNomeChat);
        swipe = findViewById(R.id.swipe_chat);
        circleImageViewFoto = findViewById(R.id.circleImageFotoChat);
        editMensagem = findViewById(R.id.editMensagem);
        recyclemensagens = findViewById(R.id.recyclerMensagens);
        imageCamera = findViewById(R.id.imageCamera);
        viewPager = findViewById(R.id.viewpager);
        botao_icone = findViewById(R.id.button_chat_icone);
        chatField = findViewById(R.id.editMensagem);

           //emotion
        root_view=findViewById(R.id.layoutroot);
        emojiPopup = EmojiPopup.Builder.fromRootView(root_view).build(chatField);
        setUpEmojiPopup();
        botao_icone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emojiPopup.toggle();

            }
        });

        //atualiza
        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
            @Override
            public void run() {

                String foto = usuariodestinatario.getFoto();
                if (foto != null) {
                    Uri url = Uri.parse(usuariodestinatario.getFoto());
                    Glide.with(getApplicationContext())
                            .load(url)
                            .into(circleImageViewFoto);
                }
            }
        });
        swipe.setColorSchemeResources
                (R.color.colorPrimaryDark, R.color.amareloclaro,
                        R.color.accent);

        //Recupera id do usuario remetente
        idUsuarioRemetente = UsuarioFirebase.getIdentificadorUsuario();
        //Recupera dados do usuario remetente
        usuarioRemetente= UsuarioFirebase.getDadosUsuarioLogado();

        //Recuperar dados do usuario Selecionado;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if(bundle.containsKey("chatGrupo")){
                grupo = (Grupo) bundle.getSerializable("chatGrupo");
                textViewNome.setText(grupo.getNome());
                idusuarioDestinatario = grupo.getId();

                String foto = grupo.getFoto();
                if (foto != null) {
                    Uri url = Uri.parse(foto);
                    Glide.with(this)
                            .load(url)
                            .into(circleImageViewFoto);

                } else {
                    circleImageViewFoto.setImageResource(R.drawable.padrao);
                }


            }else {

                /*****/
                usuariodestinatario = (Usuario) bundle.getSerializable("chatcontato");
                textViewNome.setText(usuariodestinatario.getNome());

                String foto = usuariodestinatario.getFoto();
                if (foto != null) {
                    Uri url = Uri.parse(usuariodestinatario.getFoto());
                    Glide.with(this)
                            .load(url)
                            .into(circleImageViewFoto);

                } else {
                    circleImageViewFoto.setImageResource(R.drawable.padrao);
                }

                //recuperar tados do usuario destinatario
                idusuarioDestinatario = Base64Custom.codificarBase64(usuariodestinatario.getTipoconta());

                /*****/
            }
        }

        //configurar adapter
        adapter = new MensagensAdapter(mensagens, getApplicationContext());

        //configurando recycleview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclemensagens.setLayoutManager(layoutManager);
        // recyclemensagens.setHasFixedSize(true);
        recyclemensagens.setAdapter(adapter);
        recyclemensagens.addOnItemTouchListener( new RecyclerItemClickListener(
                ChatActivity.this, recyclemensagens,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        String img =mensagens.get(position).getImagem();


                    }
                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }));

        //
        storage = ConfiguracaoFirebase.getFirebaseStorage();
        database = ConfiguracaoFirebase.getFirebaseDatabase();
        mensagensRef = database.child("mensagens")
                .child(idUsuarioRemetente)
                .child(idusuarioDestinatario);


        //Evento de clique na camera
        imageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(it.resolveActivity(getPackageManager())!=null)  {
                    startActivityForResult(it, SELECAO_GALERIA);
                }
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {
                switch (requestCode) {
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                }
                if(imagem!=null){

                    //Recuperar dados da imagem  para o  Firebase
                    ByteArrayOutputStream baos=new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG,50,baos);
                    byte[] dadosImagem= baos.toByteArray();

                    //Criar nome da imagem
                    String nomeImagem = UUID.randomUUID().toString();

                    //Configurando referencia do firebase
                    StorageReference imageRef = storage.child("imagens")
                            .child("fotos")
                            .child(idUsuarioRemetente)
                            .child(nomeImagem);
//Progress
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("Carregando...");
                    progressDialog.show();
                    UploadTask uploadTask =imageRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("erro","erro ao fazer upload da imagem");
                            Toast.makeText(ChatActivity.this, "Erro ao fazer Upload da Imagem",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String downloadUrl =taskSnapshot.getDownloadUrl().toString();
                            Mensagem mensagem = new Mensagem();
                            mensagem.setIdUsuario(idUsuarioRemetente);
                            mensagem.setMensagem("imagem.jpg");
                            mensagem.setImagem(downloadUrl);
                            progressDialog.dismiss();

                            //Salvar para  o remetente

                            SalvarMensagem(idUsuarioRemetente,idusuarioDestinatario,mensagem);

                            //Salvar para o Destinatario
                            SalvarMensagem(idusuarioDestinatario,idUsuarioRemetente,mensagem);

                            Toast.makeText(ChatActivity.this, "Sucesso ao Enviar Imagem",
                                    Toast.LENGTH_SHORT).show();



                        }
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    //Botao Voltar
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

              Intent it = new Intent(ChatActivity.this, MeusAmigosActivity.class);
              startActivity(it);

                break;

            default:break;
        }

        return true;
    }

///EMOTION
    private void setUpEmojiPopup() {
        emojiPopup = EmojiPopup.Builder.fromRootView(root_view)
                .setOnEmojiBackspaceClickListener(new OnEmojiBackspaceClickListener() {
                    @Override
                    public void onEmojiBackspaceClick(View v) {
                        if (emojiPopup.isShowing()) {
                            emojiPopup.dismiss();
                        }
                        Log.d("ss", "Clicked on Backspace");
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
                        Log.d("ss", "Clicked on Backspace");
                    }
                })
                .setOnEmojiPopupDismissListener(new OnEmojiPopupDismissListener() {
                    @Override
                    public void onEmojiPopupDismiss() {
                        botao_icone.setImageResource(R.drawable.ic_action_navigation_arrow_back);
                    }
                })
                .setOnSoftKeyboardCloseListener(new OnSoftKeyboardCloseListener() {
                    @Override
                    public void onKeyboardClose() {
                        if (emojiPopup.isShowing()) {
                            emojiPopup.dismiss();
                        }
                        Log.d("ss", "Clicked on Backspace");
                    }
                })
                .build(chatField);
    }

    public void EnviarMensagem(View view){

        String textoMensagem =editMensagem.getText().toString();

        final Calendar calendartempo = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("kk:mm   dd'/'MM");// MM'/'dd'/'y;

        String tempoMensagem = simpleDateFormat.format(calendartempo.getTime());

        if(!textoMensagem.isEmpty()){
            if(usuariodestinatario!=null){
                Mensagem mensagem= new Mensagem();
                mensagem.setIdUsuario(idUsuarioRemetente);
                mensagem.setMensagem(textoMensagem);
                mensagem.setTempo(tempoMensagem);



                //salvar mensagem para o remetente
                SalvarMensagem(idUsuarioRemetente,idusuarioDestinatario,mensagem);

                //salvar mensagem para o Destinatario
                SalvarMensagem(idusuarioDestinatario,idUsuarioRemetente,mensagem);

                //Salvar Conversa Remetente
                SalvarConversa(idUsuarioRemetente,idusuarioDestinatario,usuariodestinatario,mensagem,false);

                //Salvar Conversa Destinatario
                Usuario usuarioRemetente= UsuarioFirebase.getDadosUsuarioLogado();
                SalvarConversa(idusuarioDestinatario,idUsuarioRemetente,usuarioRemetente,mensagem,false);

            }else {
                for(Usuario membro: grupo.getMembros()){

                    String idremetenteGrupo = Base64Custom.codificarBase64(membro.getTipoconta());
                    String idUsuarioLogadoGrupo = UsuarioFirebase.getIdentificadorUsuario();

                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUsuarioLogadoGrupo);
                    mensagem.setImg_Usuario(membro.getFoto());
                    mensagem.setMensagem(textoMensagem);
                    Usuario usuarioRemetente= UsuarioFirebase.getDadosUsuarioLogado();
                    mensagem.setNome(usuarioRemetente.getNome());

                    //Salvar mensagens
                    SalvarMensagem(idremetenteGrupo,idusuarioDestinatario,mensagem);

                    //Salvar Conversa
                    SalvarConversa(idremetenteGrupo,idusuarioDestinatario,usuariodestinatario,mensagem,true);


                }
            }

        }else{
            Toast.makeText(this, "Digite uma mensagem para enviar!", Toast.LENGTH_SHORT).show();

        }

    }

    private void SalvarConversa(String idRemetente, String idDestinatario, Usuario usuarioExibicao, Mensagem msg, boolean isGroup) {

        Conversa conversaRemetente = new Conversa();
        conversaRemetente.setIdRemetente(idRemetente);
        conversaRemetente.setIdDestinatario(idDestinatario);
        conversaRemetente.setUltimaMensagem(msg.getMensagem());

        if(isGroup){//conversa de grupo

            conversaRemetente.setIsGroup("true");
            conversaRemetente.setGrupo(grupo);
        }else{//conversa normal
            conversaRemetente.setUsuarioExibicao(usuarioExibicao);
            conversaRemetente.setIsGroup("false");
        }
        //Chamando o metodo Salvar
        conversaRemetente.salvar();



    }

    public void SalvarMensagem(String idRemetente, String idDestinatario, Mensagem msg){

        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference mensagemref = database.child("mensagens");

        mensagemref.child(idRemetente)
                .child(idDestinatario)
                .push()
                .setValue(msg);
        //limpando o texto
        editMensagem.setText("");
    }

    public void onStart() {
        super.onStart();
        RecuperarMensagem();

    }

    public void onStop(){
        super.onStop();
        mensagensRef.removeEventListener(childEventListenermensagens);
    }




    //Abrir a imagem na tela
    /*public void DisplayImg(String texto){

        LayoutInflater li = getLayoutInflater();

        //inflamos o layout tela_opcao_foto.xml_foto.xml na view
        View view = li.inflate(R.layout.dialogabririmg, null);

        dialogimg =  view.findViewById(R.id.imagem_abrir);

            Uri uri = Uri.parse(texto);

            PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
            controller.setUri(uri);
            controller.setOldController(dialogimg.getController());
            controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                    super.onFinalImageSet(id, imageInfo, animatable);
                    if (imageInfo == null || dialogimg == null) {
                        return;
                    }
                    dialogimg.update(imageInfo.getWidth(), imageInfo.getHeight());
                }
            });
            dialogimg.setController(controller.build());

        //Dialog de tela
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
       // builder.setTitle("Foto de Perfil");
        builder.setView(view);
        ImgDialog = builder.create();
        ImgDialog.show();

    }*/



    private  void RecuperarMensagem(){
        //nao deixa que as mensagem duplicam
        mensagens.clear();
        childEventListenermensagens =mensagensRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Mensagem mensagem = dataSnapshot.getValue( Mensagem.class );
                mensagens.add( mensagem );
                recyclemensagens.scrollToPosition(mensagens.size()-1);
                adapter.notifyItemInserted(mensagens.size()-1);

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


    //atualiza
    @Override
    public void onRefresh() {

    }
}

