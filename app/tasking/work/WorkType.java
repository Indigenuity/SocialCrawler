package tasking.work;



 
//The types of work to be done by workers in this program.
//ORDER MATTERS. 
//Higher tasks may need to be completed before lower tasks are possible to complete
public enum WorkType{
	NO_WORK						,
	FB_PAGE_FETCH					,
	FB_FEED_FETCH					,
	CUSTOM						, 
	SUPERTASK, 
	TASK					
	;

	private static final int DEFAULT_NUM_WORKERS = 5;
	private Class<?> defaultWorker;
	private int numWorkers;
	
	private WorkType(Class<?> defaultWorker){
		this.defaultWorker = defaultWorker;
		this.numWorkers = DEFAULT_NUM_WORKERS;
	}
	
	private WorkType(Class<?> defaultWorker, int numWorkers){
		this.defaultWorker = defaultWorker;
		this.numWorkers = numWorkers;
	}
	
	private WorkType() {
		this.defaultWorker = null;
	}
	
	public Class<?> getDefaultWorker(){
		return this.defaultWorker;
	}
	
	public int getNumWorker() {
		return this.numWorkers;
	}

}
