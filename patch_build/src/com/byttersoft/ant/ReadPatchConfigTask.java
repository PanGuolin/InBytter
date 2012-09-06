package com.byttersoft.ant;

import java.io.File;
import java.util.Iterator;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.byttersoft.patchbuild.beans.BuildPackConfig;

/**
 * 读取补丁内容请单并将配置写到project变量当中
 * @author pangl
 *
 */
public class ReadPatchConfigTask extends Task{
	
	/**
	 * 补丁内容清单文件
	 */
	private File pacthConfig;
	
	private String varName = "_patchConfig.";
	
	public void execute() throws BuildException {
		try {
			//InputStream in = new FileInputStream(pacthConfig);
			BuildPackConfig config = BuildPackConfig.readFromFile(pacthConfig);
			super.log(config.getId());
			String[] vps = config.getVps().split(";");
			StringBuilder sb = new StringBuilder();
			for (String vp : vps) {
				if (sb.length() > 0) {
					sb.append(";");
				}
				vp = vp.trim();
				sb.append(vp + "_ORCL.sql;");
				//sb.append(vp + "_SQL2K.sql;");
				sb.append(vp + "_SQL2005.sql");
			}
			getProject().setProperty(varName + "sqls", sb.toString());
			Properties prop = config.toProperties(varName);
			for (@SuppressWarnings("rawtypes")
			Iterator keys = prop.keySet().iterator(); keys.hasNext();) {
				String key = (String) keys.next();
				getProject().setProperty(key, prop.getProperty(key));
			}
			super.log(getProject().getProperties().toString());
		} catch (Exception ex) {
			throw new BuildException(ex);
		}
	}


	public void setPacthConfig(File pacthConfig) {
		this.pacthConfig = pacthConfig;
	}


	public void setVarName(String varName) {
		this.varName = varName;
	}

	
}
