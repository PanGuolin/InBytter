package com.byttersoft.patchbuild.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileUtils;

import com.byttersoft.patchbuild.PackBuildLogger;
import com.byttersoft.patchbuild.beans.BuildConfig;
import com.byttersoft.patchbuild.beans.BuildFile;
import com.byttersoft.patchbuild.beans.RepositoryInfo;
import com.byttersoft.patchbuild.service.BuildPackService;
import com.byttersoft.patchbuild.service.BuildReposManager;

/**
 * 补丁发布及构建工具类
 * @author pangl
 *
 */
public class PatchUtil {
	
	
	/**
	 * 返回补丁构建的备份目录
	 * @param date
	 * @return
	 */
	public static String getBackupDir(Date date) {
		return new SimpleDateFormat("yyyy/MM/dd/").format(date);
	}
	
	/**
	 * 构建补丁包
	 */
	public static void buildPackage(BuildConfig config) throws Exception{
		
		RepositoryInfo repos = BuildReposManager.getByName(config.getVersion());
		PackBuildLogger logger = new PackBuildLogger(repos, config.getId());
		logger.startBuild();
		try {
			String id = config.getId();
			String zipName = id + ".zip";
			File buildFile = new File(repos.getBuildDir(), zipName);
			if (buildFile.isFile() && buildFile.exists()) {
				logger.logMessage("存在同名待测试构建包，请先取消测试再进行构建：" +zipName);
				throw new Exception("存在待测试构建包，请先取消测试再进行构建：" +zipName);
			}
			
			BuildFile[] infos = BuildPackService.listBildPackInfo(config.getVersion());
			String[] bFiles = config.getAllFiles();
			for (BuildFile info : infos) {
				BuildConfig bConfig = info.getConfig();
				for (String bf : bFiles) {
					if (bConfig.containsFile(bf)) {
						config.addDepend(bConfig.getId());
						logger.logMessage("文件" + bf + "已存在构建包：" + bConfig.getId() + ".zip 当中\n");
					}
				}
			}
			
			SVNUtil.checkOutModule(repos.getName(), "sql", logger);
			String[] modules = config.getModules().split(";");
			for (String module : modules) {
				if (module.trim().length() > 0)
					SVNUtil.checkOutModule(repos.getName(), module, logger);
			}
			logger.logMessage("删除临时文件===========================");
			AntTaskUtil.deleteDir(getTempDir(config), logger);
			logger.logMessage("编译Java============================");
			compileJava(config, logger);
			logger.logMessage("开始打包=============================");
			copy4BuildPack(config, logger);
			
			logger.logMessage("写入配置信息===========================");
			config.setBuildTS(System.currentTimeMillis());
			write2File(config.toXML(), new File(getPackDir(config), config.getId() + ".xml"));
			
			//打包ZIP文件
			AntTaskUtil.zip(getPackDir(config), buildFile, logger);
			logger.logMessage("打包完成：" + buildFile);
			
			//请空临时目录
			AntTaskUtil.deleteDir(getTempDir(config), logger);
		} finally {
			logger.endBuild();
		}
	}
	
	/**
	 * 判断某个构建包是否可以发布
	 * @param config 构建包信息
	 * @return
	 */
	public static void checkCanDeploy(BuildConfig config) throws Exception{
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
	 * 写文件内容到指定文件
	 * @param content 文本内容
	 * @param dest 目标文件
	 * @throws IOException
	 */
	public static void write2File(String content, File dest) throws IOException {
		byte[] bs = content.getBytes("GBK");
		FileOutputStream out = new FileOutputStream(dest);
		out.write(bs);
		out.flush();
		out.close();
	}
	
	/**
	 * 替换指字符串中的某一段
	 * @param sb 目标字符串对象
	 * @param sectionName 段的名称，将作为查找的依据
	 * @param content 段的内容， 如果内容为空则只进行删除操作
	 */
	public static void replaceSection(StringBuilder sb, String sectionName, String content) {
		String prefix = "\r\n-- -" + sectionName + "-\r\n";
		String suffix = "\r\n-- =" + sectionName + "=\r\n";
		int sIndex = sb.indexOf(prefix);
		int eIndex = sb.indexOf(suffix);
		if (sIndex != -1 && eIndex != -1 && sIndex < eIndex) {
			sb.replace(sIndex, eIndex + suffix.length(), "");
		}
		if (content.length() > 0) {
			sb.append(prefix);
			sb.append(content);
			sb.append(suffix);
		}
	}
	
	/**
	 * 合并SQL文件
	 * @param vpSqls 合并源文件列表，这些文件中的内容将被合将到patchFile文件当中
	 * @param patchFile 合并目标文件，合并之后的内容是原内容加上新增的内容
	 * @param encoding 指定文件的编码
	 * @throws IOException
	 */
	public static void mergeSqlTo(File[] vpSqls, File patchFile, String encoding) throws IOException {
		if (encoding == null)
			encoding = "UTF-8";
		StringBuilder sb = readFile(patchFile, encoding);
		for (File sqlFile : vpSqls) {
			StringBuilder sql = readFile(sqlFile, encoding);
			replaceSection(sb, sqlFile.getName(), sql.toString());
		}
		patchFile.getParentFile().mkdirs();
		FileOutputStream out = new FileOutputStream(patchFile);
		out.write(sb.toString().getBytes(encoding));
		out.flush();
		out.close();
	}
	
	/**
	 * 读取SQL文件内容
	 * @param sqlFile
	 * @return
	 * @throws IOException
	 */
	public static StringBuilder readFile(File file, String encoding) throws IOException {
		if (encoding == null)
			encoding = "UTF-8";
		StringBuilder sb = new StringBuilder();
		if (!file.exists() || !file.isFile())
			return sb;
		int buffer = 1024;
		byte[] bs = new byte[buffer];
		FileInputStream in = new FileInputStream(file);
		int len = in.read(bs);
		int total = 0;
		while(len != -1) {
			total += len;
			byte[] tmp = new byte[total+buffer];
			System.arraycopy(bs, 0, tmp, 0, total);
			bs = tmp;
			len = in.read(bs, total, buffer);
		}
		sb.append(new String(bs, 0, total, encoding));
		in.close();
		return sb;
	}
	
	/**
	 * 拷贝构建包文件
	 * @param config
	 * @throws IOException
	 */
	private static void copy4BuildPack(BuildConfig config, PackBuildLogger logger) throws IOException {
		
		File packDir = getPackDir(config);
		doCopy(config, config.getWebFileList(), "/src/main/webapp/", new File(packDir, "web"), logger);
		doCopy(config, config.getCsFileList(), "/cs/", new File(packDir, "cs"), logger);
		doCopy(config, config.getResourceFileList(), "/src/main/resources/", new File(packDir, "web/WEB-INF/classes"), logger);
		
		RepositoryInfo repos = BuildReposManager.getByName(config.getVersion());
		String[] sqlSuffix = repos.getSqlSuffixs();
		String sqlReposRoot = repos.getSqlRepos();
		String[] vps = config.getVps().split(";");
		List<String> files = new ArrayList<String>();
		for (String vp : vps) {
			vp = vp.trim();
			if (vp.length() == 0)
				continue;
			for(String suf : sqlSuffix) {
				files.add(sqlReposRoot + vp + suf);
			}
		}
		doCopy(config, (String[])files.toArray(new String[files.size()]), "/sql/patch/", new File(packDir, "sql"), logger);
	}
	
	/**
	 * 从SVN的本地目录中拷贝文件到指定目录 
	 * @param config
	 * @param files
	 * @param subDir
	 * @param destDir
	 * @throws IOException
	 */
	private static void doCopy(BuildConfig config, String[] files, String subDir, File destDir, PackBuildLogger logger) throws IOException {
		if (files == null || files.length == 0)
			return;
		if (!destDir.exists())
			destDir.mkdirs();
		subDir = subDir.replace("\\\\", "/").trim();
		if (subDir.length() > 0) {
			if (!subDir.startsWith("/"))
				subDir = "/" + subDir;
			if (!subDir.endsWith("/"))
				subDir = subDir + "/";
		}
		
		RepositoryInfo repos = BuildReposManager.getByName(config.getVersion());
		File svnRoot = repos.getSvnWorkspace();
		FileUtils fileUtil = FileUtils.getFileUtils();
		for (String file : files) {
			File srcFile = new File(svnRoot, file);
			if (!srcFile.exists()) {
				logger.logMessage("文件不存在:" + srcFile);
				continue;
			}
			String path = srcFile.getAbsolutePath();
			path = path.replaceAll("\\\\", "/");
			int index = path.indexOf(subDir);
			path = path.substring(index + subDir.length());
			File destFile = new File(destDir, path);
			fileUtil.copyFile(srcFile, destFile);
			logger.logMessage("copyFile:[" + srcFile + " ] to [" + destFile + "]");
		}
	}
	
	
	/**
	 * 获取构建对应的临时目录
	 * @param config
	 * @return
	 */
	private static File getTempDir(BuildConfig config) {
		RepositoryInfo repos = BuildReposManager.getByName(config.getVersion());
		File dir =  new File(repos.getWorkspace(), "temp/" + config.getId());
		if (!dir.exists())
			dir.mkdirs();
		return dir;
	}
	
	private static File getPackDir(BuildConfig config) {
		File dir =   new File((getTempDir(config)), "pack");
		if (!dir.exists())
			dir.mkdirs();
		return dir;
	}
	
	/** 
	 * 执行拷贝并javac编译
	 * @param config
	 * @throws IOException
	 */
	private static void compileJava(BuildConfig config, PackBuildLogger logger) throws IOException {
		
		if (config.getJavaFileList().length == 0)
			return;
		RepositoryInfo repos = BuildReposManager.getByName(config.getVersion());
		File tempDir = getTempDir(config);
		
		File src = new File(tempDir, "javaSrc");
		doCopy(config, config.getJavaFileList(), "/src/main/java/", src, logger);
		
		File complieDest = new File(getPackDir(config), "web/WEB-INF/classes/");
		complieDest.mkdirs();

		Javac javac = new Javac();
		javac.setProject(new Project());
		javac.setEncoding("GBK");
		javac.setSource("1.5");
		javac.setTarget("1.5");
		javac.setDestdir(complieDest);
		javac.setIncludeantruntime(true);
		javac.setIncludejavaruntime(true);
		javac.setFailonerror(true);
		javac.setVerbose(true);
		
		Path srcPath = new Path(javac.getProject());
		srcPath.createPathElement().setLocation(src);
		javac.setSrcdir(srcPath);
		
		Path libPath = new Path(javac.getProject());
		libPath.createPathElement().setLocation(repos.getCompileClassDir());
		FileSet fs = new FileSet();
		fs.setDir(repos.getCompileLibDir());
		libPath.addFileset(fs);
		javac.setClasspath(libPath);
		
		javac.getProject().setBaseDir(tempDir);
		if (logger != null)
			javac.getProject().addBuildListener(logger);
		javac.execute();
	}
}
