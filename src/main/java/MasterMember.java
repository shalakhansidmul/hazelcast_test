import java.util.Map;
import java.util.UUID;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.durableexecutor.DurableExecutorService;

public class MasterMember {

	public static void main(String[] args) throws Exception {
		HazelcastInstance hz = Hazelcast.newHazelcastInstance();
		Thread.sleep(20000);
		Map<String, Integer> map = hz.getMap("map");
		for (int i = 1; i < 10; i++) {
			map.put(UUID.randomUUID().toString(), 1);
		}
		System.out.println("\nTotal Nos:" + map.keySet().size());
		DurableExecutorService executor = hz.getDurableExecutorService("executor");
		System.out.println("submitting sum task to durable executor");
		executor.submitToKeyOwner(new SumTask(),map.keySet().iterator().next());
		System.out.println("main complete");
		/*result.andThen(new ExecutionCallback<Integer>(){

			@Override
			public void onResponse(Integer response) {
				System.out.println("Result : " + response );
			}

			@Override
			public void onFailure(Throwable t) {
				System.out.println("task execution failed : "  + t);
			}

		});*/
		
		/*MultiExecutionCallback cb = new MultiExecutionCallback() {
			int result = 0;

			@Override
			public void onResponse(Member member, Object value) {
				if(value instanceof Integer){
					System.out.println("completed on member: " + member.getAddress().getPort());
					result = result + (Integer)value;
				}else{

				}
			}

			@Override
			public void onComplete(Map<Member, Object> values) {

				System.out.println("Result : " + result );
			}
		};
		
		Map<Member, Future<Integer>> result = executor.submitToAllMembers(new SumTask());
		int sum = 0;
        for (Member m: result.keySet()) {
        	Future<Integer> future	= result.get(m);
        	sum += future.get();
        }

        System.out.println("Result: " + sum);
		 */		

		

	}
}
