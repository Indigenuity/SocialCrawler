package tasking.async;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import play.Logger;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import tasking.registration.RegistryEntry;
import tasking.registration.ContextItem;
import tasking.registration.WorkerRegistry;
import tasking.tools.CustomTool;
import tasking.work.WorkType;
import tasking.work.Worker;
import tasking.tasks.Task;

public class Asyncleton {
	
	public static final int DEFAULT_NUM_WORKERS = 5;

	private static final Asyncleton instance = new Asyncleton();
	
	private final Map<WorkType, ActorRef> masters = Collections.synchronizedMap(new HashMap<WorkType, ActorRef>());
	
	private ActorSystem mainSystem;
	
	private ActorRef mainListener;
	private ActorRef mainMaster;
	
	private boolean initialized;
	
	protected Asyncleton(){
		initialize();
//		for(Entry<WorkType, RegistryEntry> entry: WorkerRegistry.getInstance().getRegistry().entrySet()){
//			ActorRef master = mainSystem.actorOf(Props.create(GenericMaster.class, entry.getValue().getNumWorkers(), mainListener, entry.getValue().getClazz()));
//			masters.put(entry.getKey(), master);
//		}
	}
	
	public ActorRef getMaster(WorkType workType) {
		return masters.get(workType);
	}
	
	public ActorRef getGenericMaster(int numWorkers) {
		return mainSystem.actorOf(Props.create(GenericMaster.class, numWorkers, mainListener, Worker.class));
	}
	
	public void doTask(Task task, ActorRef sender, boolean checkRequiredContextItems) {
		RegistryEntry entry = WorkerRegistry.getInstance().getRegistrant(task.getWorkType());
		
		if(checkRequiredContextItems) {
			for(ContextItem item : entry.getRequiredContextItems()){
				String name = item.getName();
				boolean nullable  = item.isNullable();
				if(task.getContextItem(name) == null && !nullable){
					throw new IllegalArgumentException("Cannot perform " + entry.getWorkType() + " task without required context item " + name);
				}
			}
		}
		
		masters.get(entry.getWorkType()).tell(task, sender);
	}
	
	
	
	private void initialize() {
		if(!initialized) {
			
			Logger.info("Starting up main async system");
			mainSystem = ActorSystem.create("mainSystem");
			Logger.info("Main async system ready for jobs");
			 
		}
	}
	
	public void restart() {
		mainSystem.shutdown();
		initialized = false;
		initialize();
	}

	public static Asyncleton instance() { 
		return instance;
	}

	public ActorSystem getMainSystem() {
		return mainSystem;
	}

	public ActorRef getMainListener() {
		return mainListener;
	}

	public ActorRef getMainMaster() {
		return mainMaster;
	}
	
	public boolean isInitialized() {
		return initialized;
	}

	public static Asyncleton getInstance() {
		return instance;
	}

}
