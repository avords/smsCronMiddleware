package cn.com.flaginfo.ware.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import cn.com.flaginfo.spApi.SpAPI;
import cn.com.flaginfo.ware.service.IMessageSendService;
import cn.com.flaginfo.ware.vo.InstallObject;

@Service
public class MessageSendService implements IMessageSendService{
    /**
     * 发送短信
     * @throws Exception
     */
    @Override
    public String sendSMS(String sendObject,String text,Map<String, Object> otherParams) throws Exception {
        SpAPI sdk = new SpAPI(InstallObject.getSpCode(), InstallObject.getCName(),
                InstallObject.getKey(), InstallObject.getSendSMSUrl());
        String methodUrl = "/cmc/sms/send";
        Map<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("text", text);
        requestMap.put("sendObject", sendObject);
        if(otherParams!=null&&!otherParams.isEmpty()){
            requestMap.putAll(otherParams);
        }
        String resp = sdk.postRequest(methodUrl, requestMap);
        return resp;
    }

    /**
     * 发送e信
     * @throws Exception
     */
    @Override
    public String sendEMS(String sendObject,String text,String mediaId,Map<String, Object> otherParams) throws Exception {
        SpAPI sdk = new SpAPI(InstallObject.getSpCode(), InstallObject.getCName(),
                InstallObject.getKey(), InstallObject.getSendEMSUrl());
        String methodUrl = "/cmc/ems/send";
        Map<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("text", text);
        reqMap.put("sendObject", sendObject);
        reqMap.put("mediaId", mediaId);
        if(otherParams!=null&&!otherParams.isEmpty()){
            reqMap.putAll(otherParams);
        }
        String resp = sdk.postRequest(methodUrl, reqMap);
        return resp;
    }

    /**
     * 查询发送结果
     * @throws Exception
     */
    @Override
    public String getTask(String taskId) throws Exception {
        SpAPI sdk = new SpAPI(InstallObject.getSpCode(), InstallObject.getCName(),
                InstallObject.getKey(), InstallObject.getGetTaskUrl());
        String methodUrl = "/cmc/task/get";
        Map<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("taskId", taskId);
        String resp = sdk.postRequest(methodUrl, reqMap);
        return resp;
    }

    /**
     * 查询上行回复数据
     * @throws Exception
     */
    @Override
    public String listReply(String replyId,String limit) throws Exception {
        SpAPI sdk = new SpAPI(InstallObject.getSpCode(), InstallObject.getCName(),
                InstallObject.getKey(), InstallObject.getListReply());
        String methodUrl = "/cmc/reply/list";
        Map<String, Object> reqMap = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(replyId)){
            reqMap.put("replyId", replyId);
        }
        if(StringUtils.isNotBlank(limit)){
            reqMap.put("limit", limit);
        }
        String resp = sdk.postRequest(methodUrl, reqMap);
        return resp;
    }

    @Override
    public String sendDynamicSMS(String dynamicData, String text, Map<String, Object> otherParams) throws Exception {
        SpAPI sdk = new SpAPI(InstallObject.getSpCode(), InstallObject.getCName(),
                InstallObject.getKey(), InstallObject.getSendSMSUrl());
        String methodUrl = "/cmc/dynamic-sms/send";
        Map<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("text", text);
        requestMap.put("dynamicData", dynamicData);
        if(otherParams!=null&&!otherParams.isEmpty()){
            requestMap.putAll(otherParams);
        }
        String resp = sdk.postRequest(methodUrl, requestMap);
        return resp;
    }

    @Override
    public String sendDynamicEMS(String dynamicData, String text, String mediaId, Map<String, Object> otherParams)
            throws Exception {
        SpAPI sdk = new SpAPI(InstallObject.getSpCode(), InstallObject.getCName(),
                InstallObject.getKey(), InstallObject.getSendEMSUrl());
        String methodUrl = "/cmc/dynamic-ems/send";
        Map<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("text", text);
        reqMap.put("dynamicData", dynamicData);
        reqMap.put("mediaId", mediaId);
        if(otherParams!=null&&!otherParams.isEmpty()){
            reqMap.putAll(otherParams);
        }
        String resp = sdk.postRequest(methodUrl, reqMap);
        return resp;
    }

    @Override
    public String listEMSReply(String taskId, String maxId) throws Exception {
        SpAPI sdk = new SpAPI(InstallObject.getSpCode(), InstallObject.getCName(),
                InstallObject.getKey(), InstallObject.getSendEMSUrl());
        String methodUrl = "/cmc/ems/form-info/list";
        Map<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("taskId", taskId);
        requestMap.put("maxId", maxId);
        String resp = sdk.postRequest(methodUrl, requestMap);
        return resp;

    }
}
