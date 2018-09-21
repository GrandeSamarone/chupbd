package com.example.fulanoeciclano.nerdzone.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fulanoeciclano.nerdzone.Model.Comentario;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vanniktech.emoji.EmojiTextView;

import java.util.List;

public class Adapter_comentario extends RecyclerView.Adapter<Adapter_comentario.MyViewHolder> {

    private List<Comentario> comentarios;
    private Context context;

    public Adapter_comentario(List<Comentario> comentario,Context c){
        this.comentarios=comentario;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_commentario,parent,false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Comentario comentario = comentarios.get(position);
        holder.nome.setText(comentario.getAuthor());
        Log.i("asas",comentario.getAuthor());
        holder.mensagem.setText(comentario.getText());

        Uri uri = Uri.parse(comentario.getFoto());
        DraweeController controllerOne = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();

        holder.foto.setController(controllerOne);

    }

    @Override
    public int getItemCount() {
        return comentarios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView nome;
        private SimpleDraweeView foto;
        private EmojiTextView mensagem;

        public MyViewHolder(View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.commentario_author);
            mensagem = itemView.findViewById(R.id.comentario_mensagem);
            foto = itemView.findViewById(R.id.commentario_foto);
        }
    }
}
