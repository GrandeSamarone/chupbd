package com.example.fulanoeciclano.nerdzone.Adapter;

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

import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Evento.DetalheEvento;
import com.example.fulanoeciclano.nerdzone.Helper.CircleProgressDrawable;
import com.example.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.example.fulanoeciclano.nerdzone.Model.Evento;
import com.example.fulanoeciclano.nerdzone.Model.EventoLike;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.varunest.sparkbutton.SparkEventListener;

import java.util.List;

/**
 * Created by fulanoeciclano on 01/09/2018.
 */

public class Evento_Adapter extends RecyclerView.Adapter<Evento_Adapter.MyViewHolder> {

    private List<Evento> listaevento;
    private Context context;
    private DatabaseReference mDatabase;
    Usuario usuariologado = UsuarioFirebase.getDadosUsuarioLogado();

    public Evento_Adapter(List<Evento> eventos, Context c){
        this.context =c;
        this.listaevento=eventos;
    }

    public List<Evento> getevento(){
        return this.listaevento;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evento,parent,false);

        return new Evento_Adapter.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Evento evento = listaevento.get(position);
        holder.numcurtidasEvento.setText(String.valueOf(evento.getCurtirCount()));
        holder.titulo.setText(evento.getTitulo());
        holder.numcurtidasEvento.setText(String.valueOf(evento.getCurtirCount()));
        holder.subpostagem.setText(evento.getSubtitulo());

        if(evento.getCapaevento()!=null) {
            Uri uri = Uri.parse(evento.getCapaevento());
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setLocalThumbnailPreviewsEnabled(true)
                    .setProgressiveRenderingEnabled(true)
                    .build();

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .build();
            holder.imgevento.setController(controller);
            GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
            GenericDraweeHierarchy hierarchy = builder
                    .setProgressBarImage(new CircleProgressDrawable())
                    //  .setPlaceholderImage(context.getResources().getDrawable(R.drawable.carregando))
                    .build();
            holder.imgevento.setHierarchy(hierarchy);
        }

        holder.imgevento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, DetalheEvento.class);
                it.putExtra("id_do_evento",evento.getUid());
                it.putExtra("UR_do_evento",evento.getEstado());
                Log.i("asdawra",evento.getUid()+evento.getEstado());
                  context.startActivity(it);
            }
        });

      DatabaseReference eventoscurtidas= ConfiguracaoFirebase.getFirebaseDatabase()
              .child("evento-likes")
              .child(evento.getUid());
       eventoscurtidas.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
              int QtdLikes = 0;
              if(dataSnapshot.hasChild("qtdlikes")){
                  EventoLike eventoLike = dataSnapshot.getValue(EventoLike.class);
                  QtdLikes = eventoLike.getQtdlikes();
              }
           Log.i("sdsd",usuariologado.getId());
               //Verifica se já foi clicado
               if( dataSnapshot.hasChild( usuariologado.getId() ) ){
                   holder.botaocurtir.setChecked(true);
               }else {
                   holder.botaocurtir.setChecked(false);
               }

              //Montar objeto postagem curtida
               EventoLike like = new EventoLike();
              like.setEvento(evento);
              like.setUsuario(usuariologado);
              like.setQtdlikes(QtdLikes);

               //adicionar evento para curtir foto
              holder.botaocurtir.setEventListener(new SparkEventListener() {
                  @Override
                  public void onEvent(ImageView button, boolean buttonState) {
                     if(buttonState){
                        like.Salvar();
                        holder.numcurtidasEvento.setText(String.valueOf(like.getQtdlikes()));
                     }else{
                         like.removerlike();
                         holder.numcurtidasEvento.setText(String.valueOf(like.getQtdlikes()));
                     }
                  }
                  @Override
                  public void onEventAnimationEnd(ImageView button, boolean buttonState) {
                  }
                  @Override
                  public void onEventAnimationStart(ImageView button, boolean buttonState) {
                  }
              });

           }
           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });

}

    @Override
    public int getItemCount() {
        return listaevento.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView titulo;
        private TextView subpostagem;
        private com.varunest.sparkbutton.SparkButton botaocurtir;
        private SimpleDraweeView imgevento;
        private CardView card;
        private TextView numcurtidasEvento;
        public MyViewHolder(View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.tituloeventopostagem);
            subpostagem = itemView.findViewById(R.id.subpostagem);
            card = itemView.findViewById(R.id.cardclickevento);
            imgevento = itemView.findViewById(R.id.imgeventopostagem);
            numcurtidasEvento = itemView.findViewById(R.id.quantCurtida);
            botaocurtir = itemView.findViewById(R.id.botaocurtirevento);
        }


    }
}
