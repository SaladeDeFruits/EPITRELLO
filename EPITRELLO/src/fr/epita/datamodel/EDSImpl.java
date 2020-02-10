package fr.epita.datamodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class EDSImpl implements EpitrelloDataService {
	private final List<User> users = new ArrayList<>();
	private final List<Task> tasks = new ArrayList<>();
	private final List<TaskList> taskLists = new ArrayList<>();
	private final Map<String, User> nameToUser = new HashMap<>();
	private final Map<String, Task> nameToTask = new HashMap<>();
	private final Map<String, TaskList> nameToTaskList = new HashMap<>();

	// newInstance() function is called, as creator() when we create an object from
	// the dataservice interface
	private EDSImpl() {
	}

	public static EpitrelloDataService newInstance() {
		return new EDSImpl();
	}

	/**
	 * Add new user to the system Returns the “User already exists” if a duplicate
	 * name is entered for the user. Else return "Success"
	 */
	@Override
	public String addUser(final String username) {
		if (!nameToUser.containsKey(username)) {
			final User newuser = new User(username);
			users.add(newuser);
			nameToUser.put(username, newuser);

			return "Success";
		}

		return "User already exists";
	}

	/**
	 * Add new list to the system Returns the “List string already exists” if a
	 * duplicate name is entered for the list. Else return "success"
	 */
	@Override
	public String addList(final String listname) {
		if (!nameToTaskList.containsKey(listname)) {
			final TaskList newlt = new TaskList(listname);
			taskLists.add(newlt);
			nameToTaskList.put(listname, newlt);

			return "Success";
		}

		return "List string already exists";
	}

	/**
	 * 
	 */
	@Override
	public String addTask(final String taskListName, final String taskName, final int estimatedTime, final int priority,
			final String description) {
		if (nameToTask.containsKey(taskName)) {
			return "Task already exists";
		}
		if (!nameToTaskList.containsKey(taskListName)) {
			return "List does not exist";
		}
		final Task newTask = new Task(taskListName, taskName, estimatedTime, priority, description);
		tasks.add(newTask);
		nameToTask.put(taskName, newTask);
		final TaskList taskList = nameToTaskList.get(taskListName);
		taskList.addTaskToList(newTask);

		return "Success";
	}

	@Override
	public String deleteList(final String listName) {
		if (!nameToTaskList.containsKey(listName)) {
			return "List does not exist";
		}

		final TaskList taskList = nameToTaskList.get(listName);
		taskLists.remove(taskList);
		nameToTaskList.remove(listName);

		for (final Task task : tasks) {
			if (task.getTaskListName() == listName) {
				nameToTask.remove(task.getTaskName());
				tasks.remove(task);
			}
		}

		return "Success";

	}

	@Override
	public String deleteTask(final String taskName) {
		if (!nameToTask.containsKey(taskName)) {
			return "Task does not exist";
		}

		final Task task = nameToTask.get(taskName);
		tasks.remove(task);
		nameToTask.remove(taskName);

		final String listName = task.getTaskListName();
		final TaskList taskList = nameToTaskList.get(listName);
		taskList.removeTask(task);

		// TODO: After deleting the task, if the ListTask is empty, should we delete the
		// List?
		return "Success";
	}

	@Override
	public String assignTask(final String taskName, final String userName) {
		if (!nameToTask.containsKey(taskName)) {
			return "Task does not exist";
		}
		if (!nameToUser.containsKey(userName)) {
			return "User does not exist";
		}
		final Task task = nameToTask.get(taskName);
		task.setUserName(userName);
		task.setAssigned(true);

		return "Success";
	}

	@Override
	public String completeTask(final String taskName) {
		if (!nameToTask.containsKey(taskName)) {
			return "Task does not exist";
		}
		final Task task = nameToTask.get(taskName);
		task.setFinished(true);

		return "Success";
	}

	@Override
	public String editTask(final String taskName, final int estimatedTime, final int priority,
			final String description) {
		if (!nameToTask.containsKey(taskName)) {
			return "Task does not exist";
		}
		final Task task = nameToTask.get(taskName);
		task.setEstimatedTime(estimatedTime);
		task.setPriority(priority);
		task.setTaskDescript(description);

		return "Success";
	}

	@Override
	public String moveTask(final String taskName, final String newListName) {
		if (!nameToTask.containsKey(taskName)) {
			return "Task does not exist";
		}
		if (!nameToTaskList.containsKey(newListName)) {
			return "List does not exist";
		}

		final Task task = nameToTask.get(taskName);
		final String oldListName = task.getTaskListName();
		final TaskList oldTaskList = nameToTaskList.get(oldListName);
		final TaskList newTaskList = nameToTaskList.get(newListName);
		oldTaskList.removeTask(task);
		newTaskList.addTaskToList(task);
		task.setTaskListName(newListName);

		return "Success";
	}

	@Override
	public String printTask(final String taskName) {
		if (!nameToTask.containsKey(taskName)) {
			return "Task does not exist";
		}
		final Task task = nameToTask.get(taskName);
		return task.toString();
	}

	@Override
	public String printList(final String taskListName) {
		if (!nameToTaskList.containsKey(taskListName)) {
			return "List does not exist";
		}
		final TaskList taskList = nameToTaskList.get(taskListName);
		return "List " + taskList.getName() + "\n" + taskList.printAllTasks();
	}

	@Override
	public String printAllLists() {
		final StringBuilder sb = new StringBuilder();
		for (final TaskList taskList : taskLists) {
			sb.append(printList(taskList.getName()));
			sb.append("\n");
		}
		return sb.toString();
	}

	@Override
	public String printUsersByPerformance() {
		final Map<String, Integer> userNameToTotalTime = new HashMap<>();

		for (final Task task : tasks) {
			if (task.isAssigned()) {
				final int taskTime = task.isFinished() ? task.getEstimatedTime() : 0;

//				if(userNameToTotalTime.containsKey(task.getUserName())) {
//					userNameToTotalTime.put(task.getUserName(), userNameToTotalTime.get(task.getUserName()) + task.getEstimatedTime());
//				} else {
//					userNameToTotalTime.put(task.getUserName(), task.getEstimatedTime());
//				}

				userNameToTotalTime.compute(task.getUserName(), (k, v) -> v == null ? taskTime : v + taskTime);
			}
		}

		return userNameToTotalTime.entrySet().stream()
				.sorted(Comparator.comparing(Entry<String, Integer>::getValue).reversed()).map(Entry::getKey)
				.collect(Collectors.joining("\n")) + "\n";
	}

	@Override
	public String printUsersByWorkload() {
		final Map<String, Integer> userNameToTotalTime = new HashMap<>();

		for (final Task task : tasks) {
			if (task.isAssigned()) {
				userNameToTotalTime.compute(task.getUserName(),
						(k, v) -> v == null ? task.getEstimatedTime() : v + task.getEstimatedTime());
			}
		}

		return userNameToTotalTime.entrySet().stream().sorted(Comparator.comparing(Entry<String, Integer>::getValue))
				.map(Entry::getKey).collect(Collectors.joining("\n")) + "\n";
	}

	@Override
	public String printUnassignedTasksByPriority() {
		final Map<String, Integer> taskNameToPriority = new HashMap<>();

		for (final Task task : tasks) {
			if (!task.isAssigned()) {
				taskNameToPriority.compute(task.getTaskName(),
						(k, v) -> v == null ? task.getPriority() : v + task.getPriority());
			}
		}

		return taskNameToPriority.entrySet().stream().sorted(Comparator.comparing(Entry<String, Integer>::getValue))
				.map(Entry::getKey).map(name -> nameToTask.get(name).toSingleLineString())
				.collect(Collectors.joining("\n")) + "\n";
	}

	@Override
	public String printUserTasks(final String userName) {
		if (!nameToUser.containsKey(userName)) {
			return "User does not exist";
		}
//		StringBuilder sb = new StringBuilder();
//		for(Task task : tasks) {
//			if(task.getUserName().equals(userName)) {
//				sb.append(task.getTaskName());
//				sb.append("|");
//			}
//		}
//		return sb.toString();
		return tasks.stream().filter(t -> userName.equals(t.getUserName())).map(Task::toSingleLineString)
				.collect(Collectors.joining("\n")) + "\n";
	}

	@Override
	public String printAllUnfinishedTasksByPriority() {
		final Map<String, Integer> taskNameToPriority = new HashMap<>();

		for (final Task task : tasks) {
			if (!task.isFinished()) {
				taskNameToPriority.compute(task.getTaskName(),
						(k, v) -> v == null ? task.getPriority() : v + task.getPriority());
			}
		}

		return taskNameToPriority.entrySet().stream().sorted(Comparator.comparing(Entry<String, Integer>::getValue))
				.map(Entry::getKey).map(name -> nameToTask.get(name).toSingleLineString())
				.collect(Collectors.joining("\n")) + "\n";
	}

	@Override
	public List<String> getUserNames() {
		return users.stream().map(User::getUsername).collect(Collectors.toList());
	}
}
