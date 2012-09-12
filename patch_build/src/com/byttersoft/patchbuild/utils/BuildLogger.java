package com.byttersoft.patchbuild.utils;

/**
 * 构建日志信息
 */
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.SVNEvent;

import com.byttersoft.patchbuild.beans.RepositoryInfo;

public class BuildLogger implements BuildListener, ISVNEventHandler {
	
	private File logFile;
	private PrintStream writer;
	private String id;
	private PrintStream sysOut;
	private PrintStream errOut;
	public BuildLogger(RepositoryInfo repos, String id) {
		this.id = id;
		logFile = new File(repos.getLogRoot(), id + ".log");
		logFile.getParentFile().mkdirs();
	}
	
	/**
	 * 构建开始
	 * @throws IOException
	 */
	public void startBuild() throws IOException {
		if (writer == null) {
			writer = new PrintStream(logFile);
			this.sysOut = System.out;
			this.errOut = System.err;
			System.setErr(writer);
			System.setOut(writer);
		}
		logMessage("开始构建");
	}
	
	/**
	 * 构建结构
	 * @throws IOException
	 */
	public void endBuild() throws IOException {
		logMessage("构建完成");
		if (writer != null) {
			writer.flush();
			writer.close();
			writer = null;
			System.setErr(errOut);
			System.setOut(sysOut);
		}
	}

	/**
	 * 记录一日志
	 * @param message
	 */
	public void logMessage(String message) {
		writer.print("[" + new SimpleDateFormat("MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()) + "]");
		writer.print("-" + id + "-:");
		writer.print(message + "\r\n");
		writer.flush();
	}
	public void buildFinished(BuildEvent arg0) {

	}

	public void buildStarted(BuildEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void messageLogged(BuildEvent arg0) {
		logMessage(arg0.getMessage());
	}

	public void targetFinished(BuildEvent arg0) {
	}

	public void targetStarted(BuildEvent arg0) {
	}

	public void taskFinished(BuildEvent arg0) {
		logMessage("<" + arg0.getTask().getTaskName() + ">" + "finished");

	}

	public void taskStarted(BuildEvent arg0) {
		logMessage("<" + arg0.getTask().getTaskName() + ">" + "finished");
	}

	public void checkCancelled() throws SVNCancelException {
		// TODO Auto-generated method stub
		
	}

	public void handleEvent(SVNEvent paramSVNEvent, double paramDouble)
			throws SVNException {
		logMessage(paramSVNEvent.getAction() + ":" + paramSVNEvent.getFile());
	}

}
