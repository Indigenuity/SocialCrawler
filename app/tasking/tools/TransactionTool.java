package tasking.tools;

import tasking.tasks.Task;
import play.db.jpa.JPA;

public abstract class TransactionTool extends Tool {

	@Override
	protected Task safeDoTask(Task task) throws Exception{
//		System.out.println("InventoryTool processing task : " + task);
		Task[] temp = new Task[1];
		JPA.withTransaction( () -> {
			try {
				temp[0]= doTaskInTransaction(task);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JPA.em().flush();
			JPA.em().clear();
		});
		System.gc();
		return temp[0];
	}
	
	protected abstract Task doTaskInTransaction(Task task) throws Exception;
}
