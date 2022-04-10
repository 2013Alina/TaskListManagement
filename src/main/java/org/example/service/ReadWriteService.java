package org.example.service;

import org.example.entity.User;

import java.util.List;

public interface ReadWriteService {
    List<User> read(String fileName);
    void write(List<User> userList, String fileName);
}
