package org.example.service;

import org.example.entity.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadWriteSerializeService implements ReadWriteService {
    @Override
    public List<User> read(String fileName) {
        List<User> users = new ArrayList<>();
        try (InputStream in = new FileInputStream(fileName);
             ObjectInput objectInput = new ObjectInputStream(in)) {
            while (in.available() > 0) {
                User user = (User) objectInput.readObject();
                users.add(user);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void write(List<User> userList, String fileName) {
        try (OutputStream out = new FileOutputStream(fileName);
             ObjectOutput objectOutputStream = new ObjectOutputStream(out)) {
            for (User user : userList) {
                objectOutputStream.writeObject(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
