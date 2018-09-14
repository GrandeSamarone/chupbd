package com.example.fulanoeciclano.nerdzone.Mercado;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fulanoeciclano.nerdzone.Model.Mercado;
import com.example.fulanoeciclano.nerdzone.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

public class Detalhe_Mercado extends AppCompatActivity {

    private CarouselView fotos;
    private TextView titulo,legenda,descricao,endereco,categoria,estado;
    private Mercado mercadoselecionado;
    private Dialog dialog;
    private SharedPreferences preferences = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe__mercado);



        //configuracoes iniciais
        fotos = findViewById(R.id.carousel_foto_mercado);
        titulo = findViewById(R.id.detalhe_mercado_titulo);
        legenda = findViewById(R.id.detalhe_mercado_legenda);
        descricao = findViewById(R.id.detalhe_mercado_descricao);
        endereco = findViewById(R.id.detalhe_mercado_endereco);
      //  categoria = findViewById(R.id.detalhe_mercado_categoria);

        //recuperar mercado selecionado

        mercadoselecionado = (Mercado) getIntent().getSerializableExtra("mercadoelecionado");

        if(mercadoselecionado!=null){
            titulo.setText(mercadoselecionado.getTitulo());
            legenda.setText(mercadoselecionado.getFraserapida());
            descricao.setText(mercadoselecionado.getDescricao());
            endereco.setText(mercadoselecionado.getEndereco());
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

                }
            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        preferences = getSharedPreferences("primeiravez", MODE_PRIVATE);
        if (preferences.getBoolean("primeiravez", true)) {
            preferences.edit().putBoolean("primeiravez", false).apply();
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

                    finish();


                break;

            default:break;
        }

        return true;
    }


}
