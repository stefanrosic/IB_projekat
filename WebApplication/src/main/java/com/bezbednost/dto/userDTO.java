package com.bezbednost.dto;

import com.bezbednost.model.User;

import java.io.Serializable;

public class userDTO implements Serializable {
    private int id;
    private String email;
    private String password;
    private String certificate;
    private boolean active;


    public userDTO() {

    }


    public userDTO(int id, String email, String password, String certificate, boolean active) {
        super();
        this.id = id;
        this.email = email;
        this.password = password;
        this.certificate = certificate;
        this.active = active;
    }

    public userDTO(User user) {
        this(user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getCertificate(),
                user.isActive());
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getCertificate() {
        return certificate;
    }


    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }


    public boolean isActive() {
        return active;
    }


    public void setActive(boolean active) {
        this.active = active;
    }
}
