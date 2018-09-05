package com.example.fulanoeciclano.nerdzone.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fulanoeciclano.nerdzone.Model.Icones;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by fulanoeciclano on 20/05/2018.
 */

public class IconeAdapter   extends RecyclerView.Adapter<IconeAdapter.MyViewHolder> {

    View v;
    Context c;
    List<Icones> icones;
    public static final int SELECAO_ICONE = 34;

    public  IconeAdapter(Context context, List<Icones> Listicones) {
        this.c = context;
        this.icones = Listicones;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_card_icone,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.carmodelo.setRadius(0);
        holder.carmodelo.setCardElevation(0);
        final Icones icone  = icones.get(position);




        if(icone.getUrl() !=null){
            Uri uri = Uri.parse(icone.getUrl());

            DraweeController controllerOne = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setAutoPlayAnimations(true)
                    .build();

            holder.draweeView.setController(controllerOne);
            /* Glide.with(c)
            .load(uri)
            .into(holder.foto);*/


        }else{
            holder.draweeView.setImageResource(R.drawable.carregando);
        }



        /*holder.carmodelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent=new Intent(c, Cadastrar_icon_nome_Activity.class);
                    intent.putExtra("caminho_foto",icones.get(position).getUrl());
                    c.startActivity(intent);
                Toast.makeText(c, "Carregando...", Toast.LENGTH_LONG).show();

            }
        });
*/

    }

    @Override
    public int getItemCount() {
        return icones.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{


        CardView carmodelo;
        SimpleDraweeView draweeView;
        public MyViewHolder(View itemView) {
            super(itemView);
            carmodelo = itemView.findViewById(R.id.modelcard);
            draweeView= (SimpleDraweeView) itemView.findViewById(R.id.drawee_foto);
        }
    }

}
