package com.example.fulanoeciclano.nerdzone.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by fulanoeciclano on 20/05/2018.
 */

public class Comentario implements Serializable {
    public String uid;
    public String author;
    public String foto;
    public String text;
    public int totalcomentarios;

    @JsonIgnore
    private String key;

    public Comentario() {
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTotalcomentarios() {
        return totalcomentarios;
    }

    public void setTotalcomentarios(int totalcomentarios) {
        this.totalcomentarios = totalcomentarios;
    }

    public void setValues(Comentario comentario) {
        author=comentario.author;
        foto=comentario.foto;

    }
}
