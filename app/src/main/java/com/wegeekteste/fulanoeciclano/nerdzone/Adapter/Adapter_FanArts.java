package com.wegeekteste.fulanoeciclano.nerdzone.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.FanArts;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.List;

public class Adapter_FanArts extends RecyclerView.Adapter<Adapter_FanArts.MyViewHolder> {

   private List<FanArts> listarts;
   private Context context;

   public Adapter_FanArts(List<FanArts> arts, Context  context){
       this.listarts=arts;
       this.context=context;

   }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_fanarts, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final FanArts fanArts = listarts.get(position);
        if(fanArts.getArtfoto()!=null) {
            holder.progresso.getIndeterminateDrawable().setColorFilter(0xFF0000FF,
                    android.graphics.PorterDuff.Mode.MULTIPLY);

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.icon_foto_fanarts);
            Glide.with(context)
                    .load(fanArts.getArtfoto())
                    .apply(requestOptions)
                    .into(holder.img);
            holder.progresso.setVisibility(View.GONE);
        }else{

        }


   }

    @Override
    public int getItemCount() {
        return listarts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
       private ImageView img;
       private ProgressBar progresso;


       public MyViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgart);
            progresso = itemView.findViewById(R.id.progressBarfanarts);
        }
    }
}
