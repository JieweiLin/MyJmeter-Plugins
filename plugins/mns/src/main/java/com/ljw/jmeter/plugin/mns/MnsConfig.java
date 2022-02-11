package com.ljw.jmeter.plugin.mns;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ClientException;
import com.aliyun.mns.common.ServiceException;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.engine.util.NoConfigMerge;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @author 林杰炜 linjw
 *  Mns配置元件
 * @Description Mns配置元件
 * @date 2018/12/5 13:43
 */
public class MnsConfig extends ConfigTestElement implements TestBean, LoopIterationListener, NoConfigMerge, TestStateListener {

    private static final Logger log = LoggerFactory.getLogger(MnsConfig.class);

    private String variableName;
    private String accessKey;
    private String secretKey;
    private String accountEndpoint;

    @Override
    public void iterationStart(LoopIterationEvent iterEvent) {
        try {
            final JMeterContext context = getThreadContext();
            JMeterVariables variables = context.getVariables();
            if (Objects.isNull(variables.getObject(variableName))){
                if (log.isDebugEnabled()){
                    log.debug("init MnsConfig!");
                }
                CloudAccount cloudAccount = new CloudAccount(getAccessKey(), getSecretKey(), getAccountEndpoint());
                MNSClient client = cloudAccount.getMNSClient();
                variables.putObject(variableName, client);
            }
        } catch (ServiceException e) {
            log.error("init MnsConfig error", e);
        } catch (ClientException e) {
            log.error("init MnsConfig error", e);
        }
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

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getAccountEndpoint() {
        return accountEndpoint;
    }

    public void setAccountEndpoint(String accountEndpoint) {
        this.accountEndpoint = accountEndpoint;
    }
}
