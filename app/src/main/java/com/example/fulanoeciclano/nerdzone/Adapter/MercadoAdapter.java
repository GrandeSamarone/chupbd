package com.example.fulanoeciclano.nerdzone.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fulanoeciclano.nerdzone.Model.Mercado;
import com.example.fulanoeciclano.nerdzone.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by fulanoeciclano on 29/08/2018.
 */

public class MercadoAdapter extends RecyclerView.Adapter<MercadoAdapter.MyViewHolder> {

    private List<Mercado> mercados;
    private Context context;
    private FirebaseDatabase databases=FirebaseDatabase.getInstance();
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

            if (mercado.getTitulo() != null) {
                holder.titulo.setText(mercado.getTitulo());
            }
            if (mercado.getDescricao() != null) {
                holder.legenda.setText(mercado.getFraserapida());
            }

            if (mercado.getCategoria() != null) {
                holder.categoria.setText(mercado.getCategoria());
            }
            if (mercado.getEstado() != null) {
                holder.estado.setText(mercado.getEstado());
            }

            List<String> urlFotos = mercado.getFotos();
            if (urlFotos != null) {
                String stringcapa = urlFotos.get(0);
                if (stringcapa != null) {
                    Glide.with(context)
                            .load(stringcapa)
                            .into(holder.capa);

                } else {
                    Toast.makeText(context, "erro", Toast.LENGTH_SHORT).show();
                }
            }

/*
        final DatabaseReference ref = databases.getReference("ratingbar").child("comercio")
                .child("")
                .child(mercado.getIdMercado());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    float rating = Float.parseFloat(dataSnapshot.getValue().toString());
                    // float d= (float) ((number*5) /100);
                    holder.rating.setRating(rating);
                         }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        holder.rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) ref.setValue(rating);
            }
        });
*/

        }



    @Override
    public int getItemCount() {
        return mercados.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titulo;
        TextView legenda;
        TextView categoria;
        TextView estado;
        CircleImageView capa;
        RatingBar rating;



        public MyViewHolder(View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.texttitulo);
            legenda = itemView.findViewById(R.id.textlegenda);
            categoria = itemView.findViewById(R.id.textcategoria);
            estado = itemView.findViewById(R.id.textestado);
           capa = itemView.findViewById(R.id.capamercado);
           rating = itemView.findViewById(R.id.rating_mercado);
        }
    }
}
