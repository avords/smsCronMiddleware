package cn.com.flaginfo.ware.service;

import java.util.List;
import java.util.Map;

public interface IMessageService {
    /**
     * 取出需要发送的短信记录
     * @param n 记录数
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> getSMSMessages(int n) throws Exception;
    /**
     * 取出需要发送的e信记录
     * @param n 记录数
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> getEMSMessages(int n) throws Exception;
    /**
     * 获取已经发送完成的任务
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> getEMSTaskMessage(int n, int start) throws Exception;
    /**
     * 通过id更新已发送记录的状态
     * @param id 主键
     * @return
     * @throws Exception
     */
    boolean updateStatusById(Object id,Object status) throws Exception;
    /**
     * 通过id更新已发送记录的状态
     * @param id 主键
     * @return
     * @throws Exception
     */
    boolean updateTaskIdById(Object id,Object taskId) throws Exception;
    /**
     * 更新e信上行回复数据
     * @param taskId
     * @param mobile
     * @param content
     * @return
     */
    boolean updateEMSReplyContent(Object taskId,Object mobile,Object content);
}
