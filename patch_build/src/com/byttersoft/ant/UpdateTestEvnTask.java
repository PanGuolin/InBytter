package com.byttersoft.ant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * 根据pom信息从服务器下载最新环境包
 * @author pangl
 *
 */
public class UpdateTestEvnTask extends Task{

	/**
	 * 存放构建包的ftp地址
	 */
	private String ftpUrl = null;
	
	/**
	 * 本地存放更新包的目录
	 */
	private  File updateDir = null;
	
	private String jarMap = "BytterSSH.jar=framework.jar";
	
	/**
	 * 应用的配置文件文档名称，如果有更新，该文档也一定会被更新
	 */
	//private String warConfigName = "WEB-INF.zip";
	
	/**
	 * 保存在服务器上及本地的版本信息文件，以值-对形式保存
	 */
	private String versionFileName = "build_version.properties";
	
	/**
	 * 保存被替换的JAR包名称
	 */
	private String repalceJarsKey = "replace_jars";
	
	/**
	 * 最小更新间隔为5分钟
	 */
	private int updateInterval = 5;
	
	@Override
	public void execute() throws BuildException {
		if (updateDir == null)
			updateDir = new File("./update_temp");
		updateDir.mkdirs();
		log(updateDir.getAbsolutePath());
		File localVersionsFile = new File(updateDir, versionFileName);
		if (System.currentTimeMillis() - localVersionsFile.lastModified() < updateInterval*60*1000) {
			log("距上次更新时间不足" + updateInterval + "分钟，放弃本次更新！");
			return;
		}
		
		if (ftpUrl == null)
			throw new BuildException("You must set ftpUrl");
		if (!ftpUrl.endsWith("/"))
			ftpUrl += "/";
		
		Properties localVersions = new Properties();
		try {			
			//读取本地版本信息
			if (localVersionsFile.exists()) {
				localVersions.load(new FileInputStream(localVersionsFile));
			}
			//读取服务器版本信息
			Properties servVersions = new Properties();
			URL url = new URL(ftpUrl + versionFileName);
			servVersions.load(url.openStream());
			
			HashMap<String, String> maps = new HashMap<String, String>();
			if (jarMap != null) {
				String[] jarMaps = jarMap.split(";");
				for (String str : jarMaps) {
					String[] strs = str.split("=");
					maps.put(strs[0].trim(), strs[1].trim());
				}
			}
			
			int updateCount = 0;
			Enumeration<Object> keys = servVersions.keys();
			StringBuffer success = new StringBuffer();
			while(keys.hasMoreElements()) {
				String key = keys.nextElement().toString();
				String localVersion = localVersions.getProperty(key);
				String servVersion = servVersions.getProperty(key);
				//本地文件版本不存在或版本不匹配，则进行更新
				if (localVersion == null || !localVersion.equals(servVersion)) {
					if (update(key)) {
						log("成功更新文件:" + key);
						localVersions.setProperty(key, servVersion);
						updateCount ++;
						if (maps.get(key) != null)
							key = maps.get(key);
						if (key.endsWith(".jar")) {
							if (!key.equals("common.jar"))
								success.append(key.substring(0, key.length() - 4) + "*.jar;");
						}
					} else {
						log("更新文件时出错:" + key);
					}
					
					
				}
			}
			if (updateCount > 0) {
				log("成功更新" + updateCount + "个文件");
				if (success.toString().trim().length() > 0)
					getProject().setProperty(this.repalceJarsKey, success.toString());
				//if (update(warConfigName))
				//	log("成功更新文件：" + warConfigName);
			} else {
				log ("恭喜你，本地文件已经是最新的");
				log ("如果要强制更新全部，请删除" + updateDir + "/" + versionFileName);
			}
		} catch (Exception e) {
			throw new BuildException(e);
		} finally {
			//保存更新的信息，否则会出现不同步状态
			try {
				localVersions.store(new FileOutputStream(localVersionsFile), "update by UpdateTestEvnTask");
			} catch (IOException e) {
				log(e, Project.MSG_ERR);
				throw new BuildException(e);
			}
		}
	}
	
	/**
	 * 从ftp服务器上更新一个构建输出文件
	 * @param name
	 * @throws IOException
	 */
	private boolean update(String name) {
		try {
			URL url = new URL(ftpUrl + name);
			File localFile = new File(updateDir, name);
			OutputStream output = new FileOutputStream(localFile);
			InputStream in = url.openStream();
			byte[] bs = new byte[1024];
			int len = in.read(bs);
			while(len != -1) {
				output.write(bs, 0, len);
				len = in.read(bs);
			}
			in.close();
			output.flush();
			output.close();
		} catch (Exception ex) {
			log(ex, Project.MSG_ERR);
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) throws Exception {
//		UpdateTestEvnTask task = new UpdateTestEvnTask();
//		task.setUpdateDir(new File("E:\\workspaces\\20120515\\bytter_updater\\test"));
//		task.setFtpUrl("ftp://192.168.0.35/bytter_build/");
//		task.execute();
		System.out.println("acbcd.jar".substring(0, "acbcd.jar".length() - 4));
	}

	public void setFtpUrl(String ftpUrl) {
		this.ftpUrl = ftpUrl;
	}

	public void setUpdateDir(File updateDir) {
		this.updateDir = updateDir;
	}

	public void setVersionFileName(String versionFileName) {
		this.versionFileName = versionFileName;
	}

	public void setUpdateInterval(int updateInterval) {
		this.updateInterval = updateInterval;
	}

	public void setRepalceJarsKey(String repalceJarsKey) {
		this.repalceJarsKey = repalceJarsKey;
	}

	public void setJarMap(String jarMap) {
		this.jarMap = jarMap;
	}
	
}
