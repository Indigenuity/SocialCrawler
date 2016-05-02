package tasking.tasks;

import java.util.Map;

import tasking.work.WorkStatus;
import tasking.tasks.Task;

public class TaskReviewer {
	
	public static void resetTask(Task task){
		task.setWorkStatus(WorkStatus.DO_WORK);
		task.setNote("");
		matchSupertaskToSubtask(task);
	}
	
	public static void matchSupertaskToSubtask(Task subtask){
		Task supertask = subtask.getSupertask();
		if(supertask == null){
			return;
		}
		if(subtask.getWorkStatus() == WorkStatus.DO_WORK){
			supertask.setWorkStatus(WorkStatus.DO_WORK);
			supertask.setNote("");
		}else if(subtask.getWorkStatus() == WorkStatus.NEEDS_REVIEW){
			supertask.setWorkStatus(WorkStatus.NEEDS_REVIEW);
			supertask.setNote("Subtask needs review");
		}else if(subtask.getWorkStatus() == WorkStatus.WORK_COMPLETED){
			supertask.setWorkStatus(WorkStatus.DO_WORK);
			supertask.setNote("");
		}else if(subtask.getWorkStatus() == WorkStatus.MORE_WORK){
			supertask.setWorkStatus(WorkStatus.DO_WORK);
			supertask.setNote("");
		}else {
			supertask.setWorkStatus(WorkStatus.NEEDS_REVIEW);
			supertask.setNote("Subtask changed to status unknown");
		}
	}

	public static void reviewTask(Task task, Map<String, String> contextItems) {
		String action = contextItems.get("action");
		System.out.println("reviewing task " + task.getTaskId() + " with action " + action);
		if("MORE_WORK".equals(action)){
			System.out.println("Sending back for more work : " + task.getTaskId());
			task.setWorkStatus(WorkStatus.MORE_WORK);
			task.setNote("");
		}
		
		matchSupertaskToSubtask(task);
	}
	
}
