package com.example.fulanoeciclano.nerdzone.Model;


import com.example.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fulanoeciclano on 14/08/2018.
 */

public class Mercado implements Serializable {

    private String idMercado;
    private String autor;
    private String categoria;
    private String estado;
    private String titulo;
    private String endereco;
    private String telefone;
    private String descricao;
    private String fraserapida;
    private String data;
    private List<String> fotos;
    private String icone;
    @JsonIgnore
    private String key;
    public Mercado() {
        DatabaseReference mercadoref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("comercio");
        setIdMercado(mercadoref.push().getKey());
    }



    public void salvar(){
        String idUsuario = ConfiguracaoFirebase.getIdUsuario();
        DatabaseReference anuncioref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("comercio");
        anuncioref.child(getEstado())
                .child(getCategoria())
                .child(getIdMercado()).setValue(this);

      //  salvarAnuncioPublico();
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIdMercado() {
        return idMercado;
    }

    public void setIdMercado(String idMercado) {
        this.idMercado = idMercado;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValues(Mercado mercado) {
        titulo=mercado.titulo;

    }
}
