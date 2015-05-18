package io.transwarp.concurrence;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SqlPool {
	private static Log LOG=LogFactory.getLog(SqlPool.class);
	
	private static ArrayList<String> sqls=new ArrayList<String>();
	private static String fileName="sqls.sql";
	static{
		
		try {
			URL path=Thread.currentThread().getContextClassLoader().getResource(fileName);
			BufferedReader br=new BufferedReader(new InputStreamReader(path.openStream()));
			String line=null;
			while(null!=(line=br.readLine())){
				sqls.add(line);
				
			}
			br.close();
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		
	}
	
	public static String getSql(){
		int index=(int) (Math.random()*sqls.size());
		
		return sqls.get(index);
	}

}
