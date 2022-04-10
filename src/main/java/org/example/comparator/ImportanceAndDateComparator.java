package org.example.comparator;

import org.example.entity.Task;

import java.util.Comparator;

public class ImportanceAndDateComparator implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
        int taskImportance = o1.getImportance() - o2.getImportance();
        if(taskImportance == 0){
            return o1.getDeadline().compareTo(o2.getDeadline());

        }
        return taskImportance;
    }

}
