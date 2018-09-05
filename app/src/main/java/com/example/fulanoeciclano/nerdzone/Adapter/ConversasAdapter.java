package com.example.fulanoeciclano.nerdzone.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fulanoeciclano.nerdzone.Model.Conversa;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by fulanoeciclano on 12/05/2018.
 */

public class ConversasAdapter extends RecyclerView.Adapter<ConversasAdapter.MyViewHolder> {

private List<Conversa> conversas;
private Context context;

    public ConversasAdapter(List<Conversa> lista, Context c) {
        this.conversas=lista;
        this.context=c;
    }

        public List<Conversa> getConversas(){
            return this.conversas;
        }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View itemlist= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_conversas,parent,false);
       return new MyViewHolder(itemlist);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
    Conversa conversa = conversas.get(position);

    holder.ultimaMensagem.setText(conversa.getUltimaMensagem());


           Usuario usuario= conversa.getUsuarioExibicao();
           if(usuario!=null){
               holder.nome.setText(usuario.getNome());

               if(usuario.getFoto()!=null){
                   Uri uri = Uri.parse(usuario.getFoto());
                   Log.i("foto4",usuario.getFoto());
                   DraweeController controllerOne = Fresco.newDraweeControllerBuilder()
                           .setUri(uri)
                           .setAutoPlayAnimations(true)
                           .build();

                   holder.foto.setController(controllerOne);
               }else{
                   holder.foto.setImageResource(R.drawable.padrao);
               }

       }

    }

    @Override
    public int getItemCount() {
        return conversas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
     SimpleDraweeView foto;
     TextView nome,ultimaMensagem,tempo;
    public MyViewHolder(View itemView) {
        super(itemView);
     foto=itemView.findViewById(R.id.imageViewFotoConversa);

      nome= itemView.findViewById(R.id.textNomeConversa);
      ultimaMensagem = itemView.findViewById(R.id.UltimaMsgConversa);
      tempo = itemView.findViewById(R.id.chattempo);


    }


}


}
