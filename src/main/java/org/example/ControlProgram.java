package org.example;

import org.example.entity.Task;
import org.example.entity.User;
import org.example.service.ReadWriteBitService;
import org.example.service.ReadWriteSerializeService;
import org.example.service.ReadWriteService;
import org.example.service.ReadWriteTxtService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ControlProgram {
    public static final String TEXT_FILE_NAME = "texFormat.txt";
    public static final String BIN_FILE_NAME = "ToDoList.bin";
    public static final String SERIALIZE_FILE_NAME = "SERIALIZE.bin";

    private List<User> users = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);
    private ToDoList toDoList;
    private ReadWriteService readWriteService;
    private User currentUser;

    public ControlProgram() {
        readWriteService = new ReadWriteSerializeService();
        File file = new File(SERIALIZE_FILE_NAME);

        if (file.exists()) {
            users = readWriteService.read(SERIALIZE_FILE_NAME);
        }

        currentUser = authorization();
        toDoList = new ToDoList(currentUser);
        printMenuProgram();
    }

    public User authorization() {
        User user;

        while (true) {
            System.out.println(" 1 - зареєструватися");
            System.out.println(" 2 - увійти");
            String answer = scanner.next();
            if (!validateAnswer(answer)) {
                continue;
            }

            if (answer.equals("1")) {
                System.out.println("Введіть логін: ");
                String login = scanner.next();
                if (isLoginExist(login)) {
                    System.out.println("Такий коричстувач вже існує. Вибачте. Допобачення!");
                    continue;
                }
                System.out.println("Введіть пароль: ");
                String password = scanner.next();

                user = new User(login, password);
                users.add(user);
                return user;
            } else if (answer.equals("2")) {
                System.out.println("Введіть логін: ");
                String login = scanner.next();
                System.out.println("Введіть пароль: ");
                String password = scanner.next();
                user = getUserByLoginAndPwd(login, password);
                if (user == null) {
                    continue;
                }
                return user;
            }
            return null;
        }
    }

    private boolean validateAnswer(String answer) {
        try {
            int an = Integer.parseInt(answer);
            if (2 < an || an < 1) {
                System.out.println("Такого пункту меню не існує");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Помилка вводу");
            System.out.println(e.getMessage());
        }
        return false;
    }


    public void printMenuProgram() {
        int menuNumber;
        do {
            System.out.println("Оберіть бажану дію від 1 до 7");
            System.out.println("1 - відображення списку всіх не виконаних задач " +
                    "(відсортованих по важливості та даті)");
            System.out.println("2 - відображення списку всіх виконаних задач " +
                    "(відсортованих по даті у зворотньому напрямку");
            System.out.println("3 - додавання нової задачі");
            System.out.println("4 - відмітка про виконання задачі " +
                    "(архівування виконаних задач)");
            System.out.println("5 - видалення задачі");
            System.out.println("6 - архівування виконаної задачі");
            System.out.println("7 - вихід");
            System.out.println("Оберіть бажану дію: ");
            menuNumber = scanner.nextInt();
            menuSelectionProcess(menuNumber);
        } while (menuNumber != 7);
    }


    public void menuSelectionProcess(int menuItem) {
        switch (menuItem) {
            case 1:
                System.out.println("------Відображення списку всіх невиконаних задач-------");
                toDoList.showListOfPendingTasks();
                System.out.println("----------------");
                break;
            case 2:
                System.out.println("------Відображення списку всіх виконаних задач-------");
                toDoList.showListOfCompletedTasks();
                System.out.println("----------------");
                break;
            case 3:
                System.out.println("------Додавання нової задачі-------");
                System.out.println("Створіть нову задачу: ");
                toDoList.addNewTask();
                System.out.println("Ваша нова задача успішно створена!");
                System.out.println("----------------");
                break;
            case 4:
                System.out.println("------Відмітка про виконання задачі-------");
                toDoList.markCompletedTask();
                System.out.println("----------------");
                break;
            case 5:
                System.out.println("-------Видалення задачі------");
                toDoList.removeTask();
                System.out.println("----------------");
                break;
            case 6:
                System.out.println("-----Архівування виконаної задачі-----");
                toDoList.taskArchiving();
                System.out.println("----------------");
                break;
            case 7:
                updateUsersData();
                readWriteService.write(users, SERIALIZE_FILE_NAME);
        }
    }

    private void updateUsersData() {
        List<Task> taskList = new ArrayList<>();
        taskList.addAll(toDoList.getActualActionTasks());
        taskList.addAll(toDoList.getArchivedActionTasks());

        currentUser.setTaskList(taskList);
    }

    public void addUser(String login, String password) {
        users.add(new User(login, password));
        System.out.println("Новий користувач успішно створений.");
    }


    public boolean userExists(String login, String password) {
        for (User user : users) {
            if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
                System.out.println("Цей користувач icнує. ");
                return true;
            }
        }

        System.out.println("Такий користувач не існує.");
        return false;
    }

    private boolean isLoginExist(String login) {
        for (User user : users) {
            if (user.getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }

    private User getUserByLoginAndPwd(String login, String password) {
        for (User user : users) {
            if (user.getLogin().equals(login)) {
                if (user.getPassword().equals(password)) {
                    return user;
                }
            }
        }
        System.out.println("Невірний логін або пароль!");
        return null;
    }
}
