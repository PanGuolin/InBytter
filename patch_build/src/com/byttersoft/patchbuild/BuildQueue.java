package com.byttersoft.patchbuild;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.byttersoft.patchbuild.beans.BuildConfig;
import com.byttersoft.patchbuild.utils.PatchUtil;

/**
 * 构建队列
 * @author MickeyMic
 *
 */
public class BuildQueue extends Thread{
	
	private static BuildQueue buildQueue;
	
	
	/**
	 * 构建日志最大长度
	 */
	//private int MAX_LOG_SIZE = 150;
	
	/**
	 * 等待构建队列最大长度
	 */ 
	private int MAX_WAIT_SIZE = 100;
	
	static {
		buildQueue = new BuildQueue();
		buildQueue.setDaemon(true);
		buildQueue.start();
	}
	
	private BuildQueue() {}
	
	private Map<String, QueueLog> logQues = new Hashtable<String, QueueLog>();
	
	private List<QueueLog> completeQues = new ArrayList<QueueLog>();
	
	public QueueLog getLogQueue(String id) {
		QueueLog log = logQues.get(id);
		if (log == null) {
			synchronized (queue) {
				log = logQues.get(id);
				if (log == null) {
					log = new QueueLog(id);
					logQues.put(id, log);
					idList.add(id);
				}
			}
		}
		return log;
	}
	
	private List<String> idList = new ArrayList<String>();
	
	private Vector<BuildConfig> queue = new Vector<BuildConfig>();
	
	public static void addBuild(BuildConfig config) throws Exception {
		buildQueue.doAddBuild(config);
	}
	
	public void doAddBuild(BuildConfig config) throws Exception {
		synchronized (queue) {
			if (queue.size() > MAX_WAIT_SIZE) {
				throw new Exception("超过最大等待长度，无法接受新请求！");
			}
			int index = queue.indexOf(config);
			if (index != -1) {
				queue.remove(index);
				getLogQueue(config.getId()).logMessage("构建包被用户" + config.getDevelopers() + "替换");
			}
			getLogQueue(config.getId()).logMessage("等待构建");
			queue.add(config);
			queue.notify();
		}
	}
	
	@Override
	public void run() {
		while(true) {
			BuildConfig config = null;
			synchronized (queue) {
				if (queue.size() > 0) {
					config = queue.remove(0);
					getLogQueue(config.getId()).logMessage("开始构建，请稍等....");
				} else {
					try {
						queue.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} 
			}
			if (config == null)
				continue;
			try {
				PatchUtil.buildPackage(config);
				getLogQueue(config.getId()).logMessage("构建完成,请测试");
			} catch (Exception e) {
				getLogQueue(config.getId()).logMessage("构建出错" + e.getMessage());
			}
			QueueLog que = logQues.remove(config.getId());
			idList.remove(config.getId());
			if (completeQues.contains(que)) {
				completeQues.remove(que);
			}
			completeQues.add(que);
		}
	}
	
	
	/**
	 * 获取等待构建日志列表
	 * @return
	 */
	public static String[] listWaitLog() {
		List<String> list = new ArrayList<String>();
		for (int i=0; i<buildQueue.idList.size(); i++) {
			String id = buildQueue.idList.get(i);
			QueueLog log = buildQueue.logQues.get(id);
			if (log != null)
				list.add(log.getLastMessage());
		}
		return (String[]) list.toArray(new String[list.size()]);
	}
	
	/**
	 * 获取构建日志列表
	 * @return
	 */
	public static String[] listBuildLog() {
		List<String> list = new ArrayList<String>();
		for (int i=0; i<buildQueue.completeQues.size(); i++) {
			QueueLog log = buildQueue.completeQues.get(i);
			if (log != null)
				list.add(log.getLastMessage());
		}
		return (String[]) list.toArray(new String[list.size()]);
	}
}
