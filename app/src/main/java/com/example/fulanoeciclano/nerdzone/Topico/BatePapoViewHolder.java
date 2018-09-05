package com.example.fulanoeciclano.nerdzone.Topico;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fulanoeciclano.nerdzone.Model.Topico;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by fulanoeciclano on 20/05/2018.
 */

public class BatePapoViewHolder extends RecyclerView.ViewHolder {

    public TextView tituloView;
    public TextView autorView;
    public SimpleDraweeView fotoView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView totalcomentario;
    public TextView numComentario;
    public TextView mensagemView;


    public BatePapoViewHolder(View itemView) {
        super(itemView);

       tituloView = itemView.findViewById(R.id.BatePapo_titulo);
        autorView = itemView.findViewById(R.id.BatePapo_autor);
        fotoView = itemView.findViewById(R.id.BatePapo_foto_autor);
        starView = itemView.findViewById(R.id.star);
        numStarsView = itemView.findViewById(R.id.Batepapo_num_stars);
        mensagemView = itemView.findViewById(R.id.BatePapo_mensagem);
    }

    public void bindToPost(Topico topico, View.OnClickListener starClickListener) {
       tituloView.setText(topico.titulo);
        autorView.setText(topico.author);
        numStarsView.setText(String.valueOf(topico.starCount));
        mensagemView.setText(topico.mensagem);


        if(topico.foto!=null) {
            Uri uri = Uri.parse(topico.foto);
            Log.i("url", String.valueOf(uri));
            DraweeController controllerOne = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setAutoPlayAnimations(true)
                    .build();

           fotoView.setController(controllerOne);

        }




        starView.setOnClickListener(starClickListener);
    }
}