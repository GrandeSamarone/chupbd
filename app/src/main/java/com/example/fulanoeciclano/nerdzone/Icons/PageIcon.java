package com.example.fulanoeciclano.nerdzone.Icons;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.example.fulanoeciclano.nerdzone.Activits.Cadastrar_icon_nome_Activity;
import com.example.fulanoeciclano.nerdzone.Activits.MinhaConta;
import com.example.fulanoeciclano.nerdzone.Adapter.IconeAdapter;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Helper.RecyclerItemClickListener;
import com.example.fulanoeciclano.nerdzone.Model.Icones;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.fulanoeciclano.nerdzone.Activits.MainActivity.setWindowFlag;

public class PageIcon extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerViewaleatorio,recyclerViewdesenho,recyclerViewfilmes
            ,recyclerViewheroi,recyclerViewpretoebranco;
    private SwipeRefreshLayout swipeicone;
    private DatabaseReference Icones_Conf,Icones_Desenho,Icones_Filmes,Icones_Heroi,Icones_PretoeBranco;
    private ArrayList<Icones> Listicones = new ArrayList<>();
    private ArrayList<Icones> ListDesenho = new ArrayList<>();
    private ArrayList<Icones> ListFilmes = new ArrayList<>();
    private ArrayList<Icones> ListHeroi = new ArrayList<>();
    private ArrayList<Icones> ListPretoebranco = new ArrayList<>();

    private IconeAdapter adapterIcone,adapterDesenho,adapterFilme,adapterHeroi,adapterPretoebranco;
    private ValueEventListener valueEventListenerIcone,valueEventListenerIconeDesenho
            ,valueEventListenerIconeFilme,valueEventListenerHeroi,valueEventListenerPretoeBRanco;
    public static final int SELECAO_ICONE = 34;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_page_icon);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarsecundario);
        toolbar.setTitle("Escolha um Icone");
        setSupportActionBar(toolbar);


        recyclerViewaleatorio =  findViewById(R.id.mRecylcerID);
        recyclerViewdesenho = findViewById(R.id.mRecylcerdesenho);
        recyclerViewfilmes = findViewById(R.id.mRecylcerfilmes);
        recyclerViewheroi = findViewById(R.id.mRecylcerheroi);
        recyclerViewpretoebranco = findViewById(R.id.mRecylcerpretoebranco);

        //ATUALIZA
        swipeicone = findViewById(R.id.swip_icon);
        swipeicone.setRefreshing(true);
        swipeicone.setOnRefreshListener(this);
        swipeicone.post(new Runnable() {
            @Override
            public void run() {
                RecuperarIcones();
                RecuperarDesenho();
                RecuperarFilmes();
                RecuperarIconesHeroi();
                RecuperarIconesPretoebranco();
            }
        });
        Icones_Conf= ConfiguracaoFirebase.getFirebaseDatabase().child("icones").child("iconesaleatorio");
        Icones_Desenho = ConfiguracaoFirebase.getFirebaseDatabase().child("icones").child("iconesdesenho");
        Icones_Filmes =  ConfiguracaoFirebase.getFirebaseDatabase().child("icones").child("iconesfilmes");
        Icones_Heroi = ConfiguracaoFirebase.getFirebaseDatabase().child("icones").child("iconesheroi");
        Icones_PretoeBranco = ConfiguracaoFirebase.getFirebaseDatabase().child("icones").child("iconespretoebranco");

        //Configurar Adapter
        adapterIcone = new IconeAdapter(PageIcon.this,Listicones);
        adapterDesenho = new IconeAdapter(this,ListDesenho);
        adapterFilme = new IconeAdapter(this,ListFilmes);
        adapterHeroi = new IconeAdapter(this,ListHeroi);
        adapterPretoebranco = new IconeAdapter(this,ListPretoebranco);


        //Configuracao RecycleView
        recyclerViewaleatorio.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerViewaleatorio.setAdapter(adapterIcone);
        //Configuracao RecycleView
        recyclerViewdesenho.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerViewdesenho.setAdapter(adapterDesenho);

        //Configuracao RecycleView
        recyclerViewfilmes.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerViewfilmes.setAdapter(adapterFilme);

        //Configuracao RecycleView
        recyclerViewheroi.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerViewheroi.setAdapter(adapterHeroi);

        //Configuracao RecycleView
        recyclerViewpretoebranco.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerViewpretoebranco.setAdapter(adapterPretoebranco);


        recyclerViewaleatorio.addOnItemTouchListener(new RecyclerItemClickListener(
                this, recyclerViewaleatorio, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position >= 0) {
                    if (getIntent().hasExtra("minhaconta")) {
                        Intent intent = new Intent(PageIcon.this, MinhaConta.class);
                        intent.putExtra("caminho_foto", Listicones.get(position).getUrl());
                        intent.putExtra("selecaoicone", SELECAO_ICONE);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(PageIcon.this, Cadastrar_icon_nome_Activity.class);
                        intent.putExtra("caminho_foto", Listicones.get(position).getUrl());
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

        recyclerViewheroi.addOnItemTouchListener(new RecyclerItemClickListener(
                this, recyclerViewheroi, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position >= 0) {
                    if (getIntent().hasExtra("minhaconta")) {
                        Intent intent = new Intent(PageIcon.this, MinhaConta.class);
                        intent.putExtra("caminho_foto", ListHeroi.get(position).getUrl());
                        intent.putExtra("selecaoicone", SELECAO_ICONE);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(PageIcon.this, Cadastrar_icon_nome_Activity.class);
                        intent.putExtra("caminho_foto", ListHeroi.get(position).getUrl());
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
        recyclerViewpretoebranco.addOnItemTouchListener(new RecyclerItemClickListener(
                this, recyclerViewpretoebranco, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position >= 0) {
                    if (getIntent().hasExtra("minhaconta")) {
                        Intent intent = new Intent(PageIcon.this, MinhaConta.class);
                        intent.putExtra("caminho_foto", ListPretoebranco.get(position).getUrl());
                        intent.putExtra("selecaoicone", SELECAO_ICONE);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(PageIcon.this, Cadastrar_icon_nome_Activity.class);
                        intent.putExtra("caminho_foto", ListPretoebranco.get(position).getUrl());
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
        recyclerViewfilmes.addOnItemTouchListener(new RecyclerItemClickListener(
                this, recyclerViewfilmes, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position >= 0) {
                    if (getIntent().hasExtra("minhaconta")) {
                        Intent intent = new Intent(PageIcon.this, MinhaConta.class);
                        intent.putExtra("caminho_foto", ListFilmes.get(position).getUrl());
                        intent.putExtra("selecaoicone", SELECAO_ICONE);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(PageIcon.this, Cadastrar_icon_nome_Activity.class);
                        intent.putExtra("caminho_foto", ListFilmes.get(position).getUrl());
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
        recyclerViewdesenho.addOnItemTouchListener(new RecyclerItemClickListener(
                this, recyclerViewdesenho, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position >= 0) {
                    if (getIntent().hasExtra("minhaconta")) {
                        Intent intent = new Intent(PageIcon.this, MinhaConta.class);
                        intent.putExtra("caminho_foto", ListDesenho.get(position).getUrl());
                        intent.putExtra("selecaoicone", SELECAO_ICONE);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(PageIcon.this, Cadastrar_icon_nome_Activity.class);
                        intent.putExtra("caminho_foto", ListDesenho.get(position).getUrl());
                        startActivity(intent);
                        finish();
                    }
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
    //Botao Voltar
    public boolean onOptionsItemSelected(MenuItem item) {
        //Botão adicional na ToolBar voltar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                //startActivity(new Intent(this, Cadastrar_icon_nome_Activity.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)

                    finish();

                break;
            default:break;
        }
        return true;
    }

    //atualiza
    @Override
    public void onRefresh() {
        RecuperarIcones();
        RecuperarDesenho();
        RecuperarFilmes();
        RecuperarIconesHeroi();
        RecuperarIconesPretoebranco();
    }

    @Override
    protected void onStart() {
        super.onStart();
        TrocarFundos_status_bar();
        RecuperarIcones();
        RecuperarDesenho();
        RecuperarFilmes();
        RecuperarIconesHeroi();
        RecuperarIconesPretoebranco();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Icones_Conf.removeEventListener(valueEventListenerIcone);
        Icones_Desenho.removeEventListener(valueEventListenerIconeDesenho);
        Icones_Filmes.removeEventListener(valueEventListenerIconeFilme);
        Icones_Heroi.removeEventListener(valueEventListenerHeroi);
        Icones_PretoeBranco.removeEventListener(valueEventListenerPretoeBRanco);
    }

    public void RecuperarIcones(){
        Listicones.clear();
        swipeicone.setRefreshing(true);
        valueEventListenerIcone = Icones_Conf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dados:  dataSnapshot.getChildren()){
                    Icones icone = dados.getValue(Icones.class);
                    Listicones.add(icone);
                }
                adapterIcone.notifyDataSetChanged();
                swipeicone.setRefreshing(false);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void RecuperarFilmes(){
        ListFilmes.clear();
        swipeicone.setRefreshing(true);
        valueEventListenerIconeFilme = Icones_Filmes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dados:  dataSnapshot.getChildren()){
                    Icones icone = dados.getValue(Icones.class);
                    ListFilmes.add(icone);
                }
                adapterFilme.notifyDataSetChanged();
                swipeicone.setRefreshing(false);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void RecuperarDesenho(){
        ListDesenho.clear();
        swipeicone.setRefreshing(true);
        valueEventListenerIconeDesenho = Icones_Desenho.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dados:  dataSnapshot.getChildren()){
                    Icones icone = dados.getValue(Icones.class);
                    ListDesenho.add(icone);
                }
                adapterDesenho.notifyDataSetChanged();
                swipeicone.setRefreshing(false);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void RecuperarIconesHeroi(){
        ListHeroi.clear();
        swipeicone.setRefreshing(true);
        valueEventListenerHeroi = Icones_Heroi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dados:  dataSnapshot.getChildren()){
                    Icones icone = dados.getValue(Icones.class);
                    ListHeroi.add(icone);
                }
                adapterHeroi.notifyDataSetChanged();
                swipeicone.setRefreshing(false);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void RecuperarIconesPretoebranco(){
        ListPretoebranco.clear();
        swipeicone.setRefreshing(true);
        valueEventListenerPretoeBRanco = Icones_PretoeBranco.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dados:  dataSnapshot.getChildren()){
                    Icones icone = dados.getValue(Icones.class);
                    ListPretoebranco.add(icone);
                }
                adapterPretoebranco.notifyDataSetChanged();
                swipeicone.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(requestCode==9 && resultCode == RESULT_OK) {
            Uri photoData = data.getData();
            if (photoData!=null) {
                Intent it = new Intent(PageIcon.this, Cadastrar_icon_nome_Activity.class);
                it.putExtra("caminho_foto", photoData);
                startActivity(it);


            }

            Bitmap photoBitmap = null;
            try {
                photoBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoData);
                // imageNick.setImageBitmap(photoBitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    //Nao muito uteis
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

}
