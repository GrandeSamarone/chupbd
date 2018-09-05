package com.example.fulanoeciclano.nerdzone.Activits;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        SwipeRefreshLayout.OnRefreshListener {

    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout mdrawer;
    private CircleImageView img_drawer;
    private TextView nome_drawer;
    private TextView email_drawer;
    private TextView telefone_drawer;
    private StorageReference storageReference;
    private DatabaseReference database;
    private String identificadorUsuario;
    private String mPhotoUrl;
    private Usuario usuarioLogado;
    private SwipeRefreshLayout swipe;
    SharedPreferences sPreferences = null;

    private static final String TAG = "MainActivity";

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Verifica se é a primeira vez da instalacao
        sPreferences = getSharedPreferences("firstRun", MODE_PRIVATE);
        //Toolbar
       toolbar =findViewById(R.id.toolbarprincipal);
        toolbar.setTitle(R.string.app_name);
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
        navigationView.setItemIconTintList(null);
        View navHeaderView = navigationView.getHeaderView(0);
        img_drawer=navHeaderView.findViewById(R.id.Img_perfil_drawer);
        nome_drawer = navHeaderView.findViewById(R.id.Nome_usuario_drawer);
        email_drawer=navHeaderView.findViewById(R.id.Email_usuario_drawer);
        telefone_drawer=navHeaderView.findViewById(R.id.Tel_usuario_drawer);
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




        //Configurar Abas
        final FragmentPagerItemAdapter  adapter= new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                 .add("COMICS", InicioFragment.class )
                // .add("Noticia",Noticia_Fragment.class)
                 .add("TOPICOS", BatePapoFragment.class)
                 .add("EVENTOS",EventoListaFragment.class)
                // .add("Tops", RankFragment.class)
                .create()
        );



        SmartTabLayout ViewPageTab = findViewById(R.id.SmartTabLayout);
        mViewPager = findViewById(R.id.viewPager);
        mViewPager.setAdapter(adapter);
        ViewPageTab.setViewPager(mViewPager);



    }



    public  boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);

        return super.onCreateOptionsMenu(menu);
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
              mViewPager.setCurrentItem(0);
                super.onBackPressed();
            case 2:
                mViewPager.setCurrentItem(0);
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
        else if (id == R.id.mercado_menu) {
            Intent it = new Intent(MainActivity.this,MercadoActivity.class);
            startActivity(it);
        }


        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void DeslogarUsuario(){

            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // user is now signed out
                            startActivity(new Intent(MainActivity.this,Autenticacao.class));

                            finish();

        }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //carregar informacao no Drawer
        CarregarInformacoesNoDrawer();
    }


    public  void CarregarInformacoesNoDrawer(){
        swipe.setRefreshing(true);
        FirebaseUser UsuarioAtual = UsuarioFirebase.getUsuarioAtual();
        if(UsuarioAtual.getPhotoUrl()!=null){
            mPhotoUrl=UsuarioAtual.getPhotoUrl().toString();
        }
        nome_drawer.setText(UsuarioAtual.getDisplayName());
          telefone_drawer.setText(UsuarioAtual.getPhoneNumber());
          email_drawer.setText(UsuarioAtual.getEmail());

        Picasso.with(MainActivity.this)
                .load(mPhotoUrl)
                .into(img_drawer);
        swipe.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        CarregarInformacoesNoDrawer();
    }
}
