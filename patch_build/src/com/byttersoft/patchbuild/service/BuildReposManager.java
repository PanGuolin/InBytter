package com.byttersoft.patchbuild.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import com.byttersoft.patchbuild.beans.RepositoryInfo;

/**
 * 构建信息管理类，主要提供读取及获取引用的功能
 * @author pangl
 *
 */
public abstract class BuildReposManager {
	
	public static final String SVN_LIST_FILE_NAME = "build_repositories.xml";
	
	private static Map<String, RepositoryInfo> infos = new Hashtable<String, RepositoryInfo>();
	private static File configFile = null;
	private static long lastModify = -1;
	
	/**
	 * 初始化并检查配置文件是否已经被修改
	 */
	public static void init() {
		synchronized (infos) {
			if (configFile == null) {
				URL res = BuildReposManager.class.getClassLoader().getResource(SVN_LIST_FILE_NAME);
				configFile = new File(res.getFile());
				if (!configFile.isFile() && !configFile.exists()) {
					configFile = null;
					return;
				}
				lastModify = configFile.lastModified();
				try {
					readConfig(res.openStream(), false);
				} catch (Exception e) {
					configFile = null;
					e.printStackTrace();
				}
			} else if (lastModify != configFile.lastModified()) {
				try {
					readConfig(new FileInputStream(configFile), false);
					lastModify = configFile.lastModified();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
		

	/**
	 * 读取SVN信息库的XML配置
	 * @param in 解析后将被关闭
	 * @param append 是否合并，false：合并， true:读取配置之前先清空当前内存中的配置信息
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	@SuppressWarnings("rawtypes")
	public static void readConfig(InputStream in, boolean append) throws JDOMException, IOException {
		SAXBuilder builder=new SAXBuilder(false);
		Document doc = builder.build(in);
		Element root = doc.getRootElement();
		Namespace ns = root.getNamespace();
		if (!append)
			infos.clear();
		List list = root.getChildren("repository", ns);
		for (int i=0; i<list.size(); i++) {
			Element reposEl = (Element) list.get(i);
			RepositoryInfo reposInfo = new RepositoryInfo();
			reposInfo.setName(reposEl.getChildTextTrim("name", ns).toLowerCase());
			reposInfo.setSvnUrl(reposEl.getChildTextTrim("svn_url", ns));
			reposInfo.setTagUrl(reposEl.getChildTextTrim("tag_url", ns));
			reposInfo.setSvnUser(reposEl.getChildTextTrim("svn_user", ns));
			reposInfo.setSvnPassword(reposEl.getChildTextTrim("svn_password", ns));
			reposInfo.setTestUsers(reposEl.getChildTextTrim("test-users", ns));
			reposInfo.setDeployUsers(reposEl.getChildTextTrim("deploy-users", ns));
			
			String value = reposEl.getChildTextTrim("version_no", ns);
			if (value != null)
				reposInfo.setVersionNo(value);
			value = reposEl.getChildTextTrim("version_pattern", ns);
			if (value != null)
				reposInfo.setVersionPattern(value);
			value = reposEl.getChildTextTrim("version_suffix", ns);
			if (value != null)
				reposInfo.setVersionSuffix(value);
			value = reposEl.getChildTextTrim("sql_encoding", ns);
			if (value != null)
				reposInfo.setSqlEncoding(value);
			value = reposEl.getChildTextTrim("svn_root", ns);
			if (value != null)
				reposInfo.setSvnRoot(value);
			
			reposInfo.setBuildDir(toFile(reposEl, ns, "build_dir"));
			reposInfo.setDeployDir(toFile(reposEl, ns, "deploy_dir"));
			reposInfo.setCompileLibDir(toFile(reposEl, ns, "compile_lib_dir"));
			reposInfo.setCompileClassDir(toFile(reposEl, ns, "compile_class_dir"));
			reposInfo.setDeployBackupDir(toFile(reposEl, ns, "deploy_backup_dir"));
			reposInfo.setWorkspace(toFile(reposEl, ns, "workspace"));
			reposInfo.setVersionInfo(toFile(reposEl, ns, "version_info"));
			reposInfo.setBranchRoot(toFile(reposEl, ns, "branchRoot"));
			
			
			String ps = reposEl.getChildTextTrim("projects", ns);
			if (ps != null) {
				String[] projects = reposEl.getChildTextTrim("projects", ns).split(";");
				for (String p : projects){
					reposInfo.addProject(p.trim());
				}
			}
			infos.put(reposInfo.getName(), reposInfo);
		}
		in.close();
	}
	
	/**
	 * 读取DOM元素内容并转成File对象
	 * @param reposEl
	 * @param ns
	 * @param elementName
	 * @return 如果不存在DOM元素则返回null
	 */
	private static File toFile(Element reposEl, Namespace ns, String elementName) {
		String file = reposEl.getChildTextTrim(elementName, ns);
		if (file != null)
			return new File(file);
		return null;
	}
	
	/**
	 * 列出所有SVN库信息的名称
	 * @return 如果不存在则返回String[0]
	 */
	public static String[] listNames() {
		init();
		Set<String> set = infos.keySet();
		return (String[])set.toArray(new String[set.size()]);
	}
	
	/**
	 * 根据名称返回SVN库信息
	 * @param name 配置的SVN库信息名称
	 * @return 如果不存在则返回null
	 */
	public static RepositoryInfo getByName(String name) {
		init();
		return infos.get(name.toLowerCase());
	}
}
