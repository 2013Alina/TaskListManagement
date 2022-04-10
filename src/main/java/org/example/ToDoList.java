package org.example;


import org.example.comparator.DateComparator;
import org.example.comparator.ImportanceAndDateComparator;
import org.example.entity.Task;
import org.example.entity.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ToDoList {
    List<Task> actualActionTasks = new ArrayList<>();
    List<Task> archivedActionTasks = new ArrayList<>();

    public ToDoList(User user) {
        loadTasks(user);
    }

    private void loadTasks(User user) {
        List<Task> allTasks = user.getTaskList();
        for (Task task : allTasks) {
            if (task.isDone()) {
                archivedActionTasks.add(task);
            } else {
                actualActionTasks.add(task);
            }
        }
    }

    public void showListOfPendingTasks() {
        if (actualActionTasks.size() == 0) {
            System.out.println("На даний момент у Вас не має невиконаних завдань.");
        } else {
            actualActionTasks.stream()
                    .sorted(new ImportanceAndDateComparator())
                    .forEach(System.out::println);
        }
    }


    public void showListOfCompletedTasks() {
        if (archivedActionTasks.size() == 0) {
            System.out.println("На даний момент у Вас не має виконаних завдань.");
        } else {
            archivedActionTasks.stream()
                    .sorted(new DateComparator().reversed())
                    .forEach(System.out::println);
        }
    }

    public List<Task> find() {
        Predicate<Task> taskPredicate = new Predicate<Task>() {
            @Override
            public boolean test(Task task) {
                return true;
            }
        };
        return actualActionTasks.stream()
                .filter(taskPredicate)
                .collect(Collectors.toList());
    }

    public Task addNewTask() {

        Scanner scanner = new Scanner(System.in);
        String name = null;
        int importance = -1;
        Date desiredDate = null;
        boolean isDone = false;

        if (actualActionTasks.size() == 0) {
            System.out.println("Назва задачі: ");
            name = scanner.next();
            System.out.println("Назва задачі успішно створена.");
        } else {
            System.out.println("Створіть нову назву задачі, " +
                    "щоб вона не співпадала з існуючою актуальною задачею.");
            System.out.println("Назва задачі: ");
            boolean flag = true;
            while (flag) {
                flag = false;
                name = scanner.next();
                for (Task actualActionTask : actualActionTasks) {
                    if (name.equals(actualActionTask.getName())) {
                        System.out.println("Така назва задачі вже існує, створіть нову назву задачі:");
                        flag = true;
                        break;
                    }
                }
            }
            System.out.println("Назва задачі успішно створена.");
        }

        System.out.println("Виберіть важливість задачі від 1 до 5.");
        do {
            System.out.println("Важливість задачі: ");
            try {
                importance = Integer.parseInt(scanner.next());
            } catch (NumberFormatException e) {
                System.out.println("Невірний ввод. Введіть цифру від 1 до 5");
                e.printStackTrace();
            }

        } while (importance > 5 || importance < 1);
        System.out.println("Важливість задачі успішно створена.");
        System.out.println("За бажанням можете додати термін виконання задачі.");
        System.out.println("Якщо бажаєте додати термін виконання задачі натисніть - YES");
        System.out.println("якщо - ні, натисніть - NO. ");
        String deadline = scanner.next();

        if (deadline.toLowerCase().startsWith("y")) {
            System.out.println("Введіть дату до якої потрібно виконати це завдання в форматі MM/dd/yyyy: ");
            deadline = scanner.next();

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            try {
                desiredDate = sdf.parse(deadline);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out.println("Дата зафіксована.");
        } else if (deadline.toLowerCase().startsWith("n")) {
            System.out.println("Термін виконання задачі не зазначений. ");
        }

        System.out.println("За бажанням можете відмітити задачу як виконану.");
        System.out.println("Бажаєте додати відмітку про виконання натисніть - YES, якщо ні, натисніть - NO: ");
        String isDoneStr = scanner.next();
        if (isDoneStr.toLowerCase().startsWith("y")) {
            isDone = true;
            System.out.println("Відмітка успішно додана. ");
        } else if (isDoneStr.toLowerCase().startsWith("n")) {
            System.out.println("Задача додана до невиконаних задач.");
        }

        Task actionTaskNew = new Task(name, importance, desiredDate, isDone);
        if (isDone) {
            archivedActionTasks.add(actionTaskNew);
        } else {
            actualActionTasks.add(actionTaskNew);
        }
        System.out.println(actionTaskNew);
        return actionTaskNew;
    }


    public void markCompletedTask() {
        String taskName;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введіть назву завдання, яке Ви хочете відмітити як виконане:");
        taskName = scanner.next();

        Task actualActionTask = null;
        if (actualActionTasks.size() > 0) {
            for (Task actionTask : actualActionTasks) {
                if (actionTask.getName().equals(taskName)) {
                    actionTask.setDone(true);
                    System.out.println(actionTask);
                    archivedActionTasks.add(actionTask);
                    actualActionTask = actionTask;
                    break;
                } else {
                    System.out.println("Такої актуальної задачі не існує.");
                    return;
                }
            }
            actualActionTasks.remove(actualActionTask);
            System.out.println("Задача відзначена як виконана та відправлена до архіву.");
        } else {
            System.out.println("У Вас не має жодної актуальної задачі. ");
        }
    }

    public void taskArchiving() {
        String taskName;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введіть назву завдання, яке Ви хочете заархівувати.");
        taskName = scanner.next();

        Task actualActionTask = null;
        if (actualActionTasks.size() > 0) {
            for (Task actionTask : actualActionTasks) {
                if (actionTask.getName().equals(taskName)) {
                    actionTask.setDone(true);
                    System.out.println(actionTask);
                    archivedActionTasks.add(actionTask);
                    actualActionTask = actionTask;
                } else {
                    System.out.println("Такої актуальної задачі не існує.");
                    return;
                }
            }
            actualActionTasks.remove(actualActionTask);
            System.out.println("Виконана задача успішно заархівована.");
        } else {
            System.out.println("У Вас не має жодної актуальної задачі. ");
        }

    }

    public void removeTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введіть назву задачі, яку Ви хочете видалити: ");
        String taskDoNotNeed = scanner.next();
        Task actualActionTask = null;

        for (Task actionTask : actualActionTasks) {
            if (actualActionTasks.size() > 0 && actionTask.getName().equals(taskDoNotNeed)) {
                System.out.println(actionTask);
                actualActionTask = actionTask;
            }
        }
        if (actualActionTask != null) {
            actualActionTasks.remove(actualActionTask);
            System.out.println("Задача видалена із актуальних задач. ");
            return;
        }

        for (Task actionTask : archivedActionTasks) {
            if (archivedActionTasks.size() > 0 && actionTask.getName().equals(taskDoNotNeed)) {
                System.out.println(actionTask);
                actualActionTask = actionTask;
            }
        }
        if (actualActionTask != null) {
            archivedActionTasks.remove(actualActionTask);
            System.out.println("Задача видалена із архіву. ");
            return;
        }
        System.out.println("Данної задачі " + taskDoNotNeed + " не існує.");

    }

    public List<Task> getActualActionTasks() {
        return actualActionTasks;
    }

    public List<Task> getArchivedActionTasks() {
        return archivedActionTasks;
    }
}
