package tasking.tasks;

import java.util.Collection;
import java.util.Map.Entry;

import tasking.registration.ContextItem;
import tasking.registration.RegistryEntry;
import tasking.registration.WorkerRegistry;
import tasking.work.WorkOrder;
import tasking.work.WorkResult;
import tasking.work.WorkType;
import tasking.tasks.Task;

public class TaskWorkUtil {

	public static WorkOrder buildWorkOrder(Task task) {
		WorkOrder workOrder = new WorkOrder(task.getWorkType());
		
		RegistryEntry entry = WorkerRegistry.getInstance().getRegistrant(workOrder.getWorkType());
		
		for(ContextItem requiredItem : entry.getRequiredContextItems()){
			String name = requiredItem.getName();
			boolean nullable = requiredItem.isNullable();
			
			String item = task.getContextItem(name);
			if(item == null && nullable == false){
				throw new IllegalStateException("Cannot build WorkOrder for task " + task.getTaskId() + " without context item : " + name);
			}
			workOrder.addContextItem(name, item);
		}
		return workOrder;
	}
	
	public static void importResultContextItems(WorkResult workResult, Task task) {
		for(Entry<String, String> entry : workResult.getContextItems().entrySet()){
			task.addContextItem(entry.getKey(), entry.getValue());
		}
	}
	
	public static void createTaskSet(Collection<WorkType> workTypes){
		
	}
}
