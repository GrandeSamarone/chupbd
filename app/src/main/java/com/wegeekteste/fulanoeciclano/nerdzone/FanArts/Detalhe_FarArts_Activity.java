package com.wegeekteste.fulanoeciclano.nerdzone.FanArts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;
import com.wegeekteste.fulanoeciclano.nerdzone.Abrir_Imagem.AbrirImagem;
import com.wegeekteste.fulanoeciclano.nerdzone.Activits.MinhaConta;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_FanArts_detalhe;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.RecyclerItemClickListener;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.FanArts;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.FanArtsColecao;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.FanArtsLike;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Usuario;
import com.wegeekteste.fulanoeciclano.nerdzone.PerfilAmigos.Perfil;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Detalhe_FarArts_Activity extends AppCompatActivity {


    private String id;
    private SparkButton botaocurtir,botao_add_colecao;
    private ProgressBar progressBar;
    private ImageView arteimagem;
    private TextView legenda_arte,Nome_usuario,texto_toolbar,n_curtida,n_colecao;
    private CircleImageView iconeUsuario;
    private ChildEventListener ChildEventListeneruser,ChildEventListenerDetalhe;
    private String usuarioLogado;
    private Adapter_FanArts_detalhe adapter_fanArts_detalhe;
    private LinearLayout click;
    private Toolbar toolbar;
    private ArrayList<FanArts> ListaFanarts_detalhe = new ArrayList<>();
    private RecyclerView recyclerViewartedetalhe;
    private DatabaseReference databaseart,database,database_usuario;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe__far_arts_);
        toolbar = findViewById(R.id.toolbar_detalhe_art);
        setSupportActionBar(toolbar);

        //Configuracoes Iniciais
        botaocurtir = findViewById(R.id.botaocurtirart);
        botao_add_colecao = findViewById(R.id.botao_add_a_colecao);
        botao_add_colecao = findViewById(R.id.botao_add_a_colecao);
        click = findViewById(R.id.layoutclick);
        texto_toolbar = findViewById(R.id.texto_Toolbar_art);
        progressBar = findViewById(R.id.progressoart);
        usuarioLogado = UsuarioFirebase.getIdentificadorUsuario();
        databaseart = ConfiguracaoFirebase.getDatabase().getReference().child("arts");
        database_usuario = ConfiguracaoFirebase.getDatabase().getReference().child("usuarios");
          legenda_arte = findViewById(R.id.legendaart);
          arteimagem = findViewById(R.id.fanArts_detalhe_img);
          n_curtida = findViewById(R.id.num_like_fanart);
          n_colecao = findViewById(R.id.num_colecao_fanart);
          Nome_usuario = findViewById(R.id.author_art);
          iconeUsuario = findViewById(R.id.icone_author_art);

        recyclerViewartedetalhe = findViewById(R.id.recycler_detahle_fanarts);
        adapter_fanArts_detalhe  = new Adapter_FanArts_detalhe(ListaFanarts_detalhe,this);
        LinearLayoutManager layoutManagertopico = new LinearLayoutManager(
                Detalhe_FarArts_Activity.this, LinearLayoutManager.HORIZONTAL,false);
          layoutManagertopico.setReverseLayout(true);
          layoutManagertopico.setStackFromEnd(true);
        recyclerViewartedetalhe.setLayoutManager(layoutManagertopico);
        recyclerViewartedetalhe.setHasFixedSize(true);
        recyclerViewartedetalhe.setAdapter(adapter_fanArts_detalhe);


        recyclerViewartedetalhe.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                recyclerViewartedetalhe, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FanArts arteselecionada = ListaFanarts_detalhe.get(position);
                Intent it = new Intent(Detalhe_FarArts_Activity.this,Detalhe_FarArts_Activity.class);
                it.putExtra("id",arteselecionada.getId());
                startActivity(it);
            finish();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
        id = getIntent().getStringExtra("id");
        RecuperarArts(id);

        TrocarFundos_status_bar();
        RecuperarArtsRecycle();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }





    private void RecuperarArts(String idart){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater layoutInflater = LayoutInflater.from(Detalhe_FarArts_Activity.this);
        final View view  = layoutInflater.inflate(R.layout.dialog_carregando_gif_comscroop,null);
        ImageView imageViewgif = view.findViewById(R.id.gifimage);

        Glide.with(getApplicationContext())
                .asGif()
                .load(R.drawable.gif_self)
                .into(imageViewgif);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        ListaFanarts_detalhe.clear();
        ChildEventListenerDetalhe = databaseart.orderByChild("id").equalTo(idart).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FanArts fanArts = dataSnapshot.getValue(FanArts.class);
                if(fanArts!=null) {


                    addbotao_like_colecao(fanArts);
                    legenda_arte.setText(fanArts.getLegenda());
                    texto_toolbar.setText(fanArts.getLegenda());
                    n_curtida.setText(String.valueOf(fanArts.getLikecount()));
                    CarregarDados_do_Criador_do_Comercio(fanArts.getIdauthor());

                    String foto = fanArts.getArtfoto();
                    Glide.with(Detalhe_FarArts_Activity.this)
                            .load(foto)
                            .into(arteimagem);
                    progressBar.setVisibility(View.GONE);
                    dialog.dismiss();
                    arteimagem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent it = new Intent(Detalhe_FarArts_Activity.this, AbrirImagem.class);
                            it.putExtra("id_foto", fanArts.getArtfoto());
                            it.putExtra("id", fanArts.getId());
                            it.putExtra("nome_foto", fanArts.getLegenda());
                            startActivity(it);
                        }
                    });
                }else{
                    dialog.dismiss();
                    Toast.makeText(Detalhe_FarArts_Activity.this, "Selecione uma art", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(Detalhe_FarArts_Activity.this,Lista_Arts.class);
                    startActivity(it);
                    finish();
                }
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



    private void RecuperarArtsRecycle(){
        ListaFanarts_detalhe.clear();
        ChildEventListenerDetalhe = databaseart.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FanArts fanArts = dataSnapshot.getValue(FanArts.class);
                ListaFanarts_detalhe.add(0,fanArts);
                adapter_fanArts_detalhe.notifyDataSetChanged();
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






    private void CarregarDados_do_Criador_do_Comercio(String idusuario) {

        ChildEventListeneruser = database_usuario.orderByChild("id")
                .equalTo(idusuario).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Usuario user = dataSnapshot.getValue(Usuario.class);

                        assert user != null;

                        Nome_usuario.setText(user.getNome());
                        Glide.with(getApplicationContext())
                                .load(user.getFoto())
                                .into(iconeUsuario);
                        if (!usuarioLogado.equals(user.getId())) {
                            click.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent it = new Intent(Detalhe_FarArts_Activity.this, Perfil.class);
                                    it.putExtra("id", user.getId());
                                    startActivity(it);
                                }
                            });
                        } else {
                            click.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent it = new Intent(Detalhe_FarArts_Activity.this, MinhaConta.class);
                                    it.putExtra("id", user.getId());
                                    startActivity(it);

                                }
                            });
                        }
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

    private void addbotao_like_colecao(FanArts fanarts){
        Usuario usuariologado = UsuarioFirebase.getDadosUsuarioLogado();
        DatabaseReference topicoscurtidas= ConfiguracaoFirebase.getFirebaseDatabase()
                .child("fanarts-likes")
                .child(fanarts.getId());
        topicoscurtidas.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int QtdLikes = 0;
                if(dataSnapshot.hasChild("qtdlikes")){
                    FanArtsLike fanArtsLike = dataSnapshot.getValue(FanArtsLike.class);
                    QtdLikes = fanArtsLike.getQtdlikes();
                }
                //Verifica se já foi clicado
                if( dataSnapshot.hasChild(usuariologado.getId()) ){
                    botaocurtir.setChecked(true);
                }else {
                    botaocurtir.setChecked(false);
                }

                //Montar objeto postagem curtida
                FanArtsLike fanArtsLike = new FanArtsLike();
                fanArtsLike.setFanArts(fanarts);
                fanArtsLike.setUsuario(usuariologado);
                fanArtsLike.setQtdlikes(QtdLikes);
                //adicionar evento para curtir foto
               botaocurtir.setEventListener(new SparkEventListener() {
                    @Override
                    public void onEvent(ImageView button, boolean buttonState) {
                        if(buttonState){
                            fanArtsLike.Salvar();

                          n_curtida.setText(String.valueOf(fanArtsLike.getQtdlikes()));
                        }else{
                            fanArtsLike.removerlike();
                           n_curtida.setText(String.valueOf(fanArtsLike.getQtdlikes()));
                        }
                    }
                    @Override
                    public void onEventAnimationEnd(ImageView button, boolean buttonState) {
                    }
                    @Override
                    public void onEventAnimationStart(ImageView button, boolean buttonState) {
                    }
                });
               n_curtida.setText(String.valueOf(fanArtsLike.getQtdlikes()));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        DatabaseReference conto_add_colecao= ConfiguracaoFirebase.getFirebaseDatabase()
                .child("fanarts-colecao")
                .child(fanarts.getId());
        conto_add_colecao.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int QtdAdd = 0;
                if (dataSnapshot.hasChild("qtdadd")) {
                    FanArtsColecao fanArtsColecao = dataSnapshot.getValue(FanArtsColecao.class);
                    QtdAdd = fanArtsColecao.getQtdadd();
                }



                if (dataSnapshot.hasChild(usuariologado.getId())) {
                   botao_add_colecao.setChecked(true);

                } else {
                  botao_add_colecao.setChecked(false);


                }

                //Montar objeto postagem curtida
                FanArtsColecao colecao = new FanArtsColecao();
                colecao.setFanArts(fanarts);
                colecao.setUsuario(usuariologado);
                colecao.setQtdadd(QtdAdd);

                //adicionar evento para curtir foto
               botao_add_colecao.setEventListener(new SparkEventListener() {
                    @Override
                    public void onEvent(ImageView button, boolean buttonState) {
                        if (buttonState) {
                            colecao.Salvar();
                            fanarts.AdicioneiFanArts();
                            n_colecao.setText(String.valueOf(colecao.getQtdadd()));
                            Toast toast = Toast.makeText(Detalhe_FarArts_Activity.this,
                                    "Adicionado as coleções com sucesso", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                        } else {
                            colecao.removerfanarts();
                            n_colecao.setText(String.valueOf(colecao.getQtdadd()));
                            Toast toast = Toast.makeText(Detalhe_FarArts_Activity.this,
                                    "Removido com sucesso", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                        }
                    }

                    @Override
                    public void onEventAnimationEnd(ImageView button, boolean buttonState) {
                    }

                    @Override
                    public void onEventAnimationStart(ImageView button, boolean buttonState) {
                    }
                });
                n_colecao.setText(String.valueOf(colecao.getQtdadd()));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void TrocarFundos_status_bar(){
        //mudando a cor do statusbar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintResource(R.drawable.gradiente_toolbarstatusbar);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintResource(R.drawable.gradiente_toolbarstatusbar);
            //  systemBarTintManager.setStatusBarTintDrawable(Mydrawable);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.parseColor("#1565c0"));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setNavigationBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintResource(R.drawable.gradiente_toolbarstatusbar);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public boolean  onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menufiltro:
                //abrirConfiguracoes();
                break;
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
       Intent it = new Intent(Detalhe_FarArts_Activity.this, Lista_Arts.class);
       startActivity(it);
                    finish();

        }
        return super.onOptionsItemSelected(item);
    }
}
