package com.example.fulanoeciclano.nerdzone.Activits;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Icons.PageIcon;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class Cadastrar_icon_nome_Activity extends AppCompatActivity {

    private SimpleDraweeView imageNick;
    private AlertDialog alerta;
    public static final int SELECAO_GALERIA = 12;
    public static final int SELECAO_ICONE = 34;
    private StorageReference storageReference;
    private DatabaseReference database;
    private String identificadorUsuario;
    private FirebaseAuth autenticacao;
    private Usuario usuarioLogado;
    private EditText NomeUsuario;
    private Usuario usuario;
    private String nome;
    private String telefone;
    private  FirebaseUser UsuarioAtual;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_cadastrar_icon_nome_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



            //Configuracao Inicial
            imageNick = findViewById(R.id.circleImageViewFotoPerfil);
            storageReference = ConfiguracaoFirebase.getFirebaseStorage();
            database = ConfiguracaoFirebase.getDatabase().getReference().child("usuarios");
            identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
            usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

            NomeUsuario = findViewById(R.id.editPerfilNome);

            //ao clicar na iimagem abre dialig para escolher entre icone ou galeria
            imageNick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Escolher_Foto_Perfil();
                }
            });

            //Recuperar a imagem Icones da pagina Icones
            RecuperarIcone();

       //Recuperar dados do Usuario da conta
         UsuarioAtual =UsuarioFirebase.getUsuarioAtual();
        Uri url= UsuarioAtual.getPhotoUrl();
         nome = UsuarioAtual.getDisplayName();
         telefone = UsuarioAtual.getPhoneNumber();


        if(url!=null) {

            DraweeController controllerOne = Fresco.newDraweeControllerBuilder()
                    .setUri(url)
                    .setAutoPlayAnimations(true)
                    .build();

            imageNick.setController(controllerOne);
        }else{
            imageNick.setImageResource(R.drawable.padrao);
        }
        if (nome!=null){
            NomeUsuario.setText(UsuarioAtual.getDisplayName());
        } else
        {
            NomeUsuario.setText(telefone);
        }

            //Botao Cadastrar
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btn_cadastrar_icone);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 CadastrarIcone();
                }
            });



    }

    //Recebendo Icone
    private void RecuperarIcone() {

        // final Bundle it = getIntent().getExtras();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        if (getIntent().hasExtra("caminho_foto")) {
            final Uri url = Uri.parse(((getIntent().getStringExtra("caminho_foto"))));

            final StorageReference storageReference = storage.getReferenceFromUrl(String.valueOf(url));

            //Progresso
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Aguarde");
            progressDialog.setMessage("carregando... ");
            progressDialog.show();


            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    DraweeController controllerOne = Fresco.newDraweeControllerBuilder()
                            .setUri(uri)
                            .setAutoPlayAnimations(true)
                            .build();

                    imageNick.setController(controllerOne);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    progressDialog.dismiss();

                }
            });

        }
    }





   //Cadastrando Icone
    private void CadastrarIcone() {
        String nome = NomeUsuario.getText().toString();
        boolean retorno = UsuarioFirebase.atualizarNomeUsuario(nome);


    imageNick.setDrawingCacheEnabled(true);
    imageNick.buildDrawingCache();

    final Bitmap bitmap = imageNick.getDrawingCache();
     if(bitmap!=null&& retorno){
         usuarioLogado.setNome(nome);
         usuarioLogado.atualizar();
         //Recuperar dados da imagem  para o  Firebase
         ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
         byte[] dadosImagem= baos.toByteArray();

         //Salvar no Firebase
         StorageReference MontarImagemReference =
                 storageReference
                         .child("imagens")
                         .child("perfil")
                         .child(identificadorUsuario)
                         .child("perfil.jpg");
         //Progress
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         LayoutInflater layoutInflater = LayoutInflater.from(Cadastrar_icon_nome_Activity.this);
         final View view  = layoutInflater.inflate(R.layout.dialog_carregando_gif_analizando,null);
         final TextView texto;
         texto = view.findViewById(R.id.texto);
         ImageView imageViewgif = view.findViewById(R.id.gifimage);

         Glide.with(this)
                 .asGif()
                 .load(R.drawable.gif_analizando)
                 .into(imageViewgif);
         builder.setView(view);

         dialog = builder.create();
         dialog.show();
         UploadTask uploadTask = MontarImagemReference.putBytes(dadosImagem);
         //caso de errado
         uploadTask.addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 dialog.dismiss();
                 Toast.makeText(Cadastrar_icon_nome_Activity.this, "Erro ao carregar a imagem", Toast.LENGTH_SHORT).show();
             }
             //caso o carregamento no firebase de tudo certo
         }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
             @Override
             public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                 dialog.dismiss();

                 Uri url= taskSnapshot.getDownloadUrl();
                //Inserindo no banco de dados
                 InserirUsuario(url);
                 AtualizarFotoUsuario(url);



             }

         }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
             @Override
             public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
             texto.setText(R.string.analizando_informa_es_aguarde);
             }
         });

     }
    }


private void InserirUsuario(Uri url) {
    autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

    String nome =NomeUsuario.getText().toString();
    String email = UsuarioAtual.getEmail();
    String telefone = UsuarioAtual.getPhoneNumber();

    if (!nome.isEmpty()) {
        usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setFoto(String.valueOf(url));
        if(email!=null){
            usuario.setTipoconta(email);
        }else{
            usuario.setTipoconta(telefone);
        }

        usuario.setTiposuario("usuario");
        //   String  identificadorUsuario = Base64Custom.codificarBase64(usuario.getNome());
        usuario.setId(identificadorUsuario);
        usuario.salvar();
        Intent it = new Intent(Cadastrar_icon_nome_Activity.this,MainActivity.class);
        startActivity(it);
        finish();


    }else{
        Toast.makeText(this, "Coloque o nome", Toast.LENGTH_SHORT).show();
    }
}
    //Retorna a foto do usuario Cadastrada
      private void AtualizarFotoUsuario(Uri url) {
        boolean retorno = UsuarioFirebase.atualizarFotoUsuario(url);
        if(retorno){
            usuarioLogado.setFoto(url.toString());
            usuarioLogado.atualizar();
        }


    }




    //recebe a imagem da galeia
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;
            try {
                switch (requestCode) {

                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;

            }
                if(imagem !=null){
                    imageNick.setImageBitmap(imagem);
                    //Recuperar dados da imagem  para o  Firebase
                    ByteArrayOutputStream baos=new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG,70,baos);
                    byte[] dadosImagem= baos.toByteArray();

                    //Salvar no Firebase
                    StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("perfil")
                            .child(identificadorUsuario)
                            .child("perfil.jpg");
                    //Progress
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("Aguarde");
                    progressDialog.show();
                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Cadastrar_icon_nome_Activity.this, "Erro ao carregar a imagem", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Uri url= taskSnapshot.getDownloadUrl();
                            //Inserindo no banco de dados
                           // InserirUsuario(url);
                            AtualizarFotoUsuario(url);
                            DraweeController controllerOne = Fresco.newDraweeControllerBuilder()
                                    .setUri(url)
                                    .setAutoPlayAnimations(true)
                                    .build();

                            imageNick.setController(controllerOne);
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("carregando " + (int) progress + "%");
                        }
                    });


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    //dialog de opcoes
    private void  Escolher_Foto_Perfil() {
        //LayoutInflater é utilizado para inflar nosso layout em uma view.
        //-pegamos nossa instancia da classe
        LayoutInflater li = getLayoutInflater();

        //inflamos o layout dialog_opcao_foto.xml_foto.xml na view
        View view = li.inflate(R.layout.dialog_opcao_foto, null);
        //definimos para o botão do layout um clickListener
        view.findViewById(R.id.botaogaleria).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(it.resolveActivity(getPackageManager())!=null)  {
                    startActivityForResult(it, SELECAO_GALERIA);
                }
                //desfaz o dialog_opcao_foto.
                alerta.dismiss();
            }
        });
        view.findViewById(R.id.botaoicones).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //exibe um Toast informativo.


                Intent it = new Intent(Cadastrar_icon_nome_Activity.this,PageIcon.class);
                startActivityForResult(it, SELECAO_ICONE);
                //desfaz o dialog_opcao_foto.
                alerta.dismiss();
            }
        });

       //Dialog de tela
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Foto de Perfil");
        builder.setView(view);
        alerta = builder.create();
        alerta.show();

    }



    //PERMISSOES
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for( int permissaoResultado: grantResults){
            if (permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }

    private  void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissôes Negadas");
        builder.setMessage("Para ultilizar o app é nescessario aceitar as permissôes");
        builder.setCancelable(false);
        builder.setPositiveButton("confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent it = new Intent(Cadastrar_icon_nome_Activity.this,Cadastrar_icon_nome_Activity.class);
                startActivity(it);

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
