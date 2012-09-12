package com.byttersoft.patchbuild.filter;


import java.io.UnsupportedEncodingException;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;

import com.byttersoft.patchbuild.beans.BuildConfig;
import com.byttersoft.patchbuild.command.BuildCommand;
import com.byttersoft.patchbuild.command.CommandContext;
import com.byttersoft.patchbuild.utils.MailInfo;
import com.byttersoft.patchbuild.utils.MailUtil;

public class MailFilter implements ICommandFilter{

	public boolean beforeExecute(BuildCommand command) throws Exception {
		return true;
	}

	public boolean onError(BuildCommand command, Throwable t) {
		return true;
	}

	public boolean afterExecute(BuildCommand command) {
		CommandContext context = command.getContext();
		BuildConfig config = context.getBuildFile().getConfig();
		String dev = config.getDevelopers();
		if (dev == null)
			return true;
		dev = dev.trim();
		if (dev.length() == 0)
			return true;
		
		dev = dev + "@bytter.com";
		
		String subject = "您提交的构建包" + context.getFileName() + "被执行操作：" + command.getName();
		String content = "<html><body>" +
				"<b>构建包：</b>" + context.getFileName() + "<br/>" +
				"<b>操作用户：</b>" + context.getUser() + "<br/>" + 
				"<b>操作动作：</b>" + command.getName() + "<br/>" +
				"</body></html>";
		MailInfo info = new MailInfo();
		info.setContent(content);
		info.setSubject(subject);
		info.setToAddrs(dev);
		try {
			MailUtil.sendMail(info);
		} catch (UnsupportedEncodingException e1) {
			//不在过滤器中处理异常
			e1.printStackTrace();
		} catch (MessagingException e1) {
			//不在过滤器中处理异常
			e1.printStackTrace();
		}
		return true;
	}

}

class EmailAutherticator extends Authenticator {
	private String username;
	private String password;
	
    public EmailAutherticator() {
        super();
    }

    public EmailAutherticator(String user, String pwd)
    {
        super();
        username = user;
        password = pwd;
    }

    public PasswordAuthentication getPasswordAuthentication()
    {
        return new PasswordAuthentication(username, password);
    }
}
