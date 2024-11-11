package com.example.lista_2;

import java.util.HashMap;
import java.util.Map;

public class UserDatabase {
    private static final Map<String, User> users = new HashMap<>();

    static {
        users.put("1", new User("1", "1"));
        users.put("2", new User("2", "2"));
        users.put("3", new User("3", "3"));
        users.put("4", new User("4", "4"));
        users.put("5", new User("5", "5"));
    }

    public boolean addUser(User user) {
        if (users.containsKey(user.getLogin())) {
            return false;
        }
        users.put(user.getLogin(), user);
        return true;
    }

    public boolean userExists(String login) {
        return users.containsKey(login);
    }

    public boolean validateUser(String login, String password) {
        User user = users.get(login);
        return user != null && user.getPassword().equals(password);
    }
}
