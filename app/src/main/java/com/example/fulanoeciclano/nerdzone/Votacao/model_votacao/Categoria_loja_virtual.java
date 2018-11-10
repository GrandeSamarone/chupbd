package com.example.fulanoeciclano.nerdzone.Votacao.model_votacao;

import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

public class Categoria_loja_virtual implements Serializable {
    private String id;
    private String idauthor;
    private String nome;
    private String descricao;
    private String contato;
    private String data;
    private int votos=0;
    private List<String> fotos;

    public Categoria_loja_virtual() {
        DatabaseReference ref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("votacao").child("categorias").child("loja_virtual");
        setId(ref.push().getKey());
    }

    public void Salvar(){
        DatabaseReference anuncioref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("votacao")
                .child("categorias");
        anuncioref.child("loja_virtual")
                .child(getId()).setValue(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<String> getFotos() {
        return fotos;
    }

    public void setFotos(List<String> fotos) {
        this.fotos = fotos;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getIdauthor() {
        return idauthor;
    }

    public void setIdauthor(String idauthor) {
        this.idauthor = idauthor;
    }

    public int getVotos() {
        return votos;
    }

    public void setVotos(int votos) {
        this.votos = votos;
    }
}

