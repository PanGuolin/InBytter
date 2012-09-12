package com.byttersoft.patchbuild.beans;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import com.byttersoft.patchbuild.service.BuildReposManager;
import com.byttersoft.patchbuild.utils.PatchUtil;

/**
 * 补丁文件对象
 * @author pangl
 *
 */
public class PatchFile implements Comparable<PatchFile>{
	
	private static volatile Map<File, PatchFile> patchFiles = new Hashtable<File, PatchFile>();
	
	
	public static PatchFile getPatchFile(File file, String branch) {
		PatchFile patch = patchFiles.get(file);
		if (patch == null) {
			synchronized (patchFiles) {
				patch = patchFiles.get(file);
				if (patch == null) {
					patch = new PatchFile(file, branch);
					patchFiles.put(file, patch);
				}
			}
		}
		return patch;
	}

	/**
	 * 补丁文件对象
	 */
	private final File file;
	
	/**
	 * 所属分支
	 */
	private final String branch;
	
	private final File backupRoot;
	
	
	/**
	 * 构建一个补丁文件
	 * @param file 文件实际位置
	 * @param branch 所属分支
	 */
	private PatchFile(File file, String branch) {
		this.file = file;
		this.branch = branch;
		RepositoryInfo repos = BuildReposManager.getByName(branch);
		String patchName = file.getName();
		if (patchName.toLowerCase().endsWith(".zip"))
			patchName = patchName.substring(0, patchName.length() - ".zip".length());
		patchName = patchName.substring(repos.getVersionNo().length());
		patchName = patchName.substring(0, patchName.length() - repos.getVersionSuffix().length());
		
		SimpleDateFormat df = new SimpleDateFormat(repos.getVersionPattern()); 
		String path = "";
		try {
			path = PatchUtil.getBackupDir(df.parse(patchName));
			
		} catch (ParseException e) {
			//此处不应该出错，所以仅做打印处理
			e.printStackTrace();
		}
		backupRoot = new File(repos.getDeployBackupDir(), path);
	}

	public File getFile() {
		return file;
	}

	public String getBranch() {
		return branch;
	}
	
	public String getName() {
		return file.getName();
	}

	public int compareTo(PatchFile o) {
		return (int) (o.file.length() - file.length());
//		int value = (int) (o.file.lastModified() - file.lastModified());
//		if (value != 0)
//			return value;
//		return o.file.getName().compareTo(file.getName());
	}
	
	private static SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public String getLastModify() {
		Date date = new Date(file.lastModified());
		return formater.format(date);
	}
	
	
	public long getSize() {
		return file.length();
	}

	/**
	 * 包含多少个当天已发布的构建包
	 * @return
	 */
	public int getIncludePBSize() {
		File[] fs = backupRoot.listFiles();
		if (fs == null)
			return 0;
		return fs.length;
	}

}
