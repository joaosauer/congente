package com.hackathon.congente.datatype;

public class Ideia {
    public String nome;
    public String problema;
    public User user;

    public boolean isValid() {
        if(user!=null && nome!=null && !nome.isEmpty() && problema!=null && !problema.isEmpty()) {
            return true;
        }
        return false;
    }
}
