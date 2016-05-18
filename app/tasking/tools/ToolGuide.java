package tasking.tools;

import java.lang.reflect.Constructor;

import tasking.registration.RegistryEntry;
import tasking.registration.WorkerRegistry;
import tasking.work.WorkType;

public class ToolGuide {

	public static Tool findTool(WorkType workType){
//		System.out.println("ToolGuide finding tool for : " + workType);
		try {
			RegistryEntry entry = WorkerRegistry.getInstance().getRegistrant(workType);
			Class<? extends Tool> clazz = entry.getClazz();
			Constructor<? extends Tool> constructor = clazz.getConstructor();
			return constructor.newInstance();
		} 
		catch (Exception e) {
			return null;
		}
	}
}
