package tasking.work;

import java.util.UUID;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import tasking.async.Asyncleton;
import tasking.monitoring.AsyncMonitor;
import play.Logger;
import play.db.jpa.JPA;

public class MultiStepJPAWorker extends UntypedActor {
	
	protected Long uuid = UUID.randomUUID().getLeastSignificantBits();
	protected WorkOrder workOrder;
	protected ActorRef customer;
	protected Long workOrderUuid;

	@Override
	public void onReceive(Object work) throws Exception {
		System.out.println("Starting multi step work on thread " + Thread.currentThread().getName());
		WorkOrder nextStep = null;
		if(work instanceof WorkOrder) {
//			if(workOrder != null){
//				Logger.error("Received second work order : " + work);
//				throw new IllegalStateException("Received second work order : " + work);
//			}
			customer = getSender();
			workOrder = (WorkOrder) work;
			this.workOrderUuid = workOrder.getUuid();
//			System.out.println("just set work order uuid : " + this.workOrderUuid);
//			System.out.println("Performing work : " + workOrder.getWorkType());
			
			nextStep = processWorkOrder(workOrder);
			if(nextStep != null){
				Asyncleton.getInstance().getMaster(nextStep.getWorkType()).tell(nextStep,  customer);
			}
		}
		else if(work instanceof WorkResult){
			WorkResult workResult = (WorkResult)work;
			processWorkResult(workResult);
//			doNextStep();
		}
		else {
			Logger.error("got unknown work in Multi Step worker : " + work);
		}
		
		
		
	}
	
	public WorkOrder processWorkOrder(WorkOrder workOrder){
		Logger.error("in Generic work order.  Must implement for class: " + this.getClass());
		return null;
	}
	
	@Override
	public void postRestart(Throwable reason) throws Exception {
		Logger.error("Worker restarting: " + reason);
		preStart();
	}
	
	public void processWorkResult(WorkResult workResult) {
		
	}
	
	public void finish() {
		WorkResult workResult = generateWorkResult();
		customer.tell(workResult, getSelf());
	}
	
	public WorkResult generateWorkResult(){
		System.out.println("In generic generateworkresult");
		return new WorkResult();
	}

	public Long getWorkOrderUuid() {
		return workOrderUuid;
	}

	public void setWorkOrderUuid(Long workOrderUuid) {
		this.workOrderUuid = workOrderUuid;
	}

	

}