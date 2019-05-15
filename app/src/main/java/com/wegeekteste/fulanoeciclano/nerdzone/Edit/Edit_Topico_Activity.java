package com.wegeekteste.fulanoeciclano.nerdzone.Edit;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.wegeekteste.fulanoeciclano.nerdzone.Activits.MainActivity;
import com.wegeekteste.fulanoeciclano.nerdzone.Activits.Minhas_Publicacoes;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Topico;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Usuario;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Edit_Topico_Activity extends AppCompatActivity {
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private Toolbar toolbar;
    private CircleImageView icone;
    private ImageView img_topico;
    private String identificadorUsuario;
    private Button botaosalvar;
    private DatabaseReference databaseusuario,databasetopico,SeguidoresRef,Meusdatabasetopico;
    private FirebaseUser usuario;
    private EditText titulo_topico,mensagem_topico;
    private Topico topico;
    private Usuario perfil;
    private StorageReference storageReference;
    private Dialog dialog;
    private Uri url;
    private String ids;
    private ChildEventListener ChildEventListenerTopico;
    private String urlimg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__topico_);

        toolbar = findViewById(R.id.toolbarsecundario_sem_foto);
        toolbar.setTitle("Editar Tópico");
        setSupportActionBar(toolbar);

        //Configuraçoes Originais
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        databaseusuario = ConfiguracaoFirebase.getDatabase().getReference().child("usuarios");
        databasetopico = ConfiguracaoFirebase.getDatabase().getReference().child("topico");
        Meusdatabasetopico = ConfiguracaoFirebase.getDatabase().getReference().child("meustopicos");
        SeguidoresRef =ConfiguracaoFirebase.getDatabase().getReference().child("seguidores");
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        titulo_topico = findViewById(R.id.nome_topico_edit);
        mensagem_topico = findViewById(R.id.desc_topico_edit);
        botaosalvar = findViewById(R.id.botaosalvartopico_edit);
        botaosalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidarDados();
            }
        });
        img_topico = findViewById(R.id.imageTopicoCadastro_edit);
        img_topico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .getIntent(Edit_Topico_Activity.this);
                startActivityForResult(intent, SELECAO_GALERIA);
            }
        });


        CarregarDadosTopico();
        TrocarFundos_status_bar();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void CarregarDadosTopico(){
        ids=getIntent().getStringExtra("id_topico");
        Log.i("sdsd",ids);
     ChildEventListenerTopico = databasetopico.orderByChild("uid").equalTo(ids).addChildEventListener(new ChildEventListener() {
         @Override
         public void onChildAdded(DataSnapshot dataSnapshot, String s) {
             topico = dataSnapshot.getValue(Topico.class);
             assert topico != null;

             titulo_topico.setText(topico.getTitulo());
             mensagem_topico.setText(topico.getMensagem());

             Glide.with(Edit_Topico_Activity.this)
                     .load(topico.getFoto())
                     .into(img_topico);
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
            LayoutInflater layoutInflater = LayoutInflater.from(Edit_Topico_Activity.this);
            final View view  = layoutInflater.inflate(R.layout.dialog_carregando_gif_analisando,null);
            ImageView imageViewgif = view.findViewById(R.id.gifimage);

            Glide.with(this)
                    .asGif()
                    .load(R.drawable.gif_analizando)
                    .into(imageViewgif);
            builder.setView(view);

            dialog = builder.create();
            dialog.show();
            Topico topicos = new Topico();
            topicos.setUid(topico.getUid());
            topicos.setQuantcomentario(topico.getQuantcomentario());
            topicos.setFoto(topico.getFoto());
            topicos.setData(topico.getData());
            topicos.setMensagem(mensagem_topico.getText().toString());
            topicos.setTitulo(titulo_topico.getText().toString());
            topicos.setLikecount(topico.getLikecount());
            topicos.setIdauthor(topico.getIdauthor());
            databasetopico.child(topicos.getUid()).setValue(topicos).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
            Meusdatabasetopico.child(usuariologado).child(topicos.getUid()).setValue(topicos).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    dialog.dismiss();
                    Intent it = new Intent(Edit_Topico_Activity.this, Minhas_Publicacoes.class);
                    startActivity(it);
                    finish();
                    Toast toast = Toast.makeText(Edit_Topico_Activity.this, "Alterado com sucesso!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();

                      }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Intent it = new Intent(Edit_Topico_Activity.this, Minhas_Publicacoes.class);
                    startActivity(it);
                    finish();
                    Toast toast = Toast.makeText(Edit_Topico_Activity.this, "Ocorreu algum problema," +
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {
                switch (requestCode) {
                    case SELECAO_CAMERA:
                        CropImage.ActivityResult resultCAMERA = CropImage.getActivityResult(data);
                        Uri resultUriCAMERA = resultCAMERA.getUri();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUriCAMERA);
                        break;
                    case SELECAO_GALERIA:
                        CropImage.ActivityResult resultGALERIA = CropImage.getActivityResult(data);
                        Uri resultUriGALERIA = resultGALERIA.getUri();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUriGALERIA);
                        break;


                }
                if (imagem != null) {
                    img_topico.setImageBitmap(imagem);
                    //Recuperar dados da imagem  para o  Firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();
                    String nomeImagem = UUID.randomUUID().toString();
                    //Salvar no Firebase
                    StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("evento")
                            .child(identificadorUsuario)
                            .child(topico.getUid());
                    //Progress
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("Aguarde..");
                    progressDialog.setMessage("Carregando");
                    progressDialog.show();
                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    //caso de errado
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    progressDialog.dismiss();
                                    Toast.makeText(Edit_Topico_Activity.this, "Imagem Carregada com Sucesso", Toast.LENGTH_SHORT).show();

                                    urlimg = uri.toString();

                                    if(urlimg!=null){
                                        topico.setFoto(urlimg);
                                    }
                                    Glide.with(Edit_Topico_Activity.this)
                                            .load(uri)
                                            .into(img_topico);
                                    progressDialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    progressDialog.dismiss();
                                    Toast.makeText(Edit_Topico_Activity.this, "Erro ao carregar a imagem", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
