package com.wegeekteste.fulanoeciclano.nerdzone.Votacao;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.wegeekteste.fulanoeciclano.nerdzone.Activits.MainActivity;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.R;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Listar.canal_youtube.Lista_canal_youtube_Activity;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Listar.cinema.Lista_cinema_Activity;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Listar.cosplay.Lista_cosplay_Femi;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Listar.cosplay.Lista_cosplay_masc;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Listar.digital_influencer.Lista_digital_fem;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Listar.digital_influencer.Lista_digital_masc;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Listar.empreendedor.Lista_empreendedor;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Listar.empreendedora.Lista_empreendedora;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Listar.escritor_roterista.Lista_escritor;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Listar.escritora_roterista.Lista_escritora;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Listar.espaco_geek.Lista_espaco_geek;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Listar.kpop.Lista_kpop_fem;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Listar.kpop.Lista_kpop_masc;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Listar.livro_quadrinho.Lista_livro_quadrinho;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Listar.loja_virtual.Lista_loja_vitural;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Listar.portal_noticia.Lista_portal_noticia;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Listar.reporter.Lista_reporter_fem;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Listar.reporter.Lista_reporter_masc;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Listar.youtuber.Lista_youtuber_fem;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Listar.youtuber.Lista_youtuber_masc;

import de.hdodenhof.circleimageview.CircleImageView;

public class Tela_Inicial_Votacao_Activity extends AppCompatActivity implements View.OnClickListener {

    private CardView digit_masc,digital_fem,cosplay_masc,cosplay_fem,portal_noticia,livro_quadrinho,
    youtuber_masc,youtuber_fem,repoter_masc,reporter_fem,cinema,canal_youtube,kpop_masc,kpop_fem,
    espaco_geek,loja_virtual,escritor_masc,escritora_fem,empreendedor_masc,empreendedora_fem;
 private Toolbar toolbar;
    private CircleImageView icone;
    private FirebaseUser usuario;
    private LinearLayout botaovoltar,informacao;
    private DatabaseReference database_usuario;
    private ChildEventListener ChildEventListenerperfil;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela__inicial__votacao_);


        database_usuario = ConfiguracaoFirebase.getDatabase().getReference().child("usuarios");
        informacao=findViewById(R.id.votacao_informacao);
        informacao.setOnClickListener(this);
        digit_masc = findViewById(R.id.digital_influencer_masc);
        digit_masc.setOnClickListener(this);
        digital_fem = findViewById(R.id.digital_influencer_fem);
        digital_fem.setOnClickListener(this);
        cosplay_masc=findViewById(R.id.cosplay_masc);
        cosplay_masc.setOnClickListener(this);
        cosplay_fem = findViewById(R.id.cosplay_fem);
        cosplay_fem.setOnClickListener(this);
        portal_noticia=findViewById(R.id.portal_noticia);
        portal_noticia.setOnClickListener(this);
        livro_quadrinho= findViewById(R.id.livro_quadrinho);
        livro_quadrinho.setOnClickListener(this);
        youtuber_masc= findViewById(R.id.youtuber_masc);
        youtuber_masc.setOnClickListener(this);
        youtuber_fem = findViewById(R.id.youtuber_fem);
        youtuber_fem.setOnClickListener(this);
        repoter_masc = findViewById(R.id.reporter_masc);
        repoter_masc.setOnClickListener(this);
        reporter_fem = findViewById(R.id.reporter_fem);
        reporter_fem.setOnClickListener(this);
        cinema = findViewById(R.id.cinema);
        cinema.setOnClickListener(this);
        canal_youtube = findViewById(R.id.canal_do_youtube);
        canal_youtube.setOnClickListener(this);
        kpop_masc = findViewById(R.id.kpop_masc);
        kpop_masc.setOnClickListener(this);
        kpop_fem = findViewById(R.id.kpop_fem);
        kpop_fem.setOnClickListener(this);
        espaco_geek = findViewById(R.id.espaco_geek);
        espaco_geek.setOnClickListener(this);
        loja_virtual = findViewById(R.id.loja_virtual);
        loja_virtual.setOnClickListener(this);
        escritor_masc = findViewById(R.id.escritor_masc);
        escritor_masc.setOnClickListener(this);
        escritora_fem = findViewById(R.id.escritora_fem);
        escritora_fem.setOnClickListener(this);
        empreendedor_masc = findViewById(R.id.empreendedor_masc);
        empreendedor_masc.setOnClickListener(this);
        empreendedora_fem = findViewById(R.id.empreendedora_fem);
        empreendedora_fem.setOnClickListener(this);
        botaovoltar = findViewById(R.id.votacao_button_back);
        botaovoltar.setOnClickListener(this);


        TrocarFundos_status_bar();
        Dialog_Primeiravez();
    }

    private void Dialog_Primeiravez() {
        LayoutInflater li = getLayoutInflater();
        View view = li.inflate(R.layout.dialog_informacao_modo_de_voto, null);
        view.findViewById(R.id.botaoentendi).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //desfaz o dialog_opcao_foto.
                dialog.dismiss();
            }
        });
        //Dialog de tela
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();

    }


    //Botao Voltar
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                Intent it =new Intent(Tela_Inicial_Votacao_Activity.this, MainActivity.class);
                startActivity(it);
                finish();



                break;

            default:
                break;
        }

        return true;
    }
    //botaovoltade baixo
    @Override
    public void onBackPressed()
    {
        Intent it =new Intent(Tela_Inicial_Votacao_Activity.this, MainActivity.class);
        startActivity(it);
        finish();
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case  R.id.digital_influencer_masc:
                Intent it_digit_masc = new Intent( Tela_Inicial_Votacao_Activity.this, Lista_digital_masc.class);
                startActivity(it_digit_masc);
                break;

            case  R.id.digital_influencer_fem:
                Intent it_dig_fem = new Intent(Tela_Inicial_Votacao_Activity.this, Lista_digital_fem.class);
                startActivity(it_dig_fem);
                break;

            case  R.id.cosplay_masc:
                Intent it_cosp_masc= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_cosplay_masc.class);
                startActivity(it_cosp_masc);
                break;

            case  R.id.cosplay_fem:
                Intent it_cosp_fem= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_cosplay_Femi.class);
                startActivity(it_cosp_fem);
                break;

            case  R.id.portal_noticia:
                Intent it_portal_noti= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_portal_noticia.class);
                startActivity(it_portal_noti);
                break;

            case  R.id.livro_quadrinho:
                Intent it_livro_quad= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_livro_quadrinho.class);
                startActivity(it_livro_quad);
                break;

            case  R.id.youtuber_masc:
                Intent it_youtuber_masc= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_youtuber_masc.class);
                startActivity(it_youtuber_masc);
                break;

            case  R.id.youtuber_fem:
                Intent it_youtuber_fem= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_youtuber_fem.class);
                startActivity(it_youtuber_fem);
                break;

            case  R.id.reporter_masc:
                Intent it_reporter_masc= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_reporter_masc.class);
                startActivity(it_reporter_masc);
                break;

            case  R.id.reporter_fem:
                Intent it_reporter_fem= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_reporter_fem.class);
                startActivity(it_reporter_fem);
                break;

            case  R.id.cinema:
                Intent it_cinema= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_cinema_Activity.class);
                startActivity(it_cinema);
                break;

            case  R.id.canal_do_youtube:
                Intent it_canal_youtube= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_canal_youtube_Activity.class);
                startActivity(it_canal_youtube);
                break;

            case  R.id.kpop_masc:
                Intent it_kpop_masc= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_kpop_masc.class);
                startActivity(it_kpop_masc);
                break;

            case  R.id.kpop_fem:
                Intent it_kpop_fem= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_kpop_fem.class);
                startActivity(it_kpop_fem);
                break;

            case  R.id.espaco_geek:
                Intent it_epaco_geek= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_espaco_geek.class);
                startActivity(it_epaco_geek);
                break;

            case  R.id.loja_virtual:
                Intent loja_virtual= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_loja_vitural.class);
                startActivity(loja_virtual);
                break;

            case  R.id.escritor_masc:
                Intent escritor_masc= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_escritor.class);
                startActivity(escritor_masc);
                break;

            case  R.id.escritora_fem:
                Intent escritora_fem= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_escritora.class);
                startActivity(escritora_fem);
                break;

            case  R.id.empreendedor_masc:
                Intent empreendedor_masc= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_empreendedor.class);
                startActivity(empreendedor_masc);
                break;

            case  R.id.empreendedora_fem:
                Intent empreendedora_fem= new Intent(Tela_Inicial_Votacao_Activity.this, Lista_empreendedora.class);
                startActivity(empreendedora_fem);
                break;
            case R.id.votacao_button_back:
                Intent voltar= new Intent(Tela_Inicial_Votacao_Activity.this, MainActivity.class);
                startActivity(voltar);
                finish();
                break;
            case  R.id.votacao_informacao:
                Dialog_duvida();
         }
    }



    private void Dialog_duvida() {
        LayoutInflater li = getLayoutInflater();
        View view = li.inflate(R.layout.dialog_informacao_votacao, null);
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

/*
    private void CarregarDados_do_Usuario(){
        usuario = UsuarioFirebase.getUsuarioAtual();
        String email = usuario.getEmail();
        ChildEventListenerperfil=database_usuario.orderByChild("tipoconta").equalTo(email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Usuario perfil = dataSnapshot.getValue(Usuario.class );
                assert perfil != null;


                String iconeurl = perfil.getFoto();
                icone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(Tela_Inicial_Votacao_Activity.this, MinhaConta.class);
                        startActivity(it);

                    }
                });
                Glide.with(Tela_Inicial_Votacao_Activity.this)
                        .load(iconeurl)
                        .into(icone);

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
*/

    private void TrocarFundos_status_bar(){
        //mudando a cor do statusbar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            MainActivity.setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
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
            MainActivity.setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
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
