package fr.epita.datamodel;

public class Task {
	private final String taskName;
	private int estimatedTime;
	private int priority;
	private boolean isAssigned;
	private boolean isFinished;
	private String userName;
	private String taskDescript;
	private String taskListName;

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(taskName);
		sb.append("\n");
		sb.append(taskDescript);
		sb.append("\n");
		sb.append("Priority: " + priority);
		sb.append("\n");
		sb.append("Estimated Time: " + estimatedTime);
		sb.append("\n");
		if (isAssigned) {
			sb.append("Assigned to " + userName);
		} else {
			sb.append("Unassigned");
		}
		sb.append("\n");

		return sb.toString();

//		return "Task [taskname=" + taskName + ", estimatedTime=" + estimatedTime + ", priority=" + priority
//				+ ", isAssigned=" + isAssigned + ", isFinished=" + isFinished + ", username=" + userName
//				+ ", taskDescript=" + taskDescript + ", taskListName=" + taskListName + "]";
	}

	public String toSingleLineString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(priority);
		sb.append(" | ");
		sb.append(taskName);
		sb.append(" | ");
		if (isAssigned) {
			sb.append(userName);
		} else {
			sb.append("Unassigned");
		}
		sb.append(" | ");
		sb.append(estimatedTime + "h");
		return sb.toString();
	}

	public Task(final String taskListName, final String taskName, final int estimatedTime, final int priority,
			final String taskDescript) {
		this.taskListName = taskListName;
		this.taskName = taskName;
		this.estimatedTime = estimatedTime;
		this.priority = priority;
		this.taskDescript = taskDescript;
	}

	public String getTaskName() {
		return taskName;
	}

	public int getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(int estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isAssigned() {
		return isAssigned;
	}

	public void setAssigned(boolean isAssigned) {
		this.isAssigned = isAssigned;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTaskDescript() {
		return taskDescript;
	}

	public void setTaskDescript(String taskDescript) {
		this.taskDescript = taskDescript;
	}

	public String getTaskListName() {
		return taskListName;
	}

	public void setTaskListName(String taskListName) {
		this.taskListName = taskListName;
	}
}
