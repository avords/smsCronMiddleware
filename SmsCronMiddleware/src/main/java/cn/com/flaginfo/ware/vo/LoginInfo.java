package cn.com.flaginfo.ware.vo;

/**
 * 登录用户信息
 * @author hc
 *
 */
public class LoginInfo {
   /**
    * 企业服务码
    */
   private String spCode;
   /**
    * 企业名称
    */
   private String cName;
   /**
    * 企业密码
    */
   private String key;
public String getSpCode() {
	return spCode;
}
public void setSpCode(String spCode) {
	this.spCode = spCode;
}
public String getcName() {
	return cName;
}
public void setcName(String cName) {
	this.cName = cName;
}
public String getKey() {
	return key;
}
public void setKey(String key) {
	this.key = key;
}
}
