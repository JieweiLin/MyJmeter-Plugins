package com.ljw.jmeter.plugin.mns;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ClientException;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.Base64TopicMessage;
import com.aliyun.mns.model.Message;
import com.aliyun.mns.model.TopicMessage;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author 林杰炜 linjw
 * @Title Mns采样器
 * @Description Mns采样器
 * @date 2018/12/5 13:57
 */
public class MnsSampler extends AbstractSampler implements TestBean, Serializable, TestStateListener {
    private static final Logger log = LoggerFactory.getLogger(MnsSampler.class);

    private String variableName;
    private String getMode;
    private String queueOrTopicName;
    private String pushContent;
    private String resultVariableName;
    static final String TOPIC = "TOPIC";
    static final String QUEUE = "QUEUE";

    @Override
    public String toString() {
        return "MnsSampler{" +
                "variableName='" + variableName + '\'' +
                ", getMode='" + getMode + '\'' +
                ", queueOrTopicName='" + queueOrTopicName + '\'' +
                ", pushContent='" + pushContent + '\'' +
                ", resultVariableName='" + resultVariableName + '\'' +
                '}';
    }

    @Override
    public SampleResult sample(Entry entry) {
        SampleResult result = new SampleResult();
        result.setSampleLabel(getName());
        result.sampleStart();
        try {
            pushContent = JSONObject.toJSONString(JSONObject.parseObject(pushContent));
        } catch (Exception e) {
        }
        final JMeterContext context = getThreadContext();
        JMeterVariables variables = context.getVariables();
        String messageId = "";
        try {
            MNSClient client = (MNSClient) variables.getObject(variableName);
            if (QUEUE.equals(getMode)) {
                CloudQueue queue = client.getQueueRef(queueOrTopicName);
                Message message = new Message();
                message.setMessageBody(pushContent);
                message = queue.putMessage(message);
                variables.put(resultVariableName, message.getMessageId());
                messageId = message.getMessageId();
            } else if (TOPIC.equals(getMode)) {
                CloudTopic topic = client.getTopicRef(queueOrTopicName);
                TopicMessage message = new Base64TopicMessage();
                message.setMessageBody(pushContent);
                message = topic.publishMessage(message);
                variables.put(resultVariableName, message.getMessageId());
                messageId = message.getMessageId();
            }
        } catch (ServiceException e) {
            log.error("发送MNS失败:" + e);
            variables.put(resultVariableName, "发送MNS失败");
        } catch (ClientException e) {
            log.error("发送MNS失败:" + e);
            variables.put(resultVariableName, "发送MNS失败");
        }
        result.setResponseData(messageId, "utf-8");
        result.setDataType(SampleResult.TEXT);
        result.setSamplerData(toString());
        result.setResponseOK();
        result.sampleEnd();
        return result;
    }

    @Override
    public void testStarted() {
        testStarted("");
    }

    @Override
    public void testStarted(String host) {

    }

    @Override
    public void testEnded() {
        testEnded("");
    }

    @Override
    public void testEnded(String host) {

    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getGetMode() {
        return getMode;
    }

    public void setGetMode(String getMode) {
        this.getMode = getMode;
    }

    public String getQueueOrTopicName() {
        return queueOrTopicName;
    }

    public void setQueueOrTopicName(String queueOrTopicName) {
        this.queueOrTopicName = queueOrTopicName;
    }

    public String getPushContent() {
        return pushContent;
    }

    public void setPushContent(String pushContent) {
        this.pushContent = pushContent;
    }

    public String getResultVariableName() {
        return resultVariableName;
    }

    public void setResultVariableName(String resultVariableName) {
        this.resultVariableName = resultVariableName;
    }
}
