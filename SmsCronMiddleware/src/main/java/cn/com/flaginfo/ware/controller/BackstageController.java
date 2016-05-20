package cn.com.flaginfo.ware.controller;

import java.io.File;
import java.util.List;
import org.slf4j.Logger;
import java.io.OutputStream;
import java.io.FileInputStream;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import cn.com.flaginfo.ware.util.DBUtils;
import cn.com.flaginfo.ware.vo.InstallObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cn.com.flaginfo.ware.util.PropFileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 中间安装成功后
 * 		配置查询更新等操作
 * 
 * @author zyong
 *
 */
@Controller
@RequestMapping("/backstage")
public class BackstageController {
	private static final Logger log = 
			LoggerFactory.getLogger(BackstageController.class);
	
	/**
	 * 后台配置首页
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/index")
	public String index(
			HttpServletRequest request) throws Exception {
		// 获取项目的路径
		String path = 
				BackstageController.class.getClassLoader()
				.getResource("").getPath();
		path = path.substring(0, path.lastIndexOf("/WEB-INF/classes/"));
		log.info("path: " + path);
		
		InstallObject.setProUrl(path);
		InstallObject install = new InstallObject();
		PropFileUtils.readFileToStaticField(install.getClass());
		request.setAttribute("message", install);
		
		return "backstage/index";
	}
	
	/**
	 * 环境检测页面
	 * @return
	 */
	@RequestMapping("/environmentCheck")
	public String environmentCheck() {
		return "backstage/environmentCheck";
	}
	
	/**
	 * 文件权限检测页面
	 * @return
	 */
	@RequestMapping("/fileCheck")
	public String fileCheck() {
		return "backstage/fileCheck";
	}
	
	/**
	 * 数据库配置页面
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/databaseConfig")
	public String databaseConfig(
			HttpServletRequest request) throws Exception {
		InstallObject install = new InstallObject();
		PropFileUtils.readFileToStaticField(install.getClass());
		request.setAttribute("message", install);
		
		return "backstage/databaseConfig";
	}
	
	/**
	 * 接口配置页面
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/interfaceConfig")
	public String interfaceConfig(
			HttpServletRequest request) throws Exception {
		InstallObject install = new InstallObject();
		PropFileUtils.readFileToStaticField(install.getClass());
		request.setAttribute("message", install);
		
		return "backstage/interfaceConfig";
	}
	
	/**
	 * 数据库字段名更改页面
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/columnUpdate")
	public String columnUpdate(
			HttpServletRequest request) throws Exception {
		InstallObject install = new InstallObject();
		PropFileUtils.readFileToStaticField(install.getClass());
		request.setAttribute("message", install);
		
		List<Object> dynaList = JSON.parseArray(InstallObject.getDynamicJson());
		request.setAttribute("dynaList", dynaList);
		log.info("dynamic: " + InstallObject.getDynamicJson());
		
		return "backstage/columnUpdate";
	}
	
	/**
	 * 升级说明页面
	 * @return
	 */
	@RequestMapping("/introduction")
	public String introduction() {
		return "backstage/introduction";
	}
	
	/**
	 * 下载日志
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/downLog")
	public void downLog(
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		String pathName = System.getProperty("catalina.home") + 
							File.separator + "logs" + 
							File.separator + "sms-ware.log";
		String fileName = pathName.substring(
							pathName.lastIndexOf(File.separator) + 1);
		
		response.setContentType("application/x-msdownload");
//		web网页端下载文件时需要这样设置，才能正常显示中文文件名
		response.setHeader("Content-Disposition", 
				"attachment;filename=" + 
				new String(fileName.getBytes(), "ISO8859-1"));
		
		File file = new File(pathName);
		OutputStream out=response.getOutputStream();
		IOUtils.copy(new FileInputStream(file),out);
		out.flush();
	}
	
	/**
	 * 检测数据库配置连通成功后，则将相关配置保存
	 * 
	 * @param request
	 * @param dbUrl
	 * @param dbDriver
	 * @param dbName
	 * @param dbUser
	 * @param dbPasswd
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/saveDb")
	public String databaseSave(
			HttpServletRequest request, 
			String dbUrl, 
			String dbDriver, 
			String dbUser, 
			String dbPasswd) throws Exception {
		
		InstallObject.setDbUrl(dbUrl);
		InstallObject.setDbDriver(dbDriver);
		InstallObject.setDbUser(dbUser);
		InstallObject.setDbPasswd(dbPasswd);
		
		InstallObject install = new InstallObject();
		PropFileUtils.writeStaticFieldToFile(install.getClass());
		
		DBUtils.initDataSource();
		
		return "redirect:/backstage/databaseConfig";
	}
	
	/**
	 * 保存接口的相关配置
	 * 
	 * @param request
	 * @param sendSMSUrl
	 * @param sendEMSUrl
	 * @param getTaskUrl
	 * @param listReply
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/saveInterface")
	public String interfaceSave(
			HttpServletRequest request, 
			String sendSMSUrl, 
			String sendEMSUrl, 
			String getTaskUrl, 
			String listReply) throws Exception {
		
		InstallObject.setSendSMSUrl(sendSMSUrl);
		InstallObject.setSendEMSUrl(sendEMSUrl);
		InstallObject.setGetTaskUrl(getTaskUrl);
		InstallObject.setListReply(listReply);
		
		InstallObject install = new InstallObject();
		PropFileUtils.writeStaticFieldToFile(install.getClass());
		
		return "redirect:/backstage/interfaceConfig";
	}
	
	/**
	 * 数据库相关字段定义
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/saveColumn")
	public String columnSave(
			HttpServletRequest request) throws Exception {
		String smsSendTable = request.getParameter("smsSendTable");
		String messageContent = request.getParameter("messageContent");
		String sendToMobile = request.getParameter("sendToMobile");
		String id = request.getParameter("id");
		String planTime = request.getParameter("planTime");
		String sendType = request.getParameter("sendType");
		String sendSmsType = request.getParameter("sendSmsType");
		String sendEmsType = request.getParameter("sendEmsType");
		String createTime = request.getParameter("createTime");
		String status = request.getParameter("status");
		String sendedStatus = request.getParameter("sendedStatus");
		String unSendStatus = request.getParameter("unSendStatus");
		String sendFailStatus = request.getParameter("sendFailStatus");
		String serialNumber = request.getParameter("serialNumber");
		String taskId = request.getParameter("taskId");
		String textTemplateId = request.getParameter("textTemplateId");
		String mediaId = request.getParameter("mediaId");
		String mediaUrl = request.getParameter("mediaUrl");
		String dynamicJson = request.getParameter("dynamicJson");
		String replyContent = request.getParameter("replyContent");
		
		InstallObject.setSmsSendTable(smsSendTable);
		InstallObject.setMessageContent(messageContent);
		InstallObject.setSendToMobile(sendToMobile);
		InstallObject.setId(id);
		InstallObject.setPlanTime(planTime);
		InstallObject.setSendType(sendType);
		InstallObject.setSendSmsType(sendSmsType);
		InstallObject.setSendEmsType(sendEmsType);
		InstallObject.setCreateTime(createTime);
		InstallObject.setStatus(status);
		InstallObject.setSendedStatus(sendedStatus);
		InstallObject.setUnSendStatus(unSendStatus);
		InstallObject.setSendFailStatus(sendFailStatus);
		InstallObject.setSerialNumber(serialNumber);
		InstallObject.setTaskId(taskId);
		InstallObject.setTextTemplateId(textTemplateId);
		InstallObject.setMediaId(mediaId);
		InstallObject.setMediaUrl(mediaUrl);
		InstallObject.setDynamicJson(dynamicJson);
		InstallObject.setReplyContent(replyContent);
		
		InstallObject install = new InstallObject();
		PropFileUtils.writeStaticFieldToFile(install.getClass());
		
		return "redirect:/backstage/columnUpdate";
	}
}
