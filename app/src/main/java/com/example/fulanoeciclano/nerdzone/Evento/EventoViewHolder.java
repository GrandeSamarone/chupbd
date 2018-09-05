package com.example.fulanoeciclano.nerdzone.Evento;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fulanoeciclano.nerdzone.Model.Evento;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by fulanoeciclano on 20/05/2018.
 */

public class EventoViewHolder extends RecyclerView.ViewHolder {

    public TextView tituloEventoView;
    public TextView subtituloEventoView;
    public TextView autorEventoView;
    public SimpleDraweeView fotoEventoView;
    public ImageView curtirEventoView;
    public TextView numcurtidasEventoView;
    public TextView datahoje;
    public TextView mensagemEventoView;
    public TextView datainicioView;
    public TextView datafimView;
    public TextView localidadeView;
    private DatabaseReference mDatabase;
    private DatabaseReference eventoref;


    public EventoViewHolder(View itemView) {
        super(itemView);

       tituloEventoView = itemView.findViewById(R.id.tituloeventopostagem);
        subtituloEventoView= itemView.findViewById(R.id.subpostagem);

        datahoje= itemView.findViewById(R.id.data_topico_dia);
     datafimView = itemView.findViewById(R.id.data_topico_fim);
     datainicioView = itemView.findViewById(R.id.data_topico_inicio);
       // autorEventoView = itemView.findViewById(R.id.authoevento);
        fotoEventoView = itemView.findViewById(R.id.imgeventopostagem);
        curtirEventoView = itemView.findViewById(R.id.botaocurtirevento);
        numcurtidasEventoView = itemView.findViewById(R.id.quantCurtida);
       // mensagemEventoView = itemView.findViewById(R.id.texteventopostagem);
    }

    public void bindToPost(Evento evento, View.OnClickListener starClickListener) {
       tituloEventoView.setText(evento.titulo);
       subtituloEventoView.setText(evento.subtitulo);

      //  autorEventoView.setText(evento.author);

        final Calendar calendartempo = Calendar.getInstance();
        SimpleDateFormat DateFormat = new SimpleDateFormat("MM'/'dd'/'y");// MM'/'dd'/'y;
        String dataatual = DateFormat.format(calendartempo.getTime());

        datainicioView.setText(evento.datainicio);
        datafimView.setText(evento.datafim);

        if(dataatual.equals(datainicioView)){
           datahoje.setText("É HOJE MEU PATRÃO!");
        }else{
            datahoje.setText("");
        }


        numcurtidasEventoView.setText(String.valueOf(evento.curtirCount));
       // mensagemEventoView.setText(evento.mensagem);




        if(evento.fotoevento!=null) {
            Uri uri = Uri.parse(evento.fotoevento);
            Log.i("url", String.valueOf(uri));
            DraweeController controllerOne = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setAutoPlayAnimations(true)
                    .build();

           fotoEventoView.setController(controllerOne);

        }


        //eventoref= mDatabase.child("evento");
        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM''dd''yyyy");
        String tempoMensagem = simpleDateFormat.format(calendar.getTime());
        String tempoatual = (tempoMensagem);
        String temposalvo = (evento.datafim);
        Log.i("tempos",tempoatual.toString()+temposalvo.toString());

/*
        if(tempoatual>temposalvo){
            eventoref.removeValue();
        }
*/
        curtirEventoView.setOnClickListener(starClickListener);
    }
}