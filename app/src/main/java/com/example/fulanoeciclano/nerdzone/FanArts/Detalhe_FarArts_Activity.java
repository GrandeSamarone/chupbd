package com.example.fulanoeciclano.nerdzone.FanArts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fulanoeciclano.nerdzone.Abrir_Imagem.AbrirImagem;
import com.example.fulanoeciclano.nerdzone.Activits.MinhaConta;
import com.example.fulanoeciclano.nerdzone.Adapter.Adapter_FanArts;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Helper.CircleProgressDrawable;
import com.example.fulanoeciclano.nerdzone.Helper.RecyclerItemClickListener;
import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Model.FanArts;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.PerfilAmigos.Perfil;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Detalhe_FarArts_Activity extends AppCompatActivity {


    private String id;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private SimpleDraweeView arteimagem;
    private TextView legenda_arte,Nome_usuario;
    private CircleImageView iconeUsuario;
    private ChildEventListener ChildEventListeneruser,ChildEventListenerDetalhe;
    private String usuarioLogado;
    private Adapter_FanArts adapter_fanArts_detalhe;
    private LinearLayout click;
    private Toolbar toolbar;
    private ArrayList<FanArts> ListaFanarts_detalhe = new ArrayList<>();
    private RecyclerView recyclerViewartedetalhe;
    private DatabaseReference databaseart,database,database_usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe__far_arts_);
        toolbar = findViewById(R.id.toolbarlayout_detahle_arte);
        setSupportActionBar(toolbar);
        //Configuracoes Iniciais
        click = findViewById(R.id.layoutclick);
        collapsingToolbarLayout = findViewById(R.id.collapseLayoutart);
        usuarioLogado = UsuarioFirebase.getIdentificadorUsuario();
        databaseart = ConfiguracaoFirebase.getDatabase().getReference().child("arts");
        database_usuario = ConfiguracaoFirebase.getDatabase().getReference().child("usuarios");
          legenda_arte = findViewById(R.id.legendaart);
          arteimagem = findViewById(R.id.fanArts_detalhe_img);
          Nome_usuario = findViewById(R.id.author_art);
          iconeUsuario = findViewById(R.id.icone_author_art);

        recyclerViewartedetalhe = findViewById(R.id.recycler_detahle_fanarts);
        adapter_fanArts_detalhe  = new Adapter_FanArts(ListaFanarts_detalhe,this);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager
                (2, LinearLayoutManager.VERTICAL);

        recyclerViewartedetalhe.setLayoutManager(staggeredGridLayoutManager);
        recyclerViewartedetalhe.setHasFixedSize(true);
        recyclerViewartedetalhe.setAdapter(adapter_fanArts_detalhe);


        recyclerViewartedetalhe.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                recyclerViewartedetalhe, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FanArts arteselecionada = ListaFanarts_detalhe.get(position);
                Intent it = new Intent(Detalhe_FarArts_Activity.this,Detalhe_FarArts_Activity.class);
                it.putExtra("id",arteselecionada.getId());
                startActivity(it);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
        id = getIntent().getStringExtra("id");
        RecuperarArts(id);

        TrocarFundos_status_bar();
        RecuperarArtsRecycle();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }





    private void RecuperarArts(String idart){
        ListaFanarts_detalhe.clear();
        ChildEventListenerDetalhe = databaseart.orderByChild("id").equalTo(idart).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FanArts fanArts = dataSnapshot.getValue(FanArts.class);
                legenda_arte.setText(fanArts.getLegenda());
                collapsingToolbarLayout.setTitle(fanArts.getLegenda());
                collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparente));
                collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.background));
                CarregarDados_do_Criador_do_Comercio(fanArts.getIdauthor());

                Uri uri = Uri.parse(fanArts.getArtfoto());
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                        .setLocalThumbnailPreviewsEnabled(true)
                        .setProgressiveRenderingEnabled(true)
                        .build();

                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .build();
                arteimagem.setController(controller);

                GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getApplication().getResources());
                GenericDraweeHierarchy hierarchy = builder
                        .setProgressBarImage(new CircleProgressDrawable())
                        //  .setPlaceholderImage(context.getResources().getDrawable(R.drawable.carregando))
                        .build();
                arteimagem.setHierarchy(hierarchy);

                arteimagem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(Detalhe_FarArts_Activity.this, AbrirImagem.class);
                        it.putExtra("id_foto",fanArts.getArtfoto());
                        it.putExtra("id",fanArts.getId());
                        it.putExtra("nome_foto",  fanArts.getLegenda());
                        startActivity(it);
                    }
                });
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



    private void RecuperarArtsRecycle(){
        ListaFanarts_detalhe.clear();
        ChildEventListenerDetalhe = databaseart.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FanArts fanArts = dataSnapshot.getValue(FanArts.class);
                ListaFanarts_detalhe.add(0,fanArts);
                adapter_fanArts_detalhe.notifyDataSetChanged();
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






    private void CarregarDados_do_Criador_do_Comercio(String idusuario) {

        ChildEventListeneruser = database_usuario.orderByChild("id")
                .equalTo(idusuario).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Usuario user = dataSnapshot.getValue(Usuario.class);

                        assert user != null;

                        Nome_usuario.setText(user.getNome());
                        Glide.with(getApplicationContext())
                                .load(user.getFoto())
                                .into(iconeUsuario);
                        if (!usuarioLogado.equals(user.getId())) {
                            click.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent it = new Intent(Detalhe_FarArts_Activity.this, Perfil.class);
                                    it.putExtra("id", user.getId());
                                    startActivity(it);
                                }
                            });
                        } else {
                            click.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent it = new Intent(Detalhe_FarArts_Activity.this, MinhaConta.class);
                                    it.putExtra("id", user.getId());
                                    startActivity(it);
                                }
                            });
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

    public boolean  onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menufiltro:
                //abrirConfiguracoes();
                break;
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar

                    finish();

        }
        return super.onOptionsItemSelected(item);
    }
}
