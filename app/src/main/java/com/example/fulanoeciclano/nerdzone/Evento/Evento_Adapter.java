package com.example.fulanoeciclano.nerdzone.Evento;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fulanoeciclano.nerdzone.Model.Evento;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.List;

import static com.example.fulanoeciclano.nerdzone.Helper.App.getUid;

/**
 * Created by fulanoeciclano on 01/09/2018.
 */

public class Evento_Adapter extends RecyclerView.Adapter<Evento_Adapter.MyViewHolder> {

    private List<Evento> listaevento;
    private Context context;

    private DatabaseReference mDatabase;

    public Evento_Adapter(List<Evento> eventos, Context c){
        this.context =c;
        this.listaevento=eventos;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evento,parent,false);

        return new Evento_Adapter.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Evento evento = listaevento.get(position);
        holder.titulo.setText(evento.getTitulo());
        holder.numcurtidasEvento.setText(String.valueOf(evento.curtirCount));
        holder.subpostagem.setText(evento.getSubtitulo());
        holder.datafim.setText(evento.getDatafim());
        holder.datainicio.setText(evento.getDatainicio());

        if(evento.fotoevento!=null) {
            Uri uri = Uri.parse(evento.fotoevento);
            Log.i("url", String.valueOf(uri));
            DraweeController controllerOne = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setAutoPlayAnimations(true)
                    .build();

            holder.imgevento.setController(controllerOne);
        }

        if (evento.curtida.containsKey(getUid())) {
            holder.botaocurtir.setImageResource(R.drawable.likecolorido);
        } else {
            holder.botaocurtir.setImageResource(R.drawable.likepreto);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();

        holder.bindToPost(evento, new View.OnClickListener() {

            @Override
            public void onClick(View starView) {
                // Need to write to both places the post is stored
                DatabaseReference globalPostRef = mDatabase.child("evento").child(evento.getUid());
                DatabaseReference userPostRef = mDatabase.child("usuario-evento")
                        .child(evento.uid).child(evento.getUid());

                // Run two transactions
                onStarClicked(globalPostRef);
                onStarClicked(userPostRef);
            }
        });


        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent it = new Intent(context,EventoDetailActivity.class) ;
                it.putExtra("titulo",listaevento.get(position).getTitulo());
                it.putExtra("subtitulo",listaevento.get(position).getSubtitulo());
                it.putExtra("mensagem",listaevento.get(position).getMensagem());
                it.putExtra("foto",listaevento.get(position).getFotoevento());
                it.putExtra("autor",listaevento.get(position).getAuthor());
                it.putExtra("datainicio",listaevento.get(position).getDatainicio());
                it.putExtra("datafim",listaevento.get(position).getDatafim());
                context.startActivity(it);
            }
        });

    }



    // [START post_stars_transaction]
    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Evento p = mutableData.getValue(Evento.class);

                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.curtida.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    p.curtirCount = p.curtirCount - 1;
                    p.curtida.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    p.curtirCount = p.curtirCount + 1;
                    p.curtida.put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {

                // Transaction completed
               // Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }
    @Override
    public int getItemCount() {
        return listaevento.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView titulo;
        private TextView datainicio;
        private TextView datafim;
        private TextView subpostagem;
        private ImageView botaocurtir;
        private SimpleDraweeView imgevento;
        private CardView card;
        private TextView numcurtidasEvento;
        public MyViewHolder(View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.tituloeventopostagem);
            datainicio = itemView.findViewById(R.id.data_evento_inicio);
            datafim = itemView.findViewById(R.id.data_evento_fim);
            subpostagem = itemView.findViewById(R.id.subpostagem);
            card = itemView.findViewById(R.id.cardclickevento);
            imgevento = itemView.findViewById(R.id.imgeventopostagem);
            numcurtidasEvento = itemView.findViewById(R.id.quantCurtida);
            botaocurtir = itemView.findViewById(R.id.botaocurtirevento);
        }

        public void bindToPost(Evento evento, View.OnClickListener starClickListener) {
           botaocurtir.setOnClickListener(starClickListener);
        }

    }
}
