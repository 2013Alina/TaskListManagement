package org.example.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class Task implements Serializable {
    private String name;
    private int importance;
    private Date deadline;
    private boolean isDone = false;

    public Task(String name, int importance, Date deadline, boolean isDone) {
        this.name = name;
        this.importance = importance;
        this.deadline = deadline;
        this.isDone = isDone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return "Задача: {" +
                "Назва задачі: '" + name + '\'' +
                ", Важливість: " + importance +
                ", Термін виконання '" + sdf.format(deadline.getTime()) +
                ", Виконана: '" + isDone + '\'' +
                '}';
    }

    public String getStringToSave(){
        return name + ": " + importance + ": " + deadline.getTime() + ": " + isDone + "} ";
    }

    public static Task parse(String str){
        String[] parts = Arrays.stream(str.split(": "))
                               .filter(item->!item.isBlank())
                               .toArray(String[]::new);

        String name = parts[0];
        int imp = (int)Integer.parseInt(parts[1]);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String dt = parts[2];
        Date date = new Date(Long.parseLong(dt));
        boolean done = Boolean.parseBoolean(parts[3]);

        return new Task(name, imp, date, done);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return importance == task.importance && isDone == task.isDone && name.equals(task.name) && Objects.equals(deadline, task.deadline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, importance, deadline, isDone);
    }
}


