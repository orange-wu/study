package com.my9z.study.batch_message;

import org.apache.rocketmq.common.message.Message;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @description: 消息列表分割
 * @author: kim
 * @createTime: 2022-11-29  15:58
 */
public class MessageSplitter implements Iterator<List<Message>> {

    /**
     * 消息列表
     */
    private final List<Message> messages;

    /**
     * 当前所处消息列表的下标
     */
    private int currIndex;

    public MessageSplitter(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public boolean hasNext() {
        return currIndex < messages.size();
    }

    @Override
    public List<Message> next() {
        int nextIndex = currIndex;
        int totalSize = 0;
        for (; nextIndex < messages.size(); nextIndex++) {
            Message message = messages.get(nextIndex);
            //topic和消息body的长度
            int tmpSize = message.getTopic().length() + message.getBody().length;
            //消息的配置信息长度
            Map<String, String> properties = message.getProperties();
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                tmpSize += entry.getKey().length() + entry.getValue().length();
            }
            //日志的开销20字节
            tmpSize += 20;
            //如果单条消息超出了最大限制，忽略，不然会阻塞分割的过程
            int SIZE_LIMIT = 1024 * 1024 * 4;
            if (tmpSize > SIZE_LIMIT) {
                //如果nextIndex == currIndex,防止分割列表时出现问题，所以将nextIndex++;（第0条消息大于4m，分割时为sub(0,1)）
                if (nextIndex - currIndex == 0) {
                    nextIndex++;
                }
                break;
            }
            //加上当前消息的大小超出最大限制，直接跳出循环
            if (tmpSize + totalSize > SIZE_LIMIT) {
                break;
            } else {
                totalSize += tmpSize;
            }
        }
        List<Message> subList = messages.subList(currIndex, nextIndex);
        currIndex = nextIndex;
        return subList;
    }
}
