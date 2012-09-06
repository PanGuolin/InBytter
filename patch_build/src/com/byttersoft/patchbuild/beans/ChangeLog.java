package com.byttersoft.patchbuild.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 变更记录
 * @author pangl
 *
 */
public class ChangeLog {

	/** 
	 * 执行变更的用户
	 */
	private String user;
	/**
	 * 变更的动作
	 */
	private String action;
	/**
	 * 执行变更的时间
	 */
	private long ts;
	
	ChangeLog(String user, String action, long ts) {
		super();
		this.user = user;
		this.action = action;
		this.ts = ts;
	}
	
	ChangeLog(String log) {
		String[] ps = log.split(":");
		assert ps.length == 3;
		this.user = ps[0].trim();
		this.action = ps[1].trim();
		this.ts = Long.parseLong(ps[2]);
	}
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public long getTs() {
		return ts;
	}
	public void setTs(long ts) {
		this.ts = ts;
	}
	
	public String toString() {
		return user + ":" + action + ":" + ts;
	}
	
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public String getLog() {
		return "[" +  df.format(new Date(ts)) + "] user:" + user + ", action:" + action;
	}
	
}
