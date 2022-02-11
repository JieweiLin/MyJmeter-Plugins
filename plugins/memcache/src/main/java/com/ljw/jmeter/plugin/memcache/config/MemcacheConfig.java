package com.ljw.jmeter.plugin.memcache.config;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;
import org.apache.commons.lang3.StringUtils;
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

import java.io.IOException;
import java.util.Objects;

/**
 * @author 林杰炜 linjw
 *  Memcache配置元件
 * @Description Memcache配置元件
 * @date 2018/11/29 16:52
 */
public class MemcacheConfig extends ConfigTestElement implements TestBean, LoopIterationListener, NoConfigMerge, TestStateListener {

    private static final Logger log = LoggerFactory.getLogger(MemcacheConfig.class);
    private String variableName;
    private String userName;
    private String password;
    private String host;
    private String port;

    @Override
    public void iterationStart(LoopIterationEvent iterEvent) {
        final JMeterContext context = getThreadContext();
        JMeterVariables variables = context.getVariables();
        MemcachedClient client = null;
        try {
            if (Objects.isNull(variables.getObject(variableName))) {
                if (StringUtils.isBlank(getUserName())) {
                    client = new MemcachedClient(new ConnectionFactoryBuilder().setProtocol(ConnectionFactoryBuilder.Protocol.BINARY).setLocatorType(ConnectionFactoryBuilder.Locator.CONSISTENT).setFailureMode(FailureMode.Redistribute).setUseNagleAlgorithm(false).build(), AddrUtil.getAddresses(getHost() + ":" + getPort()));
                } else {
                    AuthDescriptor ad = new AuthDescriptor(new String[]{"PLAIN"}, new PlainCallbackHandler(getUserName(), getPassword()));
                    client = new MemcachedClient(new ConnectionFactoryBuilder().setProtocol(ConnectionFactoryBuilder.Protocol.BINARY).setLocatorType(ConnectionFactoryBuilder.Locator.CONSISTENT).setFailureMode(FailureMode.Redistribute).setUseNagleAlgorithm(false).setAuthDescriptor(ad).build(), AddrUtil.getAddresses(getHost() + ":" + getPort()));
                }
                variables.putObject(variableName, client);
            }
        } catch (IOException e) {
            log.error("初始化Memcache失败", e);
        }

    }

    @Override
    public void testStarted() {
        testStarted("");
    }

    @Override
    public void testStarted(String host) {
        //do nothing
    }

    @Override
    public void testEnded() {
        testEnded("");
    }

    @Override
    public void testEnded(String host) {
        //do nothing
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
