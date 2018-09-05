package com.example.fulanoeciclano.nerdzone.Topico;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fulanoeciclano.nerdzone.Model.Topico;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class Adapter_Topico extends RecyclerView.Adapter<Adapter_Topico.MyViewHolder> {

   private List<Topico> listatopicos;
    private Context context;

    public Adapter_Topico(List<Topico> topico, Context c){
        this.context=c;
        this.listatopicos = topico;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adaptertopico,parent,false);

        return new MyViewHolder(item);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Topico topico = listatopicos.get(position);

        holder.titulo.setText(topico.getTitulo());
        holder.mensagem.setText(topico.getMensagem());
        holder.autor.setText(topico.getAuthor());
        holder.num_estrela.setText(topico.getStarCount());

        if(topico.getFoto()!=null) {
            Uri uri = Uri.parse(topico.getFoto());
            Log.i("url", String.valueOf(uri));
            DraweeController controllerOne = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setAutoPlayAnimations(true)
                    .build();

            holder.foto_autor.setController(controllerOne);
        }

    }

    @Override
    public int getItemCount() {
        return listatopicos.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView titulo,mensagem,autor,num_estrela;
        private SimpleDraweeView foto_autor;

        public MyViewHolder(View itemView) {
            super(itemView);
        titulo = itemView.findViewById(R.id.titulo_topico);
        mensagem = itemView.findViewById(R.id.mensagem_topico);
        autor  = itemView.findViewById(R.id.topico_autor);
        num_estrela = itemView.findViewById(R.id.topico_num_stars);
        foto_autor = itemView.findViewById(R.id.topico_foto_autor);


        }
    }
}
