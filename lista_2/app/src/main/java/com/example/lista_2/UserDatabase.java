package com.example.lista_2;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserDatabase {
    private static final Map<String, String> users = new HashMap<>();

    static {
        users.put("1","1");
        users.put("2","2");
        users.put("3","3");
        users.put("4","4");
        users.put("5","5");
    }

    public boolean addUser(User user) {
        if (users.containsKey(user.getLogin())) {
            return false;
        }
        users.put(user.getLogin(), user.getPassword());
        return true;
    }

    public boolean validateUser(String login, String password) {
        return users.containsKey(login) && Objects.equals(users.get(login), password);
    }
}
