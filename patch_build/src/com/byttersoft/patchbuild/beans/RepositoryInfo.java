package com.byttersoft.patchbuild.beans;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.byttersoft.patchbuild.utils.SVNUtil;

/**
 * 构建配置信息
 * @author pangl
 *
 */
public class RepositoryInfo {

	/**
	 * 区别于其它SVN库配置信息的标识名称
	 */
	private String name;
	
	/**
	 * SVN库的根目录（相对于project）
	 */
	private String svnUrl;
	
	/**
	 * SVN read user
	 */
	private String svnUser;
	
	/**
	 * svn read password
	 */
	private String svnPassword;
	
	/**
	 * tag拷贝的根目录（相对于project）
	 */
	private String tagUrl;
	
	private String svnRoot;
	
	/**
	 * 工程列表
	 */
	private List<String> projects = new ArrayList<String>();
	
	private File branchRoot;
	
	/**
	 * 构建的输出地址
	 */
	private File buildDir;
	
	/**
	 * 构建的发布地址
	 */
	private File deployDir;
	
	/**
	 * 已发布文件的备份地址
	 */
	private File deployBackupDir;
	/**
	 * 编译依赖的LIB目录
	 */
	private File compileLibDir;
	
	/**
	 * 编码依赖的class目录
	 */
	private File compileClassDir;
	
	/**
	 * 获取SVN的workcopy根目录
	 */
	private File workspace;
	
	/**
	 * 补丁文件名称模式
	 */
	private String versionNo = "";
	
	private String versionPattern = "yyMMdd";
	
	private String versionSuffix = "";
	
	
	public Set<String> testUsers = new HashSet<String>();
	
	public Set<String> deployUsers = new HashSet<String>();
	
	/**
	 * 保存用户名-密码对
	 */
	public Map<String, String> userAuthInfo = new HashMap<String, String>();
	
	/**
	 * versionInfo文件的路径
	 */
	private File versionInfo;

	public String[] getProjects() {
		if (projects.size() == 0) {
			String[] ps = SVNUtil.listSVNProjects("sp1");
			for (String p : ps) {
				if (!"sql".equalsIgnoreCase(p)) {
					projects.add(p);
				}
			}
		}
		
		return (String[])projects.toArray(new String[projects.size()]);
	}

	public void addProject(String project) {
		project = project.trim();
		if (!projects.contains(project)) {
			projects.add(project);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSvnUrl() {
		return svnUrl;
	}

	public void setSvnUrl(String svnUrl) {
		if (!svnUrl.endsWith("/"))
			svnUrl += "/";
		this.svnUrl = svnUrl;
	}

	public String getTagUrl() {
		if (tagUrl == null)
			tagUrl = svnUrl.replaceAll("branches", "tags");
		return tagUrl;
	}

	public void setTagUrl(String tagUrl) {
		if (tagUrl != null && !tagUrl.endsWith("/"))
			tagUrl += "/";
		this.tagUrl = tagUrl;
	}

	public File getBuildDir() {
		if (buildDir == null)
			buildDir = new File(branchRoot, "build");
		return buildDir;
	}
	
	/**
	 * 私家包存放地址
	 * @return
	 */
	public File getPrivateDir() {
		return new File(branchRoot, "private");
	}

	public void setBuildDir(File buildDir) {
		this.buildDir = buildDir;
	}

	public File getDeployDir() {
		if (deployDir == null)
			deployDir = new File(branchRoot, "deploy");
		return deployDir;
	}

	public void setDeployDir(File deployDir) {
		this.deployDir = deployDir;
	}

	public void setProjects(List<String> projects) {
		this.projects = projects;
	}

	public File getCompileLibDir() {
		if (compileLibDir == null)
			compileLibDir = new File(branchRoot, "lib");
		return compileLibDir;
	}

	public void setCompileLibDir(File compileLibDir) {
		this.compileLibDir = compileLibDir;
	}

	public File getCompileClassDir() {
		if (compileClassDir == null)
			compileClassDir = new File(branchRoot, "classes");
		return compileClassDir;
	}

	public void setCompileClassDir(File compileClassDir) {
		this.compileClassDir = compileClassDir;
	}

	public File getDeployBackupDir() {
		if (deployBackupDir == null)
			deployBackupDir = new File(branchRoot, "deployBack");
		return deployBackupDir;
	}

	public void setDeployBackupDir(File deployBackupDir) {
		this.deployBackupDir = deployBackupDir;
	}

	public File getWorkspace() {
		if (workspace == null)
			workspace = new File(branchRoot, "workspace");
		return workspace;
	}

	public void setWorkspace(File workspace) {
		this.workspace = workspace;
	}

	public String getSvnUser() {
		return svnUser;
	}

	public void setSvnUser(String svnUser) {
		this.svnUser = svnUser;
	}

	public String getSvnPassword() {
		return svnPassword;
	}

	public void setSvnPassword(String svnPassword) {
		this.svnPassword = svnPassword;
	}
	
	public File getSvnWorkspace() {
		return new File(getWorkspace(), "svn");
	}
	
	/**
	 * SQL脚本存放的根路径
	 */
	private String sqlReposRoot = "/sql/patch/";
	public String getSqlRepos() {
		return sqlReposRoot;
	}
	
	/**
	 * SQL脚本的编码
	 */
	private String sqlEncoding = "GBK";
	public String getSqlEncoding() {
		return sqlEncoding;
	}

	public void setSqlEncoding(String sqlEncoding) {
		this.sqlEncoding = sqlEncoding;
	}

	/**
	 * SQL脚本的后缀列表
	 */
	private String[] sqlSuffixs = new String[] {"_SQL2005.sql", "_ORCL.sql"};
	public String[] getSqlSuffixs() {
		return sqlSuffixs;
	}

	public String getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	public String getVersionPattern() {
		return versionPattern;
	}

	public void setVersionPattern(String versionPattern) {
		this.versionPattern = versionPattern;
	}

	public String getVersionSuffix() {
		return versionSuffix;
	}

	public void setVersionSuffix(String versionSuffix) {
		this.versionSuffix = versionSuffix;
	}

	public String getSvnRoot() {
		return svnRoot;
	}

	public void setSvnRoot(String svnRoot) {
		if (!svnRoot.startsWith("/"))
			svnRoot = "/" + svnRoot;
		if (!svnRoot.endsWith("/"))
			svnRoot += "/";
		this.svnRoot = svnRoot;
	}

	public File getVersionInfo() {
		if (versionInfo == null)
			versionInfo = new File(branchRoot, "configs/versioninfo.xml");
		return versionInfo;
	}

	public void setVersionInfo(File versionInfo) {
		this.versionInfo = versionInfo;
	}

	public File getBranchRoot() {
		return branchRoot;
	}

	public void setBranchRoot(File branchRoot) {
		this.branchRoot = branchRoot;
	}

	public void setTestUsers(String testUsers) {
		if (testUsers == null)
			return;
		String[] users = testUsers.split(";");
		for (String u : users) {
			u = u.trim();
			if (u.length() == 0)
				continue;
			if (u.indexOf('=') != -1) {
				int index = u.indexOf('=');
				String name = u.substring(0, index);
				if (name.trim().length() == 0)
					continue;
				String psw = u.substring(index + 1);
				this.testUsers.add(name.trim());
				this.userAuthInfo.put(name.trim(), psw.trim());
			} else {
				this.testUsers.add(u);
			}
		}
	}
	
	public boolean isTestUser(String user) {
		return this.testUsers.contains(user);
	}

	public void setDeployUsers(String deployUsers) {
		if (deployUsers == null)
			return;
		String[] users = deployUsers.split(";");
		for (String u : users) {
			u = u.trim();
			if (u.length() == 0)
				continue;
			if (u.indexOf('=') != -1) {
				int index = u.indexOf('=');
				String name = u.substring(0, index);
				if (name.trim().length() == 0)
					continue;
				String psw = u.substring(index + 1);
				this.deployUsers.add(name.trim());
				this.userAuthInfo.put(name.trim(), psw.trim());
			} else {
				this.deployUsers.add(u);
			}
		}
	}
	
	public boolean isDeployUser(String user) {
		return this.deployUsers.contains(user);
	}
	
	public File getLogRoot() {
		return new File(branchRoot, "logs");
	}
	
	public boolean validUser(String user, String password) {
		String psw = this.userAuthInfo.get(user);
		return password.equals(psw);
	}
	
}
