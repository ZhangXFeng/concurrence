package io.transwarp.concurrence;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConnectionManager {

	private static Log LOG = LogFactory.getLog(ConnectionManager.class);

	private static String DRIVER_NAME = "org.apache.hadoop.hive.jdbc.HiveDriver";
	public static Properties props = new Properties();
	public static String[] servers;

	private static int i;
	private static Map<String, String> parameters = new HashMap<String, String>();
	static {
		try {
			Class.forName(DRIVER_NAME);
			URL conf = Thread.currentThread().getContextClassLoader()
					.getResource("conf.properties");
			props.load(conf.openStream());
			String strServers = props.getProperty("inceptor.servers");
			servers = strServers.split(";");
			Set<Object> keys = props.keySet();
			for (Iterator<Object> iterator = keys.iterator(); iterator
					.hasNext();) {
				String key = (String) iterator.next();
				if (key.startsWith("parameters.")) {
					String modifiedKey = key.substring(key.indexOf(".") + 1);
					String value = props.getProperty(key);

					if (null != value && "" != value) {
						parameters.put(modifiedKey, value);
					}
				}

			}

		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}

	public static Connection getConnection() {

		Connection conn = null;
		try {
			while (conn == null) {

				String url = "jdbc:hive://" + servers[i] + "/default";
				conn = DriverManager.getConnection(url);
				i = i == servers.length - 1 ? 0 : i + 1;

			}
			
			Statement stmt=conn.createStatement();
			Set<Map.Entry<String, String>> entries=parameters.entrySet();
			for (Iterator<Entry<String, String>> iterator = entries.iterator(); iterator.hasNext();) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator
						.next();
				String sql="set "+entry.getKey()+"="+entry.getValue();
				stmt.execute(sql);
				
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}

		return conn;
	}

	public static void main(String[] args) {
		ConnectionManager.getConnection();
	}
}
