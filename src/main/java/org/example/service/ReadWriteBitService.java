package org.example.service;

import org.example.entity.Task;
import org.example.entity.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReadWriteBitService implements ReadWriteService {
    @Override
    public List<User> read(String fileName) {
        List<User> users = new ArrayList<>();
        try (InputStream in = new FileInputStream(fileName);
             DataInputStream din = new DataInputStream(in)) {
            while (in.available() > 0) {
                String login = din.readUTF();
                String password = din.readUTF();
                int tasksNumber = din.readInt();
                List<Task> taskList = new ArrayList<>();
                for (int i = 0; i < tasksNumber; i++) {
                    String name = din.readUTF();
                    int importance = din.readInt();
                    Date date = new Date(din.readLong());
                    boolean isDone = din.readBoolean();
                    Task task = new Task(name, importance, date, isDone);
                    taskList.add(task);
                }
                User user = new User(login, password, taskList);
                users.add(user);
            }
        } catch (IOException ex) {
            System.out.println("No file for tasks created yet");
        }

        return users;
    }

    @Override
    public void write(List<User> userList, String fileName) {
        try (OutputStream out = new FileOutputStream(fileName);
             DataOutputStream dout = new DataOutputStream(out)) {
            for (User user : userList) {
                dout.writeUTF(user.getLogin());
                dout.writeUTF(user.getPassword());

                List<Task> tasks = user.getTaskList();
                dout.writeInt(tasks.size());
                for (Task task : tasks) {
                    dout.writeUTF(task.getName());
                    dout.writeInt(task.getImportance());
                    dout.writeLong(task.getDeadline().getTime());
                    dout.writeBoolean(task.isDone());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
