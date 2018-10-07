package com.example.fulanoeciclano.nerdzone.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.nerdzone.Model.Topico;
import com.example.fulanoeciclano.nerdzone.Model.Usuario;
import com.example.fulanoeciclano.nerdzone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_Topico extends RecyclerView.Adapter<Adapter_Topico.MyViewHolder> {

   private List<Topico> listatopicos;
    private Context context;

    public Adapter_Topico(List<Topico> topico, Context c){
        this.context=c;
        this.listatopicos = topico;
    }
    public List<Topico> getTopicos(){
        return this.listatopicos;
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


        DatabaseReference eventoscurtidas= ConfiguracaoFirebase.getFirebaseDatabase()
                .child("usuarios")
                .child(topico.getIdauthor());
        eventoscurtidas.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    Usuario  user = dataSnapshot.getValue(Usuario.class);
                    holder.autor.setText(user.getNome());

                    Glide.with(context)
                            .load(user.getFoto())
                            .into(holder.foto_autor );

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return listatopicos.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView titulo,mensagem,autor,num_estrela;
        private CircleImageView foto_autor;

        public MyViewHolder(View itemView) {
            super(itemView);
        titulo = itemView.findViewById(R.id.topico_titulo);
        mensagem = itemView.findViewById(R.id.topico_mensagem);
        autor  = itemView.findViewById(R.id.topico_autor);
        foto_autor = itemView.findViewById(R.id.topico_foto_autor);


        }
    }
}
