package io.transwarp.concurrence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Driver {
	private static Log LOG = LogFactory.getLog(Driver.class);

	public static void main(String[] args) {
		int threadsNum = Integer.parseInt(ConnectionManager.props.getProperty(
				"sqlrunner.number", "10"));
		final SqlRunner[] runners = new SqlRunner[threadsNum];

		for (int i = 0; i < runners.length; i++) {
			runners[i] = new SqlRunner();
			runners[i].start();
		}

		Thread countThread = new Thread(new Runnable() {

			@Override
			public void run() {

				int last = 0;
				int current = 0;
				long lastTime=System.currentTimeMillis();
				long currentTime;
				while (true) {
					try {
						Thread.currentThread().sleep(1000);
						
						
						for (int i = 0; i < runners.length; i++) {
							current += runners[i].getFinishedNum();
						}
						currentTime=System.currentTimeMillis();
						
						int count=current-last;
						if(count!=0){
							LOG.info("count: "+count+"; avg latency: "+((currentTime-lastTime)/count));
						}else{
							LOG.info("count: "+count+"; avg latency: -1");
						}
						
						lastTime=currentTime;
						last=current;
					} catch (Exception e) {
						LOG.error(e.getMessage());
					}

				}

			}
		});
		countThread.start();
	}

}
