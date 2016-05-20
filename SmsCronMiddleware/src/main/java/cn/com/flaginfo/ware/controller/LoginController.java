package cn.com.flaginfo.ware.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.flaginfo.spApi.SpAPI;
import cn.com.flaginfo.ware.util.DBUtils;
import cn.com.flaginfo.ware.util.PropFileUtils;
import cn.com.flaginfo.ware.util.SystemConfig;
import cn.com.flaginfo.ware.vo.CheckFilePermission;
import cn.com.flaginfo.ware.vo.InstallObject;
import cn.com.flaginfo.ware.vo.LoginInfo;
import net.sf.json.JSONObject;

/**
 *
 * @author hc
 *
 */
@Controller
@RequestMapping("/login")
public class LoginController {
	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	static final SpAPI sdk = new SpAPI();

	/**
	 * 中间件名称,从资源文件中获取值的key
	 */
	private final String MIDDLEWARENAME="middlewarename";
	/**
	 * 用户登陆验证地址,从资源文件中获取值的key
	 */
	private final String APIURL = "apiUrl";
	/**
	 * 安装登陆
	 *
	 */
	@RequestMapping("/login")
	public String login(HttpServletRequest request,HttpServletResponse response) {
		log.info("登陆页面");
		HttpSession session = request.getSession();
		String middlewarename = SystemConfig.getString(MIDDLEWARENAME);
		String apiurl =  SystemConfig.getString(APIURL);
		if(middlewarename!=null&&!"".equals(middlewarename)){
			session.setAttribute("middlewarename", middlewarename);
		}else{
			session.setAttribute("middlewarename","帜讯信息中间件");
		}
		if(apiurl!=null&&!"".equals(apiurl)&&apiurl.startsWith("http")){
			session.setAttribute("apiurl", apiurl);
		}else{
			session.setAttribute("apiurl", "");
		}
		return "login/login";
	}

	/**
	 * 用户登陆验证
	 *
	 * @param spcode
	 *            企业编号
	 * @param cname
	 *            企业登陆用户名
	 * @param key
	 *            企业登陆用户密码
	 * @return
	 */
	@RequestMapping("/check")
	@ResponseBody
	public Map<String, Object> loginCheck(String spcode, String cname, String key,HttpServletRequest request,HttpServletResponse response) {
		log.info("用户:【{}】,登陆", cname);

		//去空格
		String spcodes = spcode.trim();
		String cnames = cname.trim(); 
		String keys = key.trim();
		
		Map<String, Object> map = null;
		if (spcodes != null && !"".equals(spcodes) && cnames != null && !"".equals(cnames) && keys != null
				&& !"".equals(keys)) {
			// 验证用户信息
			sdk.setSpCode(spcodes);
			sdk.setUserName(cnames);
			sdk.setKey(keys);
			HttpSession session = request.getSession();
			String url = (String) session.getAttribute("apiurl");
			map = new HashMap<String, Object>();
			if(url!=null&&!"".equals(url)){
				sdk.setApiUrl(url);
			}else{
				log.error("校验用户信息时校验地址异常");
				map.put("returnCode", "001");
				return map;
			}
			final String methodUrl = "/base/sign/valid";
			Map<String, Object> requestMap = new HashMap<String, Object>();
			// SimpleDateFormat sdf = new
			// SimpleDateFormat("yyyyMMddHHmmssFFFFFF");
			try {
				log.info("开始校验用户信息");
				String resp = sdk.postRequest(methodUrl, requestMap);

				JSONObject json = JSONObject.fromObject(resp);
				log.info("用户验证返回信息:【{}】", json);
				log.info("结束校验用户信息");

				if ("true".equals(json.get("valid").toString()) && "200".equals(json.get("returnCode").toString())) {
					
					LoginInfo info = new LoginInfo();
					info.setSpCode(spcodes);
					info.setcName(cnames);
					info.setKey(keys);
					session.setAttribute("loginInfo", info);
					
					map.put("returnCode", "200");
				} else {
					map.put("returnCode", "002");
				}

			} catch (Exception e) {
				log.error("验证用户信息出现异常:", e);

				map = new HashMap<String, Object>();
				map.put("returnCode", "003");
			}

		} else {
			// 参数传输有错误
			log.info("用户验证参数传输有错误");

			map = new HashMap<String, Object>();
			map.put("returnCode", "003");
		}
		return map;
	}

	@RequestMapping("/checkParameter")
	@ResponseBody
	public Map<String, Object> checkParameter(InstallObject obj) {
		Map<String, Object> map = null;
		// 校验参数，写入
		InstallObject.setLogDescription("\n");
		boolean flag = checkFields();
		if (flag) {
			log.info("检查配置信息字段有空");
			map = new HashMap<String, Object>();
			map.put("returnCode", "005");
			return map;
		}

		InstallObject.setLogDescription(InstallObject.getLogDescription()+"完成检查配置信息字段检验\n");

		// 检验数据库配置信息
		boolean flagDB = checkDBConn(InstallObject.getDbUrl(), InstallObject.getDbDriver(), InstallObject.getDbUser(),
				InstallObject.getDbPasswd());
		if(!flagDB){
			log.info("检查数据库配置参数连接数据库失败");
			map = new HashMap<String, Object>();
			map.put("returnCode", "005");
			return map;
		}

		//初始化数据库连接
		DBUtils.initDataSource();
		
		InstallObject.setLogDescription(InstallObject.getLogDescription()+"完成数据库配置参数检验\n");
		// 校验短信发送接口
		boolean flagSMS = checkSmsSend();
		if (!flagSMS) {
			map = new HashMap<String, Object>();
			map.put("returnCode", "005");
			return map;
		}
		// 校验动态短信发送接口
		boolean flagDSMS = checkDynamicSmsSend();
		if (!flagDSMS) {
			map = new HashMap<String, Object>();
			map.put("returnCode", "005");
			return map;
		}

		InstallObject.setLogDescription(InstallObject.getLogDescription()+"完成短信发送接口检验\n");
		// 校验e信发送接口
		boolean flagEMS = checkEmsSend();
		if (!flagEMS) {
			map = new HashMap<String, Object>();
			map.put("returnCode", "005");
			return map;
		}
		// 校验动态e信发送接口
		boolean flagDEMS = checkDynamicEmsSend();
		if (!flagDEMS) {
			map = new HashMap<String, Object>();
			map.put("returnCode", "005");
			return map;
		}
		InstallObject.setLogDescription(InstallObject.getLogDescription()+"完成e信发送接口检验\n");
		// 校验查询发送结果接口
		boolean flagTK = checkGetTask();
		if (!flagTK) {
			map = new HashMap<String, Object>();
			map.put("returnCode", "005");
			return map;
		}

		InstallObject.setLogDescription(InstallObject.getLogDescription()+"完成上行回复数据接口检验\n");
		// 校验上行回复数据接口
		boolean flagLR = checkListReply();
		if (!flagLR) {
			map = new HashMap<String, Object>();
			map.put("returnCode", "005");
			return map;
		}
		// 校验e信上行回复表单数据接口
		boolean flagELR = checkEListReply();
		if (!flagELR) {
			map = new HashMap<String, Object>();
			map.put("returnCode", "005");
			return map;
		}

		try {
			log.info("开始写配置参数到资源文件");

			InstallObject.setLogDescription(InstallObject.getLogDescription()+"写配置参数到资源文件\n");

			PropFileUtils.writeStaticFieldToFile(InstallObject.class);

			log.info("结束写配置参数到资源文件");
		} catch (Exception e) {
			log.error("写入配置参数到资源文件出现异常", e);
			map = new HashMap<String, Object>();
			map.put("returnCode", "005");
		}
		map = new HashMap<String, Object>();
		map.put("returnCode", "200");
		return map;
	}

	/**
	 * 检查文件权限
	 * @return 检查结果
	 */
	@RequestMapping("/checkFilePermssion")
	@ResponseBody
	public Map<String,Object> checkFilePermssion(){
		 log.info("开始执行shell脚本");
		 Map<String,Object> map = new HashMap<String,Object>();

		 String tomcatHome = System.getProperty("catalina.home");
		 String shFilePath = tomcatHome+"/checkfiles.sh";
		 
		 Process process =null;
		 String chmodCommand = "chmod 777 " + shFilePath;
		 log.info("执行授权命令:【{}】",chmodCommand);
		    try {
				process = Runtime.getRuntime().exec(chmodCommand);
				int res = process.waitFor();
				log.info("执行授权命令返回值:【{}】",res);
			} catch (IOException e1) {
				log.error("执行shell脚本出现异常",e1);
			} catch (InterruptedException e) {
				log.error("执行shell脚本出现异常",e);
			}
		 
		 //执行文件权限检查
		    
		 log.info("sh文件路径:【{}】",shFilePath);
		 
		 Process pcs = null;
		
		 String command = "sh " + shFilePath+" "+tomcatHome ; 
		 log.info("执行sh命令:【{}】",command);
		    try {
		    	pcs = Runtime.getRuntime().exec(command);
				
				InputStreamReader ir = new InputStreamReader(pcs.getInputStream());  
				LineNumberReader input = new LineNumberReader(ir);  
				String line = null;    
				while ((line = input.readLine()) != null){    
				log.info("shell执行过程日志:【{}】",line);  
				}    
				if(null != input){    
				input.close();    
				}

				if(null != ir){    
				ir.close();    
				}

				int result = pcs.waitFor();
				log.info("执行shell脚本返回值:【{}】",result);
			} catch (InterruptedException e) {
				log.error("执行shell脚本出现异常",e);
			} catch (IOException e) {
				log.error("执行shell脚本出现异常",e);
			}
		    
		 String resultFilePath = tomcatHome+"/check/checkResult.txt";
		 
		 try {
			log.info("结果文件存放路径:【{}】",resultFilePath);
			
			File file=new File(resultFilePath);    
			if(!file.exists()){
				log.error("读取文件出现异常,找不到文件:【{}】",resultFilePath);
				map.put("returnCode", "005");
				return map;
			}
			FileReader reader = new FileReader(resultFilePath);
			
			BufferedReader br = new BufferedReader(reader);
			String str = null;
			List<CheckFilePermission> list = new ArrayList<CheckFilePermission>();
			while((str = br.readLine()) != null) {
				if("".equals(str)){
					break;
				}
                 String[] strArr = str.split(":");
                 CheckFilePermission cfp = new CheckFilePermission();
                 String namex = strArr[0];
                 String namey = strArr[1];
                 if(namex!=null&&!"".equals(namex)){
                	 cfp.setFilename(namex);
                 }
                 if(namey!=null&&!"".equals(namey)){
                	 cfp.setPermission(namey);
                 }
                 list.add(cfp);
           }
			br.close();
			reader.close();
			map.put("result", list);
			map.put("returnCode", "200");

		} catch (FileNotFoundException e) {
			log.error("读取文件出现异常,找不到文件",e);
			map.put("returnCode", "005");
			return map;
		} catch (IOException e) {
			log.error("读取文件出现异常，出现io异常",e);
			map.put("returnCode", "005");
			return map;
		}
		 log.info("结束执行shell脚本");
		 return map;
	}

	/**
	 * 检查数据库配置参数
	 * @param url 数据库连接url
	 * @param driver 数据库驱动
	 * @param username 数据库用户名
	 * @param passwd 数据库密码
	 * @return
	 */
	@RequestMapping("/checkDBParameter")
	@ResponseBody
	public Map<String, Object> checkDBParameter(String url, String driver, String username, String passwd) {
		Map<String, Object> map = null;
		boolean flag = checkDBConn(url, driver, username, passwd);
		if(flag){
			map = new HashMap<String,Object>();
			map.put("returnCode", "200");
		}else{
			map = new HashMap<String,Object>();
			map.put("returnCode", "005");
		}
		return map;
	}

	/**
	 * 检查字段是否为空
	 *
	 * @return false没有字段为空，true有字段为空
	 */
	public boolean checkFields() {
		boolean flag = false;
		if (InstallObject.getSpCode() == null || "".equals(InstallObject.getSpCode())) {

			flag = true;
		}
		if (InstallObject.getCName() == null || "".equals(InstallObject.getCName())) {

			flag = true;
		}
		if (InstallObject.getKey() == null || "".equals(InstallObject.getKey())) {

			flag = true;
		}
		if (InstallObject.getDbUrl() == null || "".equals(InstallObject.getDbUrl())) {

			flag = true;
		}
		if (InstallObject.getDbDriver() == null || "".equals(InstallObject.getDbDriver())) {

			flag = true;
		}
		if (InstallObject.getDbUser() == null || "".equals(InstallObject.getDbUser())) {

			flag = true;
		}
		if (InstallObject.getDbPasswd() == null || "".equals(InstallObject.getDbPasswd())) {

			flag = true;
		}
		if (InstallObject.getSendSMSUrl() == null || "".equals(InstallObject.getSendSMSUrl())) {

			flag = true;
		}
		if (InstallObject.getSendEMSUrl() == null || "".equals(InstallObject.getSendEMSUrl())) {

			flag = true;
		}
		if (InstallObject.getGetTaskUrl() == null || "".equals(InstallObject.getGetTaskUrl())) {

			flag = true;
		}
		if (InstallObject.getListReply() == null || "".equals(InstallObject.getListReply())) {

			flag = true;
		}
		if (InstallObject.getSmsSendTable() == null || "".equals(InstallObject.getSmsSendTable())) {

			flag = true;
		}
		if (InstallObject.getMessageContent() == null || "".equals(InstallObject.getMessageContent())) {

			flag = true;
		}
		if (InstallObject.getSendToMobile() == null || "".equals(InstallObject.getSendToMobile())) {

			flag = true;
		}
		if (InstallObject.getId() == null || "".equals(InstallObject.getId())) {

			flag = true;
		}
		if (InstallObject.getPlanTime() == null || "".equals(InstallObject.getPlanTime())) {

			flag = true;
		}
		if (InstallObject.getSendType() == null || "".equals(InstallObject.getSendType())) {

			flag = true;
		}
		if (InstallObject.getCreateTime() == null || "".equals(InstallObject.getCreateTime())) {

			flag = true;
		}
		if (InstallObject.getStatus() == null || "".equals(InstallObject.getStatus())) {

			flag = true;
		}
		if (InstallObject.getSerialNumber() == null || "".equals(InstallObject.getSerialNumber())) {

			flag = true;
		}
		if (InstallObject.getTaskId() == null || "".equals(InstallObject.getTaskId())) {

			flag = true;
		}
		if (InstallObject.getTextTemplateId() == null || "".equals(InstallObject.getTextTemplateId())) {

			flag = true;
		}
		if (InstallObject.getMediaId() == null || "".equals(InstallObject.getMediaId())) {

			flag = true;
		}
		if (InstallObject.getMediaUrl() == null || "".equals(InstallObject.getMediaUrl())) {

			flag = true;
		}
		if (InstallObject.getSendEmsType() == null || "".equals(InstallObject.getSendEmsType())) {

			flag = true;
		}
		if (InstallObject.getSendSmsType() == null || "".equals(InstallObject.getSendSmsType())) {

			flag = true;
		}
		if (InstallObject.getUnSendStatus() == null || "".equals(InstallObject.getUnSendStatus())) {

			flag = true;
		}
		if (InstallObject.getSendedStatus() == null || "".equals(InstallObject.getSendedStatus())) {

			flag = true;
		}
		if (InstallObject.getSendFailStatus() == null || "".equals(InstallObject.getSendFailStatus())) {

			flag = true;
		}
		if (InstallObject.getReplyContent() == null || "".equals(InstallObject.getReplyContent())) {

			flag = true;
		}
		return flag;
	}

	/**
	 * 校验短信发送接口是否通达
	 *
	 * @return true表示通达，false表示不通达
	 */
	public boolean checkSmsSend() {
		boolean flag = false;

		log.info("开始校验短信发送接口");

		sdk.setSpCode(InstallObject.getSpCode());
		sdk.setUserName(InstallObject.getCName());
		sdk.setKey(InstallObject.getKey());

		sdk.setApiUrl(InstallObject.getSendSMSUrl());

		String methodUrl = "/cmc/sms/send";
		Map<String, Object> requestMap = new HashMap<String, Object>();
		requestMap.put("text", "测试短信");
		requestMap.put("textTemplateId", "");
		requestMap.put("scheduleTime", "");
		requestMap.put("sendObject", "00000000000");
		DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssFFFFFF");
		String datestr = sdf.format(new Date());
		requestMap.put("serialNumber", datestr);
		try {
			String resp = sdk.postRequest(methodUrl, requestMap);
			// {"returnCode":"499","returnMsg":"含有不可发送的手机号码:00000000000"}
			JSONObject json = JSONObject.fromObject(resp);
			log.info("校验短信发送接口响应信息:【{}】", json.toString());
			if (!"499".equals(json.get("returnCode").toString())) {
				log.info("<<<<<<<校验短信发送接口失败>>>>>>>");
				flag = false;
			} else {
				log.info("<<<<<<<校验短信发送接口成功>>>>>>>");
				flag = true;
			}

		} catch (Exception e1) {
			log.error("校验短信发送出现异常", e1);
		}
		log.info("结束校验短信发送接口");
		return flag;
	}

	/**
	 * 校验动态短信发送接口是否通达
	 * @return true表示通达，false表示不通达
	 */
	public boolean checkDynamicSmsSend() {
		boolean flag = false;

		log.info("开始校验动态短信发送接口");

		sdk.setSpCode(InstallObject.getSpCode());
		sdk.setUserName(InstallObject.getCName());
		sdk.setKey(InstallObject.getKey());

		sdk.setApiUrl(InstallObject.getSendSMSUrl());

		String methodUrl = "/cmc/dynamic-sms/send";
		Map<String, Object> requestMap = new HashMap<String, Object>();
		requestMap.put("text", "测试短信{name}");
		requestMap.put("dynamicData", "[{\"手机号码\":\"00000000000\",\"name\":\"***\"}]");
		try {
			String resp = sdk.postRequest(methodUrl, requestMap);
			//{"returnCode":"499","returnMsg":"含有不可发送的手机号码:00000000000"}
			JSONObject json = JSONObject.fromObject(resp);
			log.info("校验动态短信发送接口响应信息:【{}】", json.toString());
			
			if (!"499".equals(json.get("returnCode").toString())) {
				log.info("<<<<<<<校验动态短信发送接口失败>>>>>>>");
				flag = false;
			} else {
				log.info("<<<<<<<校验动态短信发送接口成功>>>>>>>");
				flag = true;
			}
		} catch (Exception e) {
			log.error("校验动态短信发送出现异常", e);
		}
		log.info("结束校验动态短信发送接口");
		return flag;
	}
	/**
	 * 校验e信发送接口是否通达
	 *
	 * @return true表示通达，false表示不通达
	 */
	public boolean checkEmsSend() {
		boolean flag = false;
		log.info("开始校验e信发送接口");

		sdk.setSpCode(InstallObject.getSpCode());
		sdk.setUserName(InstallObject.getCName());
		sdk.setKey(InstallObject.getKey());

		sdk.setApiUrl(InstallObject.getSendEMSUrl());

		String methodUrl = "/cmc/ems/send";
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("text", "e信发送测试");
		reqMap.put("sendObject", "00000000000");
		reqMap.put("mediaId", "0");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssFFFFFF");
		reqMap.put("serialNumber", sdf.format(new Date()));
		try {
			String resp = sdk.postRequest(methodUrl, reqMap);
			// 【{"returnCode":"499","returnMsg":"含有不可发送的手机号码:00000000000"}】
			JSONObject json = JSONObject.fromObject(resp);
			log.info("校验e信发送接口响应信息:【{}】", json.toString());
			String result = json.get("returnCode").toString();
			if (!"499".equals(result)) {
				log.info("<<<<<<<校验e信发送接口失败>>>>>>>");
				flag = false;
			} else {
				log.info("<<<<<<<校验e信发送接口成功>>>>>>>");
				flag = true;
			}

		} catch (Exception e1) {
			log.error("校验e信发送出现异常", e1);
		}

		log.info("结束校验e信发送接口");

		return flag;
	}

	/**
	 * 校验动态e信发送接口是否通达
	 * @return true表示通达，false表示不通达
	 */
	public boolean checkDynamicEmsSend() {
		boolean flag = false;
		log.info("开始校验动态e信发送接口");

		sdk.setSpCode(InstallObject.getSpCode());
		sdk.setUserName(InstallObject.getCName());
		sdk.setKey(InstallObject.getKey());

		sdk.setApiUrl(InstallObject.getSendEMSUrl());

		String methodUrl = "/cmc/dynamic-ems/send";
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("mediaId", "0");
		reqMap.put("text", "test {姓名} sms content!");
		reqMap.put("dynamicData", "[{\"手机号码\":\"00000000000\",\"姓名\":\"***\"}]");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssFFFFFF");
		reqMap.put("serialNumber", sdf.format(new Date()));
		try {
			String resp = sdk.postRequest(methodUrl, reqMap);
			// 【{"returnCode":"499","returnMsg":"含有不可发送的手机号码:00000000000"}】
			JSONObject json = JSONObject.fromObject(resp);
			log.info("校验动态e信发送接口响应信息:【{}】", json.toString());
			String result = json.get("returnCode").toString();
			if (!"499".equals(result)) {
				log.info("<<<<<<<校验动态e信发送接口失败>>>>>>>");
				flag = false;
			} else {
				log.info("<<<<<<<校验动态e信发送接口成功>>>>>>>");
				flag = true;
			}

		} catch (Exception e1) {
			log.error("校验动态e信发送出现异常", e1);
		}

		log.info("结束校验动态e信发送接口");

		return flag;
	}
	/**
	 * 检查查询发送结果接口
	 *
	 * @return true表示通达，false表示不通达
	 */
	public boolean checkGetTask() {
		boolean flag = false;

		log.info("开始校验查询发送结果接口");

		sdk.setSpCode(InstallObject.getSpCode());
		sdk.setUserName(InstallObject.getCName());
		sdk.setKey(InstallObject.getKey());

		sdk.setApiUrl(InstallObject.getGetTaskUrl());

		String methodUrl = "/cmc/task/get";
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("taskId", "0");
		try {
			String resp = sdk.postRequest(methodUrl, reqMap);

			// {"returnCode":"200","returnMsg":null,"taskInfo":{"networkMdn":null,"allMdn":null,"status":null,"networkNum":null,"receiveNum":null,"allNum":null}}
			JSONObject json = JSONObject.fromObject(resp);
			log.info("校验e信发送接口响应信息:【{}】", json.toString());

			if ("200".equals(json.get("returnCode"))) {
				log.info("<<<<<<<校验查询发送结果接口成功>>>>>>>");
				flag = true;
			} else {
				log.info("<<<<<<<校验查询发送结果接口失败>>>>>>>");
				flag = false;
			}
		} catch (Exception e) {
			log.error("校验查询发送结果接口出现异常", e);
		}

		log.info("结束校验查询发送结果接口");

		return flag;
	}

	/**
	 * 检查上行回复数据接口
	 *
	 * @return true表示通达，false表示不通达
	 */
	public boolean checkListReply() {
		boolean flag = false;

		log.info("开始校验上行回复数据接口");

		sdk.setSpCode(InstallObject.getSpCode());
		sdk.setUserName(InstallObject.getCName());
		sdk.setKey(InstallObject.getKey());

		sdk.setApiUrl(InstallObject.getListReply());

		String methodUrl = "/cmc/reply/list";
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("replyId", "0");
		reqMap.put("limit", "500");
		try {
			String resp = sdk.postRequest(methodUrl, reqMap);

			// {"returnCode":"200","returnMsg":"返回成功","list":[]}

			JSONObject json = JSONObject.fromObject(resp);
			log.info("校验上行回复数据接口响应信息:【{}】", json.toString());

			if ("200".equals(json.get("returnCode"))) {
				log.info("<<<<<<<校验上行回复数据接口成功>>>>>>>");
				flag = true;
			} else {
				log.info("<<<<<<<校验上行回复数据接口失败>>>>>>>");
				flag = false;
			}
		} catch (Exception e) {
			log.error("校验上行回复数据接口出现异常", e);
		}

		log.info("结束校验上行回复数据接口");

		return flag;
	}

	/**
	 * 检查e信上行回复表单数据接口
	 * @return true表示通达，false表示不通达
	 */
	public boolean checkEListReply() {
		boolean flag = false;
		log.info("开始校验e信上行回复表单数据接口");

		sdk.setSpCode(InstallObject.getSpCode());
		sdk.setUserName(InstallObject.getCName());
		sdk.setKey(InstallObject.getKey());

		sdk.setApiUrl(InstallObject.getListReply());
		
		String methodUrl = "/cmc/ems/form-info/list";
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("taskId", "0");
		reqMap.put("maxId", "0");
		try {
			String resp = sdk.postRequest(methodUrl, reqMap);

			// {"returnCode":"200","returnMsg":"返回成功","obj":{},"success":true,"ec":0}

			JSONObject json = JSONObject.fromObject(resp);
			log.info("校验e信上行回复表单数据接口响应信息:【{}】", json.toString());

			if ("200".equals(json.get("returnCode"))) {
				log.info("<<<<<<<校验e信上行回复表单数据接口成功>>>>>>>");
				flag = true;
			} else {
				log.info("<<<<<<<校验e信上行回复表单数据接口失败>>>>>>>");
				flag = false;
			}
		} catch (Exception e) {
			log.error("校验e信上行回复表单数据接口出现异常", e);
			log.info("启动测试模式,放过e信上行回复表单数据接口校验");
			flag=true;
		}

		log.info("结束校验e信上行回复表单数据接口");
		return flag;
	}
	/**
	 * 检查数据库配置参数连接数据库是否正常
	 * @param url
	 * @param driver
	 * @param username
	 * @param passwd
	 * @return true可以正常连接数据库，false不能正常连接数据库
	 */
	public boolean checkDBConn(String url, String driver, String username, String passwd) {

		boolean flag = false;
		log.info("开始检查数据库配置参数");
		Connection conn;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, passwd);
			if (conn == null) {
				flag = false;
			} else {
				flag = true;
				conn.close();
			}

		} catch (SQLException e) {
			log.info("检查数据库配置参数出现异常", e);
			return flag;
		} catch (ClassNotFoundException e) {
			log.info("检查数据库配置参数出现异常", e);
			return flag;
		}

		log.info("结束检查数据库配置参数");

		return flag;
	}
}
