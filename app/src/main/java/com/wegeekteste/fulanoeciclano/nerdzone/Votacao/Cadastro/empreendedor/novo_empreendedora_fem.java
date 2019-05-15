package com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Cadastro.empreendedor;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.wegeekteste.fulanoeciclano.nerdzone.Activits.MainActivity;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.Permissoes;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Usuario;
import com.wegeekteste.fulanoeciclano.nerdzone.R;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Listar.digital_influencer.Lista_digital_masc;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.Listar.empreendedora.Lista_empreendedora;
import com.wegeekteste.fulanoeciclano.nerdzone.Votacao.model_votacao.Categoria_empreendedora;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class novo_empreendedora_fem extends AppCompatActivity implements View.OnClickListener {

    private static final String padrao = "Obrigatório";
    private AppCompatEditText campotitulo, campodesc;
    private EditText campocontato;
    private CircleImageView imagem1,imagem2,imagem3,imagem4,imagem5,imagem6;
    private StorageReference storageReference;
    private Button botaosalvar;
    private DatabaseReference database,databaseconta;
    private String identificadorUsuario;
    private FirebaseAuth autenticacao;
    private Categoria_empreendedora digital;
    private Usuario usuarioLogado;
    private FirebaseUser UsuarioAtual;
    private Toolbar toolbar;
    private AlertDialog dialog;
    private CircleImageView icone;
    private String id_do_usuario;

    private String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private List<String> listaFotosRecuperadas = new ArrayList<>();
    private List<String> listaURLFotos = new ArrayList<>();
    private String[] pegandoUrl;
    private FirebaseUser usuario;
    private ChildEventListener ChildEventListenerperfil;
    private ChildEventListener ChildEventListenerSeguidores;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_empreendedora_fem);


        toolbar = findViewById(R.id.toolbarsecundario_sem_foto);
        toolbar.setTitle("novo Empreendedora");
        setSupportActionBar(toolbar);

        //Configuração Basica
        campotitulo = findViewById(R.id.nome_digital_empreendedora_fem);
        campodesc = findViewById(R.id.desc_digital_empreendedora_fem);
        campocontato = findViewById(R.id.desc_contato_empreendedora_fem);
        imagem1 = findViewById(R.id.imageLojaCadastro_empreendedora_fem_m1);
        imagem1.setOnClickListener(this);
        imagem2 =findViewById(R.id.imageLojaCadastro_empreendedora_fem_m2);
        imagem2.setOnClickListener(this);
        imagem3 = findViewById(R.id.imageLojaCadastro_empreendedora_fem_m3);
        imagem3.setOnClickListener(this);
        imagem4 = findViewById(R.id.imageLojaCadastro_empreendedora_fem_m4);
        imagem4.setOnClickListener(this);
        imagem5 = findViewById(R.id.imageLojaCadastro_empreendedora_fem_m5);
        imagem5.setOnClickListener(this);
        imagem6 = findViewById(R.id.imageLojaCadastro_empreendedora_fem_m6);
        imagem6.setOnClickListener(this);

        digital = new Categoria_empreendedora();
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        database = ConfiguracaoFirebase.getDatabase().getReference()
                .child("votacao").child("categorias").child("empreendedora");
        databaseconta = ConfiguracaoFirebase.getDatabase().getReference().child("usuarios");
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
        botaosalvar = findViewById(R.id.botaosalvar_empreendedora_fem);
        botaosalvar.setOnClickListener(this);


        /// /validar permissoes
        Permissoes.validarPermissoes(permissoes, this, 1);





        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        TrocarFundos_status_bar();


    }

    public void  Salvar_Digital_Influence(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater layoutInflater = LayoutInflater.from(novo_empreendedora_fem.this);
        final View view  = layoutInflater.inflate(R.layout.dialog_carregando_gif_analisando,null);
        ImageView imageViewgif = view.findViewById(R.id.gifimage);

        Glide.with(this)
                .asGif()
                .load(R.drawable.gif_analizando)
                .into(imageViewgif);
        builder.setView(view);

        dialog = builder.create();
        dialog.show();
        /*salva no storage*/
        for(int i = 0; i< listaFotosRecuperadas.size();i++){
            String urlimagem = listaFotosRecuperadas.get(i);
            int tamanholista = listaFotosRecuperadas.size();

            SalvarFotoStorage(urlimagem,tamanholista,i);
        }
    }

    private void SalvarFotoStorage(String urlimagem, final int totalfotos, int contador) {
        //criar nó no storage
        StorageReference imagemMercado = storageReference
                .child("imagens")
                .child("empreendedora_fem")
                .child(digital.getId())
                .child("imagem"+contador);

        //fazer upload arquivo
        UploadTask uploadTask = imagemMercado.putFile(Uri.parse(urlimagem));
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String urlConvertida =uri.toString();

                        listaURLFotos.add(urlConvertida);

                        if(totalfotos==listaURLFotos.size()){
                            digital.setFotos(listaURLFotos);
                            digital.Salvar();
                            dialog.dismiss();
                            Toast toast = Toast.makeText(novo_empreendedora_fem.this, "Cadastrado com sucesso!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                            Intent it = new Intent(novo_empreendedora_fem.this,Lista_empreendedora.class);
                            startActivity(it);
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        dialog.dismiss();
                        Toast.makeText(novo_empreendedora_fem.this, "Erro ao cadastrar", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
    private Categoria_empreendedora configurar_categoria_influence(){
        String titulo = campotitulo.getText().toString();
        String contato = campocontato.getText().toString();
        String descricao = campodesc.getText().toString();
        final Calendar calendartempo = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd'-'MM'-'y",java.util.Locale.getDefault());// MM'/'dd'/'y;
        String data = simpleDateFormat.format(calendartempo.getTime());


        digital.setNome(titulo);
        digital.setIdauthor(identificadorUsuario);
        digital.setContato(contato);
        digital.setDescricao(descricao);
        digital.setData(data);

        return digital;
    }

    public void validarDados() {
        digital =configurar_categoria_influence();
        if (listaFotosRecuperadas.size() != 0) {
            if (TextUtils.isEmpty(digital.getNome())) {
                campotitulo.setError(padrao);
                return;
            }
            if (TextUtils.isEmpty(digital.getDescricao())) {
                campodesc.setError(padrao);
                return;
            }

            if (TextUtils.isEmpty(digital.getContato())) {
                campocontato.setError(padrao);
                return;
            }
            Salvar_Digital_Influence();
        }



    }



    //click na imagem para cadastra comercio
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case  R.id.imageLojaCadastro_empreendedora_fem_m1:
                EscolherImagem(1);
                break;
            case  R.id.imageLojaCadastro_empreendedora_fem_m2:
                EscolherImagem(2);
                break;
            case  R.id.imageLojaCadastro_empreendedora_fem_m3:
                EscolherImagem(3);
                break;
            case  R.id.imageLojaCadastro_empreendedora_fem_m4:
                EscolherImagem(4);
                break;
            case  R.id.imageLojaCadastro_empreendedora_fem_m5:
                EscolherImagem(5);
                break;
            case  R.id.imageLojaCadastro_empreendedora_fem_m6:
                EscolherImagem(6);
                break;
            case R.id.botaosalvar_empreendedora_fem:
                validarDados();
                break;


        }
    }

    public  void EscolherImagem(int requestCode){
        Intent intent = CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .getIntent(this);
        startActivityForResult(intent,requestCode );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){

            CropImage.ActivityResult resultCAMERA = CropImage.getActivityResult(data);
            Uri imagemSelecionada = resultCAMERA.getUri();
            String caminhoImagem = imagemSelecionada.toString();
            // Configurar imagem  no ImageView

            if(requestCode == 1){
                imagem1.setImageURI(imagemSelecionada);
                imagem2.setVisibility(View.VISIBLE);
            } else if(requestCode == 2){
                imagem2.setImageURI(imagemSelecionada);
                imagem3.setVisibility(View.VISIBLE);
            }else if(requestCode == 3){
                imagem3.setImageURI(imagemSelecionada);
                imagem4.setVisibility(View.VISIBLE);
            }else if(requestCode == 4){
                imagem4.setImageURI(imagemSelecionada);
                imagem5.setVisibility(View.VISIBLE);
            }else if(requestCode == 5){
                imagem5.setImageURI(imagemSelecionada);
                imagem6.setVisibility(View.VISIBLE);
            }else if(requestCode==6){
                imagem6.setImageURI(imagemSelecionada);
            }
            listaFotosRecuperadas.add(caminhoImagem);
        }
    }



    //Botao Voltar
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                Intent it = new Intent(novo_empreendedora_fem.this, Lista_digital_masc.class);
                startActivity(it);

                break;

            default:
                break;
        }

        return true;
    }



    //Permissao
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int permissaoResultado:grantResults){
            if(permissaoResultado== PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Permissao Negada");
        builder.setMessage("Para ultilizar o app é preciso aceita as permissoes");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

