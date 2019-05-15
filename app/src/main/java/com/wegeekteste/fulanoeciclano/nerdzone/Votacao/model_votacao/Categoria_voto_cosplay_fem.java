package com.wegeekteste.fulanoeciclano.nerdzone.Votacao.model_votacao;

import com.google.firebase.database.DatabaseReference;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;

public class Categoria_voto_cosplay_fem {

    private Categoria_cosplay_fem categoria_cosplay_fem;
    private int votos=0;

    public Categoria_voto_cosplay_fem() {
    }

    public Categoria_cosplay_fem getCategoria_cosplay_fem() {
        return categoria_cosplay_fem;
    }

    public void setCategoria_cosplay_fem(Categoria_cosplay_fem categoria_cosplay_fem) {
        this.categoria_cosplay_fem = categoria_cosplay_fem;
    }

    public int getVotos() {
        return votos;
    }

    public void setVotos(int votos) {
        this.votos = votos;
    }
    public void SalvarVoto(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference categoriaref=firebaseRef
                .child("votacao")
                .child("categorias")
                .child("cosplay_fem")
                .child(categoria_cosplay_fem.getId())
                .child("votos");

        categoria_cosplay_fem.setVotos(categoria_cosplay_fem.getVotos()+1);
        categoriaref.setValue(getVotos());
    }
}
