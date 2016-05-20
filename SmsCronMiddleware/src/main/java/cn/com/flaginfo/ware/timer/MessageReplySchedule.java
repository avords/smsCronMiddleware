package cn.com.flaginfo.ware.timer;

import java.util.Map;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.com.flaginfo.ware.vo.InstallObject;
import org.springframework.stereotype.Component;
import cn.com.flaginfo.ware.service.IMessageService;
import cn.com.flaginfo.ware.service.IMessageSendService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class MessageReplySchedule {
    @Autowired
    private IMessageService messageService;
    @Autowired
    private IMessageSendService messageSendService;

    @Scheduled(cron = "0 */5 * * * ?")
    public void getEMSReplyMessage() throws Exception {
    	int count = 0;
        while (true) {
        	count++;
            List<Map<String, Object>> messages = messageService.getEMSTaskMessage(100,(count-1)*100);
            if (messages == null || messages.size() == 0) {
                break;
            }
            for (Map<String, Object> param : messages) {
            	emsReplyOperate(
            			param.get(InstallObject.getTaskId()).toString(), 
            			param.get(InstallObject.getSendToMobile()).toString(),
            			"0", 
            			0);
            }
        }
    }
    
    /**
     * 每次查询最大值1000，因此采用递归处理
     * @param taskid
     * @param maxId
     * @param listNum
     * @throws Exception
     */
    private void emsReplyOperate(String taskid, String mobieMdn, String maxId, int listNum) throws Exception {
    	String resp = messageSendService.listEMSReply(taskid, maxId);

        JSONObject resultJson = JSON.parseObject(resp);
        if ("200".equals(resultJson.getString("returnCode"))) {
        	int total = resultJson.getIntValue("total");
        	String lastRowid = null;
        	
        	JSONArray jsonArray = resultJson.getJSONArray("list");
        	
        	if (jsonArray != null) {
        		listNum += jsonArray.size();
        		JSONObject tempJson = null;
        		for (int i = 0; i < jsonArray.size(); i++) {
                    tempJson = jsonArray.getJSONObject(i);
                    messageService.updateEMSReplyContent(taskid,
                            tempJson.get("mdn").toString(), 
                            tempJson);
                }
        		lastRowid = tempJson.getString("rowid");
        		
        		if (total > listNum) {
        			emsReplyOperate(taskid, mobieMdn, lastRowid, listNum);
        		}
        	} else {
//        		messageService.updateEMSReplyContent(taskid,
//        				mobieMdn, 
//                        "测试");
        	}
        	
        } else {
            // 出现异常，没有上行回复不处理
        }
    }
}
