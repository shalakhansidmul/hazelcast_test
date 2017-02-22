import java.io.Serializable;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.durableexecutor.DurableExecutorService;

public class PutSumInMap implements Runnable, Serializable, HazelcastInstanceAware {
	Integer sumTaskResult = 0;
	private transient HazelcastInstance hz;
	
	public PutSumInMap(Integer sumTaskResult) {
		super();
		this.sumTaskResult = sumTaskResult;
	}



	public Object call() {
		System.out.println("starting callback");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Result  = " + sumTaskResult);	
		hz.getMap("context").put("sum", sumTaskResult);
		System.out.println("sumitting square task to executor.");
		DurableExecutorService executor = this.hz.getDurableExecutorService("executor");
		executor.submitToKeyOwner(new SquareTask(),"sum");
		return null;
		
	}



	@Override
	public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
		this.hz = hazelcastInstance;
	}



	@Override
	public void run() {
		call();
	}

}
