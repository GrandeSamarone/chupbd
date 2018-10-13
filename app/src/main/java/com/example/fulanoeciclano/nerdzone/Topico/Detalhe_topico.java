package com.example.fulanoeciclano.nerdzone.Topico;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fulanoeciclano.nerdzone.Activits.MinhaConta;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Model.Topico;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.PerfilAmigos.Perfil;
import com.example.fulanoeciclano.nerdzone.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class Detalhe_topico extends AppCompatActivity {

    private Toolbar toolbar;
    private CircleImageView icone;
    private TextView nome_autor,titulo,mensagem,titulotoolbar;
    private DatabaseReference database,database_topico;
    private FirebaseUser usuario;
    private Topico topicoselecionado;
    private ChildEventListener ChildEventListenerdetalhe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhetopico);
        toolbar = findViewById(R.id.toolbartopico);
        toolbar.setTitle("Detalhe do Tópico");
        setSupportActionBar(toolbar);

        //Configuracoes Iniciais

        icone = findViewById(R.id.icon_topico_detalhe_author);
        nome_autor = findViewById(R.id.nome_topico__detalhe_autor);
        titulo = findViewById(R.id.detalhe_topico_titulo);
        mensagem = findViewById(R.id.detalhe_topico_mensagem);

        database = ConfiguracaoFirebase.getDatabase().getReference().child("usuarios");
        topicoselecionado = (Topico)  getIntent().getSerializableExtra("topicoselecionado");
        if(topicoselecionado!=null){

            titulo.setText(topicoselecionado.getTitulo());
            mensagem.setText(topicoselecionado.getMensagem());
            RecuperarIcone_e_nome_author(topicoselecionado.getIdauthor());
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void RecuperarIcone_e_nome_author(String id ) {
        database.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario  user = dataSnapshot.getValue(Usuario.class);
               nome_autor.setText(user.getNome());
           nome_autor.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent it = new Intent(Detalhe_topico.this, Perfil.class);
                   it.putExtra("id",user.getId());
                   startActivity(it);
               }
           });
                Glide.with(Detalhe_topico.this)
                        .load(user.getFoto())
                        .into(icone);
                icone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(Detalhe_topico.this, Perfil.class);
                        it.putExtra("id",user.getId());
                        startActivity(it);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CarregarDados_do_Usuario();
        TrocarFundos_status_bar();
    }


    public boolean  onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar

                    finish();

        }

        return super.onOptionsItemSelected(item);
    }
    private void CarregarDados_do_Usuario(){
        usuario = UsuarioFirebase.getUsuarioAtual();
        String email = usuario.getEmail();
        ChildEventListenerdetalhe=database.orderByChild("tipoconta")
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
                Intent it = new Intent(Detalhe_topico.this, MinhaConta.class);
                startActivity(it);

            }
        });
        Glide.with(Detalhe_topico.this)
                .load(url)
                .into(icone);
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
