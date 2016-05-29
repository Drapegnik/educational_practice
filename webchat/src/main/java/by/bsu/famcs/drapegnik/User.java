package by.bsu.famcs.drapegnik;

import java.util.UUID;

/**
 * Created by Drapegnik on 02.05.16.
 */
public class User {
    private String name;
    private int password;
    private String id;


    public User(String name, String password) {
        this.name = name;
        this.password = password.hashCode();
        this.id = UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password.hashCode();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        User user = (User) obj;
        return name.equals(user.name) && password == user.password;
    }

    @Override
    public String toString() {
        return "{" +
                "'name':'" + name + '\'' +
                ", 'password':'" + password + '\'' +
                ", 'id':'" + id + '\'' +
                '}';
    }
}
