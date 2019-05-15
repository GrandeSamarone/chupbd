package com.wegeekteste.fulanoeciclano.nerdzone.Model;

import com.google.firebase.database.DatabaseReference;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;

import java.util.HashMap;

public class FanArtsColecao {

    private FanArts fanArts;
    private Usuario usuario;
    private Boolean adicionado;
    private int qtdadd=0;

    public FanArtsColecao(){

    }
    public void Salvar(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        HashMap<String,Object> dadosusuario = new HashMap<>();
        dadosusuario.put("id",usuario.getId());

        DatabaseReference pLikeRef=firebaseRef
                .child("fanarts-colecao")
                .child(fanArts.getId())
                .child(usuario.getId());
        pLikeRef.setValue(dadosusuario);

        //Atualizar quantidade de like
        atualizarQtd(1);


    }


    private void atualizarQtd(int valor){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference pLikeRef=firebaseRef
                .child("fanarts-colecao")
                .child(fanArts.getId())
                .child("qtdadd");

        setQtdadd(getQtdadd()+valor);
        pLikeRef.setValue(getQtdadd());
        //  atualizarQtd_Colecao();
        atualizarQtd_MeusColecoes();

    }
    private void atualizarQtd_MeusColecoes(){
        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        DatabaseReference firebaseRefs = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference pLikeQuantRef=firebaseRefs
                .child("minhasarts")
                .child(fanArts.getIdauthor())
                .child(fanArts.getId())
                .child("quantcolecao");

        pLikeQuantRef.setValue(getQtdadd());
    }

    public   void removerfanarts(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference pLikeRef=firebaseRef
                .child("fanarts-colecao")
                .child(fanArts.getId())
                .child(usuario.getId());
        pLikeRef.removeValue();
        //Atualizar quantidade de like
        atualizarQtd(-1);
        remover_adicionei_colecao();
        atualizarQtd_MeusColecoes();
        atualizarQtd_MeusColecoes();
    }


    public   void remover_adicionei_colecao(){
        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference pLikeRef=firebaseRef
                .child("adicionei-fanart")
                .child(identificadorUsuario)
                .child(fanArts.getId());
        pLikeRef.removeValue();

    }


    public FanArts getFanArts() {
        return fanArts;
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

    public Boolean getAdicionado() {
        return adicionado;
    }

    public void setAdicionado(Boolean adicionado) {
        this.adicionado = adicionado;
    }

    public int getQtdadd() {
        return qtdadd;
    }

    public void setQtdadd(int qtdadd) {
        this.qtdadd = qtdadd;
    }
}
