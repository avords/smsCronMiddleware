package cn.com.flaginfo.ware.vo;

import java.io.Serializable;

/**
 * 序列化安装过程中的页面对象
 * @author hc
 *
 */
public class InstallObject implements Serializable {
	/**
	 * 企业服务码
	 */
    private static String spCode;
    /**
     * 企业名称
     */
    private static String cName;
    /**
     * 企业密码
     */
    private static String key;
    /**
     * 数据库地址url
     */
    private static String dbUrl;
    /**
     * 数据库驱动地址
     */
    private static String dbDriver;
    /**
     * 数据库用户名称
     */
    private static String dbUser;
    /**
     * 数据库用户密码
     */
    private static String dbPasswd;
    /**
     * 中间件安装路径
     */
    private static String proUrl;
    /**
     * 日志描述
     */
    private static String logDescription;
    /**
     * 发送短信url
     */
    private static String sendSMSUrl;
    /**
     * 发送e信url
     */
    private static String sendEMSUrl;
    /**
     * 查询发送结果url
     */
    private static String getTaskUrl;
    /**
     * 查询上行回复结果url
     */
    private static String listReply;
    /**
     * 信息发送表名
     */
    private static String smsSendTable;
    /**
     * 短信内容
     */
    private static String messageContent;
    /**
     * 要发送的号码
     */
    private static String sendToMobile;
    /**
     * 唯一键值
     */
    private static String id;
    /**
     * 定时发送时间
     */
    private static String planTime;
    /**
     * 发送类型
     */
    private static String sendType;
    /**
     * 发送短信类型值
     */
    private static String sendSmsType;
    /**
     * 发送e信类型值
     */
    private static String sendEmsType;
    /**
     * 发送任务创建时间
     */
    private static String createTime;
    /**
     * 短信状态
     */
    private static String status;
    /**
     * 短信未发送状态值
     */
    private static String unSendStatus;
    /**
     * 短信已发送状态值
     */
    private static String sendedStatus;
    /**
     * 短信发送失败状态值
     */
    private static String sendFailStatus;
    /**
     * 发送任务流水号
     */
    private static String serialNumber;
    /**
     * 发送任务编号
     */
    private static String taskId;
    /**
     * 短信模板
     */
    private static String textTemplateId;
    /**
     * e信素材Id
     */
    private static String mediaId;
    /**
     * e信素材Url
     */
    private static String mediaUrl;
    /**
     * 动态json
     */
    private static String dynamicJson;
    /**
     * 上行回复内容
     */
    private static String replyContent;
    public static String getSpCode() {
        return spCode;
    }
    public static void setSpCode(String spCode) {
        InstallObject.spCode = spCode;
    }
    public static String getCName() {
        return cName;
    }
    public static void setCName(String cName) {
        InstallObject.cName = cName;
    }
    public static String getKey() {
        return key;
    }
    public static void setKey(String key) {
        InstallObject.key = key;
    }
    public static String getDbUrl() {
        return dbUrl;
    }
    public static void setDbUrl(String dbUrl) {
        InstallObject.dbUrl = dbUrl;
    }
    public static String getDbDriver() {
        return dbDriver;
    }
    public static void setDbDriver(String dbDriver) {
        InstallObject.dbDriver = dbDriver;
    }
    public static String getDbUser() {
        return dbUser;
    }
    public static void setDbUser(String dbUser) {
        InstallObject.dbUser = dbUser;
    }
    public static String getDbPasswd() {
        return dbPasswd;
    }
    public static void setDbPasswd(String dbPasswd) {
        InstallObject.dbPasswd = dbPasswd;
    }
    public static String getSendSMSUrl() {
        return sendSMSUrl;
    }
    public static void setSendSMSUrl(String sendSMSUrl) {
        InstallObject.sendSMSUrl = sendSMSUrl;
    }
    public static String getSendEMSUrl() {
        return sendEMSUrl;
    }
    public static void setSendEMSUrl(String sendEMSUrl) {
        InstallObject.sendEMSUrl = sendEMSUrl;
    }
    public static String getGetTaskUrl() {
        return getTaskUrl;
    }
    public static void setGetTaskUrl(String getTaskUrl) {
        InstallObject.getTaskUrl = getTaskUrl;
    }
    public static String getListReply() {
        return listReply;
    }
    public static void setListReply(String listReply) {
        InstallObject.listReply = listReply;
    }
    public static String getSmsSendTable() {
        return smsSendTable;
    }
    public static void setSmsSendTable(String smsSendTable) {
        InstallObject.smsSendTable = smsSendTable;
    }
    public static String getMessageContent() {
        return messageContent;
    }
    public static void setMessageContent(String messageContent) {
        InstallObject.messageContent = messageContent;
    }
    public static String getSendToMobile() {
        return sendToMobile;
    }
    public static void setSendToMobile(String sendToMobile) {
        InstallObject.sendToMobile = sendToMobile;
    }
    public static String getId() {
        return id;
    }
    public static void setId(String id) {
        InstallObject.id = id;
    }
    public static String getPlanTime() {
        return planTime;
    }
    public static void setPlanTime(String planTime) {
        InstallObject.planTime = planTime;
    }
    public static String getSendType() {
        return sendType;
    }
    public static void setSendType(String sendType) {
        InstallObject.sendType = sendType;
    }
    public static String getCreateTime() {
        return createTime;
    }
    public static void setCreateTime(String createTime) {
        InstallObject.createTime = createTime;
    }
    public static String getStatus() {
        return status;
    }
    public static void setStatus(String status) {
        InstallObject.status = status;
    }
    public static String getSerialNumber() {
        return serialNumber;
    }
    public static void setSerialNumber(String serialNumber) {
        InstallObject.serialNumber = serialNumber;
    }
    public static String getTaskId() {
        return taskId;
    }
    public static void setTaskId(String taskId) {
        InstallObject.taskId = taskId;
    }
    public static String getTextTemplateId() {
        return textTemplateId;
    }
    public static void setTextTemplateId(String textTemplateId) {
        InstallObject.textTemplateId = textTemplateId;
    }
    public static String getMediaId() {
        return mediaId;
    }
    public static void setMediaId(String mediaId) {
        InstallObject.mediaId = mediaId;
    }
    public static String getMediaUrl() {
        return mediaUrl;
    }
    public static void setMediaUrl(String mediaUrl) {
        InstallObject.mediaUrl = mediaUrl;
    }
	public static String getProUrl() {
		return proUrl;
	}
	public static void setProUrl(String proUrl) {
		InstallObject.proUrl = proUrl;
	}
	public static String getLogDescription() {
		return logDescription;
	}
	public static void setLogDescription(String logDescription) {
		InstallObject.logDescription = logDescription;
	}
	public static String getcName() {
		return cName;
	}
	public static void setcName(String cName) {
		InstallObject.cName = cName;
	}
	public static String getSendSmsType() {
		return sendSmsType;
	}
	public static void setSendSmsType(String sendSmsType) {
		InstallObject.sendSmsType = sendSmsType;
	}
	public static String getSendEmsType() {
		return sendEmsType;
	}
	public static void setSendEmsType(String sendEmsType) {
		InstallObject.sendEmsType = sendEmsType;
	}
	public static String getUnSendStatus() {
		return unSendStatus;
	}
	public static void setUnSendStatus(String unSendStatus) {
		InstallObject.unSendStatus = unSendStatus;
	}
	public static String getSendedStatus() {
		return sendedStatus;
	}
	public static void setSendedStatus(String sendedStatus) {
		InstallObject.sendedStatus = sendedStatus;
	}
	public static String getDynamicJson() {
		return dynamicJson;
	}
	public static void setDynamicJson(String dynamicJson) {
		InstallObject.dynamicJson = dynamicJson;
	}
	public static String getSendFailStatus() {
		return sendFailStatus;
	}
	public static void setSendFailStatus(String sendFailStatus) {
		InstallObject.sendFailStatus = sendFailStatus;
	}
	public static String getReplyContent() {
		return replyContent;
	}
	public static void setReplyContent(String replyContent) {
		InstallObject.replyContent = replyContent;
	}

}
