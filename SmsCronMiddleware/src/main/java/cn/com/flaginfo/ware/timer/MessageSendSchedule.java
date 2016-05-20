package cn.com.flaginfo.ware.timer;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.flaginfo.ware.service.IMessageSendService;
import cn.com.flaginfo.ware.service.IMessageService;
import cn.com.flaginfo.ware.vo.InstallObject;

@Component
public class MessageSendSchedule {
	private static final Logger log =
			LoggerFactory.getLogger(MessageSendSchedule.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final SimpleDateFormat sdf_db = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private IMessageService messageService;
    @Autowired
    private IMessageSendService messageSendService;

    /**
     * 定时发送短信,每隔2分钟执行一次
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 */2 * * * ?")
    public void smsSend() throws Exception {
        while (true) {
            List<Map<String, Object>> messages = messageService.getSMSMessages(100);
            
            log.debug("resultList: ", JSON.toJSONString(messages));
            
            if (messages == null || messages.size() == 0) {
                break;
            }
            String json = InstallObject.getDynamicJson();
            if (StringUtils.isNotBlank(json)) {
                // 调用动态的
                for (Map<String, Object> param : messages) {
                    if (param.get("text") == null) {
                        continue;
                    }
                    List<Map<String, String>> tempList = new ArrayList<Map<String, String>>();
                    Map<String, String> map = new HashMap<String, String>();
                    for(String key:param.keySet()){
                        Object value = param.get(key);
                        if(value!=null){
                            map.put(key, value.toString());
                        }else{
                            map.put(key, "");
                        }
                    }
                    tempList.add(map);
                    Map<String, Object> otherParam = new HashMap<String, Object>();
                    if (param.get("textTemplateId") != null) {
                        otherParam.put("textTemplateId", param.get("textTemplateId").toString());
                    }
                    if (param.get("serialNumber") != null) {
                        otherParam.put("serialNumber", param.get("serialNumber").toString());
                    }
                    if (param.get("scheduleTime") != null) {
                        Timestamp date = (Timestamp) param.get("scheduleTime");
                        otherParam.put("scheduleTime", sdf.format(date));
                    }

                    String resp = messageSendService.sendDynamicSMS(JSON.toJSONString(tempList),
                            param.get("text").toString(), otherParam);

                    log.debug("resultJson: ", resp);

                    JSONObject resultJson = JSON.parseObject(resp);
                    if ("200".equals(resultJson.getString("returnCode"))) {
                        messageService.updateStatusById(param.get(InstallObject.getId()).toString(),
                                InstallObject.getSendedStatus());
                        messageService.updateTaskIdById(param.get(InstallObject.getId()).toString(),
                                resultJson.getString("taskId"));
                    } else {
                        messageService.updateStatusById(param.get(InstallObject.getId()).toString(),
                                InstallObject.getSendFailStatus());
                    }
                }
            } else {
                // 调用静态的
                for (Map<String, Object> param : messages) {
                    if (param.get("text") == null) {
                        continue;
                    }

                    Map<String, Object> otherParam = new HashMap<String, Object>();
                    if (param.get("textTemplateId") != null) {
                        otherParam.put("textTemplateId", param.get("textTemplateId").toString());
                    }
                    if (param.get("serialNumber") != null) {
                        otherParam.put("serialNumber", param.get("serialNumber").toString());
                    }
                    if (param.get("scheduleTime") != null) {
                        Timestamp date = (Timestamp) param.get("scheduleTime");
                        otherParam.put("scheduleTime", sdf.format(date));
                    }

                    String resp = messageSendService.sendSMS(param.get("sendObject").toString(),
                            param.get("text").toString(), otherParam);

                    log.debug("resultJson: ", resp);

                    JSONObject resultJson = JSON.parseObject(resp);
                    if ("200".equals(resultJson.getString("returnCode"))) {
                        messageService.updateStatusById(param.get(InstallObject.getId()).toString(),
                                InstallObject.getSendedStatus());
                        messageService.updateTaskIdById(param.get(InstallObject.getId()).toString(),
                                resultJson.getString("taskId"));
                    } else {
                        messageService.updateStatusById(param.get(InstallObject.getId()).toString(),
                                InstallObject.getSendFailStatus());
                    }
                }
            }
        }
    }

    /**
     * 定时发送E信,每隔2分钟执行一次
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 */2 * * * ?")
    public void emsSend() throws Exception {
        while (true) {
            List<Map<String, Object>> messages = messageService.getEMSMessages(100);
            
            log.debug("resultList: ", JSON.toJSONString(messages));
            
            if (messages == null || messages.size() == 0) {
                break;
            }
            String json = InstallObject.getDynamicJson();
            if (StringUtils.isNotBlank(json)) {
                // 调用动态的
                for (Map<String, Object> param : messages) {

                    if (param.get("text") == null) {
                        continue;
                    }

                    List<Map<String, String>> tempList = new ArrayList<Map<String, String>>();
                    Map<String, String> map = new HashMap<String, String>();
                    for(String key:param.keySet()){
                        Object value = param.get(key);
                        if(value!=null){
                            map.put(key, value.toString());
                        }else{
                            map.put(key, "");
                        }
                    }
                    tempList.add(map);

                    Map<String, Object> otherParam = new HashMap<String, Object>();
                    if (param.get("mediaId") != null) {
                        otherParam.put("mediaId", param.get("mediaId").toString());
                    }
                    if (param.get("mediaUrl") != null) {
                        otherParam.put("mediaUrl", param.get("mediaUrl").toString());
                    }
                    if (param.get("serialNumber") != null) {
                        otherParam.put("serialNumber", param.get("serialNumber").toString());
                    }
                    if (param.get("scheduleTime") != null) {
                        Timestamp date = (Timestamp) param.get("scheduleTime");
                        otherParam.put("scheduleTime", sdf.format(date));
                    }

                    String resp = messageSendService.sendDynamicEMS(JSON.toJSONString(tempList),
                            param.get("text").toString(), InstallObject.getMediaId(), otherParam);

                    log.debug("resultJson: ", resp);

                    JSONObject resultJson = JSON.parseObject(resp);
                    if ("200".equals(resultJson.getString("returnCode"))) {
                        messageService.updateStatusById(param.get(InstallObject.getId()).toString(),
                                InstallObject.getSendedStatus());
                        messageService.updateTaskIdById(param.get(InstallObject.getId()).toString(),
                                resultJson.getString("taskId"));
                    } else {
                        messageService.updateStatusById(param.get(InstallObject.getId()).toString(),
                                InstallObject.getSendFailStatus());
                    }
                }
            } else {
                // 调用静态的
                for (Map<String, Object> param : messages) {
                    if (param.get("text") == null) {
                        continue;
                    }

                    Map<String, Object> otherParam = new HashMap<String, Object>();
                    if (param.get("mediaId") != null) {
                        otherParam.put("mediaId", param.get("mediaId").toString());
                    }
                    if (param.get("mediaUrl") != null) {
                        otherParam.put("mediaUrl", param.get("mediaUrl").toString());
                    }
                    if (param.get("serialNumber") != null) {
                        otherParam.put("serialNumber", param.get("serialNumber").toString());
                    }
                    if (param.get("scheduleTime") != null) {
                        Timestamp date = (Timestamp) param.get("scheduleTime");
                        otherParam.put("scheduleTime", sdf.format(date));
                    }

                    String resp = messageSendService.sendEMS(param.get("sendObject").toString(),
                            param.get("text").toString(), InstallObject.getMediaId(), otherParam);

                    log.debug("resultJson: ", resp);

                    JSONObject resultJson = JSON.parseObject(resp);
                    if ("200".equals(resultJson.getString("returnCode"))) {
                        messageService.updateStatusById(param.get(InstallObject.getId()).toString(),
                                InstallObject.getSendedStatus());
                        messageService.updateTaskIdById(param.get(InstallObject.getId()).toString(),
                                resultJson.getString("taskId"));
                    } else {
                        messageService.updateStatusById(param.get(InstallObject.getId()).toString(),
                                InstallObject.getSendFailStatus());
                    }
                }
            }
        }
    }
}
