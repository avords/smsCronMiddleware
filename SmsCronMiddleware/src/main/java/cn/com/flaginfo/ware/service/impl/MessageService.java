package cn.com.flaginfo.ware.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.flaginfo.ware.dao.BaseDao;
import cn.com.flaginfo.ware.service.IMessageService;
import cn.com.flaginfo.ware.vo.InstallObject;
@Service
public class MessageService implements IMessageService{
    private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);
    @Autowired
    private BaseDao baseDao;
    @Override
    public List<Map<String, Object>> getSMSMessages(int n) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        String json = InstallObject.getDynamicJson();
        if(StringUtils.isNotBlank(json)){
            sql.append(InstallObject.getSendToMobile()+" as 手机号码,");//动态参数
            try{
                JSONArray jsonArray = JSON.parseArray(json);
                for(int i=0;i<jsonArray.size();i++){
                    JSONObject jo = jsonArray.getJSONObject(i);
                    String key = jo.getString("skey");
                    String value = jo.getString("svalue");
                    if(StringUtils.isNotBlank(key)&&StringUtils.isNotBlank(value)){
                        sql.append(value+" as "+key+",");
                    }
                }
             }catch(Exception e){
                 LOG.error("转json出错:{}",json);
             }
        }else{
            sql.append(InstallObject.getSendToMobile()+" as sendObject,");//静态参数
        }
        sql.append(InstallObject.getId()).append(",");
        if(StringUtils.isNotBlank(InstallObject.getTextTemplateId())){
            sql.append(InstallObject.getTextTemplateId()).append(" as textTemplateId,");
        }
        if(StringUtils.isNotBlank(InstallObject.getSerialNumber())){
            sql.append(InstallObject.getSerialNumber()).append(" as serialNumber,");
        }
        if(StringUtils.isNotBlank(InstallObject.getPlanTime())){
            sql.append(InstallObject.getPlanTime()).append(" as scheduleTime,");
        }
        sql.append(InstallObject.getMessageContent()+" as text")
        .append(" from ").append(InstallObject.getSmsSendTable())
        .append(" where ").append(InstallObject.getStatus()).append("=? and ").append(InstallObject.getSendType()).append("=?")
        .append(" limit 0,?");
        return baseDao.queryListMap(sql.toString(), InstallObject.getUnSendStatus(),
                InstallObject.getSendSmsType(),n);
    }
    @Override
    public List<Map<String, Object>> getEMSMessages(int n) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        String json = InstallObject.getDynamicJson();
        if(StringUtils.isNotBlank(json)){
            sql.append(InstallObject.getSendToMobile()+" as 手机号码,");//动态参数
            try{
                JSONArray jsonArray = JSON.parseArray(json);
                for(int i=0;i<jsonArray.size();i++){
                    JSONObject jo = jsonArray.getJSONObject(i);
                    String key = jo.getString("skey");
                    String value = jo.getString("svalue");
                    if(StringUtils.isNotBlank(key)&&StringUtils.isNotBlank(value)){
                        sql.append(value+" as "+key+",");
                    }
                }
             }catch(Exception e){
                 LOG.error("转json出错:{}",json);
             }
        } else {
            sql.append(InstallObject.getSendToMobile()+" as sendObject,");//静态参数
        }
        sql.append(InstallObject.getId()).append(",")
        .append(InstallObject.getMediaId()+" as mediaId,");
        if(StringUtils.isNotBlank(InstallObject.getSerialNumber())){
            sql.append(InstallObject.getSerialNumber()).append(" as serialNumber,");
        }
        if(StringUtils.isNotBlank(InstallObject.getMediaUrl())){
            sql.append(InstallObject.getMediaUrl()).append(" as mediaUrl,");
        }
        if(StringUtils.isNotBlank(InstallObject.getPlanTime())){
            sql.append(InstallObject.getPlanTime()).append(" as scheduleTime,");
        }
        sql.append(InstallObject.getMessageContent()+" as text")
        .append(" from ").append(InstallObject.getSmsSendTable())
        .append(" where ").append(InstallObject.getStatus()).append("=? and ").append(InstallObject.getSendType()).append("=?")
        .append(" limit 0,?");
        return baseDao.queryListMap(sql.toString(), InstallObject.getUnSendStatus(),
                InstallObject.getSendEmsType(),n);
    }
    @Override
    public boolean updateStatusById(Object id,Object status){
        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(InstallObject.getSmsSendTable())
        .append(" set ").append(InstallObject.getStatus()).append("=?")
        .append(" where ").append(InstallObject.getId()).append("=?");
        boolean result;
        try {
            result = baseDao.update(sql.toString(), status,id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    @Override
    public boolean updateTaskIdById(Object id, Object taskId) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(InstallObject.getSmsSendTable())
        .append(" set ").append(InstallObject.getTaskId()).append("=?")
        .append(" where ").append(InstallObject.getId()).append("=?");
        boolean result;
        try {
            result = baseDao.update(sql.toString(), taskId,id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    @Override
    public boolean updateEMSReplyContent(Object taskId, Object mobilephone, Object content) {
        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(InstallObject.getSmsSendTable())
        .append(" set ").append(InstallObject.getReplyContent()).append("=?")
        .append(" where ").append(InstallObject.getTaskId()).append("=?")
        .append(" and ").append(InstallObject.getSendToMobile()).append("=?");
        boolean result;
        try {
            result = baseDao.update(sql.toString(), content,taskId,mobilephone);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
	@Override
	public List<Map<String, Object>> getEMSTaskMessage(int n, int start) throws Exception {
		StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append(InstallObject.getId()).append(",")
        .append(InstallObject.getSendToMobile()).append(",")
        .append(InstallObject.getMessageContent()+" as text,")
        .append(InstallObject.getTaskId())
        .append(" from ").append(InstallObject.getSmsSendTable())
        .append(" where ").append(InstallObject.getStatus())
        .append("=? and ").append(InstallObject.getSendType()).append("=?")
        .append(" and ").append(InstallObject.getTaskId()).append(" is not null")
        .append(" and ").append(InstallObject.getReplyContent()).append(" is null")
        .append(" order by ").append(InstallObject.getTaskId()).append(" desc")
        .append(" limit ?,?");
        return baseDao.queryListMap(sql.toString(), InstallObject.getSendedStatus(),
                InstallObject.getSendEmsType(), start, n);
	}

}
