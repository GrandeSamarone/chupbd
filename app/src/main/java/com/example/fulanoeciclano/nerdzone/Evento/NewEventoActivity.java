package com.example.fulanoeciclano.nerdzone.Evento;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Date.DatePickFragment;
import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Model.Evento;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.UUID;

import static com.example.fulanoeciclano.nerdzone.Helper.App.getUid;

public class NewEventoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

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
    private Toolbar toolbar;
    // [START declare_database_ref]
    private DatabaseReference mDatabaseEvento;
    private Button botaoiniciodata, botaofimdata;
    // [END declare_database_ref]
    private String[] estados ;
    private Evento eventos;
    private String estado;
    private EditText titulo, subtitulo, mensagem;
    private TextView data_inicio, data_fim;
    private ImageView imgevento;
    private AlertDialog alerta;
    private String urlimg;
    private DatePickerDialog datePickerDialog;
    private int dia, mes, ano;


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
        eventos = new Evento();



        mDatabaseEvento = FirebaseDatabase.getInstance().getReference();
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
        botaofimdata = findViewById(R.id.botaodatafim);
        botaoiniciodata = findViewById(R.id.botaodatainicio);
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        imgevento = findViewById(R.id.img_add_foto_evento);
        titulo = findViewById(R.id.titulo_evento);
        subtitulo = findViewById(R.id.subtitulo_evento);
        data_inicio = findViewById(R.id.data_topico_inicio);
        data_fim = findViewById(R.id.data_topico_fim);
        mensagem = findViewById(R.id.desc_evento);
        botaoSalvar = findViewById(R.id.btn_salvar_topico);
        spinner = findViewById(R.id.spnilocalidade);


        //Adicionando Estados no ArrayList
        estados = getResources().getStringArray(R.array.estados);
        //Cria um ArrayAdapter usando um padrão de layout da classe R do android, passando o ArrayList nomes
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, estados);
        ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);
        //Método do Spinner para capturar o item selecionado
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                //pega nome pela posição
                    estado = parent.getItemAtPosition(posicao).toString();
                //imprime um Toast na tela com o nome que foi selecionado

                Toast.makeText(NewEventoActivity.this, estado, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        imgevento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Escolher_Foto_Evento();
            }
        });


        //######Guardando a  data#########
        data_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickFragment();
                datePicker.show(getSupportFragmentManager(), datainicio);
            }
        });


        data_fim.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatePicker datePicker = new DatePicker(NewEventoActivity.this);
                final Calendar calendar1 = Calendar.getInstance();
                Log.i("dataa", String.valueOf(calendar1.getTime()));
                ano = calendar1.get(Calendar.YEAR);
                mes = calendar1.get(Calendar.MONTH);
                dia = calendar1.get(Calendar.DAY_OF_MONTH);


                datePickerDialog = new DatePickerDialog(NewEventoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int ano, int mes, int dia) {
                        final String currentDate = DateFormat.getDateInstance().format(calendar1.getTime());
                        data_fim.setText(currentDate);
                    }
                }, ano, mes, dia);

                datePickerDialog.show();


            }

        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    //dialog de opcoes
    private void Escolher_Foto_Evento() {
        //LayoutInflater é utilizado para inflar nosso layout em uma view.
        //-pegamos nossa instancia da classe
        LayoutInflater li = getLayoutInflater();

        //inflamos o layout tela_opcao_foto.xml_foto.xml na view
        View view = li.inflate(R.layout.dialog_opcao_evento, null);
        //definimos para o botão do layout um clickListener
        view.findViewById(R.id.botaocamera).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //exibe um Toast informativo.


                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (it.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(it, SELECAO_CAMERA);
                }
                //desfaz o tela_opcao_foto.
                alerta.dismiss();
            }
        });

        view.findViewById(R.id.botaogaleria).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (it.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(it, SELECAO_GALERIA);
                }
                //desfaz o tela_opcao_foto.
                alerta.dismiss();
            }
        });


        //Dialog de tela
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        alerta = builder.create();
        alerta.show();

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
                            Toast.makeText(NewEventoActivity.this, "Erro ao carregar a imagem", Toast.LENGTH_SHORT).show();
                        }
                        //caso o carregamento no firebase de tudo certo
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(NewEventoActivity.this, "Imagem Carregada com Sucesso", Toast.LENGTH_SHORT).show();

                            Uri url = taskSnapshot.getDownloadUrl();
                            // SalvarPost(url);
                            urlimg = url.toString();


                            Glide.with(NewEventoActivity.this)
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


    private Evento  configurarEvento() {
        final String tituloDoEvento = titulo.getText().toString();
        final String subtituloDoEvento = subtitulo.getText().toString();
        final String mensagemDoEvento = mensagem.getText().toString();
        final String dataDoEventoFim = data_fim.getText().toString();
        final String dataDoEventoInicio = data_inicio.getText().toString();
        final String estadoDoEvento = estado;
        final String capaevento = urlimg;


        eventos.setTitulo(tituloDoEvento);
        eventos.setSubtitulo(subtituloDoEvento);
        eventos.setMensagem(mensagemDoEvento);
        eventos.setDatainicio(dataDoEventoFim);
        eventos.setDatafim(dataDoEventoInicio);
        eventos.setFotoevento(capaevento);

        return  eventos;
    }

    public void validardados(){
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

        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        Toast.makeText(this, "Postando...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
        final String userId = getUid();
        Log.i("carz", userId);
        mDatabaseEvento.child("usuarios").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
/*
                        writeNewPost(userId, usuarioLogado.getNome(), usuarioLogado.getFoto(), tituloDoEvento
                                , subtituloDoEvento, capaevento, mensagemDoEvento, dataDoEventoFim, dataDoEventoInicio, estadoDoEvento);
*/

                        // Finish this Activity, back to the stream
                        setEditingEnabled(true);
                        finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // [START_EXCLUDE]
                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
        // [END single_value_read]
    }

    public void salvarMercado(){

    }
    private void setEditingEnabled(boolean enabled) {
        titulo.setEnabled(enabled);
        mensagem.setEnabled(enabled);
        if (enabled) {
            botaoSalvar.setVisibility(View.VISIBLE);
        } else {
            botaoSalvar.setVisibility(View.GONE);
        }
    }


}




