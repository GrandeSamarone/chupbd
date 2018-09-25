package com.example.fulanoeciclano.nerdzone.Evento;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.bumptech.glide.Glide;
import com.example.fulanoeciclano.nerdzone.Activits.MainActivity;
import com.example.fulanoeciclano.nerdzone.Activits.MinhaConta;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Helper.RecyclerItemClickListener;
import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Model.Evento;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.R;
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

public class Evento_Lista extends AppCompatActivity  implements SwipeRefreshLayout.OnRefreshListener
{

    private DatabaseReference mDatabaseevento;
    private SwipeRefreshLayout swipeatualizar;
    private CircleImageView icone;
    private MaterialSearchView SeachView;
    private FirebaseAuth autenticacao;
    private FirebaseAuth mFirebaseAuth;
    private FloatingActionButton Novo_Evento;
    private RecyclerView recyclerEvento;
    private Evento_Adapter adapterevento;
    private ArrayList<Evento> ListaEvento = new ArrayList<>();
    private ChildEventListener valueEventListenerEvento;
    private LinearLayoutManager mManager;
    private Usuario user;
    private Toolbar toolbar;
    private String mPhotoUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento__lista);


        toolbar = findViewById(R.id.toolbarsecundario);
        toolbar.setTitle(R.string.name_evento);
        setSupportActionBar(toolbar);

        IconeUsuario();


        //ROLAR PARA ATUALIZAR
        swipeatualizar= findViewById(R.id.swipe_list_evento);
        swipeatualizar.setOnRefreshListener(Evento_Lista.this);

        swipeatualizar.post(new Runnable() {
            @Override
            public void run() {
                swipeatualizar.setRefreshing(true);
                RecuperarEventos();

            }
        });
        swipeatualizar.setColorSchemeResources
                (R.color.colorPrimaryDark, R.color.amareloclaro,
                        R.color.accent);

        //Configuracoes Basicas
        recyclerEvento = findViewById(R.id.lista_evento);
        mDatabaseevento = ConfiguracaoFirebase.getFirebaseDatabase().child("evento");

        //Configura Adapter
        adapterevento = new Evento_Adapter(ListaEvento,this);

        //Adapter
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerEvento.setLayoutManager(layoutManager);
        recyclerEvento.setHasFixedSize(true);
        recyclerEvento.setAdapter(adapterevento);

        recyclerEvento.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                recyclerEvento, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<Evento> liseventoatualizado = adapterevento.getevento();

                if (liseventoatualizado.size() > 0) {
                    Evento eventoselecionado = liseventoatualizado.get(position);
                    Intent it = new Intent(Evento_Lista.this, DetalheEvento.class);
                    it.putExtra("eventoselecionado", eventoselecionado);
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

//Botao Pesquisa
        SeachView = findViewById(R.id.materialSeachComercio);
        SeachView.setHint("Pesquisar");
        SeachView.setHintTextColor(R.color.cinzaclaro);
        SeachView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                  icone.setVisibility(View.GONE);
                if(newText!=null && !newText.isEmpty()){
                   PesquisarEvento(newText.toLowerCase());

                }else{

                    icone.setVisibility(View.VISIBLE);
                    recarregarEvento();
                }

                return true;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TrocarFundos_status_bar();

    }

    private void TrocarFundos_status_bar(){
        //mudando a cor do statusbar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintResource(R.drawable.gradiente_toolbar);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintResource(R.drawable.gradiente_toolbar);
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
            systemBarTintManager.setStatusBarTintResource(R.drawable.gradiente_toolbar);
        }
    }


    //botao Pesquisar
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);

        //Botao Pesquisa

        MenuItem item = menu.findItem(R.id.menuPesquisa);
        SeachView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
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

    private void IconeUsuario() {
        //Imagem do icone do usuario
        icone = findViewById(R.id.icone_user_toolbar);
        icone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Evento_Lista.this, MinhaConta.class);
                startActivity(it);
            }
        });
        FirebaseUser UsuarioAtual = UsuarioFirebase.getUsuarioAtual();
        mPhotoUrl=UsuarioAtual.getPhotoUrl().toString();

        Glide.with(Evento_Lista.this)
                .load(mPhotoUrl)
                .into(icone);
    }

    @Override
    public void onRefresh() {
        RecuperarEventos();
    }

    @Override
    public void onStop() {
        super.onStop();
        mDatabaseevento.removeEventListener(valueEventListenerEvento);
    }

    //recupera e nao deixa duplicar
    public void RecuperarEventos(){
        ListaEvento.clear();
        swipeatualizar.setRefreshing(true);
        valueEventListenerEvento =mDatabaseevento.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Evento evento = dataSnapshot.getValue(Evento.class );
                ListaEvento.add(0,evento);

                adapterevento.notifyDataSetChanged();
                swipeatualizar.setRefreshing(false);

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

    private void PesquisarEvento(String texto) {
//  String nick_null =  getString(R.string.buscar_usuario, usuarioAtual.getDisplayName());
        List<Evento> listaEventoBusca = new ArrayList<>();
        for (Evento evento : ListaEvento) {
            String nome=evento.getTitulo().toLowerCase();
            if(nome.contains(texto)){
                listaEventoBusca.add(evento);

            }else if(listaEventoBusca.size()==0){
                // Toast.makeText(this, "zero seu merda", Toast.LENGTH_SHORT).show();
            }

        }
        adapterevento = new Evento_Adapter(listaEventoBusca, Evento_Lista.this);
        recyclerEvento.setAdapter(adapterevento);
        adapterevento.notifyDataSetChanged();
    }

    private void recarregarEvento() {
        adapterevento = new Evento_Adapter(ListaEvento, Evento_Lista.this);
        recyclerEvento.setAdapter(adapterevento);
        adapterevento.notifyDataSetChanged();
    }

}
