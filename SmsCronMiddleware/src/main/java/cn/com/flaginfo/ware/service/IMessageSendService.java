package cn.com.flaginfo.ware.service;

import java.util.Map;

public interface IMessageSendService {
    /**
     * 发送短信
     * @param sendObject 发送的手机号，多个用逗号隔开
     * @param text 发送内容
     * @param otherParams 其他可选参数：
                 textTemplateId：短信模板id；
                 scheduleTime：预约发送时间，格式:yyyyMMddHHmmss,如‘20090901010101’，
                 立即发送请填空（预约时间要写当前时间5分钟之后的时间，若预约时间少于5分钟，
                 则为立即发送。）；
                 serialNumber：流水号，20位数字，唯一 （规则自定义,建议时间格式精确到毫秒）
     * @return json字符串
       {
            "returnMsg":返回码的描述
            "returnCode":返回码
            "taskId":任务ID
         }
     * @throws Exception
     */
    public String sendSMS(String sendObject,String text,Map<String, Object> otherParams) throws Exception ;
    /**
     * 发送动态短信
     * @param dynamicData
              动态数据，json字符串数组,如:
        [
        {
        “手机号码”:”13391180200”，
        “姓名”:”张三”,
        “年龄”:”25”
        }
        ]，键值对应动态短信的占位符，其中“手机号码”必传，最多可以发送1000个号码。
     * @param text 含占位符的text
     * @param otherParams 其他可选参数
     * @return
     * @throws Exception
     */
    public String sendDynamicSMS(String dynamicData,String text,Map<String, Object> otherParams) throws Exception ;

    /**
     *发送e信
     * @param sendObject 发送号码，多个号码以逗号或分号隔开
     * @param text 发送内容
     * @param mediaId 素材id
     * @param otherParams 其他可选参数：
              mediaUrl：素材url
              serialNumber：流水号，20位数字，唯一 （规则自定义,建议时间格式精确到毫秒）
     * @return json字符串
        {
            "returnMsg":返回码的描述
            "returnCode":返回码
            "taskId":任务ID
         }
     * @throws Exception
     */
    public String sendEMS(String sendObject,String text,String mediaId,Map<String, Object> otherParams) throws Exception;
    /**
     * 发送动态e信
     * @param dynamicData
              动态数据，json字符串数组,如:
        [
        {
        “手机号码”:”13391180200”，
        “姓名”:”张三”,
        “年龄”:”25”
        }
        ]，键值对应动态短信的占位符，其中“手机号码”必传，最多可以发送1000个号码。
     * @param text 发送内容
     * @param mediaId 素材id
     * @param otherParams 其他可选参数：
     * @return
     * @throws Exception
     */
    public String sendDynamicEMS(String dynamicData,String text,String mediaId,Map<String, Object> otherParams) throws Exception;
    /**
     * 查询发送结果
     * @param taskId 任务id
     * @return json字符串
        {
            "returnMsg":返回码的描述
            "returnCode":返回码
            "taskInfo":{
                            status:“4”,//状态：0待发送1待审核2发送失败4发送成功
                            allNum:“20”,//发送总条数
                            allMdn:“20”,//发送总号码数
                            networkNum: {unum,tnum,mnum},//发送条数明细
                            networkMdn:{umdn,tmdn,mmdn},//发送号码数
                            receiveNum:{uSuccNum,tSuccNum,mSuccNum}//提交结果明细
                        }
         }
     * @throws Exception
     */
    public String getTask(String taskId) throws Exception;

    /**
     * 查询上行回复数据
     * @param replyId 回复id，增量查询大于等于该id的回复数据，可传0
     * @param limit 最大结果条数limit，默认值100,最大值5000
     * @return json字符串
      {
            "returnMsg":返回码的描述
            "returnCode":返回码
            "list":[
                        {
                            id: “1”,
                            content:”test”,
                            replyTime:”2016-03-03 17:30:00”，
                            mdn:”13292630326”
                        },
                        {
                            id: “2”,
                            content:”test”,
                            replyTime:”2016-03-03 17:35:00”，
                            mdn:”13292630326”
                        }
                    ]

         }
     * @throws Exception
     */
    public String listReply(String replyId,String limit) throws Exception;
    /**
     * 查询e信上行回复结果
     * @param taskId
     * @param maxId
     * @return
     * @throws Exception
     */
    public String listEMSReply(String taskId,String maxId) throws Exception;
}
