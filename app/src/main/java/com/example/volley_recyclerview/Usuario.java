package com.example.volley_recyclerview;

public class Usuario {
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Usuario(String nombre, String mail, String foto) {
        this.nombre = nombre;
        this.mail = mail;
        this.foto = foto;
    }

    private String nombre,mail,foto;

}
