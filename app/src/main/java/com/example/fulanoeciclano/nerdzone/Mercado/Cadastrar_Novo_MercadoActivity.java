package com.example.fulanoeciclano.nerdzone.Mercado;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Helper.Permissoes;
import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Model.Mercado;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Cadastrar_Novo_MercadoActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String padrao = "Obrigatório";


    private AppCompatEditText campotitulo, campodesc, campotelefone, campoendereco,campofraserapida;
    private CircleImageView imagem1,imagem2,imagem3,imagem4,imagem5,imagem6;
    private StorageReference storageReference;
    private Spinner campoLocal, campoloja;
    private Button botaosalvar;
    private DatabaseReference database;
    private String identificadorUsuario,estadostring,lojastring,autorstring;
    private FirebaseAuth autenticacao;
    private Mercado mercado;
    private Usuario usuarioLogado;
    private FirebaseUser UsuarioAtual;
    private Toolbar toolbar;
    private AlertDialog dialog;



    private String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE

    };

    private List<String> listaFotosRecuperadas = new ArrayList<>();
    private List<String> listaURLFotos = new ArrayList<>();
    private String[] pegandoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar__novo__mercado);

        toolbar = findViewById(R.id.toolbarprincipal);
        toolbar.setTitle("Novo Comercio");
        setSupportActionBar(toolbar);

        //Configuração Basica
        campotitulo = findViewById(R.id.nome_mercado);
        campodesc = findViewById(R.id.desc_mercado);
        campotelefone = findViewById(R.id.desc_telefone);
        campoendereco = findViewById(R.id.desc_endereco);
        campofraserapida= findViewById(R.id.fraserapida_mercado);
        campoloja = findViewById(R.id.spinnerloja);
        campoLocal = findViewById(R.id.spinneralocal);
        imagem1 = findViewById(R.id.imageLojaCadastro1);
        imagem1.setOnClickListener(this);
        imagem2 =findViewById(R.id.imageLojaCadastro2);
        imagem2.setOnClickListener(this);
        imagem3 = findViewById(R.id.imageLojaCadastro3);
        imagem3.setOnClickListener(this);
        imagem4 = findViewById(R.id.imageLojaCadastro4);
        imagem4.setOnClickListener(this);
        imagem5 = findViewById(R.id.imageLojaCadastro5);
        imagem5.setOnClickListener(this);
        imagem6 = findViewById(R.id.imageLojaCadastro6);
        imagem6.setOnClickListener(this);

        mercado = new Mercado();
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        database = ConfiguracaoFirebase.getDatabase().getReference().child("mercado");
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        botaosalvar = findViewById(R.id.botaosalvarmercado);
        botaosalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDados();
            }
        });

        /// /validar permissoes
        Permissoes.validarPermissoes(permissoes, this, 1);

        //carregar SPINNER
        CarregarDadosSpinner();



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void  SalvarMercado(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = LayoutInflater.from(Cadastrar_Novo_MercadoActivity.this);
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
                .child("mercado")
                .child(mercado.getIdMercado())
                .child("imagem"+contador);

        //fazer upload arquivo
        UploadTask uploadTask = imagemMercado.putFile(Uri.parse(urlimagem));
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri firebaseUrl = taskSnapshot.getDownloadUrl();
                String urlConvertida = firebaseUrl.toString();

                listaURLFotos.add(urlConvertida);

                if(totalfotos==listaURLFotos.size()){
                    mercado.setFotos(listaURLFotos);
                    mercado.salvar();
                    dialog.dismiss();
                    finish();
                }

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Cadastrar_Novo_MercadoActivity.this, "Falha ao fazer upload", Toast.LENGTH_SHORT).show();
                Log.i("INFO","falha ao fazer upload:"+e.getMessage());
            }
        });
    }

      //carregar spinner
    private void CarregarDadosSpinner() {
        //
        String[] artista = getResources().getStringArray(R.array.estados);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, artista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campoLocal.setAdapter(adapter);
        campoLocal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                estadostring = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //spinner categoria
        String[] loja = getResources().getStringArray(R.array.loja);
        ArrayAdapter<String> adaptercat = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, loja);
        adaptercat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campoloja.setAdapter(adaptercat);

        campoloja.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lojastring = parent.getItemAtPosition(position).toString();
                Toast.makeText(Cadastrar_Novo_MercadoActivity.this, lojastring, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private Mercado configurarMercado(){
        String estado = campoLocal.getSelectedItem().toString();
        String loja = campoloja.getSelectedItem().toString();
        String titulo = campotitulo.getText().toString();
        String telefone = campotelefone.getText().toString();
        String descricao = campodesc.getText().toString();
        String fraserapida = campofraserapida.getText().toString();
        String endereco = campoendereco.getText().toString();
        String autor = usuarioLogado.getNome();

        mercado.setEstado(estado);
        mercado.setAutor(autor);
        mercado.setCategoria(loja);
        mercado.setTitulo(titulo);
        mercado.setTelefone(telefone);
        mercado.setDescricao(descricao);
        mercado.setFraserapida(fraserapida);
        mercado.setEndereco(endereco);

        return  mercado;
    }

    public void validarDados() {
        mercado = configurarMercado();
        if (listaFotosRecuperadas.size() != 0) {
            if ( (!lojastring.equals("Categoria"))) {

           if ( (!estadostring.equals("Estado"))) {
               // verificando se  está vazio
               if (TextUtils.isEmpty(mercado.getTitulo())) {
                   campotitulo.setError(padrao);
                   return;
               }
               if (TextUtils.isEmpty(mercado.getFraserapida())) {
                   campofraserapida.setError(padrao);
                   return;
               }
               if (TextUtils.isEmpty(mercado.getDescricao())) {
                   campodesc.setError(padrao);
                   return;
               }

               if (TextUtils.isEmpty(mercado.getEndereco())) {
                   campoendereco.setError(padrao);
                   return;
               }
               if (TextUtils.isEmpty(mercado.getTelefone())) {
                   campotelefone.setError(padrao);
                   return;
               }
               SalvarMercado();
           }else{
               Toast.makeText(this, "Selecione um Estado", Toast.LENGTH_SHORT).show();
           }

           }else {
                Toast.makeText(this, "Selecione uma Categoria", Toast.LENGTH_SHORT).show();
            }
        } else {
                Toast.makeText(this, "Selecione ao menos uma foto!", Toast.LENGTH_SHORT).show();
            }


        }

    //Botao Voltar
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                Intent it = new Intent(Cadastrar_Novo_MercadoActivity.this, MercadoActivity.class);
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


    //click na imagem para cadastra mercado
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case  R.id.imageLojaCadastro1:
                EscolherImagem(1);
                break;
            case  R.id.imageLojaCadastro2:
                EscolherImagem(2);
                break;
            case  R.id.imageLojaCadastro3:
                EscolherImagem(3);
                break;
            case  R.id.imageLojaCadastro4:
                EscolherImagem(4);
                break;
            case  R.id.imageLojaCadastro5:
                EscolherImagem(5);
                break;
            case  R.id.imageLojaCadastro6:
                EscolherImagem(6);
                break;


        }
    }

    public  void EscolherImagem(int requestCode){
        Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(it,requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            Uri imagemSelecionada = data.getData();
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



}
