import java.io.Serializable;
import java.util.Map;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.durableexecutor.DurableExecutorService;

public class SumTask implements Runnable, Serializable, HazelcastInstanceAware {

    private transient HazelcastInstance hz;
    public void setHazelcastInstance(HazelcastInstance hz) {
        this.hz = hz;
    }

    public void run()  {
        Map<String, Integer> map = hz.getMap("map");
        System.out.println("starting task");
        int result = 0;
        for (String key : map.keySet()) {
            System.out.println("Calculating for key: " + key);
            result += map.get(key);
            try {
				Thread.sleep(5*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
          }
        System.out.println("Local result: " + result);
        submitCallbackToExecutor(result,map);
        System.out.println("sum task completed");
    }

	private void submitCallbackToExecutor(int result, Map<String, Integer> map) {
		PutSumInMap rc = new PutSumInMap(result);
		DurableExecutorService executor = this.hz.getDurableExecutorService("executor");
		executor.submitToKeyOwner(rc,map.keySet().iterator().next());
	}
}
