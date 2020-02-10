package fr.epita.launcher;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import fr.epita.datamodel.*;
import fr.epita.services.WriteToFile;

/**
 * 
 * @author wangsong_epita_group1 2019Fall JavaFundamental Homework2 Delivered
 *         Delivered on Feb10th, 2020
 *
 */

public class main {

	public static void main(String[] args) {
		EpitrelloDataService dataservice = EDSImpl.newInstance();

		System.out.println(dataservice.addUser("Thomas")); // addUser(string username)
		System.out.println(dataservice.addUser("AmirAli"));
		System.out.println(dataservice.addUser("Rabih"));

		System.out.println(dataservice.addList("Code")); // addList(string name)
		System.out.println(dataservice.addList("Description"));
		System.out.println(dataservice.addList("Misc"));

		System.out.println(dataservice.addTask("Code", "Do Everything", 12, 1, "Write the whole code"));
		/*
		 * addTask(string list, string name, unsigned int estimatedTime, unsigned int
		 * priority, string description)
		 */
		System.out.println(dataservice.editTask("Do Everything", 12, 10, "Write the whole code"));
		/*
		 * editTask(string task, unsigned int estimatedTime, unsigned int priority,
		 * string description)
		 */

		System.out.println(dataservice.assignTask("Do Everything", "Rabih")); // assignTask(string task, string user)
		System.out.println(dataservice.printTask("Do Everything")); // printTask(string task)

		System.out.println(dataservice.addTask("Code", "Destroy code formatting", 1, 2,
				"Rewrite the whole code in a worse format"));
		System.out.println(dataservice.assignTask("Destroy code formatting", "Thomas"));

		System.out.println(dataservice.addTask("Description", "Write Description", 3, 1, "Write the damn description"));
		System.out.println(dataservice.assignTask("Write Description", "AmirAli"));
		System.out.println(dataservice.addTask("Misc", "Upload Assignment", 1, 1, "Upload it"));

		System.out.println(dataservice.completeTask("Do Everything")); // completeTask(string task)
		System.out.println(dataservice.printUsersByPerformance());
		System.out.println(dataservice.printUsersByWorkload());

		System.out.println(dataservice.printUnassignedTasksByPriority());
		System.out.println(dataservice.deleteTask("Upload Assignment")); // deleteTask(string task)
		System.out.println(dataservice.printAllUnfinishedTasksByPriority());

		System.out.println(dataservice.addTask("Misc", "Have fun", 10, 2, "Just do it"));
		System.out.println(dataservice.moveTask("Have fun", "Code")); // moveTask(string task, string list)
		System.out.println(dataservice.printTask("Have fun"));

		System.out.println(dataservice.printList("Code")); // printList(string list)

		System.out.println(dataservice.printAllLists());

		System.out.println(dataservice.printUserTasks("AmirAli")); // printUserTasks(string user)

		System.out.println(dataservice.printUnassignedTasksByPriority());

		System.out.println(dataservice.printAllUnfinishedTasksByPriority());

		// TODO Write all the prints into a file.
		{
			EpitrelloDataService dataServiceWriter = EDSImpl.newInstance();
			StringBuilder sb = new StringBuilder();
			sb.append(dataServiceWriter.addUser("Thomas") + "\n");
			sb.append(dataServiceWriter.addUser("AmirAli") + "\n");
			sb.append(dataServiceWriter.addUser("Rabih") + "\n");
			sb.append(dataServiceWriter.addList("Code") + "\n");
			sb.append(dataServiceWriter.addList("Description") + "\n");
			sb.append(dataServiceWriter.addList("Misc") + "\n");
			sb.append(dataServiceWriter.addTask("Code", "Do Everything", 12, 1, "Write the whole code") + "\n");
			sb.append(dataServiceWriter.editTask("Do Everything", 12, 10, "Write the whole code") + "\n");
			sb.append(dataServiceWriter.assignTask("Do Everything", "Rabih") + "\n");
			sb.append(dataServiceWriter.printTask("Do Everything") + "\n");
			sb.append(dataServiceWriter.addTask("Code", "Destroy code formatting", 1, 2,
					"Rewrite the whole code in a worse format") + "\n");
			sb.append(dataServiceWriter.assignTask("Destroy code formatting", "Thomas") + "\n");
			sb.append(dataServiceWriter.addTask("Description", "Write Description", 3, 1, "Write the damn description")
					+ "\n");
			sb.append(dataServiceWriter.assignTask("Write Description", "AmirAli") + "\n");
			sb.append(dataServiceWriter.addTask("Misc", "Upload Assignment", 1, 1, "Upload it") + "\n");
			sb.append(dataServiceWriter.completeTask("Do Everything") + "\n"); // completeTask(string task)
			sb.append(dataServiceWriter.printUsersByPerformance());
			sb.append(dataServiceWriter.printUsersByWorkload());
			sb.append(dataServiceWriter.printUnassignedTasksByPriority());
			sb.append(dataServiceWriter.deleteTask("Upload Assignment")); // deleteTask(string task)
			sb.append(dataServiceWriter.printAllUnfinishedTasksByPriority());
			sb.append(dataServiceWriter.addTask("Misc", "Have fun", 10, 2, "Just do it") + "\n");
			sb.append(dataServiceWriter.moveTask("Have fun", "Code") + "\n"); // moveTask(string task, string list)
			sb.append(dataServiceWriter.printTask("Have fun") + "\n");
			sb.append(dataServiceWriter.printList("Code") + "\n"); // printList(string list)
			sb.append(dataServiceWriter.printAllLists());
			sb.append(dataServiceWriter.printUserTasks("AmirAli") + "\n"); // printUserTasks(string user)
			sb.append(dataServiceWriter.printUnassignedTasksByPriority());
			sb.append(dataServiceWriter.printAllUnfinishedTasksByPriority());
			String content = sb.toString();
			new WriteToFile(content);
		}

		// TODO Save users in a db.
		saveUserinDataBase(dataservice);
	}

	private static void saveUserinDataBase(final EpitrelloDataService dataservice) {
		try {
			Class.forName("org.h2.Driver");

			try (final Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost:9092/~/tmp/h2dbs/epitrello", "epitrello",
					"epitrello")) {
				final String dropTable = "DROP TABLE IF EXISTS USER;";
				final String creationQuery = "CREATE TABLE IF NOT EXISTS USER(ID INT auto_increment PRIMARY KEY, NAME VARCHAR(256))";
				try (final PreparedStatement creationStatement = connection.prepareStatement(creationQuery);
						PreparedStatement clearStatement = connection.prepareStatement(dropTable)) {
					clearStatement.execute();
					creationStatement.execute();
				}

				final List<String> userNames = dataservice.getUserNames();
				final String insertionQuery = "INSERT INTO USER(NAME) VALUES ( ? )";

				for (final String userName : userNames) {
					try (final PreparedStatement insertionStatement = connection.prepareStatement(insertionQuery)) {
						insertionStatement.setString(1, userName);
						insertionStatement.execute();
					}
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}
