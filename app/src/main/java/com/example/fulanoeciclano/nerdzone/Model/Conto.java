package com.example.fulanoeciclano.nerdzone.Model;

import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Conto   implements Serializable {

    private String uid;
    private String idauthor;
    private String titulo;
    private String mensagem;
    private String data;
    private int likecount = 0;
    private int quantcolecao=0;


    public Conto() {
        DatabaseReference eventoref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("conto");
        setUid(eventoref.push().getKey());  }

    public  void SalvarConto(){
        String idUsuario = ConfiguracaoFirebase.getIdUsuario();
        DatabaseReference anuncioref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("meusconto");
        anuncioref.child(idUsuario)
                .child(getUid()).setValue(this);

        salvarContoPublico();
    }

    public void salvarContoPublico(){
        DatabaseReference anuncioref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("conto");
        anuncioref.child(getUid()).setValue(this);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIdauthor() {
        return idauthor;
    }

    public void setIdauthor(String idauthor) {
        this.idauthor = idauthor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getLikecount() {
        return likecount;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }

    public int getQuantcolecao() {
        return quantcolecao;
    }

    public void setQuantcolecao(int quantcolecao) {
        this.quantcolecao = quantcolecao;
    }
}
