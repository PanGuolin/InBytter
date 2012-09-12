package com.byttersoft.patchbuild.utils;
/**
 * 邮件信息
 * @author pangl
 *
 */
public class MailInfo {
	
	/**
	 * 接收者地址
	 */
	private String toAddrs;
	
	/**
	 * 主题
	 */
	private String subject;
	
	/**
	 * 内容
	 */
	private String content;

	public String getToAddrs() {
		return toAddrs;
	}

	public void setToAddrs(String toAddrs) {
		this.toAddrs = toAddrs;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}