package com.example.fulanoeciclano.nerdzone.Model;

import com.example.fulanoeciclano.geek.Config.ConfiguracaoFirebase;
import com.example.fulanoeciclano.geek.helper.UsuarioFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fulanoeciclano on 21/05/2018.
 */

public class Usuario implements Serializable {

    private String id;
    private String tipoconta;
    private String nome;
    private String foto;
    private String tiposuario;

    public Usuario() {
    }
    public void salvar(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuario = firebaseRef.child("usuarios").child(getId());
        usuario.setValue(this);
    }
    public void atualizar(){
        String identificadorUSuario = UsuarioFirebase.getIdentificadorUsuario();
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();

        DatabaseReference usuariosref = database.child("usuarios")
                .child(identificadorUSuario);

        Map<String,Object> valoresUsuario = ConverterparaMap();
        usuariosref.updateChildren(valoresUsuario);
    }

    @Exclude
    public Map<String,Object> ConverterparaMap(){
        HashMap<String,Object> usuarioMap = new HashMap<>();
        usuarioMap.put("nome",getNome());
        usuarioMap.put("foto",getFoto());



        return usuarioMap;
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getTiposuario() {
        return tiposuario;
    }

    public void setTiposuario(String tiposuario) {
        this.tiposuario = tiposuario;
    }

    public String getTipoconta() {
        return tipoconta;
    }

    public void setTipoconta(String tipoconta) {
        this.tipoconta = tipoconta;
    }
}
