package io.transwarp.concurrence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SqlPool {
	private static Log LOG=LogFactory.getLog(SqlPool.class);
	
	
	
	public static String getSql(){
		String sql="show tables";
		
		return sql;
	}

}
