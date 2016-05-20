package cn.com.flaginfo.ware.vo;

import java.io.Serializable;

/**
 * 检查文件授权信息vo
 * @author hc
 *
 */
public class CheckFilePermission implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2206013397762768510L;
	/**
	 * 文件(夹)名称
	 */
    private String filename;
    /**
     * 授权信息
     */
    private String permission;
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
    
}
