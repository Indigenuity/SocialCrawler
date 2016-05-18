package tasking.tasks;

import akka.actor.ActorRef;
import tasking.async.Asyncleton;
import tasking.work.WorkOrder;
import tasking.work.WorkStatus;
import tasking.work.WorkType;
import tasking.tasks.Task;
import tasking.tasks.TaskSet;

public class TaskMaster {

	public static void doTaskSetWork(TaskSet taskSet, int numWorkers, int numToProcess, int offset) {
		int count = 0;
		System.out.println("TaskMaster initializing master with " + numWorkers + " workers");
		ActorRef master = Asyncleton.getInstance().getGenericMaster(numWorkers);
		for(Task task : taskSet.getTasks()) {
			if(count < numToProcess && (task.getWorkStatus() == WorkStatus.DO_WORK || task.getWorkStatus()== WorkStatus.MORE_WORK)){
				count++;
				if(count < offset){
					continue;
				}
				WorkOrder workOrder = new WorkOrder(WorkType.TASK);
				workOrder.addContextItem("taskId", task.getTaskId() + "");
				master.tell(workOrder, Asyncleton.getInstance().getMainListener());
			}
		}
	}
}
