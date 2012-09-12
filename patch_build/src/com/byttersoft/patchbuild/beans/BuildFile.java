package com.byttersoft.patchbuild.beans;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.byttersoft.patchbuild.cache.Cacheable;
import com.byttersoft.patchbuild.service.BuildReposManager;
import com.byttersoft.patchbuild.utils.AntTaskUtil;

/**
 * 构建包文件对象
 * @author pangl
 *
 */
public class BuildFile implements Comparable<BuildFile>, Cacheable{
	
	/**
	 * 所属分支
	 */
	private String branchName;
	
	/**
	 * 文件名称
	 */
	private File file;
	
	
	private BuildConfig config;
	
	/**
	 * 是否包含SQL文件
	 */
	private boolean includeSqls;
	
	/**
	 * 最后载入时间
	 */
	private long lastLoadTime = -1;
	
	
	/**
	 * 创建一个构建包信息对象
	 * @param branch 所属分支
	 * @param file 构建包文件，以ZIP格式压缩
	 */
	public BuildFile(String branchName, File file) throws Exception{
		this.branchName = branchName;
		this.file = file;
		reload();
	}
	
	public String getBranchName() {
		return branchName;
	}

	public String getFileName() {
		return file.getName();
	}
	
	public File getFile() {
		return this.file;
	}

	public String getSubmiter() {
		return config.getDevelopers();
	}

	/**
	 * 获取构建时间，格式为 MM-dd HH:mm:ss
	 * @return
	 */
	public String getBuildTime() {
		Date date = new Date(config.getBuildTS());
		return new SimpleDateFormat("MM-dd HH:mm:ss").format(date);
	}
	
	public String getDeployTime() {
		Date date = new Date(config.getDeployTS());
		return new SimpleDateFormat("MM-dd HH:mm:ss").format(date);
	}
	
	/**
	 * 测试通过时间,格式为MM-dd HH:mm:ss
	 * @return 如果没有通过测试则返回""
	 */
	public String getTestPassTime() {
		Date date = new Date(config.getDeployTS());
		return new SimpleDateFormat("MM-dd HH:mm:ss").format(date);
	}

	public String getVps() {
		return config.getVps();
	}

	public int compareTo(BuildFile o) {
		int res = branchName.compareTo(o.branchName);
		if (res == 0)
			res = getBuildTime().compareTo(o.getBuildTime());
		if (res == 0)
			res = getFileName().compareTo(o.file.getName());
		return res;
	}


	public BuildConfig getConfig() {
		return config;
	}
	
	/**
	 * 替换内容
	 * @param contextXML
	 */
	public void updateConfig(String contextXML) throws Exception {
		ByteArrayInputStream in = new ByteArrayInputStream(contextXML.getBytes());
		BuildConfig config = BuildConfig.readFromStream(in, getConfig().getId());
		this.config = config;
	}
	
	public boolean getIncludeSqls() {
		return this.includeSqls;
	}
	
	private boolean isStoring = false;
	/**
	 * 将配置信息写到ZIP包中
	 */
	public void storeConfig() {
		if (isStoring || !file.exists()) return;
		Thread t = new Thread("store" + config.getId()) {
			public void run() {
				synchronized (BuildFile.class) {
					isStoring = true;
					storeAndWait();
					isStoring = false;
				}
			}
		};
		t.start();
	}
	
	/**
	 * 将配置信息写到ZIP包中并等待返回
	 */
	public void storeAndWait() {
		if (isStoring || !file.exists()) return;
		String name = file.getName();
		name = name.substring(0, name.lastIndexOf('.'));
		File root = BuildReposManager.getByName(branchName).getWorkspace();
		root = new File(root, "temp_" + System.currentTimeMillis() + "/" + name);
		root.mkdirs();
		File xmlFile = new File(root, name + ".xml");
		AntTaskUtil.unzip(file, root, null);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(xmlFile));
			writer.write(config.toXML());
			writer.flush();
			writer.close();
			AntTaskUtil.zip(root, file, null);
			AntTaskUtil.deleteDir(root, null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		lastLoadTime = file.lastModified();
		updateUseTime();
		root.delete();
	}
	
	/**
	 * 重新载入配置信息
	 * @throws Exception
	 */
	public void reload() throws Exception {
		if (this.lastLoadTime == file.lastModified())
			return;
		ZipFile zipFile = new ZipFile(file);
		String name = file.getName();
		name = name.substring(0, name.lastIndexOf('.'));
		try {
			ZipEntry entry = zipFile.getEntry(name + ".xml");
			if (entry != null) {
				this.config = BuildConfig.readFromStream(zipFile.getInputStream(entry), name);
				Enumeration<? extends ZipEntry> enu = zipFile.entries();
				while(enu.hasMoreElements()) {
					ZipEntry ze = enu.nextElement();
					if (ze.getName().startsWith("sql/") && !ze.isDirectory()) {
						this.includeSqls = true;
					}
				}
			}
		} finally {
			zipFile.close();
		}
		this.lastLoadTime = file.lastModified();
	}
	
	public boolean hasTester() {
		return getTester().length() > 0;
	}
	public String getTester() {
		String t = config.getTesters();
		if ("null".equals(t))
			return "";
		return t == null ? "" : t;
	}
	
	public String getDepends() {
		return config.listDepends();
	}

	private long lastUseTime = -1;
	public long getLastUseTime() {
		return lastUseTime;
	}

	public void updateUseTime() {
		lastUseTime = System.currentTimeMillis();
	}
	
	public long addChangeLog(String user, String action) {
		return config.addChangeLog(user, action);
	}
	
	public String[] getChangeLogs() {
		List<ChangeLog> logs = config.getChangeLogs();
		String[] chs = new String[logs.size()];
		for (int i=0; i<logs.size(); i++) {
			ChangeLog log = logs.get(i);
			chs[i] = log.getLog();
		}
		return chs;
	}
}
