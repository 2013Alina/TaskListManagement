package org.example.service;

import org.example.entity.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReadWriteTxtService implements ReadWriteService {

    public List<User> read(String file) {
        ArrayList<User> users = new ArrayList<>();
        try(InputStream in = new FileInputStream(file)){
            String text;
            byte[] buffer = new byte[in.available()];
            int length = in.read(buffer);
            if(length != buffer.length){
                throw new RuntimeException("Information is inconsistent!");
            }

            text = new String(buffer, 0, length, StandardCharsets.UTF_8);
            users = Arrays.stream(text.split("\n")).filter((s) -> (s != null && !s.isBlank()))
                    .map(User::parse).collect(Collectors.toCollection(ArrayList::new));

        }catch (IOException e){
            e.printStackTrace();
        }
        return users;
    }

    public void write(List<User> users, String file) {
        try (OutputStream out = new FileOutputStream(file)) {
            for (User user : users) {
                out.write(user.toBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
