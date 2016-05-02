package by.bsu.famcs.drapegnik;

import javax.jws.soap.SOAPBinding;

/**
 * Created by Drapegnik on 02.05.16.
 */
public class User {
    private String name;
    private int password;

    public User(String name, String password) {
        this.name = name;
        this.password = password.hashCode();
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
                '}';
    }
}
