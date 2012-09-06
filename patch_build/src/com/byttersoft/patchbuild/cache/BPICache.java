package com.byttersoft.patchbuild.cache;

import java.io.File;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import com.byttersoft.patchbuild.beans.BuildPackInfo;

/**
 * Build Package Info Cache Manager
 * 缓存系统中的BuildPackInfo对象，避免频繁读取文件
 * @author pangl
 *
 */
public abstract class BPICache {
	
	private static int MAX_CACHE_SIZE = 500;
	
	private static long EXPIRE_INTERVAL = 60*60*1000;
	
	private static Map<File, BuildPackInfo> infos = new Hashtable<File, BuildPackInfo>();
	
	/**
	 * 获取一个BuildPackInfo对象，并视条件进行缓存
	 * @param branchName
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static BuildPackInfo getBuildPackInfo(String branchName, File file) {
		if (!file.exists()) {
			infos.remove(file);
			return null;
		}
		BuildPackInfo info = infos.get(file);
		if (info == null) {
			synchronized (infos) {
				 info = infos.get(file);
				 if (info == null) {
					 try {
						info = new BuildPackInfo(branchName, file);
						addInfo(file, info);
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("==========" + branchName + "======" + file);
					}
				 }
			}
		}
		try {
			if (info != null) {
				info.reload();
				info.updateUseTime();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}
	
	public static void remove(BuildPackInfo info) {
		synchronized (infos) {
			infos.remove(info.getFile());
		}
	}
	
	/**
	 * 检查缓存情况并增加一个对象
	 * @param file
	 * @param info
	 */
	private static void addInfo(File file, BuildPackInfo info) {
		if (infos.size() >= MAX_CACHE_SIZE) {
			synchronized (infos) {
				Set<File> removeKeys = new HashSet<File>();
				long ts = System.currentTimeMillis() - EXPIRE_INTERVAL;
				for (File f : infos.keySet()) {
					BuildPackInfo ifo = infos.get(f);
					if (ifo.getLastUseTime() < ts) {
						removeKeys.add(f);
					}
				}
				for (File f : removeKeys) {
					infos.remove(f);
				}
			}
		}
		infos.put(file, info);
	}
}
