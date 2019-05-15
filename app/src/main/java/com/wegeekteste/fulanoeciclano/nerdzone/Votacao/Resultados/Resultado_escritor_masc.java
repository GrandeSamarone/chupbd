package com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Resultados;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.R;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Adapter_resultado.Adapter_resultado_escritor_masc;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Listar.escritor_roterista.Lista_escritor;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.model_votacao.Categoria_escritor_masc;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Resultado_escritor_masc extends AppCompatActivity {


    private Toolbar toolbar;
    private FloatingActionButton novo_influencer;
    private FirebaseUser usuario;
    private Adapter_resultado_escritor_masc adapter;
    private Query mDatabase_digital;
    private ChildEventListener valuedigitalListener;
    private Categoria_escritor_masc digital;
    private ArrayList<Categoria_escritor_masc> lista_digital_influence = new ArrayList<>();
    private Dialog dialog;
    private AlertDialog alerta;
    private CircleImageView icone;
    private LinearLayoutManager mManager;
    private DatabaseReference database_usuario;
    private ChildEventListener ChildEventListenerperfil;
    private TextView errobusca;
    private LinearLayout linear_nada_cadastrado, linearerro, linear;
    private RecyclerView recyclerViewresultado;
    private SharedPreferences preferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_escritor_masc);

        toolbar = findViewById(R.id.toolbarsecundario_sem_foto);
        toolbar.setTitle("Resultado Final");
        setSupportActionBar(toolbar);

        mDatabase_digital = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("votacao").child("categorias").child("escritor_masc").orderByChild("votos");
        //recycleview
        recyclerViewresultado = findViewById(R.id.resultado_escritor_masc);
        //recyclerViewresultado.setHasFixedSize(true);
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        recyclerViewresultado.setLayoutManager(mManager);
        adapter = new Adapter_resultado_escritor_masc(this, lista_digital_influence);

        recyclerViewresultado.setAdapter(adapter);
        //Aplicar Evento click



   /*     recyclerViewresultado.addOnItemTouchListener(new RecyclerItemClickListener(this,
                recyclerViewresultado, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<Categoria_Pessoa_masc> listCategoriaAtualizado = adapter.getcategoria();
                if (listCategoriaAtualizado.size() > 0) {
                    Categoria_Pessoa_masc categoriaselecionada = listCategoriaAtualizado.get(position);
                    Intent it = new Intent(Resultado_digital_masc.this,detalhe_votacao_masc.class);
                    it.putExtra("categoria_selecionada", categoriaselecionada);
                    it.putExtra("link","digital_influence_masc");
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
*/
        RecuperarResultado_digital_masc();


    }

    @Override
    protected void onStart() {
        super.onStart();
        TrocarFundos_status_bar();
        preferences = getSharedPreferences("primeiravezvotacao", MODE_PRIVATE);
        if (preferences.getBoolean("primeiravezvotacao", true)) {
            preferences.edit().putBoolean("primeiravezvotacao", false).apply();
            //  Dialog_informacao();
        } else {

        }
    }

    //botao Pesquisar
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_votacao_finalizar, menu);

        //Botao Pesquisa


        return super.onCreateOptionsMenu(menu);
    }


    //Botao Voltar
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                Intent it = new Intent(Resultado_escritor_masc.this, Lista_escritor.class);
                startActivity(it);
                finish();
                break;
            case R.id.menu_finalizar:
                tela_finalizar();
                break;
            default:
                break;
        }

        return true;
    }


    private void tela_finalizar() {
        android.app.AlertDialog.Builder msgbox = new android.app.AlertDialog.Builder(Resultado_escritor_masc.this);
        //configurando o titulo
        msgbox.setTitle("Deseja finalizar?");
        msgbox.setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int wich) {
                        Intent it = new Intent(Resultado_escritor_masc.this, Lista_escritor.class);
                        startActivity(it);
                        finish();
                    }

                });


        msgbox.setNegativeButton("Não",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int wich) {
                        dialog.dismiss();
                    }
                });
        msgbox.show();
    }

    private void RecuperarResultado_digital_masc() {
        lista_digital_influence.clear();
        valuedigitalListener = mDatabase_digital.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Categoria_escritor_masc cat = dataSnapshot.getValue(Categoria_escritor_masc.class);
                lista_digital_influence.add(cat);
                // Collections.reverse(lista_digital_influence);
                adapter.notifyDataSetChanged();
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

    private void TrocarFundos_status_bar() {
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

    private void Dialog_informacao() {
        LayoutInflater li = getLayoutInflater();


        View view = li.inflate(R.layout.dialog_informar_click_votacao, null);


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
}
