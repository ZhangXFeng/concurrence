package io.transwarp.concurrence;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SqlRunner extends Thread {
	private static Log LOG = LogFactory.getLog(SqlRunner.class);
	private int finished;
	@Override
	public void run() {
		Connection conn = ConnectionManager.getConnection();
		while (true) {
			try {
				
				String sql = SqlPool.getSql();
				Statement stmt = conn.createStatement();
				stmt.execute(sql);
				finished++;
//				LOG.info("endtime: "+new Date()+" ;finished: "+finished);
			} catch (Exception e) {
				LOG.error(e.getMessage());
			}

		}
	}
	public int getFinishedNum(){
		return finished;
	}
	
	public static void main(String[] args) {
		SqlRunner sr=new SqlRunner();
		sr.run();
	}

}
