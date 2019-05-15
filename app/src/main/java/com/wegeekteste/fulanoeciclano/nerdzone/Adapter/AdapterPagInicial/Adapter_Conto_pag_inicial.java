package com.wegeekteste.fulanoeciclano.nerdzone.Adapter.AdapterPagInicial;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wegeekteste.fulanoeciclano.nerdzone.Model.Conto;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.List;

public class Adapter_Conto_pag_inicial extends RecyclerView.Adapter<Adapter_Conto_pag_inicial.MyviewHolder> {

    private Context context;
    private List<Conto> conto;

    public Adapter_Conto_pag_inicial(List<Conto> listaconto, Context c){

        this.context=c;
        this.conto=listaconto;
    }

    public List<Conto> getContos(){
        return this.conto;
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapterconto_pag_inicial,parent,false);

        return new MyviewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {

        final Conto contos = conto.get(position);
        holder.contotexto.setText(contos.getMensagem());
        holder.contotitulo.setText(contos.getTitulo());

    }

    @Override
    public int getItemCount() {
        return conto.size();
    }






    public class MyviewHolder extends RecyclerView.ViewHolder {


        TextView contotexto,contotitulo;

        CardView card;
        public MyviewHolder(View itemView) {
            super(itemView);
            contotexto = itemView.findViewById(R.id.Conto_pag_inicial);
            contotitulo = itemView.findViewById(R.id.conto_titulo_pag_inicial);

        }
    }
}