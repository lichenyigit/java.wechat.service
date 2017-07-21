package java.wechat.service.cache;

import java.util.LinkedHashMap;
import java.wechat.service.util.ExecutorUtil;
import java.wechat.service.util.ExecutorUtil.Job;

public class TimerCache<Key,Value> {
	private static int counter = 0;
	private int size;
	private long vaildTime;
	private int pCounter = ++TimerCache.counter;
	private final LinkedHashMap<Key, Container> map; 
	public TimerCache(int size,long vaildTime) {
		map = new LinkedHashMap<Key, Container>(){
			private static final long serialVersionUID = 3240397593603585518L;
			@Override
			protected boolean removeEldestEntry(java.util.Map.Entry<Key, Container> eldest) {
				return size()>TimerCache.this.size;
			}
		};
		this.size = size;
		this.vaildTime = vaildTime;
	}
	
	public Value put(final Key key,Value value){
		final String refKey;
		synchronized (this) {
			refKey = "TimerCache-"+pCounter+"-"+System.currentTimeMillis()+"-"+System.nanoTime();
		}
		Container container = new Container(value, refKey);
		synchronized (map) {
			container = map.put(key, container);			
		}
		ExecutorUtil.scheduleJobAtTime(new Job() {
			@Override
			public void run() {
				synchronized (map) {
					map.remove(key);					
				}
			}
			@Override
			public String getRefKey() {
				return refKey;
			}
		}, vaildTime+System.currentTimeMillis());
		if(container!=null){
			ExecutorUtil.cancelScheduleJob(container.refKey);
			return container.value;
		}
		return null;
	}
	
	public Value get(Key key){
		Container container = map.get(key);
		if(container!=null){
			return container.value;
		}
		return null;
	}
	
	private class Container{
		private Value value;
		private String refKey;
		public Container(Value value,String refKey) {
			this.value = value;
			this.refKey = refKey;
		}
	}
}
