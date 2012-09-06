package com.byttersoft.patchbuild.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

/**
 * 构建包的配置及状态信息
 * @author MickeyMic
 *
 */
public class BuildPackConfig {
	
	public static final String DIR_JAVA = "/src/main/java/";
	
	public static final String DIR_RESOURCE = "/src/main/resources/";
	
	public static final String DIR_WEB = "/src/main/webapp/";
	
	/**
	 * ����ID��
	 */
	private String id;
	
	/**
	 * ���ÿͻ�
	 */
	private String customer = "all";
	
	/**
	 * javaԴ�ļ��б?���Դ�ļ�֮���÷ֺ�����
	 */
	private String javaFiles = "";
	
	/**
	 * WEB�ļ��б?����ļ�֮���÷ֺ�����
	 */
	private String webFiles = "";
	
	/**
	 * ��Դ�ļ��б?����ļ�֮���÷ֺ�����
	 */
	private String resourceFiles = "";
	
	/**
	 * CS�ͻ����ļ��б?����ļ�֮���÷ֺ�����
	 */
	private String csFiles = "";
	
	/**
	 * ��Ӧ��VP���б�
	 */
	private String vps = "";
	
	private String version = "";
	
	private String developers = "";
	
	private String testers = "";
	
	private Set<String> modules = new HashSet<String>();
	
	private long buildTS = -1;
	
	private long deployTS = -1;
	
	/**
	 * 测试通过时间
	 */
	private long passTS = -1;

	/**
	 * 构建的依赖信息
	 */
	private Set<String> depends = new HashSet<String>();
	/**
	 * 构建说明
	 */
	private String comment;
	
	public BuildPackConfig(){
		
	}
	
	/**
	 * 从XML流中读取配置信息
	 * @param in
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static BuildPackConfig readFromStream(InputStream in, String id) throws Exception{
		BuildPackConfig config = new BuildPackConfig();
		config.id = id;
		
		SAXBuilder builder=new SAXBuilder(false);
		Document doc = builder.build(in);
		Element patch = doc.getRootElement();
		Namespace ns = patch.getNamespace();
		
		config.vps = patch.getChildTextTrim("vps", ns);
		config.customer = patch.getChildTextTrim("customer", ns);
		config.version = patch.getChildTextTrim("version", ns);
		config.testers = patch.getChildTextTrim("testers", ns);
		config.developers = patch.getChildTextTrim("developers", ns);
		config.comment = patch.getChildTextTrim("comment", ns);
		config.setDepends(patch.getChildTextTrim("depends", ns));
		
		String ts = patch.getChildTextTrim("build_ts", ns);
		if (ts != null)
			config.buildTS = Long.parseLong(ts);
		
		ts = patch.getChildTextTrim("deploy_ts", ns);
		if (ts != null)
			config.deployTS = Long.parseLong(ts);
		
		ts = patch.getChildTextTrim("pass_ts", ns);
		if (ts != null)
			config.passTS = Long.parseLong(ts);
		
		List<Element> files = patch.getChild("files", ns).getChildren("file", ns);
		//新的文件格式
		if (files.size() > 0) {
			for (Element fileEl : files) {
				config.addFile(fileEl.getTextTrim());
			}
		}
		Element chglogsEl = patch.getChild("change_logs", ns);
		if (chglogsEl != null) {
			List <Element> chgLogEls = chglogsEl.getChildren("change_log", ns);
			for (Element logEl : chgLogEls) {
				config.addChangeLog(logEl.getTextTrim());
			}
		}

		return config;
	}
	
	/**
	 * 从XML文件中读入配置信息
	 * @param xmlFile
	 * @return
	 * @throws Exception
	 */
	public static BuildPackConfig readFromFile(File xmlFile) throws Exception{
		String id = xmlFile.getName();
		if (id.indexOf(".") > 0) {
			id = id.substring(0, id.lastIndexOf('.'));
		}
		FileInputStream in = new FileInputStream(xmlFile);
		try {
			return readFromStream(in, id);
		} finally {
			in.close();
		}
	}


	public String toString() {
		Properties prop = toProperties("_pathConfig.");
		return prop.toString();
	}
	
	public String toXML() {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"GBK\" ?>\r\n");
		sb.append("<build xmlns=\"http://www.byttersoft.com\" \r\n");
		sb.append("	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \r\n"); 
		sb.append("	xsi:schemaLocation=\"http://www.byttersoft.com patch.xsd\">\r\n");
		sb.append("    <vps>" + vps + "</vps>\r\n");
		sb.append("    <customer>" + customer + "</customer>\r\n");
		sb.append("    <version>" + version + "</version>\r\n");
		sb.append("    <developers>" + developers + "</developers>\r\n");
		sb.append("    <testers>" + testers + "</testers>\r\n");
		sb.append("    <build_ts>" + buildTS + "</build_ts>\r\n");
		sb.append("    <deploy_ts>" + deployTS + "</deploy_ts>\r\n");
		sb.append("    <pass_ts>" + passTS + "</pass_ts>\r\n");
		sb.append("    <depends>" + listDepends() + "</depends>\r\n");
		sb.append("    <comment><![CDATA[\r\n" + comment + "\r\n]]></comment>\r\n");
	
		
		sb.append("    <files>\r\n");
		String[] files = getJavaFileList();
		for (String file : files) {
			sb.append("        <file>" + file + "</file>\r\n");
		}
		files = getWebFileList();
		for (String file : files) {
			sb.append("        <file>" + file + "</file>\r\n");
		}
		files = getResourceFileList();
		for (String file : files) {
			sb.append("        <file>" + file + "</file>\r\n");
		}
		files = getCsFileList();
		for (String file : files) {
			sb.append("        <file>" + file + "</file>\r\n");
		}
		sb.append("    </files>\r\n");
		
		if (this.changeLogs.size() > 0) {
			sb.append("    <change_logs>\r\n");
			for (ChangeLog log : changeLogs) {
				sb.append("        <change_log>" + log.toString() + "</change_log>\r\n");
			}
			sb.append("    </change_logs>\r\n");
		}
		sb.append("</build>");
		return sb.toString();
	}
	
	/**
	 * �����Properties����
	 * @param prefix KEY��ǰ׺
	 * @return
	 */
	public Properties toProperties(String prefix) {
		Properties prop = new Properties();
		prop.put(prefix + "id", id);
		prop.put(prefix + "vps", vps);
		prop.put(prefix + "customer", customer);
		prop.put(prefix + "version", version);
		prop.put(prefix + "testers", testers);
		prop.put(prefix + "developers", developers);
		prop.put(prefix + "modules", modules);
		prop.put(prefix + "javaFiles", javaFiles);
		prop.put(prefix + "webFiles", webFiles);
		prop.put(prefix + "csFiles", csFiles);
		prop.put(prefix + "resourceFiles", resourceFiles);
		StringBuilder sb = new StringBuilder();
		sb.append(javaFiles);
		if (webFiles.length() > 0)
			sb.append(";" + webFiles);
		if (resourceFiles.length() > 0)
			sb.append(";" + resourceFiles);
		if (csFiles.length() > 0)
			sb.append(";" + csFiles);
		prop.put(prefix + "allFiles", sb.toString() );
		return prop;
	}
	
	/**
	 * 新增一个构建文件
	 * @param path
	 * @return 如果指定的文件已存在或未知格式则返回false
	 */
	public boolean addFile(String path) {
		if (path.startsWith(".") || path.trim().length() == 0)
			return true;
		path = formatFilePath(path);
		modules.add(path.substring(0, path.indexOf('/')));
		if (path.startsWith("cs/")
				&& csFiles.indexOf(path) == -1) {
			csFiles += path;
			return true;
		}
		if (path.indexOf(DIR_JAVA) != -1 
				&& javaFiles.indexOf(path) == -1) {
			javaFiles += path;
			return true;
		} 
		if (path.indexOf(DIR_RESOURCE) != -1 
				&& resourceFiles.indexOf(path) == -1) {
			resourceFiles += path;
			return true;
		} 
		if (path.indexOf(DIR_WEB) != -1 
				&& webFiles.indexOf(path) == -1) {
			webFiles += path;
			return true;
		} 
		
		return false;
	}

	public String getId() {
		return id;
	}

	public String getCustomer() {
		return customer;
	}

	public String getJavaFiles() {
		return javaFiles;
	}
	
	private static final String[] EMPTY_ARRAY = new String[0];
	public String[] getJavaFileList() {
		return toList(javaFiles);
	}

	public String getWebFiles() {
		return webFiles;
	}
	
	public String[] getWebFileList() {
		return toList(webFiles);
	}

	public String getResourceFiles() {
		return resourceFiles;
	}
	
	public String[] getResourceFileList() {
		return toList(resourceFiles);
	}

	public String getCsFiles() {
		return csFiles;
	}
	
	public String[] getCsFileList() {
		return toList(csFiles);
	}
	
	/**
	 * 获取所有的文件列表
	 * @return
	 */
	public String[] getAllFiles() {
		Set<String> files = new HashSet<String>();
		for (String f : toList(javaFiles)) {
			files.add(f);
		}
		for (String f : toList(webFiles)) {
			files.add(f);
		}
		for (String f : toList(resourceFiles)) {
			files.add(f);
		}
		for (String f : toList(csFiles)) {
			files.add(f);
		}
		return (String[])files.toArray(new String[files.size()]);
	}
	
	private static String[] toList(String str) {
		if (str == null || str.trim().length() == 0)
			return EMPTY_ARRAY;
		return str.split(";");
	}

	public String getVps() {
		return vps;
	}

	public String getVersion() {
		return version;
	}

	public String getDevelopers() {
		return developers;
	}

	public String getTesters() {
		return testers;
	}

	public String getModules() {
		StringBuilder sb = new StringBuilder();
		for (String s : modules) {
			sb.append(s + ";");
		}
		return sb.toString();
	}
	
	public String[] listModules() {
		return (String[])modules.toArray(new String[modules.size()]);
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setId(String id) {
		id.replaceAll(";", "_");
		this.id = id.trim();
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public void setVps(String vps) {
		this.vps = vps;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setDevelopers(String developers) {
		this.developers = developers;
	}

	public void setTesters(String testers) {
		this.testers = testers;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (!(o instanceof BuildPackConfig))
			return false;
		BuildPackConfig conf = (BuildPackConfig)o;
		return id.equals(conf.id);
	}
	
	/**
	 * 判断是否包含某个文件
	 * @param file
	 * @return
	 */
	public boolean containsFile(String file) {
		file = formatFilePath(file);
		String low = file.toLowerCase();
		if( (this.csFiles.toLowerCase() + ";").indexOf(low) > -1)
			return true;
		if( (this.webFiles.toLowerCase() + ";").indexOf(low) > -1)
			return true;
		if( (this.javaFiles.toLowerCase() + ";").indexOf(low) > -1)
			return true;
		if( (this.resourceFiles.toLowerCase() + ";").indexOf(low) > -1)
			return true;
		return false;
	}
	
	private static String formatFilePath(String path) {
		path = path.trim().replaceAll("\\\\", "/");
		if (path.startsWith("/"))
			path = path.substring(1);
		path += ";";
		return path;
	}

	public long getBuildTS() {
		return buildTS;
	}

	public void setBuildTS(long buildTS) {
		this.buildTS = buildTS;
	}

	public long getDeployTS() {
		return deployTS;
	}

	public void setDeployTS(long deployTS) {
		this.deployTS = deployTS;
	}
	
	public void setDepends(String deps) {
		if (deps == null) return;
		String[] ps = deps.split(";");
		for (String p : ps) {
			this.depends.add(p.trim());
		}
	}
	
	/**
	 * 增加依赖包
	 * @param buildId
	 */
	public void addDepend(String buildId) {
		this.depends.add(buildId);
	}
	
	public String[] getDepends() {
		return (String[])depends.toArray(new String[depends.size()]);
	}
	
	public String listDepends() {
		StringBuilder sb = new StringBuilder();
		for (String d : depends) {
			if (sb.length() > 0)
				sb.append(";");
			sb.append(d);
		}
		return sb.toString();
	}
	
	/**
	 * 判断当前构建是否依赖指定ID的构建
	 * @param id
	 * @return
	 */
	public boolean isDepend(String id) {
		return depends.contains(id);
	}
	
	public boolean removeDepend(String id) {
		return depends.remove(id);
	}

	public long getPassTS() {
		return passTS;
	}

	public void setPassTS(long passTS) {
		this.passTS = passTS;
	}
	
	/** 变更历史 **/
	private List<ChangeLog> changeLogs = new ArrayList<ChangeLog>();
	public long addChangeLog(String user, String action) {
		long ts = System.currentTimeMillis();
		changeLogs.add(new ChangeLog(user, action, ts));
		return ts;
	}
	
	void addChangeLog(String log) {
		try {
			changeLogs.add(new ChangeLog(log));
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	List<ChangeLog> getChangeLogs() {
		List<ChangeLog> list = new  ArrayList<ChangeLog>();
		list.addAll(changeLogs);
		return list; 
	}
	
	void rollbackChange(long ts) {
		//TODO: 同步
		for (int i=0; i<changeLogs.size(); i++) {
			if (changeLogs.get(i).getTs() == ts) {
				changeLogs.remove(i);
				break;
			}
		}
	}
}
