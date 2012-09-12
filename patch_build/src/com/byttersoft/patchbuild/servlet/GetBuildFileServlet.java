package com.byttersoft.patchbuild.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.byttersoft.patchbuild.beans.RepositoryInfo;
import com.byttersoft.patchbuild.service.BuildFileService;
import com.byttersoft.patchbuild.service.BuildReposManager;
import com.byttersoft.patchbuild.service.PatchFileService;
import com.byttersoft.patchbuild.utils.UserUtil;

/**
 * 获取补丁包
 * @author pangl
 *
 */
public class GetBuildFileServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 文件名编码，应当与操作系统的相同
	 */
	private String fileNameEncoding = "utf-8";
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String coding = config.getInitParameter("fileNameEncoding");
		if (coding != null)
			fileNameEncoding = coding;
	}

	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String type = req.getParameter("type");
		String fileName = req.getParameter("filename");
		String branch = UserUtil.getBranch(req);
		RepositoryInfo repos = BuildReposManager.getByName(branch);
		DownloadFileType fileType = DownloadFileType.valueOf(type);
		
		File file = null;
		switch (fileType) {
		case build:
			file = new File(repos.getBuildDir(), fileName);
			break;
		case dbuild:
			String[] ps = fileName.split("/");
			try {
				File root = PatchFileService.getRootOfBPByPatch(branch, ps[0]);
				file = new File(root, ps[1]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			break;
		case epatch:
			file = PatchFileService.encodePatch(branch, fileName);
			break;
		case log:
			file = new File(repos.getLogRoot(), fileName + ".log");
			break;
		case patch:
			file = new File(repos.getDeployDir(), fileName);
			break;
		case pbuild:
			file = BuildFileService.getPrivateFile(branch, fileName);
			break;
		}
		downloadFile(req, resp, file);
	}
	
	public void downloadFile(HttpServletRequest req, HttpServletResponse resp, File f) throws ServletException, UnsupportedEncodingException,IOException {
		String fileName = f.getName();
		if (fileName.indexOf('/') != -1 || fileName.indexOf('\\') != -1) {
			throw new ServletException("文件名称不合法" + fileName);
		}
		
		if (!f.exists() && !f.isFile())
			throw new ServletException("文件不存在" + fileName);
		
		FileInputStream fileInputStream = new FileInputStream(f);
		if (fileInputStream != null) {
			ServletOutputStream out = resp.getOutputStream();
			try {
				resp.setContentType("application/x-msdownload");
				resp.setHeader("Content-Disposition", "attachment; filename=" + 
						new String(fileName.getBytes(fileNameEncoding),"iso8859-1") + "");
				
				final int BUFFER_SIZE = 1024;
				byte[] bs = new byte[BUFFER_SIZE];
				int len = fileInputStream.read(bs);
				while(len != -1) {
					out.write(bs, 0, len);
					len = fileInputStream.read(bs);
				}
			} finally {
				fileInputStream.close();
				out.flush();
				out.close();
			}
		}
		
	}

}
