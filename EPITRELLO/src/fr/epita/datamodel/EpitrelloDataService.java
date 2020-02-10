package fr.epita.datamodel;

import java.util.List;

public interface EpitrelloDataService {

	String addUser(String username);

	String addList(String name);

	String addTask(String list, String name, int estimatedTime, int priority, String description);
	
	String deleteList(String listname);

	String editTask(String task, int estimatedTime, int priority, String description);

	String assignTask(String task, String user);

	String printTask(String task);

	String completeTask(String task);
	
	String printUsersByPerformance();
	
	String printUsersByWorkload();
	
	String printUnassignedTasksByPriority();
	
	String deleteTask(String task);
	
	String moveTask(String task, String list);
	
	String printList(String list);
	
	String printAllLists();
	
	String printUserTasks(String user);
		
	String printAllUnfinishedTasksByPriority();

	List<String> getUserNames();
}
