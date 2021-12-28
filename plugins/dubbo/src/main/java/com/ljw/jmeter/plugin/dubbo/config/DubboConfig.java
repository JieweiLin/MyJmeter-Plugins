package com.ljw.jmeter.plugin.dubbo.config;

import com.ljw.jmeter.plugin.dubbo.common.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.AbstractConfig;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.engine.util.NoConfigMerge;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testbeans.gui.GenericTestBeanCustomizer;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author 林杰炜 linjw
 * @Title dubbo配置元件
 * @Description dubbo配置元件
 * @date 2018/12/12 17:13
 */
public class DubboConfig extends ConfigTestElement implements TestBean, LoopIterationListener, NoConfigMerge, TestStateListener {
    private static final Logger log = LoggerFactory.getLogger(DubboConfig.class);
    private String variableName;
    private String registryProtocol;
    private String rpcProtocol;
    private String address;
    private String timeout;
    private String version;
    private String retries;
    private String cluster;
    private String group;
    private String connections;
    private String async;
    private String loadbalance;
    private String interFace;

    @Override
    public String toString() {
        return "DubboConfig{" +
                "variableName='" + variableName + '\'' +
                ", registryProtocol='" + registryProtocol + '\'' +
                ", rpcProtocol='" + rpcProtocol + '\'' +
                ", address='" + address + '\'' +
                ", timeout='" + timeout + '\'' +
                ", version='" + version + '\'' +
                ", retries='" + retries + '\'' +
                ", cluster='" + cluster + '\'' +
                ", group='" + group + '\'' +
                ", connections='" + connections + '\'' +
                ", async='" + async + '\'' +
                ", loadbalance='" + loadbalance + '\'' +
                ", interFace='" + interFace + '\'' +
                '}';
    }

    @Override
    public void iterationStart(LoopIterationEvent iterEvent) {
        final JMeterVariables variables = getThreadContext().getVariables();
        if (Objects.isNull(variables.getObject(variableName))) {
            if (log.isDebugEnabled()) {
                log.debug("init dubbo config");
                log.debug(toString());
            }
            ApplicationConfig application = new ApplicationConfig();
            application.setName("JMeter-Dubbo");
            ReferenceConfig reference = new ReferenceConfig();
            reference.setApplication(application);
            RegistryConfig registry = null;
            switch (registryProtocol) {
                case Constants.REGISTRY_ZOOKEEPER:
                    registry = new RegistryConfig();
                    registry.setProtocol(Constants.REGISTRY_ZOOKEEPER);
                    registry.setAddress(address);
                    reference.setRegistry(registry);
                    reference.setProtocol(rpcProtocol);
                    break;
                case Constants.REGISTRY_MULTICAST:
                    registry = new RegistryConfig();
                    registry.setProtocol(Constants.REGISTRY_MULTICAST);
                    registry.setAddress(address);
                    reference.setRegistry(registry);
                    reference.setProtocol(rpcProtocol);
                    break;
                case Constants.REGISTRY_REDIS:
                    registry = new RegistryConfig();
                    registry.setProtocol(Constants.REGISTRY_REDIS);
                    registry.setAddress(address);
                    reference.setRegistry(registry);
                    reference.setProtocol(rpcProtocol);
                    break;
                case Constants.REGISTRY_SIMPLE:
                    registry = new RegistryConfig();
                    registry.setAddress(address);
                    reference.setRegistry(registry);
                    reference.setProtocol(rpcProtocol);
                    break;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append(rpcProtocol).append("://").append(address).append("/").append(interFace);
                    log.debug("rpc invoker url: {}", sb);
                    reference.setUrl(sb.toString());
                    break;
            }
            reference.setInterface(interFace);
            reference.setRetries(Integer.valueOf(retries));
            reference.setCluster(cluster);
            reference.setVersion(version);
            reference.setTimeout(Integer.valueOf(timeout));
            reference.setGroup(StringUtils.isBlank(group) ? null : group);
            reference.setConnections(Integer.valueOf(connections));
            reference.setLoadbalance(loadbalance);
            reference.setAsync(async.endsWith(Constants.ASYNC));
            reference.setGeneric(true);

            ReferenceConfigCache cache = ReferenceConfigCache.getCache(address, AbstractConfig::toString);
            GenericService genericService = (GenericService) cache.get(reference);

            variables.putObject(variableName, genericService);
            variables.put(variableName + "_samplerData", getSamplerData());
        }
    }

    public String getSamplerData() {
        StringBuilder sb = new StringBuilder();
        sb.append("Registry Protocol: ").append(registryProtocol).append("\n");
        sb.append("Address: ").append(address).append("\n");
        sb.append("RPC Protocol: ").append(rpcProtocol).append("://").append("\n");
        sb.append("Timeout: ").append(timeout).append("\n");
        sb.append("Version: ").append(version).append("\n");
        sb.append("Retries: ").append(retries).append("\n");
        sb.append("Cluster: ").append(cluster).append("\n");
        sb.append("Group: ").append(group).append("\n");
        sb.append("Connections: ").append(connections).append("\n");
        sb.append("Loadbalance: ").append(loadbalance).append("\n");
        sb.append("Async: ").append(async).append("\n");
        sb.append("Interface: ").append(interFace).append("\n");
        return sb.toString();
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

    protected static final String[] registryProtocols = new String[]{"none", "zookeeper", "multicast", "redis", "simple"};
    protected static final String[] rpcProtocols = new String[]{"dubbo", "hessian", "webservice", "memcached", "rpcredis"};
    protected static final String[] consumerAsyncs = new String[]{"async", "sync"};
    protected static final String[] consumerLoadbalance = new String[]{"random", "roundrobin", "leastactive", "consistenthash"};

    @Override
    public void setProperty(JMeterProperty property) {
        if (property instanceof StringProperty) {
            final String pn = property.getName();
            if ("registryProtocol".equals(pn)) {
                final String objValue = property.getStringValue();
                try {
                    final BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
                    final ResourceBundle rb = (ResourceBundle) beanInfo.getBeanDescriptor().getValue(GenericTestBeanCustomizer.RESOURCE_BUNDLE);
                    for (String protocol : registryProtocols) {
                        if (objValue.equals(rb.getString(protocol))) {
                            ((StringProperty) property).setValue(protocol);
                            super.setProperty(property);
                            return;
                        }
                    }
                } catch (IntrospectionException e) {
                    log.error("Could not find BeanInfo", e);
                }
            } else if ("rpcProtocol".equals(pn)) {
                final Object objValue = property.getObjectValue();
                try {
                    final BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
                    final ResourceBundle rb = (ResourceBundle) beanInfo.getBeanDescriptor().getValue(GenericTestBeanCustomizer.RESOURCE_BUNDLE);
                    for (String protocol : rpcProtocols) {
                        if (objValue.equals(rb.getString(protocol))) {
                            ((StringProperty) property).setValue(protocol);
                            super.setProperty(property);
                            return;
                        }
                    }
                } catch (IntrospectionException e) {
                    log.error("Could not find BeanInfo", e);
                }
            } else if ("async".equals(pn)) {
                final Object objValue = property.getObjectValue();
                try {
                    final BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
                    final ResourceBundle rb = (ResourceBundle) beanInfo.getBeanDescriptor().getValue(GenericTestBeanCustomizer.RESOURCE_BUNDLE);
                    for (String protocol : consumerAsyncs) {
                        if (objValue.equals(rb.getString(protocol))) {
                            ((StringProperty) property).setValue(rb.getString(protocol));
                            super.setProperty(property);
                            return;
                        }
                    }
                } catch (IntrospectionException e) {
                    log.error("Could not find BeanInfo", e);
                }
            } else if ("loadbalance".equals(pn)) {
                final Object objValue = property.getObjectValue();
                try {
                    final BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
                    final ResourceBundle rb = (ResourceBundle) beanInfo.getBeanDescriptor().getValue(GenericTestBeanCustomizer.RESOURCE_BUNDLE);
                    for (String protocol : consumerLoadbalance) {
                        if (objValue.equals(rb.getString(protocol))) {
                            ((StringProperty) property).setValue(protocol);
                            super.setProperty(property);
                            return;
                        }
                    }
                } catch (IntrospectionException e) {
                    log.error("Could not find BeanInfo", e);
                }
            }
        }
        super.setProperty(property);
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getRegistryProtocol() {
        return registryProtocol;
    }

    public void setRegistryProtocol(String registryProtocol) {
        this.registryProtocol = registryProtocol;
    }

    public String getRpcProtocol() {
        return rpcProtocol;
    }

    public void setRpcProtocol(String rpcProtocol) {
        this.rpcProtocol = rpcProtocol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRetries() {
        return retries;
    }

    public void setRetries(String retries) {
        this.retries = retries;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getConnections() {
        return connections;
    }

    public void setConnections(String connections) {
        this.connections = connections;
    }

    public String getAsync() {
        return async;
    }

    public void setAsync(String async) {
        this.async = async;
    }

    public String getLoadbalance() {
        return loadbalance;
    }

    public void setLoadbalance(String loadbalance) {
        this.loadbalance = loadbalance;
    }

    public String getInterFace() {
        return interFace;
    }

    public void setInterFace(String interFace) {
        this.interFace = interFace;
    }
}
