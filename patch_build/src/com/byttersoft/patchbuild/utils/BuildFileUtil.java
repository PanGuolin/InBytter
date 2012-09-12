package com.byttersoft.patchbuild.utils;

import com.byttersoft.patchbuild.beans.BuildFile;

/**
 * 构建包工具类
 * @author pangl
 *
 */
public abstract class BuildFileUtil {
	
	/** JAVA 目录的相对路径 **/
	public static final String DIR_JAVA = "/src/main/java/";
	
	/** 资源文件的相对路径 **/
	public static final String DIR_RESOURCE = "/src/main/resources/";
	
	/** WEB文件的相对路径 **/
	public static final String DIR_WEB = "/src/main/webapp/";
	
	public static String[] listWebFile(BuildFile file) {
		return null;
	}

}
