package com.example.fulanoeciclano.nerdzone.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fulanoeciclano.nerdzone.Model.Evento;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

/**
 * Created by fulanoeciclano on 17/07/2018.
 */

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.MyviewHolder> {

    private Context context;
    private List<Evento> eventos;

    public  EventoAdapter(List<Evento> listeventos, Context c){

        this.context=c;
        this.eventos=listeventos;
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapterevento,parent,false);

        return new  MyviewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {

       /*
        final DatabaseReference postRef = position;
        // Set click listener for the whole post view
        final String postKey = postRef.getKey();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch EventoDetailActivity
                Intent intent = new Intent(context, EventoDetailActivity.class);
                intent.putExtra(EventoDetailActivity.EXTRA_POST_KEY, postKey);
                context.startActivity(intent);
            }
        });
*/

        final Evento ev = eventos.get(position);


        boolean cabecalho = ev.author.isEmpty();

        holder.eventonome.setText(ev.titulo);

        if(ev.fotoevento!=null){

            Uri uri = Uri.parse(ev.fotoevento);
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setProgressiveRenderingEnabled(true)
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setTapToRetryEnabled(true)
                    .setImageRequest(request)
                    .setOldController(holder.eventocapa.getController())
                    .build();
            /*mSimpleDraweeView.setController(controller);
            DraweeController controllerOne = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setAutoPlayAnimations(true)
                    .build();
*/
            holder.eventocapa.setController(controller);
//            holder.foto.getHierarchy().setRetryImage(R.drawable.emoji_google_1f3ca_1f3fb);

      holder.card.setRadius(9);

        }else {
          /* if (cabecalho) {
               holder.eventocapa.setImageResource(R.drawable.icone_grupo);
               holder.eventonome.setVisibility(View.GONE);
           } else {
               holder.eventocapa.setImageResource(R.drawable.padrao);
           }
           holder.eventocapa.setImageResource(R.drawable.carregando);
       }
       */
        }
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }






    public class MyviewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView eventocapa;
        TextView eventonome;
        LinearLayout eventolayout;
        CardView card;
        public MyviewHolder(View itemView) {
            super(itemView);


            eventocapa = itemView.findViewById(R.id.iconeevento);
            card = itemView.findViewById(R.id.cardevento);
            eventonome = itemView.findViewById(R.id.nomeevento);

        }
    }
}