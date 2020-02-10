package fr.epita.datamodel;

import java.util.ArrayList;
import java.util.List;

public class TaskList {
	private final String name;
	private final List<Task> tasks = new ArrayList<>();

	public String getName() {
		return name;
	}

	public TaskList(final String name) {
		this.name = name;
	}

	public void addTaskToList(final Task task) {
		tasks.add(task);
	}

	public void removeTask(final Task task) {
		tasks.remove(task);
	}

	public String printAllTasks() {
		final StringBuilder sb = new StringBuilder();
		for (final Task task : tasks) {
			sb.append(task.toSingleLineString());
			sb.append("\n");
		}
		return sb.toString();
	}

}
