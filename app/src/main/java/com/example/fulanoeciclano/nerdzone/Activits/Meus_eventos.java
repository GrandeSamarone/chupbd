package com.example.fulanoeciclano.nerdzone.Activits;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fulanoeciclano.nerdzone.Adapter.Adapter_Meus_Comercio;
import com.example.fulanoeciclano.nerdzone.Adapter.Adapter_Meus_Eventos;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Model.Comercio;
import com.example.fulanoeciclano.nerdzone.Model.Evento;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Meus_eventos extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView_meus_eventos,recyclerView_meus_comercios;
    private DatabaseReference database,databaseusuario;
    private Query meus_eventosref,meus_comercioref;
    private String identificadoUsuario;
    private TextView nomeevento,nomecomercio,errobusca;
    private Evento evento;
    private AlertDialog alerta;
    private Toolbar toolbar;
    private List<Evento> lista_Meus_Eventos=new ArrayList<>();
    private List<Comercio> lista_meus_comercio = new ArrayList<>();
    private Adapter_Meus_Eventos mAdapter;
    private Adapter_Meus_Comercio adapterComercio;
    private ChildEventListener childEventListenerMeus_Eventos,childEventListenerMeus_Comercio;
    private CircleImageView icone;
    private FirebaseUser usuario;
    private ChildEventListener ChildEventListenerperfil;
    private SwipeRefreshLayout refresh;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_eventos);
        toolbar = findViewById(R.id.toolbarsecundario);
        toolbar.setTitle("Minhas Publicações");
        setSupportActionBar(toolbar);

        //Configurações Originais
        linearLayout = findViewById(R.id.linear_nada_cadastrado_lojaevento);
        database = FirebaseDatabase.getInstance().getReference();
        nomeevento = findViewById(R.id.eventotexto);
        nomecomercio = findViewById(R.id.comerciotexto);
        databaseusuario= ConfiguracaoFirebase.getDatabase().getReference().child("usuarios");
        evento = new Evento();
        refresh = findViewById(R.id.atualizarmeuseventosrefresh);
        refresh.setOnRefreshListener(this);
        refresh.post(new Runnable() {
            @Override
            public void run() {
                RecuperarMeus_Comercio();
                RecuperarMeus_Eventos();
            }
        });
        refresh.setColorSchemeResources
                (R.color.colorPrimaryDark, R.color.amareloclaro,
                        R.color.accent);
        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        meus_eventosref=database.child("meusevento").child(identificadorUsuario);
        meus_comercioref = database.child("meuscomercio").child(identificadorUsuario);

         mAdapter = new Adapter_Meus_Eventos(lista_Meus_Eventos,Meus_eventos.this);
         adapterComercio = new Adapter_Meus_Comercio(lista_meus_comercio,Meus_eventos.this);

         recyclerView_meus_comercios = findViewById(R.id.recycler_minhas_lojas);
        RecyclerView.LayoutManager layoutManagercomercio = new LinearLayoutManager(Meus_eventos.this);
        recyclerView_meus_comercios.setLayoutManager(layoutManagercomercio);
        recyclerView_meus_comercios.setHasFixedSize(true);
        recyclerView_meus_comercios.setAdapter(adapterComercio);

        recyclerView_meus_eventos = findViewById(R.id.recycler_meus_eventos);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Meus_eventos.this);
       recyclerView_meus_eventos.setLayoutManager(layoutManager);
       recyclerView_meus_eventos.setHasFixedSize(true);
       recyclerView_meus_eventos.setAdapter(mAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public void onStart() {
        super.onStart();
        TrocarFundos_status_bar();
        CarregarDados_do_Usuario();
    }
    @Override
    public void onRefresh() {
        RecuperarMeus_Comercio();
        RecuperarMeus_Eventos();
    }
    private void RecuperarMeus_Eventos() {
        //Progress
         linearLayout.setVisibility(View.VISIBLE);
        refresh.setRefreshing(false);
        lista_Meus_Eventos.clear();
        childEventListenerMeus_Eventos = meus_eventosref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Evento evento =dataSnapshot.getValue(Evento.class);
            lista_Meus_Eventos.add(evento);
            if(lista_Meus_Eventos.size()>0){
                nomeevento.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
            }else{
                nomeevento.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);

            }
            mAdapter.notifyDataSetChanged();

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
    private void RecuperarMeus_Comercio() {
        //Progress

        refresh.setRefreshing(false);
        lista_meus_comercio.clear();
        childEventListenerMeus_Comercio = meus_comercioref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Comercio comercio =dataSnapshot.getValue(Comercio.class);
                lista_meus_comercio.add(comercio);
                if(lista_meus_comercio.size()>0){
                    nomecomercio.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                }else{
                    nomecomercio.setVisibility(View.GONE);


                }
                adapterComercio.notifyDataSetChanged();

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
    @Override
    public void onStop() {
        super.onStop();
        meus_eventosref.removeEventListener(childEventListenerMeus_Eventos);
        meus_comercioref.removeEventListener(childEventListenerMeus_Comercio);
    }
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    //Botao Voltar
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                startActivity(new Intent(this, MainActivity.class)); //O efeito ao ser pressionado do botão (no caso abre a activity)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }else{
                    finish();
                }
                break;



            default:break;
        }

        return true;
    }
    private void CarregarDados_do_Usuario(){
        usuario = UsuarioFirebase.getUsuarioAtual();
        String email = usuario.getEmail();
        ChildEventListenerperfil=databaseusuario.orderByChild("tipoconta").equalTo(email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Usuario perfil = dataSnapshot.getValue(Usuario.class );
                assert perfil != null;


                String icone = perfil.getFoto();
                IconeUsuario(icone);

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
    private void IconeUsuario(String url) {
        //Imagem do icone do usuario
        icone = findViewById(R.id.icone_user_toolbar);
        icone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Meus_eventos.this, MinhaConta.class);
                startActivity(it);

            }
        });


        Glide.with(Meus_eventos.this)
                .load(url)
                .into(icone);
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


}
