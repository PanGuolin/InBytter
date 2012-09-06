package com.byttersoft.patchbuild;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 构建队列日志对象
 * @author pangl
 *
 */
public class QueueLog {

	private String id;
	private List<String> messages = new ArrayList<String>();
	
	public QueueLog(String id) {
		this.id = id;
	}
	
	public String getLastMessage() {
		if (messages.size() == 0)
			return "";
		return messages.get(messages.size() - 1);
	}
	
	public void logMessage(String msg) {
		this.messages.add("[" + id + "]" + getTS()  + ": " + msg);
	}
	
	private String getTS() {
		return new SimpleDateFormat("MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof QueueLog))
			return false;
		QueueLog lg = (QueueLog)obj;
		return id.endsWith(lg.id);
	}
	
	
}
