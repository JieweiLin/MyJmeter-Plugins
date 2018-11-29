package com.ljw.jmeter.plugin.memcache.config;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;
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

/**
 * @author 林杰炜 linjw
 * @Title Memcache配置元件
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
        AuthDescriptor ad = new AuthDescriptor(new String[]{"PLAIN"}, new PlainCallbackHandler(getUserName(), getPassword()));
        MemcachedClient client = null;
        try {
            client = new MemcachedClient(new ConnectionFactoryBuilder().setProtocol(ConnectionFactoryBuilder.Protocol.BINARY).setAuthDescriptor(ad).build(), AddrUtil.getAddresses(getHost() + ":" + getPort()));
        } catch (IOException e) {
            log.error("初始化Memcache失败", e);
        }
        final JMeterContext context = getThreadContext();
        JMeterVariables variables = context.getVariables();
        variables.putObject(variableName, client);
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
