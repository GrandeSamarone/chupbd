package com.example.fulanoeciclano.nerdzone.Model;


import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fulanoeciclano on 14/08/2018.
 */

public class Mercado implements Serializable {

    private String idMercado;
    private String loja;
    private String artista;
    private String titulo;
    private String endereco;
    private String telefone;
    private String descricao;
    private String fraserapida;
    private List<String> fotos;

    public Mercado() {
        DatabaseReference mercadoref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("mercado");
        setIdMercado(mercadoref.push().getKey());
    }



    public void salvar(){
        String idUsuario = ConfiguracaoFirebase.getIdUsuario();
        DatabaseReference anuncioref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("mercado");
        anuncioref.child(idUsuario)
                .child(getLoja())
                .child(getArtista())
                .child(getIdMercado()).setValue(this);

      //  salvarAnuncioPublico();
    }


    public String getIdMercado() {
        return idMercado;
    }

    public void setIdMercado(String idMercado) {
        this.idMercado = idMercado;
    }

    public String getLoja() {
        return loja;
    }

    public void setLoja(String loja) {
        this.loja = loja;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFraserapida() {
        return fraserapida;
    }

    public void setFraserapida(String fraserapida) {
        this.fraserapida = fraserapida;
    }

    public List<String> getFotos() {
        return fotos;
    }

    public void setFotos(List<String> fotos) {
        this.fotos = fotos;
    }
}