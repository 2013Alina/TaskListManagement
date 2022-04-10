package org.example.entity;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User implements Serializable {
    private final String login;
    private final String password;
    private List<Task> taskList;

    private static final int LOGIN = 0;
    private static final int PASSWORD = 1;
    private static final int TASK_LIST = 2;


    public User(String login, String password) {
        this.login = login;
        this.password = password;
        taskList = new ArrayList<>();
    }

    public User(String login, String password, List<Task> taskList){
        this.login = login;
        this.password = password;
        this.taskList = taskList;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public byte[] toBytes(){
        String userStr = login + "; " + password + "; " + taskListToString() + " \n";
        return userStr.getBytes(StandardCharsets.UTF_8);
    }

    private String taskListToString(){
        StringBuilder str = new StringBuilder();
        for (Task task : taskList) {
            str.append(task.getStringToSave());
        }
        return str.toString();
    }

    public static User parse(String str){
//        String[] parts = str.split(";");
        String[] parts = Arrays.stream(str.split("; "))
                                          .filter(item -> !item.isBlank())
                                          .toArray(String[]::new);
        if(parts.length < 2){
            throw new IllegalArgumentException("User string must consist of at list 2 parts");
        }
        if(parts.length == 2){
            return new User(parts[LOGIN], parts[PASSWORD]);
        }
        if(parts.length == 3){
            return new User(parts[LOGIN], parts[PASSWORD], parseTaskList(parts[TASK_LIST]));
        }
        System.out.println("Непередбачувана ситуація!!!!!");
        return null;
    }

    private static ArrayList<Task> parseTaskList(String taskListStr) {
        ArrayList<Task> tasks = new ArrayList<>();

        String[] tasksStr = Arrays.stream(taskListStr.split("} "))
                .filter(item -> !item.isBlank())
                .toArray(String[]::new);

        for (String task : tasksStr) {
            tasks.add(Task.parse(task));
        }
        return tasks;
    }

    public List<Task> getTaskList(){
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
