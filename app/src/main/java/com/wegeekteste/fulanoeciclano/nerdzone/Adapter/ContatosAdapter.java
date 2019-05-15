package com.wegeekteste.fulanoeciclano.nerdzone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.wegeekteste.fulanoeciclano.nerdzone.Activits.MinhaConta;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Usuario;
import com.wegeekteste.fulanoeciclano.nerdzone.PerfilAmigos.Perfil;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by fulanoeciclano on 06/05/2018.
 */

public class ContatosAdapter extends RecyclerView.Adapter<ContatosAdapter.MyViewHolder> {

     private List<Usuario> contatos;
     private Context context;
    private ChildEventListener ChildEventListenerperfil;
    private DatabaseReference database;

    public ContatosAdapter(List<Usuario> listaContatos, Context c) {
       this.contatos=listaContatos;
       this.context= c;
    }

    public List<Usuario> getContatos(){
        return this.contatos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_contatos,parent,false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Usuario usuario = contatos.get(position);



        database = ConfiguracaoFirebase.getDatabase().getReference().child("usuarios");
        ChildEventListenerperfil=database.orderByChild("id").equalTo(usuario.getId())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Usuario perfil = dataSnapshot.getValue(Usuario.class );
                        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
                        assert perfil != null;

                        holder.nome.setText(perfil.getNome());
                        holder.email.setText(perfil.getFrase());

                        if(perfil.getFoto()!=null){
                            Uri uri = Uri.parse(perfil.getFoto());
                            Glide.with(context)
                                    .load(uri)
                                    .into( holder.foto );
                        }else{
                            holder.foto.setImageResource(R.drawable.fundo_user);
                        }


                        if(!perfil.getId().equals(identificadorUsuario)) {
                            holder.click.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent it = new Intent(context, Perfil.class);
                                    it.putExtra("id", perfil.getId());
                                    context.startActivity(it);
                                }
                            });
                        }else{
                            holder.click.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent it = new Intent(context, MinhaConta.class);
                                    context.startActivity(it);
                                }
                            });
                        }
                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    }
                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                    }
                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




    }

    @Override
    public int getItemCount() {
        return contatos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

     private CircleImageView foto;
     private TextView nome,email;
     private LinearLayout click;

        public MyViewHolder(View itemView) {
            super(itemView);
           foto = itemView.findViewById(R.id.imageViewFotoContato);
           nome= itemView.findViewById(R.id.textNomeContato);
           email= itemView.findViewById(R.id.textEmailContato);
            click = itemView.findViewById(R.id.contatoclick);
        }
    }
}
