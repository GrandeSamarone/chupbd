package com.example.fulanoeciclano.nerdzone.Votacao.Detalhe;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fulanoeciclano.nerdzone.Abrir_Imagem.AbrirImagemComercio;
import com.example.fulanoeciclano.nerdzone.Activits.MinhaConta;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.PerfilAmigos.Perfil;
import com.example.fulanoeciclano.nerdzone.R;
import com.example.fulanoeciclano.nerdzone.Votacao.model_votacao.Categoria_pessoa_fem;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.io.Serializable;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class detalhe_votacao_fem extends AppCompatActivity {

    private CarouselView fotos;
    private ProgressBar progresso;
    private TextView titulo, descricao, criador,toolbar_texto;
    private Button botaovotar;
    private String child;
    private LinearLayout botaovoltar;
    private Categoria_pessoa_fem categoriaselecionado;
    private Dialog dialog;
    private String identificadorUsuario;
    private DatabaseReference database, mDatabasecategoria, database_usuario;
    private ChildEventListener ChildEventListenercategoria;
    private AlertDialog alerta;
    private CircleImageView perfil;
    private ChildEventListener ChildEventListeneruser;
    private RelativeLayout click;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_votacao_fem);

        toolbar = findViewById(R.id.toolbar_detalhe_votacao_fem);
        setSupportActionBar(toolbar);

        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        mDatabasecategoria = ConfiguracaoFirebase.getDatabase().getReference()
                .child("votacao").child("categorias").child("digital_influence_fem");
        database_usuario = ConfiguracaoFirebase.getDatabase().getReference().child("usuarios");
        fotos = findViewById(R.id.carousel_foto_selecionada_categoria_fem);
        perfil = findViewById(R.id.icone_author_selecionado_votacao_fem);
        titulo = findViewById(R.id.detalhe_selecionado_votacao_titulo_fem);
        toolbar_texto = findViewById(R.id.texto_Toolbar_votacao_fem);
        criador = findViewById(R.id.detalhe_selecionado_votacao_criador_fem);
        descricao = findViewById(R.id.detalhe_selecionado_votacao_descricao_fem);
        click = findViewById(R.id.click_layout);
        progresso = findViewById(R.id.progresso_detalhe_fem);
        progresso.setVisibility(View.VISIBLE);

        categoriaselecionado = (Categoria_pessoa_fem) getIntent().getSerializableExtra("categoria_selecionada");

        if (categoriaselecionado != null) {
            toolbar_texto.setText(categoriaselecionado.getNome());
            titulo.setText(categoriaselecionado.getNome());
            descricao.setText(categoriaselecionado.getDescricao());
            CarregarDados_do_Criador_do_Comercio(categoriaselecionado.getIdauthor());


            ImageListener imageListener = new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    String urlstring = categoriaselecionado.getFotos().get(position);

                    Glide.with(getApplicationContext())
                            .load(urlstring)
                            .into(imageView);
                    progresso.setVisibility(View.GONE);
                }
            };
            fotos.setPageCount(categoriaselecionado.getFotos().size());
            fotos.setImageListener(imageListener);

            fotos.setImageClickListener(new ImageClickListener() {
                @Override
                public void onClick(int position) {
                    /*Bundle bundle = new Bundle();
                    bundle.putString("id", mercadoselecionado.getIdMercado());
                    Intent it = new Intent(Detalhe_Mercado.this, AbrirImagemComercio.class);
                    it.putExtras(bundle);
                    startActivity(it);
                    */


                    List<String> ff = categoriaselecionado.getFotos();
                    Intent it = new Intent(detalhe_votacao_fem.this, AbrirImagemComercio.class);
                    it.putExtra("fotoselecionada", (Serializable) ff);
                    it.putExtra("nome", categoriaselecionado.getNome());
                    startActivity(it);

                }
            });
        }


        TrocarFundos_status_bar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
        private void CarregarDados_do_Criador_do_Comercio(String idusuario) {

            ChildEventListeneruser = database_usuario.orderByChild("id")
                    .equalTo(idusuario).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Usuario user = dataSnapshot.getValue(Usuario.class);

                            assert user != null;
                            String foto = user.getFoto();
                            Glide.with(detalhe_votacao_fem.this)
                                    .load(foto)
                                    .into(perfil);
                            criador.setText(user.getNome());






                            if (!identificadorUsuario.equals(user.getId())) {
                                click.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent it = new Intent(detalhe_votacao_fem.this, Perfil.class);
                                        it.putExtra("id", user.getId());
                                        startActivity(it);
                                    }
                                });
                            } else {
                                click.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent it = new Intent(detalhe_votacao_fem.this, MinhaConta.class);
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
