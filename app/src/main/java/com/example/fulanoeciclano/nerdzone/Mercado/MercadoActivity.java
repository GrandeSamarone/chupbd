package com.example.fulanoeciclano.nerdzone.Mercado;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.fulanoeciclano.nerdzone.Activits.MainActivity;
import com.example.fulanoeciclano.nerdzone.Activits.MinhaConta;
import com.example.fulanoeciclano.nerdzone.Adapter.MercadoAdapter;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Helper.RecyclerItemClickListener;
import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Model.Mercado;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.fulanoeciclano.nerdzone.Activits.MainActivity.setWindowFlag;

public class MercadoActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private Toolbar toolbar;
    private FloatingActionButton novoMercado;
    private FirebaseAuth autenticacao;
    private SwipeRefreshLayout refresh;
    private MaterialSearchView SeachView;
    private RecyclerView recyclerViewMercadoPublico;
    private MercadoAdapter adapter;
    private DatabaseReference mercadopublico;
    private ChildEventListener valueMercadoListener;
    private Mercado mercado;
    private List<Mercado> listamercado = new ArrayList<>();
    private ImageView botaoPesquisar;
    private SharedPreferences preferences = null;
    private Dialog dialog;
    private CircleImageView icone;
    private String mPhotoUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_mercado);

        IconeUsuario();
        toolbar = findViewById(R.id.toolbarsecundario);
        toolbar.setTitle(R.string.mercado);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = getSharedPreferences("primeiravezcomercio", MODE_PRIVATE);
        if (preferences.getBoolean("primeiravezcomercio", true)) {
            preferences.edit().putBoolean("primeiravezcomercio", false).apply();
            Dialog_Primeiravez();
        }else{

        }

        //Configuraçoes iniciais
        mercado = new Mercado();
        refresh = findViewById(R.id.refreshmercado);
        refresh.setOnRefreshListener(this);
        refresh.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(true);
                RecuperarMercadoPublicos();
            }
        });
        refresh.setColorSchemeResources
                (R.color.colorPrimaryDark, R.color.amareloclaro,
                        R.color.accent);

        mercadopublico = ConfiguracaoFirebase.getFirebaseDatabase().child("mercado");
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        recyclerViewMercadoPublico = findViewById(R.id.recycleviewmercado);

        //recycleview
        recyclerViewMercadoPublico.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMercadoPublico.setHasFixedSize(true);
        adapter = new MercadoAdapter(listamercado, this);
        recyclerViewMercadoPublico.setAdapter(adapter);

        novoMercado = findViewById(R.id.buton_novo_mercado);
        novoMercado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MercadoActivity.this, Cadastrar_Novo_MercadoActivity.class);
                startActivity(it);


            }
        });
       //Aplicar Evento click
        recyclerViewMercadoPublico.addOnItemTouchListener(new RecyclerItemClickListener(this,
                recyclerViewMercadoPublico, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Mercado mercadoselecionado = listamercado.get(position);
                Intent it = new Intent(MercadoActivity.this,Detalhe_Mercado.class);
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

                if(newText!=null && !newText.isEmpty()){
                    PesquisarComercio(newText.toLowerCase());

                }else{


                    recarregarContatos();
                }

                return true;
            }
        });

        TrocarFundos_status_bar();

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

    private void IconeUsuario() {
        //Imagem do icone do usuario
        icone = findViewById(R.id.icone_user_toolbar);
        icone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MercadoActivity.this, MinhaConta.class);
                startActivity(it);
            }
        });
        FirebaseUser UsuarioAtual = UsuarioFirebase.getUsuarioAtual();
        mPhotoUrl=UsuarioAtual.getPhotoUrl().toString();

        Glide.with(MercadoActivity.this)
                .load(mPhotoUrl)
                .into(icone);
    }

    private void Dialog_Primeiravez() {
        LayoutInflater li = getLayoutInflater();


        View view = li.inflate(R.layout.dialog_informar_click_foto, null);



        view.findViewById(R.id.botaoentendi).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {



                //desfaz o dialog_opcao_foto.
                dialog.dismiss();
            }
        });



        //Dialog de tela
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();

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

                Intent it = new Intent(MercadoActivity.this, MainActivity.class);
                startActivity(it);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }else{
                    finish();
                }
                break;

            default:
                break;
        }

        return true;
    }



    public void onStop() {
        super.onStop();
        mercadopublico.removeEventListener(valueMercadoListener);
    }

    public void RecuperarMercadoPublicos() {

        listamercado.clear();
        refresh.setRefreshing(true);
      valueMercadoListener = mercadopublico.addChildEventListener(new ChildEventListener() {
          @Override
          public void onChildAdded(DataSnapshot dataSnapshot, String s) {
              for(DataSnapshot loja:dataSnapshot.getChildren()){
                  for(DataSnapshot estado:loja.getChildren()){
                      for(DataSnapshot mercados:estado.getChildren()){

                          Mercado mercado = mercados.getValue(Mercado.class);
                          listamercado.add(mercado);

                          Collections.reverse(listamercado);
                          adapter.notifyDataSetChanged();
                          refresh.setRefreshing(false);

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


    }

    @Override
    public void onRefresh() {
        refresh.setRefreshing(true);
        RecuperarMercadoPublicos();
    }

    public void PesquisarComercio(String texto) {
    //  String nick_null =  getString(R.string.buscar_usuario, usuarioAtual.getDisplayName());
    List<Mercado> listaMercadoBusca = new ArrayList<>();
        for (Mercado mercados : listamercado) {
        String nome=mercados.getTitulo().toLowerCase();
        if(nome.contains(texto)){
            listaMercadoBusca.add(mercados);

        }else if(listaMercadoBusca.size()==0){
           // Toast.makeText(this, "zero seu merda", Toast.LENGTH_SHORT).show();
        }

    }
    adapter = new MercadoAdapter(listaMercadoBusca, MercadoActivity.this);
        recyclerViewMercadoPublico.setAdapter(adapter);
        adapter.notifyDataSetChanged();
}

    public void recarregarContatos(){
        //textoAviso.setVisibility(View.VISIBLE);

        adapter = new MercadoAdapter(listamercado, MercadoActivity.this);
        recyclerViewMercadoPublico.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
