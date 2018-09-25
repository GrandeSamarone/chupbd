package com.example.fulanoeciclano.nerdzone.Mercado;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fulanoeciclano.nerdzone.Activits.AbrirImagem;
import com.example.fulanoeciclano.nerdzone.Model.Mercado;
import com.example.fulanoeciclano.nerdzone.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.io.Serializable;
import java.util.List;

public class Detalhe_Mercado extends AppCompatActivity {

    private CarouselView fotos;
    private FloatingActionButton fabcontato;
    private ImageView botaovoltar;
    private TextView titulo,legenda,descricao,endereco,categoria,estado,criador;
    private Button botaoavaliar;
    private Mercado mercadoselecionado;
    private Dialog dialog;
    private AlertDialog alerta;
    private SharedPreferences preferences = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_detalhe_mercados);



        //configuracoes iniciais

        fotos = findViewById(R.id.carousel_foto_mercado);
        titulo = findViewById(R.id.detalhe_mercado_titulo);
        botaoavaliar = findViewById(R.id.botao_avaliar_mercado);
        botaoavaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mostra_Dialog_Rating();
            }
        });
        legenda = findViewById(R.id.detalhe_mercado_legenda);
        criador = findViewById(R.id.detalhe_mercado_criador);
        descricao = findViewById(R.id.detalhe_mercado_descricao);
        endereco = findViewById(R.id.detalhe_mercado_endereco);
        botaovoltar = findViewById(R.id.mercado_button_back);
        botaovoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Detalhe_Mercado.this,MercadoActivity.class);
                startActivity(it);
                finish();
            }
        });

        fabcontato = findViewById(R.id.fab_entrar_em_contato);
        fabcontato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        //recuperar mercado selecionado

        mercadoselecionado = (Mercado) getIntent().getSerializableExtra("mercadoelecionado");

        if(mercadoselecionado!=null){
            titulo.setText(mercadoselecionado.getTitulo());
            legenda.setText(mercadoselecionado.getFraserapida());
            descricao.setText(mercadoselecionado.getDescricao());
            endereco.setText(mercadoselecionado.getEndereco());
            criador.setText(mercadoselecionado.getAutor());
         //   categoria.setText(mercadoselecionado.getCategoria());

            //carregar as imagens

            ImageListener imageListener = new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
              String urlstring = mercadoselecionado.getFotos().get(position);

                    Glide.with(getApplicationContext())
                            .load(urlstring)
                        .into(imageView);
                }
            };

        fotos.setPageCount(mercadoselecionado.getFotos().size());
        fotos.setImageListener(imageListener);

            fotos.setImageClickListener(new ImageClickListener() {
                @Override
                public void onClick(int position) {
                    /*Bundle bundle = new Bundle();
                    bundle.putString("id", mercadoselecionado.getIdMercado());
                    Intent it = new Intent(Detalhe_Mercado.this, AbrirImagem.class);
                    it.putExtras(bundle);
                    startActivity(it);
                    */


                    List<String>  ff= mercadoselecionado.getFotos();
                    Intent it = new Intent(Detalhe_Mercado.this,AbrirImagem.class);
                    it.putExtra("fotoselecionada", (Serializable) ff);
                    it.putExtra("nome",mercadoselecionado.getTitulo());
                    startActivity(it);

                }
            });
        }

    }




    @Override
    protected void onStart() {
        super.onStart();
        preferences = getSharedPreferences("primeiravezdetalhe", MODE_PRIVATE);
        if (preferences.getBoolean("primeiravezdetalhe", true)) {
            preferences.edit().putBoolean("primeiravezdetalhe", false).apply();
            Dialog_click_foto();
        }else{

        }
    }

    private void Dialog_click_foto() {

        LayoutInflater li = getLayoutInflater();


        View view = li.inflate(R.layout.dialog_informar_click_foto, null);



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


    //Botao Voltar
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }else{
                    finish();
                }


                break;

            default:break;
        }

        return true;
    }
    private void Mostra_Dialog_Rating() {
        //LayoutInflater é utilizado para inflar nosso layout em uma view.
        //-pegamos nossa instancia da classe
        LayoutInflater li = getLayoutInflater();

        //inflamos o layout dialog_opcao_foto.xml_foto.xml na view
        View view = li.inflate(R.layout.dialog_avaliar_rating, null);
        //definimos para o botão do layout um clickListener
        view.findViewById(R.id.botaosalvarrating).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //exibe um Toast informativo.



                //desfaz o dialog_opcao_foto.
                alerta.dismiss();
            }
        });


        //Dialog de tela
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        alerta = builder.create();
        alerta.show();

    }

}
