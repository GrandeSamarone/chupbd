package com.wegeekteste.fulanoeciclano.nerdzone.Model;

import com.google.firebase.database.DatabaseReference;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;

import java.util.HashMap;

public class FanArtsLike {

    private FanArts fanArts;
    private Usuario usuario;
    private int qtdlikes=0;

    public FanArtsLike(){

    }

    public FanArts getFanArts() {
        return fanArts;
    }


    public void Salvar(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

        HashMap<String,Object> dadosusuario = new HashMap<>();
        dadosusuario.put("nomeUsuario",usuario.getNome());
        dadosusuario.put("foto",usuario.getFoto());

        DatabaseReference pLikeRef=firebaseRef
                .child("fanarts-likes")
                .child(fanArts.getId())
                .child(usuario.getId());
        pLikeRef.setValue(dadosusuario);

        //Atualizar quantidade de like
        atualizarQtd(1);

    }

    private void atualizarQtd(int valor){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference pLikeRef=firebaseRef
                .child("fanarts-likes")
                .child(fanArts.getId())
                .child("qtdlikes");

        setQtdlikes(getQtdlikes()+valor);
        pLikeRef.setValue(getQtdlikes());
        atualizarQtd_fanart();
        atualizarQtd_MinhasArts();

    }
    public   void removerlike(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference pLikeRef=firebaseRef
                .child("fanarts-likes")
                .child(fanArts.getId())
                .child(usuario.getId());
        pLikeRef.removeValue();
        //Atualizar quantidade de like
        atualizarQtd(-1);
        atualizarQtd_fanart();
        atualizarQtd_MinhasArts();


    }


    private void atualizarQtd_fanart(){
        DatabaseReference firebaseRefs = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference pLikeQuantRef=firebaseRefs
                .child("arts")
                .child(fanArts.getId())
                .child("likecount");

        pLikeQuantRef.setValue(getQtdlikes());
    }
    private void atualizarQtd_MinhasArts(){
        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        DatabaseReference firebaseRefs = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference pLikeQuantRef=firebaseRefs
                .child("minhasarts")
                .child(fanArts.getIdauthor())
                .child(fanArts.getId())
                .child("likecount");

        pLikeQuantRef.setValue(getQtdlikes());
    }



    public void setFanArts(FanArts fanArts) {
        this.fanArts = fanArts;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getQtdlikes() {
        return qtdlikes;
    }

    public void setQtdlikes(int qtdlikes) {
        this.qtdlikes = qtdlikes;
    }
}
