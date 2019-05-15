package com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Listar.digital_influencer;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.wegeekteste.fulanoeciclano.nerdzone.Activits.MainActivity;
import com.wegeekteste.fulanoeciclano.nerdzone.Activits.MinhaConta;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.RecyclerItemClickListener;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Usuario;
import com.wegeekteste.fulanoeciclano.nerdzone.R;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Adapter_votacao.Adapter_categoria_pessoa_masc;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Cadastro.digital_influencer.novo_digital_masc;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Detalhe.detalhe_votacao_masc;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.model_votacao.Categoria_Pessoa_masc;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.model_votacao.Categoria_Tempo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Lista_digital_masc extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private Toolbar toolbar;
    private FloatingActionButton novo_influencer;
    private FirebaseAuth autenticacao;
    private FirebaseUser usuario;
    private SwipeRefreshLayout refresh;
    private MaterialSearchView SeachView;
    private RecyclerView recyclerView_lista_digital;
    private Adapter_categoria_pessoa_masc adapter;
    private DatabaseReference mDatabase_digital,database_tempo;
    private ChildEventListener valuedigitalListener;
    private Categoria_Pessoa_masc digital;
    private ArrayList<Categoria_Pessoa_masc> lista_digital_influence = new ArrayList<>();
    private ImageView botaoPesquisar;
    private SharedPreferences preferences = null;
    private Dialog dialog;
    private AlertDialog alerta;
    private CircleImageView icone;
    private LinearLayoutManager mManager;
    private DatabaseReference database_usuario;
    private ChildEventListener ChildEventListenerperfil,childEventListener_d_masc;
    private TextView errobusca;
    private LinearLayout linear_nada_cadastrado,linearerro,linear;
    private String identificadorUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_lista_digital_masc);

        toolbar = findViewById(R.id.toolbarsecundario);
        toolbar.setTitle("D.Influencer Masculino");
        setSupportActionBar(toolbar);

        //Configuracoes Originais
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        novo_influencer = findViewById(R.id.buton_novo_digital_masc);
        novo_influencer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent( Lista_digital_masc.this,novo_digital_masc.class);
                startActivity(it);
                finish();
            }
        });

        //Configuraçoes iniciais
        icone = findViewById(R.id.icone_user_toolbar);
        linearerro=findViewById(R.id.linearinformacoeserro_mercado);
        linear_nada_cadastrado= findViewById(R.id.linear_nada_cadastrado_digital_masc);
        errobusca = findViewById(R.id.textoerrobusca_mercado);
        database_usuario = ConfiguracaoFirebase.getDatabase().getReference().child("usuarios");
        digital = new Categoria_Pessoa_masc();
        refresh = findViewById(R.id.refresh_digital_influenc);
        refresh.setOnRefreshListener(this);
        refresh.post(new Runnable() {
            @Override
            public void run() {
                CarregarDados_do_Usuario();
                TrocarFundos_status_bar();
                Verificar_Tempo_votacao();
            }
        });
        refresh.setColorSchemeResources
                (R.color.colorPrimaryDark, R.color.amareloclaro,
                        R.color.accent);

        final int TIME_BETWEEN_RELOAD = 5000;
        final Handler myHandler = new Handler();


        final Runnable reloadWebViewRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d("run", "running the runnable now");
                RecuperarLista_digital_masc();
                // Continue the reload every 5 seconds
                myHandler.postDelayed(this, TIME_BETWEEN_RELOAD);

            }
        };
// start the initial reload
        myHandler.postDelayed(reloadWebViewRunnable, TIME_BETWEEN_RELOAD);
        mDatabase_digital = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("votacao").child("categorias").child("digital_influence_masc");
        database_tempo =  ConfiguracaoFirebase.getDatabase().getReference()
                .child("votacao").child("categorias").child("digital_influence_masc_TIME").child(identificadorUsuario);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        recyclerView_lista_digital = findViewById(R.id.recycleview_digital_influence);
        recyclerView_lista_digital.setHasFixedSize(true);
        //recycleview
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        recyclerView_lista_digital.setLayoutManager(mManager);
        adapter = new Adapter_categoria_pessoa_masc(this,lista_digital_influence);

        recyclerView_lista_digital.setAdapter(adapter);


        //Aplicar Evento click
        recyclerView_lista_digital.addOnItemTouchListener(new RecyclerItemClickListener(this,
                recyclerView_lista_digital, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<Categoria_Pessoa_masc> listCategoriaAtualizado = adapter.getcategoria();

                if (listCategoriaAtualizado.size() > 0) {
                    Categoria_Pessoa_masc categoriaselecionada = listCategoriaAtualizado.get(position);
                    Intent it = new Intent(Lista_digital_masc.this,detalhe_votacao_masc.class);
                    it.putExtra("categoria_selecionada", categoriaselecionada);
                    startActivity(it);
                    finish();
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }




    private void Verificar_Tempo_votacao() {

        final Calendar calendartempo = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());// MM'/'dd'/'y;

        Query data_time = database_tempo;
        database_tempo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Categoria_Tempo tempo = dataSnapshot.getValue(Categoria_Tempo.class);
                    String DataAtual = simpleDateFormat.format(calendartempo.getTime());
                    Log.i("sdsds", DataAtual + "datacadastrada" + tempo.getTempodata());
                    if (!DataAtual.equals(tempo.getTempodata())) {
                        database_tempo.removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();
        mDatabase_digital.removeEventListener(valuedigitalListener);
    }

    private void RecuperarLista_digital_masc(){
        linear_nada_cadastrado.setVisibility(View.VISIBLE);
        lista_digital_influence.clear();
        valuedigitalListener = mDatabase_digital.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                 Categoria_Pessoa_masc cat = dataSnapshot.getValue(Categoria_Pessoa_masc.class);
                 lista_digital_influence.add(0,cat);
                 if(lista_digital_influence.size()>0){
                     linear_nada_cadastrado.setVisibility(View.GONE);
                 }
                adapter.notifyDataSetChanged();
                refresh.setRefreshing(false);
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

    private void CarregarDados_do_Usuario(){
        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        ChildEventListenerperfil=database_usuario.orderByChild("id").equalTo(identificadorUsuario).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Usuario perfil = dataSnapshot.getValue(Usuario.class );
                assert perfil != null;


                String iconeurl = perfil.getFoto();
                icone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(Lista_digital_masc.this, MinhaConta.class);
                        startActivity(it);

                    }
                });
                Glide.with(Lista_digital_masc.this)
                        .load(iconeurl)
                        .into(icone);

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

    private void TrocarFundos_status_bar(){
        //mudando a cor do statusbar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            MainActivity.setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
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
            MainActivity.setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.parseColor("#1565c0"));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setNavigationBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintResource(R.drawable.gradiente_toolbarstatusbar);
        }
    }

    @Override
    public void onRefresh() {
RecuperarLista_digital_masc();
    }

    //Botao Voltar
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                finish();



                break;

            default:
                break;
        }

        return true;
    }

}
