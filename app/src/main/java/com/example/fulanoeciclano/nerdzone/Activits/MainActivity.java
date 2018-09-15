package com.example.fulanoeciclano.nerdzone.Activits;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fulanoeciclano.nerdzone.Adapter.AdapterPagInicial.AdapterMercado;
import com.example.fulanoeciclano.nerdzone.Adapter.EventoAdapter;
import com.example.fulanoeciclano.nerdzone.Adapter.GibiAdapter;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Helper.HeaderDecoration;
import com.example.fulanoeciclano.nerdzone.Helper.RecyclerItemClickListener;
import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Mercado.Detalhe_Mercado;
import com.example.fulanoeciclano.nerdzone.Mercado.MercadoActivity;
import com.example.fulanoeciclano.nerdzone.Model.Evento;
import com.example.fulanoeciclano.nerdzone.Model.Gibi;
import com.example.fulanoeciclano.nerdzone.Model.Mercado;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        SwipeRefreshLayout.OnRefreshListener {


    private RecyclerView recyclerViewListaGibiMercado;
    private RecyclerView recyclerViewListaGibiDC;
    private RecyclerView recyclerViewListaGibiOutros;
    private RecyclerView recyclerVieweventos;
    private GibiAdapter adapterDC,adapterOutros;
    private AdapterMercado adapterMercado;
    private EventoAdapter adapterEvento;
    private List<Mercado> ListaGibiMercado = new ArrayList<>();
    private ArrayList<Gibi> ListaGibiDC = new ArrayList<>();
    private ArrayList<Gibi> ListaGibiOutros = new ArrayList<>();
    private ArrayList<Evento> ListaEvento = new ArrayList<>();
    private ArrayList<String> mKeys = new ArrayList<>();
    private DatabaseReference GibiMercado;
    private DatabaseReference GibiDC;
    private DatabaseReference GibiOutros;
    private DatabaseReference GibiEventos;
    private ChildEventListener valueEventListenerMercado;
    private ChildEventListener valueEventListenerEvento;
    private ChildEventListener valueEventListenerDC;
    private ChildEventListener valueEventListenerOutros;
    private TextView maisventoTxt;

    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout mdrawer;
    private CircleImageView img_drawer,img_toolbar;
    private TextView nome_drawer;
    private TextView email_drawer;
    private StorageReference storageReference;
    private DatabaseReference database;
    private String identificadorUsuario;
    private String mPhotoUrl;
    private Usuario usuarioLogado;
    private SwipeRefreshLayout swipe;
    SharedPreferences sPreferences = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Verifica se é a primeira vez da instalacao
        sPreferences = getSharedPreferences("firstRun", MODE_PRIVATE);
        //Toolbar
       toolbar =findViewById(R.id.toolbarmain);
       // toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        //carregando das informacoes do usuario no Drawer
        mdrawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, mdrawer, R.string.open, R.string.close);
        mdrawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigat_id);
        navigationView.setNavigationItemSelectedListener(this);
       //esse cod desativa o padrao das cores da naevagacao
        //configuraçoes basica navigation
        navigationView.setItemIconTintList(null);
        View navHeaderView = navigationView.getHeaderView(0);
        img_drawer=navHeaderView.findViewById(R.id.Img_perfil_drawer);
        img_toolbar = findViewById(R.id.icone_img_toolbar);
        nome_drawer = navHeaderView.findViewById(R.id.Nome_usuario_drawer);
        email_drawer=navHeaderView.findViewById(R.id.Email_usuario_drawer);

          //Configuracoes Originais
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        database = ConfiguracaoFirebase.getDatabase().getReference().child("usuarios");
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        usuarioLogado = new Usuario();


        //REFRESH
        swipe = findViewById(R.id.swipe_ac_main);
        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
            @Override
            public void run() {
                    CarregarInformacoesNoDrawer();
            }
        });

        swipe.setColorSchemeResources
                (R.color.colorPrimaryDark, R.color.amareloclaro,
                        R.color.accent);



//Recycle
        recyclerViewListaGibiDC = findViewById(R.id.RecycleViewGibiDC);
        recyclerViewListaGibiOutros = findViewById(R.id.RecycleViewGibiOutros);
        recyclerViewListaGibiMercado = findViewById(R.id.RecycleViewMercado);
        recyclerVieweventos = findViewById(R.id.RecycleViewEventos);

        GibiMercado = ConfiguracaoFirebase.getFirebaseDatabase().child("mercado");
        GibiDC = ConfiguracaoFirebase.getFirebaseDatabase().child("DC");
        GibiOutros = ConfiguracaoFirebase.getFirebaseDatabase().child("Outros");
        GibiEventos = ConfiguracaoFirebase.getFirebaseDatabase().child("evento");

        //Configurar Adapter
        adapterMercado=new AdapterMercado(ListaGibiMercado,MainActivity.this);
        adapterDC = new GibiAdapter(ListaGibiDC,MainActivity.this);
        adapterOutros = new GibiAdapter(ListaGibiOutros,MainActivity.this);
        adapterEvento = new EventoAdapter(ListaEvento,MainActivity.this);

        //Configurar recycleView Evento
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
        MainActivity.this, LinearLayoutManager.HORIZONTAL,false);
        recyclerVieweventos.setLayoutManager(layoutManager);
        recyclerVieweventos.setHasFixedSize(true);
        recyclerVieweventos.setAdapter(adapterEvento);
        recyclerVieweventos.addItemDecoration(new HeaderDecoration(MainActivity.this,
                recyclerVieweventos,  R.layout.header_evento));



        //Configurar recycleView Marvel
        RecyclerView.LayoutManager layoutManagerMarvel = new LinearLayoutManager
                (MainActivity.this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewListaGibiMercado.setLayoutManager(layoutManagerMarvel);
        recyclerViewListaGibiMercado.setHasFixedSize(true);
        recyclerViewListaGibiMercado.setAdapter(adapterMercado);
        recyclerViewListaGibiMercado.addItemDecoration(new HeaderDecoration(MainActivity.this,
                recyclerViewListaGibiMercado,  R.layout.header_evento));


        //Configurar recycleView DC
        RecyclerView.LayoutManager layoutManagerdc = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewListaGibiDC.setLayoutManager(layoutManagerdc);
        recyclerViewListaGibiDC.setHasFixedSize(true);
        recyclerViewListaGibiDC.setAdapter(adapterDC);

        //Configurar recycleView Outros
        RecyclerView.LayoutManager layoutManageroutros = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewListaGibiOutros.setLayoutManager(layoutManageroutros);
        recyclerViewListaGibiOutros.setHasFixedSize(true);
        recyclerViewListaGibiOutros.setAdapter(adapterOutros);



        recyclerViewListaGibiMercado.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                recyclerViewListaGibiMercado, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Mercado mercadoselecionado = ListaGibiMercado.get(position);
                Intent it = new Intent(MainActivity.this,Detalhe_Mercado.class);
                it.putExtra("mercadoelecionado",mercadoselecionado);
                startActivity(it);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));


    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            swipe.setRefreshing(true);
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        // This will get you total fragment in the backStack
        int count = getFragmentManager().getBackStackEntryCount();
        switch(count){
            case 0:
                super.onBackPressed();
                break;
            case 1:

                super.onBackPressed();
            case 2:

                super.onBackPressed();
            default:
                getFragmentManager().popBackStack();
                break;
        }


    }

    public boolean  onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.menusair:
                DeslogarUsuario();
                finish();
                break;
            case R.id.menuconfiguracoes:
                //abrirConfiguracoes();
                break;
        }
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


  private void botoes_Mais(){
      maisventoTxt = findViewById(R.id.maisevento);
      maisventoTxt.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent it = new Intent(MainActivity.this, MercadoActivity.class);
              startActivity(it);
          }
      });

  }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        if (id == R.id.minhascolecoes_menu) {
           Intent it = new Intent(MainActivity.this,DescricaoGibiActivity.class);
           startActivity(it);
        } else if (id == R.id.meus_evento_menu) {
      Intent it = new Intent(MainActivity.this,Meus_eventos.class);
      startActivity(it);
        } else if (id == R.id.mensagens_menu) {
            Intent it = new Intent(MainActivity.this,MeusAmigosActivity.class);
            startActivity(it);
        }else if (id == R.id.minha_conta_menu) {
        Intent it = new Intent(MainActivity.this,MinhaConta.class);
        startActivity(it);
        }
        else if (id == R.id.comercio_menu) {
            Intent it = new Intent(MainActivity.this,MercadoActivity.class);
            startActivity(it);
        }


        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void DeslogarUsuario(){

           /* AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // user is now signed out
                            startActivity(new Intent(MainActivity.this,Autenticacao.class));

                            finish();

        }
        });
        */
    }

    @Override
    protected void onStart() {
        super.onStart();
        //carregar informacao no Drawer
        CarregarInformacoesNoDrawer();
        botoes_Mais();
        RecuperarMercado();
        RecuperarDC();
        RecuperarOutros();
        RecuperarEvento();
    }

    @Override
    public void onStop() {
        super.onStop();
        GibiMercado.removeEventListener(valueEventListenerMercado);
        GibiDC.removeEventListener(valueEventListenerDC);
        GibiOutros.removeEventListener(valueEventListenerOutros);
        GibiEventos.removeEventListener(valueEventListenerEvento);
    }
    public  void CarregarInformacoesNoDrawer(){
        swipe.setRefreshing(true);
        FirebaseUser UsuarioAtual = UsuarioFirebase.getUsuarioAtual();
        if(UsuarioAtual.getPhotoUrl()!=null){
            mPhotoUrl=UsuarioAtual.getPhotoUrl().toString();
        }
        nome_drawer.setText(UsuarioAtual.getDisplayName());
          email_drawer.setText(UsuarioAtual.getEmail());

        Glide.with(MainActivity.this)
                .load(mPhotoUrl)
                .into(img_drawer);

        Glide.with(MainActivity.this)
                .load(mPhotoUrl)
                .into(img_toolbar);
        swipe.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        RecuperarMercado();
        RecuperarDC();
        RecuperarOutros();
        RecuperarEvento();
        CarregarInformacoesNoDrawer();
    }
    //recupera e nao deixa duplicar
    public void RecuperarEvento(){
        ListaEvento.clear();
        swipe.setRefreshing(true);
        valueEventListenerEvento =GibiEventos.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Evento evento = dataSnapshot.getValue(Evento.class );
                ListaEvento.add(0,evento);


                adapterEvento.notifyDataSetChanged();
                swipe.setRefreshing(false);

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



    //recupera e nao deixa duplicar
    public void RecuperarMercado(){
        ListaGibiMercado.clear();
        swipe.setRefreshing(true);
        valueEventListenerMercado =GibiMercado.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot loja:dataSnapshot.getChildren()){
                    for(DataSnapshot artista:loja.getChildren()){
                        for(DataSnapshot mercados:artista.getChildren()){

                            Mercado mercado = mercados.getValue(Mercado.class);
                            ListaGibiMercado.add(mercado);

                            Collections.reverse(ListaGibiMercado);
                            adapterMercado.notifyDataSetChanged();
                            swipe.setRefreshing(false);

                        }
                    }
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
        swipe.setRefreshing(false);
    }
    public void remove(Gibi gibi){
        //  GibiMarvel.child(gibi.getKey()).removeValue();
    }
    //recupera e nao deixa duplicar DC
    public void RecuperarDC(){
        ListaGibiDC.clear();
        swipe.setRefreshing(true);
        valueEventListenerDC =GibiDC.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Gibi gibi = dataSnapshot.getValue(Gibi.class );
                gibi.setKey(dataSnapshot.getKey());
                ListaGibiDC.add(0,gibi);
                adapterDC.notifyDataSetChanged();
                swipe.setRefreshing(false);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Gibi gibi =dataSnapshot.getValue(Gibi.class);
                String key =dataSnapshot.getKey();
                for(Gibi gb:ListaGibiDC){
                    if(gb.getKey().equals(key)){
                        gb.setValues(gibi);
                        break;
                    }
                }
                adapterDC.notifyDataSetChanged();
                swipe.setRefreshing(false);
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

    //recupera e nao deixa duplicar Outros
    public void RecuperarOutros(){
        ListaGibiOutros.clear();
        swipe.setRefreshing(true);
        valueEventListenerOutros =GibiOutros.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Gibi gibi = dataSnapshot.getValue(Gibi.class );
                gibi.setKey(dataSnapshot.getKey());
                ListaGibiOutros.add(0,gibi);
                adapterOutros.notifyDataSetChanged();
                swipe.setRefreshing(false);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Gibi gibi =dataSnapshot.getValue(Gibi.class);
                String key =dataSnapshot.getKey();
                for(Gibi gb:ListaGibiOutros){
                    if(gb.getKey().equals(key)){
                        gb.setValues(gibi);
                        break;
                    }
                }
                adapterOutros.notifyDataSetChanged();
                swipe.setRefreshing(false);
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
