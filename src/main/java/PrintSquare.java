import java.io.Serializable;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;

public class PrintSquare implements Runnable , Serializable, HazelcastInstanceAware{

	private transient HazelcastInstance hz;

	public Object call() {
		IMap<String, Object> map = hz.getMap("context");
		Object objectSum = map.get("sum");
		Integer s = (Integer)objectSum;

		Object objectSquare = map.get("square");
		Integer sq = (Integer)objectSquare;
		System.out.println("square of " + s  + " is: " +  sq);
		return null;
	}

	public void setHazelcastInstance(HazelcastInstance hz) {
		this.hz = hz;
	}

	@Override
	public void run() {
		System.out.println("starting print square task");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		call();
	}

}
