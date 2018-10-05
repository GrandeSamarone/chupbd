package com.example.fulanoeciclano.nerdzone.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fulanoeciclano.nerdzone.Activits.Meus_eventos;
import com.example.fulanoeciclano.nerdzone.Edit.Edit_evento_Activity;
import com.example.fulanoeciclano.nerdzone.Evento.DetalheEvento;
import com.example.fulanoeciclano.nerdzone.Model.Evento;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

;

public class Adapter_Meus_Eventos extends RecyclerView.Adapter<Adapter_Meus_Eventos.MyviewHolder> {

private Context context;
private List<Evento> eventos;

public Adapter_Meus_Eventos(List<Evento> listeventos, Context c){

        this.context=c;
        this.eventos=listeventos;
        }

@Override
public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_meus_evento,parent,false);

        return new  MyviewHolder(itemLista);
        }

@Override
public void onBindViewHolder(MyviewHolder holder, int position) {

final Evento ev = eventos.get(position);
        holder.eventonome.setText(ev.titulo);

        if(ev.capaevento!=null){

        Uri uri = Uri.parse(ev.capaevento);
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
        }
        holder.linerclick.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Intent it = new Intent(context, DetalheEvento.class);
        it.putExtra("id_do_evento",ev.getUid());
        it.putExtra("UR_do_evento",ev.getEstado());
        context.startActivity(it);
        }
        });

       holder.edit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent it = new Intent(context, Edit_evento_Activity.class);
               it.putExtra("id_do_evento",ev.getUid());
               it.putExtra("UR_do_evento",ev.getEstado());
               context.startActivity(it);
           }
       });

       holder.deletar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {




               AlertDialog.Builder msgbox = new AlertDialog.Builder(context);
               //configurando o titulo
               msgbox.setTitle("Excluir");
               // configurando a mensagem
               msgbox.setMessage("Deseja Realmente excluir o evento "+ev.getTitulo()+" ?");
               // Botao negativo

               msgbox.setPositiveButton("Sim",
                       new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int wich) {
                               ev.remover();
                               Intent it = new Intent(context, Meus_eventos.class);
                               context.startActivity(it);
                               Toast.makeText(context, "Deletado com Sucesso!", Toast.LENGTH_LONG).show();
                           }

                       });


               msgbox.setNegativeButton("Não",
                       new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int wich) {

                           }
                       });
               msgbox.show();
           }
       });
}

@Override
public int getItemCount() {
        return eventos.size();
        }






public class MyviewHolder extends RecyclerView.ViewHolder {

    private SimpleDraweeView eventocapa;
    private TextView eventonome,edit,deletar;
    private LinearLayout eventolayout;

    private LinearLayout linerclick;
    public MyviewHolder(View itemView) {
        super(itemView);


        eventocapa = itemView.findViewById(R.id.iconeevento);
        linerclick = itemView.findViewById(R.id.linerclick);
        eventonome = itemView.findViewById(R.id.nomeevento);
        edit = itemView.findViewById(R.id.editarevento);
        deletar = itemView.findViewById(R.id.excluirevento);

    }
}
}