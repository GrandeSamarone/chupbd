package com.example.fulanoeciclano.nerdzone.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by fulanoeciclano on 20/05/2018.
 */

public class Comentario {
    public String uid;
    public String author;
    public String foto;
    public String text;
    public int totalcomentarios;

    @JsonIgnore
    private String key;

    public Comentario() {
    }

    public Comentario(String uid, String author, String text, String foto, int comentariostotal) {
        this.uid = uid;
        this.author = author;
        this.text = text;
        this.foto=foto;
        this.totalcomentarios=comentariostotal;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValues(Comentario comentario) {
        author=comentario.author;
        foto=comentario.foto;

    }
}
