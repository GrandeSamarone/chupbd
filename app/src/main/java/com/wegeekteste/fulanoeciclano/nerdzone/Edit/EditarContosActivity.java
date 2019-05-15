package com.wegeekteste.fulanoeciclano.nerdzone.Edit;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.wegeekteste.fulanoeciclano.nerdzone.Activits.MainActivity;
import com.wegeekteste.fulanoeciclano.nerdzone.Activits.Minhas_Publicacoes;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Conto;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Usuario;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarContosActivity extends AppCompatActivity {
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private Toolbar toolbar;
    private CircleImageView icone;
    private ImageView img_topico;
    private String identificadorUsuario;
    private Button botaosalvar;
    private DatabaseReference databaseusuario,databaseconto,SeguidoresRef,Meusdatabaseconto;
    private FirebaseUser usuario;
    private EditText titulo_conto,mensagem_conto;
    private Conto conto;
    private Usuario perfil;
    private StorageReference storageReference;
    private Dialog dialog;
    private Uri url;
    private String ids;
    private ChildEventListener ChildEventListenerconto;
    private String urlimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_contos);

        toolbar = findViewById(R.id.toolbarsecundario_sem_foto);
        toolbar.setTitle("Editar Conto");
        setSupportActionBar(toolbar);

        //Configuraçoes Originais
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        databaseusuario = ConfiguracaoFirebase.getDatabase().getReference().child("usuarios");
        databaseconto = ConfiguracaoFirebase.getDatabase().getReference().child("conto");
        Meusdatabaseconto = ConfiguracaoFirebase.getDatabase().getReference().child("meusconto");
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        titulo_conto = findViewById(R.id.titulo_conto_edit);
        mensagem_conto = findViewById(R.id.desc_conto_edit);
        botaosalvar = findViewById(R.id.botaosalvarconto_edit);
        botaosalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidarDados();
            }
        });



        CarregarDadosConto();
        TrocarFundos_status_bar();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void CarregarDadosConto(){
        ids=getIntent().getStringExtra("id_conto");
        Log.i("sdsd",ids);
        ChildEventListenerconto = databaseconto.orderByChild("uid").equalTo(ids).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                conto = dataSnapshot.getValue(Conto.class);
                assert conto != null;

                titulo_conto.setText(conto.getTitulo());
                mensagem_conto.setText(conto.getMensagem());

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

    public void ValidarDados(){
        String usuariologado = UsuarioFirebase.getIdentificadorUsuario();



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater layoutInflater = LayoutInflater.from(EditarContosActivity.this);
        final View view  = layoutInflater.inflate(R.layout.dialog_carregando_gif_analisando,null);
        ImageView imageViewgif = view.findViewById(R.id.gifimage);

        Glide.with(this)
                .asGif()
                .load(R.drawable.gif_analizando)
                .into(imageViewgif);
        builder.setView(view);

        dialog = builder.create();
        dialog.show();
        Conto contos = new Conto();
        contos.setUid(conto.getUid());
        contos.setData(conto.getData());
        contos.setMensagem(mensagem_conto.getText().toString());
        contos.setTitulo(titulo_conto.getText().toString());
        contos.setLikecount(conto.getLikecount());
        contos.setIdauthor(conto.getIdauthor());
        databaseconto.child(contos.getUid()).setValue(contos).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        Meusdatabaseconto.child(usuariologado).child(contos.getUid()).setValue(contos).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialog.dismiss();
                Intent it = new Intent(EditarContosActivity.this, Minhas_Publicacoes.class);
                startActivity(it);
                finish();
                Toast toast = Toast.makeText(EditarContosActivity.this, "Alterado com sucesso!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Intent it = new Intent(EditarContosActivity.this, Minhas_Publicacoes.class);
                startActivity(it);
                finish();
                Toast toast = Toast.makeText(EditarContosActivity.this, "Ocorreu algum problema," +
                        "tente novamente", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
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


    private void TrocarFundos_status_bar() {
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

