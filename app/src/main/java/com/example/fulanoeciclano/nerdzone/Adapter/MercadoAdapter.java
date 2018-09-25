package com.example.fulanoeciclano.nerdzone.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fulanoeciclano.nerdzone.Model.Mercado;
import com.example.fulanoeciclano.nerdzone.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by fulanoeciclano on 29/08/2018.
 */

public class MercadoAdapter extends RecyclerView.Adapter<MercadoAdapter.MyViewHolder> {

    private List<Mercado> mercados;
    private Context context;

    public MercadoAdapter(List<Mercado> mercado, Context context){
        this.mercados=mercado;
        this.context=context;
    }
    public List<Mercado> getmercados(){
        return this.mercados;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mercado,parent,false);

        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Mercado mercado = mercados.get(position);
        if(mercado.getTitulo()!=null){
        holder.titulo.setText(mercado.getTitulo());
        }
        if(mercado.getDescricao()!=null){
        holder.legenda.setText(mercado.getFraserapida());
        }

        if(mercado.getCategoria()!=null){
        holder.categoria.setText(mercado.getCategoria());
        }

        List<String> urlFotos = mercado.getFotos();
              if(urlFotos!=null) {
                  String stringcapa = urlFotos.get(0);
                  if (stringcapa != null) {
                      Glide.with(context)
                              .load(stringcapa)
                              .into(holder.capa);

                  }else{
                      Toast.makeText(context, "erro", Toast.LENGTH_SHORT).show();
                  }
              }

    }

    @Override
    public int getItemCount() {
        return mercados.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titulo;
        TextView legenda;
        TextView categoria;
        CircleImageView capa;



        public MyViewHolder(View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.texttitulo);
            legenda = itemView.findViewById(R.id.textlegenda);
            categoria = itemView.findViewById(R.id.textcategoria);
           capa = itemView.findViewById(R.id.capamercado);
        }
    }
}
