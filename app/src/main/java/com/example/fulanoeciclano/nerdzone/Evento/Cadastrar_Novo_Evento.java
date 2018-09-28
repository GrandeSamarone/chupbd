package com.example.fulanoeciclano.nerdzone.Evento;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fulanoeciclano.nerdzone.Activits.MinhaConta;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Date.DatePickFragment;
import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Model.Evento;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.fulanoeciclano.nerdzone.Activits.MainActivity.setWindowFlag;
import static com.example.fulanoeciclano.nerdzone.Helper.App.getUid;

public class Cadastrar_Novo_Evento extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private static final String datainicio = "date picker";
    private static final String datafim = "date picker2";
    private static final String TITULO = "Obrigatório";
    private static final String SUBTITULO = "Obrigatório";
    private static final String MENSAGEM = "Obrigatório";
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;

    private StorageReference storageReference;
    private Usuario usuarioLogado;
    private String identificadorUsuario;
    private Spinner spinner;
    private CircleImageView icone;
    private Toolbar toolbar;
    // [START declare_database_ref]
    private DatabaseReference mDatabaseEvento;
    private Button botaoiniciodata, botaofimdata;
    // [END declare_database_ref]
    private String[] estados;
    private Evento eventos;
    private String estado,idAutor;
    private EditText titulo, subtitulo, mensagem;
    private TextView data_inicio, data_fim;
    private ImageView imgevento;
    private AlertDialog alerta;
    private String urlimg;
    private DatePickerDialog datePickerDialog;
    private int dia, mes, ano;
    private AlertDialog dialog;


    private FloatingActionButton botaoSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_evento);

        final Calendar calendar12 = Calendar.getInstance();
        Log.i("dataa", String.valueOf(calendar12.getTime()));
        //Configuraçoes
        toolbar = findViewById(R.id.toolbarsecundario);
        toolbar.setTitle("Criar Evento");
        setSupportActionBar(toolbar);


        //Configuracao Inicial
        eventos = new Evento();
        mDatabaseEvento = FirebaseDatabase.getInstance().getReference();
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
        botaofimdata = findViewById(R.id.botaodatafim);
        botaoiniciodata = findViewById(R.id.botaodatainicio);
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        imgevento = findViewById(R.id.img_add_foto_evento);
        imgevento.setOnClickListener(this);
        titulo = findViewById(R.id.titulo_evento);
        subtitulo = findViewById(R.id.subtitulo_evento);
        data_inicio = findViewById(R.id.data_topico_inicio);
        data_inicio.setOnClickListener(this);
        data_fim = findViewById(R.id.data_topico_fim);
        data_fim.setOnClickListener(this);
        mensagem = findViewById(R.id.desc_evento);
        botaoSalvar = findViewById(R.id.btn_salvar_topico);
        botaoSalvar.setOnClickListener(this);
        spinner = findViewById(R.id.spnilocalidade);


        TrocarFundos_status_bar();
        IconeUsuario();
        CarregarDadosSpinner();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_add_foto_evento:
                Intent intent = CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .getIntent(Cadastrar_Novo_Evento.this);
                startActivityForResult(intent, SELECAO_GALERIA);
                break;

            case R.id.data_topico_inicio:
                DialogFragment datePicker = new DatePickFragment();
                datePicker.show(getSupportFragmentManager(), datainicio);
                break;
            case R.id.data_topico_fim:
                DatePicker datePickera = new DatePicker(Cadastrar_Novo_Evento.this);
                final Calendar calendar1 = Calendar.getInstance();
                Log.i("dataa", String.valueOf(calendar1.getTime()));
                ano = calendar1.get(Calendar.YEAR);
                mes = calendar1.get(Calendar.MONTH);
                dia = calendar1.get(Calendar.DAY_OF_MONTH);


                datePickerDialog = new DatePickerDialog(Cadastrar_Novo_Evento.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int ano, int mes, int dia) {
                        final String currentDate = DateFormat.getDateInstance().format(calendar1.getTime());
                        data_fim.setText(currentDate);
                    }
                }, ano, mes, dia);

                datePickerDialog.show();
                break;
            case R.id.btn_salvar_topico:
                validardados();
                break;
        }
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
    public void onDateSet(DatePicker view, int ano, int mes, int dia) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, ano);
        calendar.set(Calendar.MONTH, mes);
        calendar.set(Calendar.DAY_OF_MONTH, dia);

        String currentDateString = DateFormat.getDateInstance().format(calendar.getTime());

        data_fim.setText(currentDateString);
        data_inicio.setText(currentDateString);


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
                    imgevento.setImageBitmap(imagem);
                    //Recuperar dados da imagem  para o  Firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    byte[] dadosImagem = baos.toByteArray();
                    String nomeImagem = UUID.randomUUID().toString();
                    //Salvar no Firebase
                    StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("evento")
                            .child(identificadorUsuario)
                            .child("Capa_do_Evento")
                            .child(nomeImagem);
                    //Progress
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("Aguarde..");
                    progressDialog.show();
                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    //caso de errado
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Cadastrar_Novo_Evento.this, "Erro ao carregar a imagem", Toast.LENGTH_SHORT).show();
                        }
                        //caso o carregamento no firebase de tudo certo
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(Cadastrar_Novo_Evento.this, "Imagem Carregada com Sucesso", Toast.LENGTH_SHORT).show();

                            Uri url = taskSnapshot.getDownloadUrl();
                            // SalvarPost(url);
                            urlimg = url.toString();


                            Glide.with(Cadastrar_Novo_Evento.this)
                                    .load(url)
                                    .into(imgevento);

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


    private Evento configurarEvento() {
        final String tituloDoEvento = titulo.getText().toString();
        final String subtituloDoEvento = subtitulo.getText().toString();
        final String mensagemDoEvento = mensagem.getText().toString();
        final String dataDoEventoFim = data_fim.getText().toString();
        final String dataDoEventoInicio = data_inicio.getText().toString();
        final String estadoDoEvento = spinner.getSelectedItem().toString();
        final String id = identificadorUsuario;
        final String capaevento = urlimg;


        eventos.setTitulo(tituloDoEvento);
        eventos.setIdUsuario(id);
        eventos.setSubtitulo(subtituloDoEvento);
        eventos.setMensagem(mensagemDoEvento);
        eventos.setDatainicio(dataDoEventoFim);
        eventos.setDatafim(dataDoEventoInicio);
        eventos.setEstado(estadoDoEvento);
        eventos.setCapaevento(capaevento);

        return eventos;
    }

    public void validardados() {
        eventos = configurarEvento();

        // verificando se o titulo está vazio
        if (TextUtils.isEmpty(eventos.getTitulo())) {
            titulo.setError(TITULO);
            return;
        }
        if (TextUtils.isEmpty(eventos.getSubtitulo())) {
            titulo.setError(SUBTITULO);
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(eventos.getMensagem())) {
            mensagem.setError(MENSAGEM);
            return;
        }

        eventos.salvar();
        Toast.makeText(this, "Evento Criado Com Sucesso!", Toast.LENGTH_SHORT).show();
        Intent it = new Intent( Cadastrar_Novo_Evento.this,Evento_Lista.class);
        startActivity(it);
        finish();
        // [START single_value_read]
        final String userId = getUid();

    }




    private void TrocarFundos_status_bar() {
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

    private void IconeUsuario() {
        //Imagem do icone do usuario
        icone = findViewById(R.id.icone_user_toolbar);
        icone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Cadastrar_Novo_Evento.this, MinhaConta.class);
                startActivity(it);
            }
        });
        FirebaseUser UsuarioAtual = UsuarioFirebase.getUsuarioAtual();
        String mPhotoUrl = UsuarioAtual.getPhotoUrl().toString();

        Glide.with(Cadastrar_Novo_Evento.this)
                .load(mPhotoUrl)
                .into(icone);
    }

    //carregar spinner
    private void CarregarDadosSpinner() {
        //
        String[] artista = getResources().getStringArray(R.array.estados);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, artista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                estado = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}




