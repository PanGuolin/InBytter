package com.byttersoft.patchbuild.service;

import java.io.File;
import java.io.FileFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TreeSet;

import com.byttersoft.code.Base64Util;
import com.byttersoft.patchbuild.beans.BuildPackInfo;
import com.byttersoft.patchbuild.beans.PatchInfo;
import com.byttersoft.patchbuild.beans.RepositoryInfo;
import com.byttersoft.patchbuild.utils.PatchUtil;

/**
 * 补丁包服务对象
 * @author pangl
 *
 */
public class PatchService {

	/**
	 * 产生加密补丁包
	 * @param branch
	 * @param patchName
	 */
	public static File encodePatch(String branch, String patchName) {
		if (patchName.equalsIgnoreCase("latest.zip"))
			return null;
		RepositoryInfo repos = BuildReposManager.getByName(branch);
		File file = new File(repos.getDeployDir(), patchName);
		if (!file.exists())
			return null;
		File dest = new File(repos.getDeployDir(), patchName.substring(0, patchName.lastIndexOf('.')) + ".patch");
		if (dest.exists() && dest.lastModified() > file.lastModified()) {
			return dest;
		}
		try {
			Base64Util.encodeFile(file.getAbsolutePath(), dest.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dest;
	}
	
	/**
	 * 列出所有补丁信息
	 * @param branch 分支名称
	 * @return
	 */
	public static PatchInfo[] listPachInfo(String branch) {
		RepositoryInfo repos = BuildReposManager.getByName(branch);
		File file = repos.getDeployDir();
		File[] patchs = file.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".zip");
			}
		});
		TreeSet<PatchInfo> pInfos = new TreeSet<PatchInfo>();
		if (patchs != null) {
			for (File f : patchs) {
				if (f.isDirectory() || f.getName().equals("latest.zip"))
					continue;
				PatchInfo info = new PatchInfo();
				info.setBranch(branch);
				info.setFile(f);
				try {
					File[] fs = getRootOfBPByPatch(branch, f.getName()).listFiles();
					if (fs != null)
						info.setIncludePBSize(fs.length);
				} catch (Exception ex) {ex.printStackTrace();}
				pInfos.add(info);
			}
		}
		return (PatchInfo[])pInfos.toArray(new PatchInfo[pInfos.size()]);
		
	}
	
	/**
	 * 列出补丁包中包含的构建包列表
	 * @param branch
	 * @param patchName
	 * @return
	 * @throws ParseException
	 */
	public static File getRootOfBPByPatch(String branch, String patchName) throws ParseException {
		RepositoryInfo repos = BuildReposManager.getByName(branch);
		if (patchName.toLowerCase().endsWith(".zip"))
			patchName = patchName.substring(0, patchName.length() - ".zip".length());
		patchName = patchName.substring(repos.getVersionNo().length());
		patchName = patchName.substring(0, patchName.length() - repos.getVersionSuffix().length());
		
		SimpleDateFormat df = new SimpleDateFormat(repos.getVersionPattern()); 
		String path = PatchUtil.getBackupDir(df.parse(patchName));
		File root = new File(repos.getDeployBackupDir(), path);
		return root;
	}
	
	/**
	 * 列出补丁中包含的构建包
	 * @param pathName
	 * @return
	 * @throws ParseException 
	 */
	public static BuildPackInfo[] listBuildPackInfoOfPatch(String branch, String patchName) throws ParseException {
		
		File[] zips = getRootOfBPByPatch(branch, patchName).listFiles();
		if (zips == null  || zips.length == 0) {
			return new BuildPackInfo[0];
		}
		
		TreeSet<BuildPackInfo> bpInfos = new TreeSet<BuildPackInfo>();
		for (File zip : zips) {
			if (zip.getName().endsWith(".zip")) {
				try {
					BuildPackInfo info = BuildPackService.getBuildPackInfo(branch, zip);
					bpInfos.add(info);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return (BuildPackInfo[])bpInfos.toArray(new BuildPackInfo[bpInfos.size()]);
	}
	
}
