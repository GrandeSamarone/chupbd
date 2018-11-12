package com.example.fulanoeciclano.nerdzone.Votacao.Listar.empreendedora;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fulanoeciclano.nerdzone.Activits.MinhaConta;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Helper.RecyclerItemClickListener;
import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.R;
import com.example.fulanoeciclano.nerdzone.Votacao.Adapter_votacao.Adapter_empreendedora_fem;
import com.example.fulanoeciclano.nerdzone.Votacao.Cadastro.empreendedor.novo_empreendedora_fem;
import com.example.fulanoeciclano.nerdzone.Votacao.Detalhe.Detalhe_empreendedora;
import com.example.fulanoeciclano.nerdzone.Votacao.model_votacao.Categoria_empreendedora;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.fulanoeciclano.nerdzone.Activits.MainActivity.setWindowFlag;

public class Lista_empreendedora extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    private Toolbar toolbar;
    private FloatingActionButton novo_influencer;
    private FirebaseAuth autenticacao;
    private FirebaseUser usuario;
    private SwipeRefreshLayout refresh;
    private MaterialSearchView SeachView;
    private RecyclerView recyclerView_lista_digital;
    private Adapter_empreendedora_fem adapter;
    private DatabaseReference mDatabase_digital;
    private ChildEventListener valuedigitalListener;
    private Categoria_empreendedora digital;
    private ArrayList<Categoria_empreendedora> lista_digital_influence = new ArrayList<>();
    private ImageView botaoPesquisar;
    private SharedPreferences preferences = null;
    private Dialog dialog;
    private AlertDialog alerta;
    private CircleImageView icone;
    private LinearLayoutManager mManager;
    private DatabaseReference database_usuario;
    private ChildEventListener ChildEventListenerperfil;
    private TextView errobusca;
    private LinearLayout linear_nada_cadastrado,linearerro,linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_empreendedora);
        toolbar = findViewById(R.id.toolbarsecundario);
        toolbar.setTitle("Empreendedora");
        setSupportActionBar(toolbar);

        //Configuracoes Originais
        linear_nada_cadastrado=findViewById(R.id.linear_nada_cadastrado_empreendedora);
        novo_influencer = findViewById(R.id.buton_novo_empreendedora);
        novo_influencer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent( Lista_empreendedora.this,novo_empreendedora_fem.class);
                startActivity(it);
                finish();
            }
        });

        //Configuraçoes iniciais
        icone = findViewById(R.id.icone_user_toolbar);
        linearerro=findViewById(R.id.linearinformacoeserro_mercado);
        errobusca = findViewById(R.id.textoerrobusca_mercado);
        database_usuario = ConfiguracaoFirebase.getDatabase().getReference().child("usuarios");
        digital = new Categoria_empreendedora();
        refresh = findViewById(R.id.refresh_empreendedora);
        refresh.setOnRefreshListener(this);
        refresh.post(new Runnable() {
            @Override
            public void run() {

            }
        });
        refresh.setColorSchemeResources
                (R.color.colorPrimaryDark, R.color.amareloclaro,
                        R.color.accent);

        mDatabase_digital = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("votacao").child("categorias").child("empreendedora");
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        recyclerView_lista_digital = findViewById(R.id.recycleview_empreendedora);
        recyclerView_lista_digital.setHasFixedSize(true);
        //recycleview
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        recyclerView_lista_digital.setLayoutManager(mManager);
        adapter = new Adapter_empreendedora_fem(this,lista_digital_influence);

        recyclerView_lista_digital.setAdapter(adapter);
        //Aplicar Evento click
        recyclerView_lista_digital.addOnItemTouchListener(new RecyclerItemClickListener(this,
                recyclerView_lista_digital, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<Categoria_empreendedora> listCategoriaAtualizado = adapter.getcategoria();

                if (listCategoriaAtualizado.size() > 0) {
                    Categoria_empreendedora categoriaselecionada = listCategoriaAtualizado.get(position);
                    Intent it = new Intent(Lista_empreendedora.this,Detalhe_empreendedora.class);
                    it.putExtra("categoria_selecionada", categoriaselecionada);
                    startActivity(it);
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


    @Override
    public void onRefresh() {
        RecuperarLista_digital_fem();
    }

    @Override
    protected void onStart() {
        super.onStart();
        RecuperarLista_digital_fem();
        CarregarDados_do_Usuario();
        TrocarFundos_status_bar();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDatabase_digital.removeEventListener(valuedigitalListener);
    }

    private void RecuperarLista_digital_fem(){
       linear_nada_cadastrado.setVisibility(View.VISIBLE);
        lista_digital_influence.clear();
        valuedigitalListener = mDatabase_digital.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Categoria_empreendedora cat = dataSnapshot.getValue(Categoria_empreendedora.class);
                lista_digital_influence.add(cat);
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
        usuario = UsuarioFirebase.getUsuarioAtual();
        String email = usuario.getEmail();
        ChildEventListenerperfil=database_usuario.orderByChild("tipoconta").equalTo(email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Usuario perfil = dataSnapshot.getValue(Usuario.class );
                assert perfil != null;


                String iconeurl = perfil.getFoto();
                icone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(Lista_empreendedora.this, MinhaConta.class);
                        startActivity(it);

                    }
                });
                Glide.with(Lista_empreendedora.this)
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

