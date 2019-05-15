package com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Detalhe;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;
import com.wegeekteste.fulanoeciclano.nerdzone.Abrir_Imagem.AbrirImagemComercio;
import com.wegeekteste.fulanoeciclano.nerdzone.Activits.MinhaConta;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Usuario;
import com.wegeekteste.fulanoeciclano.nerdzone.PerfilAmigos.Perfil;
import com.wegeekteste.fulanoeciclano.nerdzone.R;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Resultados.Resultado_digital_masc;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.model_votacao.Categoria_Pessoa_masc;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.model_votacao.Categoria_Tempo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class detalhe_votacao_masc extends AppCompatActivity {

    private CarouselView fotos;
    private TextView titulo, descricao, criador,toolbar_texto;
    private Button botaovotar;
    private String child;
    private LinearLayout botaovoltar;
    private Categoria_Pessoa_masc categoriaselecionado;
    private Dialog dialog;
    private String identificadorUsuario;
    private DatabaseReference database, mDatabasecategoria, database_usuario,Database_tempo;
    private ChildEventListener ChildEventListenercategoria;
    private AlertDialog alerta;
    private CircleImageView perfil;
    private ChildEventListener ChildEventListeneruser;
    private RelativeLayout click;
    private Toolbar toolbar;
    private SharedPreferences preferences = null;
    private int idvoto;
    private ChildEventListener childEventListener_d_masc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_votacao);

        toolbar = findViewById(R.id.toolbar_detalhe_votacao  );
        setSupportActionBar(toolbar);

        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        mDatabasecategoria =  ConfiguracaoFirebase.getDatabase().getReference()
                .child("votacao").child("categorias").child("digital_influence_masc");
        Database_tempo =  ConfiguracaoFirebase.getDatabase().getReference()
                .child("votacao").child("categorias").child("digital_influence_masc_TIME").child(identificadorUsuario);
        database_usuario = ConfiguracaoFirebase.getDatabase().getReference().child("usuarios");
        fotos = findViewById(R.id.carousel_foto_selecionada_categoria);
        perfil = findViewById(R.id.icone_author_selecionado_votacao);
        titulo = findViewById(R.id.detalhe_selecionado_votacao_titulo);
        toolbar_texto = findViewById(R.id.texto_Toolbar_votacao);
        criador = findViewById(R.id.detalhe_selecionado_votacao_criador);
        descricao = findViewById(R.id.detalhe_selecionado_votacao_descricao);
        click = findViewById(R.id.click_layout);
        botaovotar=findViewById(R.id.botaovotar_digital_influence);

        botaovotar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    categoriaselecionado = (Categoria_Pessoa_masc) getIntent().getSerializableExtra("categoria_selecionada");

    if(categoriaselecionado!=null){
        CarregarDados_do_Criador_do_Comercio(categoriaselecionado.getIdauthor());
        toolbar_texto.setText(categoriaselecionado.getNome());
        titulo.setText(categoriaselecionado.getNome());
        descricao.setText(categoriaselecionado.getDescricao());


        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                String urlstring = categoriaselecionado.getFotos().get(position);

                Glide.with(getApplicationContext())
                        .load(urlstring)
                        .into(imageView);
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
                Intent it = new Intent(detalhe_votacao_masc.this, AbrirImagemComercio.class);
                it.putExtra("fotoselecionada", (Serializable) ff);
                it.putExtra("nome", categoriaselecionado.getNome());
                startActivity(it);

            }
        });
    }

        preferences = getSharedPreferences("primeiravezdetalhevotacao", MODE_PRIVATE);
        if (preferences.getBoolean("primeiravezdetalhevotacao", true)) {
            preferences.edit().putBoolean("primeiravezdetalhevotacao", false).apply();
            Dialog_informacao();
        }
        TrocarFundos_status_bar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void Dialog_informacao() {
        LayoutInflater li = getLayoutInflater();


        View view = li.inflate(R.layout.dialog_informar_click_botao_votar, null);


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

 private void Tempo(){
    Database_tempo.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(!dataSnapshot.exists()){
                ValidarVoto();

            }else{
                Toast toast = Toast.makeText(detalhe_votacao_masc.this, "Seu voto nessa categoria já foi registrado,por favor volte amanhã."
                        ,Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                toast.show(); }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
 }
    private void ValidarVoto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater layoutInflater = LayoutInflater.from(detalhe_votacao_masc.this);
        final View view  = layoutInflater.inflate(R.layout.dialog_carregando_gif_comscroop,null);
        ImageView imageViewgif = view.findViewById(R.id.gifimage);

        Glide.with(this)
                .asGif()
                .load(R.drawable.gif_self)
                .into(imageViewgif);
        builder.setView(view);

        final Calendar calendartempo = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",java.util.Locale.getDefault());// MM'/'dd'/'y;
        long millis = new Date().getTime();
        Categoria_Tempo tempo = new Categoria_Tempo();
        String DataDoVoto = simpleDateFormat.format(calendartempo.getTime());
        tempo.setId_categoria(categoriaselecionado.getId());
        tempo.setId_usuario(identificadorUsuario);
        tempo.setTempodata(DataDoVoto);
        tempo.setTempo_milisigundos(millis);
        tempo.SalvarTempo();

        dialog = builder.create();
        dialog.show();
        int qtdVotos=idvoto+1;
        HashMap<String,Object> dados = new HashMap<>();
        dados.put("votos",qtdVotos);
        mDatabasecategoria.child(categoriaselecionado.getId()).updateChildren(dados).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialog.dismiss();
                Toast toast = Toast.makeText(detalhe_votacao_masc.this, "Voto confirmado com sucesso!",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
                Intent it = new Intent(detalhe_votacao_masc.this, Resultado_digital_masc.class);
                startActivity(it);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast toast = Toast.makeText(detalhe_votacao_masc.this, "Ocorreu um erro, tente novamente ou verifique sua conexão"
                        ,Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            }
        });
        //  categoriaselecionado.atualizarQtdVotos();

    }


    private void CarregarDados_do_Criador_do_Comercio(String idusuario) {

        ChildEventListeneruser = database_usuario.orderByChild("id")
                .equalTo(idusuario).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Usuario user = dataSnapshot.getValue(Usuario.class);

                        assert user != null;
                        String foto = user.getFoto();
                        Glide.with(detalhe_votacao_masc.this)
                                .load(foto)
                                .into(perfil);
                        criador.setText(user.getNome());

                        if (!identificadorUsuario.equals(user.getId())) {
                            click.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent it = new Intent(detalhe_votacao_masc.this, Perfil.class);
                                    it.putExtra("id", user.getId());
                                    startActivity(it);
                                }
                            });
                        } else {
                            click.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent it = new Intent(detalhe_votacao_masc.this, MinhaConta.class);
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


    //botao Pesquisar

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_votar,menu);

        //Botao Pesquisa



        return super.onCreateOptionsMenu(menu);
    }

    //Botao Voltar
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                    finish();
                break;
            case R.id.menu_votar:
                Tempo();
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
