package com.example.fulanoeciclano.nerdzone.Activits;

import android.Manifest;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Helper.Permissoes;
import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Icons.PageIcon;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class MinhaConta extends AppCompatActivity {

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private ImageButton imageButtonCamera,imageButtonGaleria;
    private static final int SELECAO_CAMERA=100;
    private static final int SELECAO_GALERIA=200;
    private static final int SELECAO_ICONE=300;
    private static final int MINHA_CONTA=12;

    private CircleImageView circleImageViewperfil;
    private StorageReference storageReference;
    private String identificadorUsuario;
    private EditText nomePerfilUsuario;
    private FloatingActionButton botaotrocarfoto;
    private ImageView imageatualizarnome;
    private Usuario usuarioLogado;
    private Usuario user;
    private FirebaseUser identificador;
    private URL url;
    private String mPhotoUrl;
    private AlertDialog alerta;
    private com.google.firebase.database.ChildEventListener ChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_minha_conta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Minha Conta");
        setSupportActionBar(toolbar);

        //configuracoes iniciais
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        identificador= UsuarioFirebase.getUsuarioAtual();
      botaotrocarfoto = findViewById(R.id.fabminhaconta);
        circleImageViewperfil=findViewById(R.id.circleImageViewFotoPerfil);
        nomePerfilUsuario= findViewById(R.id.editPerfilNome);
        imageatualizarnome= findViewById(R.id.imagematualizarnome);
        usuarioLogado=UsuarioFirebase.getDadosUsuarioLogado();
        user = new Usuario();

        //validar permissoes
        Permissoes.validarPermissoes(permissoesNecessarias,this,1);


        //Botao Cadastrar
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btn_atualizar_cadastro);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlterarNome();


            }
        });

        //Recuperar dados do Usuario

        FirebaseUser usuario =UsuarioFirebase.getUsuarioAtual();
        Uri url= usuario.getPhotoUrl();
        // Uri uriOne= Uri.parse(mArrayListOne.get(position));
        Glide.with(MinhaConta.this)
                .load(url)
                .into(circleImageViewperfil );

        /*   DraweeController controllerOne = Fresco.newDraweeControllerBuilder()
                .setUri(url)
                .setAutoPlayAnimations(true)
                .build();

        circleImageViewperfil.setController(controllerOne);
        */

        nomePerfilUsuario.setText(usuarioLogado.getNome());

        //Recuperar a imagem Icones da pagina Icones
      botaotrocarfoto.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Escolher_Foto_Perfil();
          }
      });




        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    protected void onStart() {
        super.onStart();
        RecuperarIcone();
    }

    //Botao Voltar
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:


                    finish();


                break;

            default:break;
        }

        return true;
    }

    //Salvar o novo nome do usuario no firebase
    private void AlterarNome(){
        final String nome = nomePerfilUsuario.getText().toString();
        final boolean retorno = UsuarioFirebase.atualizarNomeUsuario(nome);


        if (retorno) {
            usuarioLogado.setNome(nome);

            usuarioLogado.atualizar();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("alteracao");
            builder.setMessage("Alterado com Sucesso!");
            builder.setCancelable(false);
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent it = new Intent(MinhaConta.this,MainActivity.class);
                    startActivity(it);
                    finish();

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();



        }

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

                    atualizaFotoUsuario(uri);

                    Glide.with(MinhaConta.this)
                            .load(uri)
                            .into(circleImageViewperfil);

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {
                switch (requestCode) {
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;


                }
                if (imagem != null) {
                    circleImageViewperfil.setImageBitmap(imagem);
                    //Recuperar dados da imagem  para o  Firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //Salvar no Firebase
                    StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("perfil")
                            .child(identificadorUsuario)
                            .child("perfil.jpg");
                    //Progress
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("Carregando...");
                    progressDialog.show();
                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    //caso de errado
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(MinhaConta.this, "Erro ao carregar a imagem", Toast.LENGTH_SHORT).show();
                        }
                        //caso o carregamento no firebase de tudo certo
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(MinhaConta.this, "Imagem Carregada com Sucesso", Toast.LENGTH_SHORT).show();

                            Uri url = taskSnapshot.getDownloadUrl();
                            atualizaFotoUsuario(url);

                            Glide.with(MinhaConta.this)
                                    .load(url)
                                    .into(circleImageViewperfil);

                        }

                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Carregando... "/* + (int) progress + "%"*/);
                        }
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void atualizaFotoUsuario(Uri url) {
        boolean retorno = UsuarioFirebase.atualizarFotoUsuario(url);
        if(retorno){
            usuarioLogado.setFoto(url.toString());
            usuarioLogado.atualizar();
        }
        Toast.makeText(this, "Sua foto foi alterada", Toast.LENGTH_SHORT).show();



    }
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
                Intent it = new Intent(MinhaConta.this, MainActivity.class);
                startActivity(it);

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //dialog de opcoes
    private void  Escolher_Foto_Perfil() {
        //LayoutInflater é utilizado para inflar nosso layout em uma view.
        //-pegamos nossa instancia da classe
        LayoutInflater li = getLayoutInflater();

        //inflamos o layout dialog_opcao_foto.xml_foto.xml na view
        View view = li.inflate(R.layout.dialog_opcao_foto, null);
        //definimos para o botão do layout um clickListener
        view.findViewById(R.id.botaocamera).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //exibe um Toast informativo.


                Intent it=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(it.resolveActivity(getPackageManager())!=null)  {
                    startActivityForResult(it, SELECAO_CAMERA);
                }
                //desfaz o dialog_opcao_foto.
                alerta.dismiss();
            }
        });

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


                Intent it = new Intent(MinhaConta.this,PageIcon.class);
                it.putExtra("minhaconta",MINHA_CONTA);
                startActivityForResult(it, SELECAO_ICONE);
                //desfaz o dialog_opcao_foto.
                alerta.dismiss();
            }
        });

        //Dialog de tela
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alterar Foto");
        builder.setView(view);
        alerta = builder.create();
        alerta.show();

    }


}