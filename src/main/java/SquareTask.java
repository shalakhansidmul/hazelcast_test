import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.durableexecutor.DurableExecutorService;

import java.io.Serializable;
import java.util.concurrent.Callable;

public class SquareTask implements Runnable, Serializable, HazelcastInstanceAware {

    private transient HazelcastInstance hz;

    public void setHazelcastInstance(HazelcastInstance hz) {
        this.hz = hz;
    }

    public Integer call() throws Exception {
        IMap<String, Object> map = hz.getMap("context");
        Object objectSum = map.get("sum");
        Integer s = (Integer)objectSum;
        System.out.println("Calculating square of: "+ s);
        map.put("square", s*s);
        System.out.println("square task sleeping");
        Thread.sleep(10000);
        System.out.println("sumitting PrintSquare task to executor");
        DurableExecutorService executor = this.hz.getDurableExecutorService("executor");
		executor.submitToKeyOwner(new PrintSquare(),"square");
        return (s*s);
    }
    @Override
	public void run() {
		try {
			call();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
