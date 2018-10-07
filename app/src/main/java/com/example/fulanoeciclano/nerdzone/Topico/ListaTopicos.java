package com.example.fulanoeciclano.nerdzone.Topico;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.bumptech.glide.Glide;
import com.example.fulanoeciclano.nerdzone.Activits.MainActivity;
import com.example.fulanoeciclano.nerdzone.Activits.MinhaConta;
import com.example.fulanoeciclano.nerdzone.Adapter.Adapter_Topico;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Helper.RecyclerItemClickListener;
import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Model.Topico;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.R;
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

public class ListaTopicos extends AppCompatActivity {
    private Toolbar toolbar;
    private CircleImageView icone;
    private DatabaseReference database,database_topico;
    private FirebaseUser usuario;
    private MaterialSearchView SeachView;
    private Adapter_Topico adapter_topico;
    private ChildEventListener ChildEventListenerperfil;
    private FloatingActionButton botaoMaisTopicos;
    private RecyclerView recyclerView_lista_topico;
    private ArrayList<Topico> ListaTopico = new ArrayList<>();
    private ChildEventListener valueEventListenerTopicos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_topicos);

        toolbar = findViewById(R.id.toolbarsecundario);
        toolbar.setTitle("Tópicos");
        setSupportActionBar(toolbar);


       //Configuraçoes Basicas
        recyclerView_lista_topico = findViewById(R.id.recycleview_topico);
        botaoMaisTopicos=findViewById(R.id.buton_novo_topico);
        botaoMaisTopicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ListaTopicos.this,Novo_Topico.class);
                startActivity(it);
            }
        });
        database = ConfiguracaoFirebase.getDatabase().getReference().child("usuarios");
        database_topico = ConfiguracaoFirebase.getDatabase().getReference().child("topico");
        //adapter
        adapter_topico = new Adapter_Topico(ListaTopico,this);

        //Adapter
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView_lista_topico.setLayoutManager(layoutManager);
        recyclerView_lista_topico.setHasFixedSize(true);
        recyclerView_lista_topico.setAdapter(adapter_topico);

//Aplicar Evento click
        recyclerView_lista_topico.addOnItemTouchListener(new RecyclerItemClickListener(this,
                recyclerView_lista_topico, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<Topico> listTopicoAtualizado = adapter_topico.getTopicos();

                if (listTopicoAtualizado.size() > 0) {
                    Topico topicoselecionado = listTopicoAtualizado.get(position);
                    Intent it = new Intent(ListaTopicos.this, Detalhe_topico.class);
                    it.putExtra("topicoselecionado", topicoselecionado);
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

        CarregarDados_do_Usuario();
        TrocarFundos_status_bar();


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
                   // PesquisarEvento(newText.toLowerCase());

                }else{

                    icone.setVisibility(View.VISIBLE);
                  //  recarregarEvento();
                }

                return true;
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        RecuperarTopicos();
    }

    private void RecuperarTopicos(){
        ListaTopico.clear();
        valueEventListenerTopicos = database_topico.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Topico topico = dataSnapshot.getValue(Topico.class);
                    ListaTopico.add(0, topico);

                    adapter_topico.notifyDataSetChanged();



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
        ChildEventListenerperfil=database.orderByChild("tipoconta")
                .equalTo(email).addChildEventListener(new ChildEventListener() {
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
                Intent it = new Intent(ListaTopicos.this, MinhaConta.class);
                startActivity(it);

            }
        });
        Glide.with(ListaTopicos.this)
                .load(url)
                .into(icone);
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

    public boolean  onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menufiltro:
                //abrirConfiguracoes();
                break;
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(this, MainActivity.class)); //O efeito ao ser pressionado do botão (no caso abre a activity)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }else{
                    finish();
                }
        }/*
        case android.R.id.home:
        // NavUtils.navigateUpFromSameTask(this);
        startActivity(new Intent(this, MainActivity.class)); //O efeito ao ser pressionado do botão (no caso abre a activity)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }else{
            finish();
        }

        break;
        */

        return super.onOptionsItemSelected(item);
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
