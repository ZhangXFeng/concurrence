package io.transwarp.concurrence;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConnectionManager {

	private static Log LOG = LogFactory.getLog(ConnectionManager.class);

	private static String DRIVER_NAME = "org.apache.hadoop.hive.jdbc.HiveDriver";
	public static Properties props = new Properties();
	public static String[] servers;

	private static int i;
	static {
		try {
			Class.forName(DRIVER_NAME);
			URL conf = Thread.currentThread().getContextClassLoader()
					.getResource("conf.properties");
			props.load(conf.openStream());
			String strServers = props.getProperty("inceptor.servers");
			servers = strServers.split(";");

		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}

	public  static Connection getConnection() {

		Connection conn = null;

		while (conn == null) {
			try {
				String url = "jdbc:hive://" + servers[i] + "/default";
				conn = DriverManager.getConnection(url);
				i=i==servers.length-1?0:i+1;
			} catch (Exception e) {
				LOG.error(e.getMessage());
			}
		}
		return conn;
	}
	
	public static void main(String[] args) {
		ConnectionManager.getConnection();
	}
}
