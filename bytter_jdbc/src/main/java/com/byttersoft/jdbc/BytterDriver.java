package com.byttersoft.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * JDBC驱动的实现类，用于包装JDBC驱动
 * 使用BytterDriver的连接URL必须是
 * PREFIX:dbtype:orgUrl
 * <UL>
 * <li>其中PREFIX 表示前缀，即jdbc:byttersoft:</li>
 * <li>dbtype 表示数据库类型，如oracle, 数据库类型列表可参考 DBType {@link DBType}</li>
 * <li>orgUrl 表示指定数据下的完整连接URL，如jdbc:oracle:thin:@192.168.0.62:1521:orcl</li>
 * </UL>
 * 完整的代码示例如：
 * <code>
 * Class.forName("com.byttersoft.jdbc.BytterDriver");
 * Connection conn = DriverManager.getConnection(
 * 	"jdbc:byttersoft:oracle:jdbc:oracle:thin:@192.168.0.62:1521:orcl", "clou", "123456");
 * Statement statement = conn.createStatement();
 * ResultSet rs = statement.executeQuery("select count(*) from bt_user");
 * while(rs.next()) {
 * 	System.out.println(rs.getInt(1));
 * }
 * conn.close();
 * </code>
 * 
 * @author pangl
 *
 */
public class BytterDriver implements Driver {
	
	//private static final Logger logger = Logger.getLogger(BytterDriver.class);
	
	/**
	 * 驱动前缀
	 */
	public static final String PREFIX = "jdbc:byttersoft:";
	
	public static final String DRIVER_LIST_URL = "com/byttersoft/jdbc/bytter_jdbc_driver.lst";
	
	private Driver orgDriver; 
	
	static {
		try {
			System.out.println("Load BytterDriver================");
			DriverManager.registerDriver(new BytterDriver());
		} catch (SQLException e) {
			e.printStackTrace();
			//logger.error("Error occured when register dirver", e);
		}
	}

	public Connection connect(String url, Properties info) throws SQLException {
		if (!acceptsURL(url))
			return null;
		DBType type = getDBType(url);
		loadDriver(type);
		String orgUrl = getOrgUrl(url);
		orgDriver = DriverManager.getDriver(orgUrl);
		if (orgDriver == null) { 
			System.out.println("Unsuitable database dirver in url:" + url);
			//logger.error("Unsuitable database dirver in url:" + url);
			return null;
		}
		
		
		return new BytterConnection(orgDriver.connect(orgUrl, info), type);
	}
	
	static String getOrgUrl(String url) {
		String orgUrl = url;
		url = url.toLowerCase();
		if (!url.startsWith(PREFIX))
			return null;
		url = url.substring(PREFIX.length());
		int index = url.indexOf(':');
		if (index == -1)
			return null;
		return orgUrl.substring(PREFIX.length() + index + 1);
	}
	
	static DBType getDBType(String url) {
		url = url.toLowerCase();
		if (!url.startsWith(PREFIX))
			return null;
		url = url.substring(PREFIX.length());
		int index = url.indexOf(':');
		if (index == -1)
			return null;
		return DBType.valueOf(url.substring(0, index));
	}

	public boolean acceptsURL(String url) throws SQLException {
		String orgUrl = url;
		url = url.toLowerCase();
		if (!url.startsWith(PREFIX))
			return false;
		url = url.substring(PREFIX.length());
		int index = url.indexOf(':');
		if (index == -1) 
			return false;
		String type = url.substring(0, index);
		DBType dbType = DBType.valueOf(type);
		if (dbType == null) {
			System.err.println("Unrecognizable database type in url:" + orgUrl);
			return false;
		}
		return true;
	}

	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
			throws SQLException {
		return orgDriver == null ? null : orgDriver.getPropertyInfo(url, info);
	}

	public int getMajorVersion() {
		return orgDriver == null ? 0 : orgDriver.getMajorVersion();
	}

	public int getMinorVersion() {
		return orgDriver == null ? 0 : orgDriver.getMinorVersion();
	}

	public boolean jdbcCompliant() {
		return orgDriver == null ? true : orgDriver.jdbcCompliant();
	}
	
	private static HashMap<DBType, List<Class<Driver>>> loadedTypes = new HashMap<DBType, List<Class<Driver>>>();
	/**
	 * 装载原始class类
	 */
	@SuppressWarnings("unchecked")
	static Class<Driver>[] loadDriver(DBType type) {
		List<Class<Driver>> list = loadedTypes.get(type);
		if (list == null) {
			list = new ArrayList<Class<Driver>>();
			try {
				Enumeration<URL> urls = BytterDriver.class.getClassLoader().getResources(DRIVER_LIST_URL);
				while(urls.hasMoreElements()) {
					URL url = urls.nextElement();
					BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
					String line = reader.readLine();
					while(line != null) {
						line = line.trim();
						if (line.startsWith("#")) {
							line = reader.readLine();
							continue;
						}
						int index = line.indexOf('=');
						if (index != -1) {
							String typeKey = line.substring(0, index);
							if (type.equals(DBType.valueOf(typeKey.trim()))) {
								String driverName = line.substring(index + 1).trim();
								try {
									Class<Driver> cls = (Class<Driver>) Class.forName(driverName);
									System.out.println("Driver " + driverName + " loaded for " + type);
									if (!list.contains(cls))
										list.add(cls);
								} catch (Exception ex) {
									ex.printStackTrace();
									System.out.println("Can't load driver :" + driverName);
								}
							}
						}
						line = reader.readLine();
					}
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("load driver error");
			}
			loadedTypes.put(type, list);
		}
		return (Class<Driver>[])list.toArray(new Class<?>[list.size()]);
	}
	
	/**
	 * 装载原始class类
	 * @return 如果没有合适的驱动类则返回null
	 * @throws IOException 如果无法加载文件或者读取配置文件出错时抛出该异常 
	 */
	@SuppressWarnings("unchecked")
	static Class<Driver> loadDriver2(DBType type) throws IOException {
		URL url = BytterDriver.class.getClassLoader().getResource(DRIVER_LIST_URL);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		try {
			String line = reader.readLine();
			while(line != null) {
				int index = line.indexOf('=');
				if (index != -1) {
					String typeKey = line.substring(0, index);
					if (type.equals(DBType.valueOf(typeKey.trim()))) {
						String driverName = line.substring(index + 1).trim();
						try {
							Class<Driver> cls = (Class<Driver>) Class.forName(driverName);
							return cls;
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
				line = reader.readLine();
			}
		} finally {
			reader.close();
		}
		return null;
	}
}
