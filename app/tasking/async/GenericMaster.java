package tasking.async;

import java.util.ArrayList;
import java.util.List;

import play.Logger;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import tasking.monitoring.AsyncMonitor;
import tasking.work.WorkOrder;
import tasking.work.WorkResult;
import tasking.work.WorkStatus;

public class GenericMaster extends UntypedActor {
	

	private final int numWorkers;
	
	private Router router;
	
	private Class<?> clazz;
	
	private WaitingRoom waitingRoom;
	
	public GenericMaster(int numWorkers, ActorRef listener, Class<?> clazz) {
		System.out.println("Generic master starting with " + numWorkers + " workers of type " + clazz);
		this.numWorkers = numWorkers;
		this.clazz = clazz;
		this.waitingRoom = new WaitingRoom("Waiting room for " + clazz);
//		Router balancedRouter = getContext().actorOf(new BalancingPool(numWorkers).props(Props.create(clazz)), "BalancedRouter");
		List<Routee> routees = new ArrayList<Routee>();
	    for (int i = 0; i < this.numWorkers; i++) {
	      ActorRef r = getContext().actorOf(Props.create(clazz));
	      getContext().watch(r);
	      routees.add(new ActorRefRoutee(r));
	    }
	    router = new Router(new RoundRobinRoutingLogic(), routees); 
	    
	}
	
	@Override
	public void onReceive(Object work) throws Exception {
		try{
//			System.out.println("received message in generic master (" + this.clazz + ") : " + work);
			if (work instanceof WorkOrder) {
				WorkOrder workOrder = (WorkOrder) work;
//				System.out.println("GenericMaster got work order: " + workOrder);
				if(!waitingRoom.add(workOrder.getUuid(), getSender())){
					//TODO figure out what to do when duplicate work order is sent in
					return;
				}
				router.route(workOrder, getSelf());
			}
			else if(work instanceof WorkResult) {
				WorkResult workResult = (WorkResult) work;
//				System.out.println("GenericMaster got work result: " + workResult);
				ActorRef customer = waitingRoom.remove(workResult.getWorkOrder().getUuid()); 
				if(customer == null){
					//TODO figure out what to do when receiving work result for no customer
					return;
				}
				if(customer.equals(ActorRef.noSender())){
					//TODO figure out what to do when customer didn't leave a number
					return;
				}
				customer.tell(workResult, getSelf());
				if(waitingRoom.size() == 0) {
					System.out.println("shutting down master and children");
					context().stop(getSelf());
				}
			}
//			else if (work instanceof InfoFetch) {
//				ActorRef r = getContext().actorOf(Props.create(clazz));
//			    r.tell(work, getSender());
//			}
			else if(work instanceof Terminated) {
				Logger.error("Generic Master (" + this.clazz + ") received terminated worker");
//				router = router.removeRoutee(((Terminated) work).actor());
//				ActorRef worker = getContext().actorOf(Props.create(this.clazz));
//				getContext().watch(worker);
//				router = router.addRoutee(new ActorRefRoutee(worker));
			}
			else {
				System.out.println("Got unknown work in Generic Master (" + this.clazz + ") : " + work);
				Logger.warn("Got unknown work in Generic Master (" + this.clazz + ") : " + work);
			}
		}
		catch(Exception e){
			Logger.error("Exception caught in Generic Master (" + this.clazz + ") : " + e);
			e.printStackTrace();
		}
	}

}
