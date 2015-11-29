package com.hackathon.congente.datatype;

public class User {
    public String usuario;
    public String senha;
    public String fb_token;

    public boolean isValid() {
        if(fb_token != null || (usuario != null && !usuario.isEmpty())) {
            return true;
        }
        return false;
    }
}
