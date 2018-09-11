package com.example.fulanoeciclano.nerdzone.Mercado;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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
