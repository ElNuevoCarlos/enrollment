package model;

public class User {
    private String name;
    private String password;
    
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getUsername() {
        return this.name;
    }
    public String getPassword() {
        return this.password;
    }
}

