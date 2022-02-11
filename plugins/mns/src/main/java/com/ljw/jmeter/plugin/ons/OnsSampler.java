package com.ljw.jmeter.plugin.ons;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author 林杰炜 linjw
 *  Ons采样器
 * @Description Ons采样器
 * @date 2018/12/17 14:16
 */
public class OnsSampler extends AbstractSampler implements TestBean, Serializable, TestStateListener {
    private static final Logger log = LoggerFactory.getLogger(OnsSampler.class);

    private String variableName;
    private String onsTopic;
    private String onsTag;
    private String onsKey;
    private String pushContent;
    private String resultVariableName;

    @Override
    public String toString() {
        return "OnsSampler{" +
                "variableName='" + variableName + '\'' +
                ", onsTopic='" + onsTopic + '\'' +
                ", onsTag='" + onsTag + '\'' +
                ", onsKey='" + onsKey + '\'' +
                ", pushContent='" + pushContent + '\'' +
                ", resultVariableName='" + resultVariableName + '\'' +
                '}';
    }

    @Override
    public SampleResult sample(Entry e) {
        SampleResult result = new SampleResult();
        result.setSampleLabel(getName());
        result.sampleStart();
        result.setSamplerData(toString());
        try {
            pushContent = JSONObject.toJSONString(JSONObject.parseObject(pushContent));
        } catch (Exception e1) {
        }
        final JMeterVariables variables = getThreadContext().getVariables();
        Producer producer = (Producer) variables.getObject(variableName);
        String messageId = "";
        try {
            Message message = new Message(onsTopic, onsTag, pushContent.getBytes("utf-8"));
            message.setKey(onsKey);
            SendResult sendResult = producer.send(message);
            if (sendResult != null){
                messageId = sendResult.getMessageId();
                variables.put(resultVariableName, sendResult.getMessageId());
                result.setResponseData(messageId, "utf-8");
            } else {
                log.error("投递ONS失败");
                variables.put(resultVariableName, "投递ONS失败");
                result.setResponseData("投递ONS失败", "utf-8");
            }
        } catch (Exception e1) {
            log.error("投递ONS失败！", e1);
            variables.put(resultVariableName, "投递ONS失败");
            result.setResponseData("投递ONS失败" + e1, "utf-8");
        }
        result.setDataType(SampleResult.TEXT);
        result.setResponseOK();
        result.sampleEnd();
        return result;
    }

    @Override
    public void testStarted() {

    }

    @Override
    public void testStarted(String host) {

    }

    @Override
    public void testEnded() {

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

    public String getOnsTopic() {
        return onsTopic;
    }

    public void setOnsTopic(String onsTopic) {
        this.onsTopic = onsTopic;
    }

    public String getOnsTag() {
        return onsTag;
    }

    public void setOnsTag(String onsTag) {
        this.onsTag = onsTag;
    }

    public String getOnsKey() {
        return onsKey;
    }

    public void setOnsKey(String onsKey) {
        this.onsKey = onsKey;
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
