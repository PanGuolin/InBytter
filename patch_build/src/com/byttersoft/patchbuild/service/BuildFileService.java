package com.byttersoft.patchbuild.service;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.tools.ant.util.FileUtils;

import com.byttersoft.patchbuild.PackBuildLogger;
import com.byttersoft.patchbuild.beans.BuildConfig;
import com.byttersoft.patchbuild.beans.BuildFile;
import com.byttersoft.patchbuild.beans.RepositoryInfo;
import com.byttersoft.patchbuild.cache.BPICache;
import com.byttersoft.patchbuild.utils.AntTaskUtil;
import com.byttersoft.patchbuild.utils.PatchUtil;

/**
 * 构建包相关服务
 * @author pangl
 *
 */
public class BuildFileService {
	
	/**
	 * 获取指定分支上的一个BuildPackInfo对象
	 * @param branchName
	 * @param fileName 不带路径的文件名称
	 * @return
	 */
	public static BuildFile getBuildPackInfo(String branchName, String fileName) {
		fileName = fileName.trim();
		if (fileName.length() == 0)
			return null;
		if (!fileName.endsWith(".zip"))
			fileName = fileName + ".zip";
		RepositoryInfo info = BuildReposManager.getByName(branchName);
		File file = new File(info.getBuildDir(), fileName);
		return BPICache.getBuildPackInfo(branchName, file);
	}
	
	/**
	 * 获取指定补丁中包含的BuilPackInfo对象
	 * @param branchName
	 * @param patchName
	 * @param fileName
	 * @return
	 */
	public static BuildFile getBuildPackInfo(String branchName, File file) {
		return BPICache.getBuildPackInfo(branchName, file);
	}

	/**
	 * 判断某个构建包是否可以发布
	 * @param config 构建包信息
	 * @return
	 */
	public static void checkCanDeploy(BuildFile info) throws Exception{
		BuildConfig config = info.getConfig();
		RepositoryInfo reposInfo = BuildReposManager.getByName(config.getVersion());
		File root = reposInfo.getBuildDir();
		String[] deps = config.getDepends();
		for (String dep : deps) {
			if (new File(root, dep + ".zip").exists()) {
				throw new Exception("依赖的包未发布：" + dep);
			}
		}
	}
	
	/**
	 * 发布补丁包
	 * @param branch
	 * @param id
	 * @throws Exception
	 */
	public static void deployPack(BuildFile info) throws Exception {
		BuildConfig config = info.getConfig();
		String id = config.getId();
		RepositoryInfo reposInfo = BuildReposManager.getByName(info.getBranchName());
		PackBuildLogger logger = new PackBuildLogger(reposInfo, id + "_deploy");
		logger.startBuild();
		try {
			File buidPackFile = new File(reposInfo.getBuildDir(), id+ ".zip");
			checkCanDeploy(info);
			File tempBackDir = new File(reposInfo.getWorkspace(), "temp/" + id);
			AntTaskUtil.unzip(buidPackFile, tempBackDir, logger);
			
			//修改配置文件
			File configXML = new File(tempBackDir, id + ".xml");
			config.setDeployTS(System.currentTimeMillis());
			PatchUtil.write2File(config.toXML(), configXML);
			
			//重新压缩到备份包
			Date curDate = Calendar.getInstance().getTime();
			String path = PatchUtil.getBackupDir(curDate);
			File backFile = new File(reposInfo.getDeployBackupDir(), path + id + ".zip");
			AntTaskUtil.zip(tempBackDir, backFile, logger);
			
			//合并发布包
			synchronized (BuildFileService.class) {
				String patchName = reposInfo.getVersionNo() +
						(reposInfo.getVersionPattern() == null ? "" : new SimpleDateFormat(reposInfo.getVersionPattern()).format(curDate)) +
						reposInfo.getVersionSuffix();
				if (patchName.endsWith(".zip"))
					patchName = patchName.substring(0, patchName.length() - ".zip".length());
				File patchTemp = new File(reposInfo.getWorkspace(), "temp/" + patchName);
				AntTaskUtil.deleteDir(patchTemp, logger);
				patchTemp.mkdirs();
				
				logger.logMessage("拷贝前一次补丁内容===========================");
				File latestFile = new File(reposInfo.getDeployDir(), "latest.zip");
				AntTaskUtil.unzip(latestFile, patchTemp, logger);
				
				logger.logMessage("拷贝构建包内容===========================");
				AntTaskUtil.copyFiles(tempBackDir, patchTemp, "web/**/*.*", logger);
				AntTaskUtil.copyFiles(tempBackDir, patchTemp, "cs/**/*.*", logger);
				//copyDir(new File(tempBackDir, "web"), new File(patchTemp, "web"));
				//copyDir(new File(tempBackDir, "cs"), new File(patchTemp, "cs"));
				
				//合并SQL内容
				for (String sqlSufix : reposInfo.getSqlSuffixs()) {
					final String suffix = sqlSufix;
					final StringBuilder sb = new StringBuilder();
					File[] vpSqls = new File(tempBackDir, "/sql").listFiles(new FilenameFilter() {
						public boolean accept(File dir, String name) {
							if (name.endsWith(suffix)) {
								sb.append(name + ";"); 
								return true;
							}
							return false;
						}
					});
					if (vpSqls == null || vpSqls.length == 0)
						continue;
					File patchSql = new File(patchTemp, "sql/" + patchName + sqlSufix);
					logger.logMessage("合并SQL文件：" + sb.toString());
					PatchUtil.mergeSqlTo(vpSqls, patchSql, reposInfo.getSqlEncoding());
				}
				//合并readme内容
				File readmeFile = new File(patchTemp, "readme.txt");
				logger.logMessage("合并ReadMe文件：" + readmeFile + "===========================");
				StringBuilder sb = new StringBuilder();
				if (readmeFile.exists()) {
					sb = PatchUtil.readFile(readmeFile, "GBK");
				}
				PatchUtil.replaceSection(sb, id, config.getVps() + "\r\n" + config.getComment());
				PatchUtil.write2File(sb.toString(), readmeFile);
	
				logger.logMessage("写入版本信息 " + reposInfo.getVersionInfo() + "===========================");
				StringBuilder versionInfo = PatchUtil.readFile(reposInfo.getVersionInfo(), "UTF-8");
				int start = versionInfo.indexOf("<bs_ver>");
				int end = versionInfo.indexOf("</bs_ver>");
				versionInfo.replace(start+"<bs_ver>".length(), end, patchName);
				PatchUtil.write2File(versionInfo.toString(), new File(patchTemp, "web/WEB-INF/versioninfo.xml"));
				
				//重新压缩到补丁
				File patchFile = new File(reposInfo.getDeployDir(), patchName + ".zip");
				logger.logMessage("重新压缩补丁 ===========================");
				AntTaskUtil.zip(patchTemp, patchFile, logger);
				FileUtils.getFileUtils().copyFile(patchFile, latestFile);
				
				
				//拷贝class文件
				
				logger.logMessage("拷贝依赖文件 ===========================");
				AntTaskUtil.copyFiles(new File(tempBackDir + "/web/WEB-INF/classes"),
						reposInfo.getCompileClassDir(), "**/*.class", logger);
				
				logger.logMessage("清除临时文件===========================");
				AntTaskUtil.deleteDir(tempBackDir, logger);
				AntTaskUtil.deleteDir(patchTemp, logger);
				buidPackFile.delete();
				removeDepend(info);
				BPICache.remove(info);
			}
		} catch (Exception ex) {
			logger.logMessage(ex.getMessage());
			throw ex;
		} finally {
			logger.endBuild();
		}
	}
	
	/**
	 * 去掉依赖信息，只对已构建未发布的构建包有效
	 * @param branch
	 * @param dependId
	 */
	public static void removeDepend(BuildFile dependedInfo) {
		BuildFile[] infos = listBildPackInfo(dependedInfo.getBranchName());
		String id = dependedInfo.getConfig().getId();
		for (BuildFile info : infos) {
			BuildConfig conf = info.getConfig();
			if (conf.removeDepend(id)) {
				info.storeConfig();
			}
		}
	}
	
	/**
	 * 开始测试某个包
	 * @param info
	 * @param user
	 */
	public static void startTest(BuildFile info, String user) {
		info.getConfig().setTesters(user);
	}
	
	/**
	 * 取消测试
	 * @param info
	 */
	public static void cancelTest(BuildFile info) {
		info.getConfig().setTesters(null);
	}
	
	/**
	 * 查找包含某个文件的构建包
	 * @param branch
	 * @param filePattern
	 * @return
	 */
	public static BuildFile[] findBPIByFile(String branch, String filePattern) {
		BuildFile[] infos = listBildPackInfo(branch);
		List<BuildFile> list = new ArrayList<BuildFile>();
		for (BuildFile info : infos) {
			String[] allFs = info.getConfig().getAllFiles();
			for (String f : allFs) {
				if (f.indexOf(filePattern) != -1) {
					list.add(info);
					break;
				}
			}
		}
		return (BuildFile[])list.toArray(new BuildFile[list.size()]);
	}
	
	/**
	 * 列出所有已构建的包信息
	 * @return 不存在则返回长度为0的数组对象
	 */
	public static BuildFile[] listBildPackInfo(String branch) {
		
		RepositoryInfo repos = BuildReposManager.getByName(branch);
		TreeSet<BuildFile> bpInfos = new TreeSet<BuildFile>();
		File root = repos.getBuildDir();
		//System.out.println("List from " + root);
		File[] zips = root.listFiles();
		if (zips == null  || zips.length == 0) {
			//System.out.println("[" + branch + "] No build package in dir:" + root);
			return new BuildFile[0];
		}
		for (File zip : zips) {
			if (zip.getName().endsWith(".zip")) {
				try {
					BuildFile info = getBuildPackInfo(branch, zip.getName());
					if (info != null)
						bpInfos.add(info);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return (BuildFile[])bpInfos.toArray(new BuildFile[bpInfos.size()]);
	}
	
	/**
	 * 获取所有已构建但未发布的构建包中包含的文件列表
	 * @param branch
	 * @return
	 */
	public static Set<String> listAllTestingFile(String branch) {
		BuildFile[] infos = listBildPackInfo(branch);
		Set<String> allFiles = new HashSet<String>();
		for (BuildFile info : infos) {
			String[] files = info.getConfig().getAllFiles();
			for (String f : files) {
				allFiles.add(f);
			}
		}
		return allFiles;
	}
	
	/**
	 * 生成私家包并返回文件路径
	 * @param branchName
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	public static File getPrivateFile(String branchName, String file)  {
		RepositoryInfo repos = BuildReposManager.getByName(branchName);
		BuildFile buildFile = getBuildPackInfo(branchName, file);
		
		BuildConfig config = buildFile.getConfig();
		String[] depends = config.getDepends();
		if (depends != null) {
			List<String> dependList = new ArrayList<String>();
			dependList.addAll(Arrays.asList(depends));
			retriveDepends(buildFile, dependList);
			depends = (String[])dependList.toArray(new String[dependList.size()]);
		}
		
		File ws = repos.getWorkspace();
		File root = new File(ws, "privateBuild" + buildFile.getConfig().getId() + System.currentTimeMillis());
		root.mkdirs();
		
		StringBuilder sb = new StringBuilder();
		if (depends != null) {
			for (int i=depends.length - 1; i >=0; i--) {
				BuildFile depFile = BuildFileService.getBuildPackInfo(buildFile.getBranchName(), depends[i]);
				if (depFile != null) {
					AntTaskUtil.unzip(depFile.getFile(), root, null);
					sb.append(depends[i] + "\r\n");
				}
			}
		}
		try {
			FileWriter writer = new FileWriter(new File(root, config.getId() + "_depends.txt"));
			writer.write(sb.toString());
			writer.flush();
			writer.close();
		} catch (Exception ex) {
			//此处不做处理，如果写入错误则简单忽略，不影响流程
			ex.printStackTrace();
		}
		AntTaskUtil.unzip(buildFile.getFile(), root, null);
		
		File targetFile = new File(repos.getPrivateDir(), "private_" + buildFile.getFileName());
		AntTaskUtil.zip(root, targetFile, null);
		
		AntTaskUtil.deleteDir(root, null);
		return targetFile;
	}
	
	/**
	 * 获取所有依赖的构建包对象，被依赖的构建包放在列表后面
	 * @param depends
	 * @param buildFile
	 */
	private static void retriveDepends(BuildFile buildFile, List<String> dependList) {
		String[] depends = buildFile.getConfig().getDepends();
		if (depends != null) {
			for (String depend : depends) {
				BuildFile depFile = BuildFileService.getBuildPackInfo(buildFile.getBranchName(), depend);
				if (depFile == null) 
					continue;
				String id = depFile.getConfig().getId();
				//被依赖的包移到最后
				if (dependList.contains(id)) {
					dependList.remove(id);
				}
				dependList.add(id);
				retriveDepends(depFile, dependList);
			}
		}
	}
}
