package com.ljw.jmeter.plugin.dubbo.config;

import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;

/**
 * @author 林杰炜 linjw
 * @Title dubbo配置元件Gui
 * @Description dubbo配置元件Gui
 * @date 2018/12/12 17:14
 */
public class DubboConfigBeanInfo extends BeanInfoSupport {

    private static final Logger log = LoggerFactory.getLogger(DubboConfigBeanInfo.class);
    private static final String VARIABLE_NAME = "variableName";
    private static final String DUBBO_REGISTRY_PROTOCOL = "registryProtocol";
    private static final String DUBBO_RPC_PROTOCOL = "rpcProtocol";
    private static final String DUBBO_ADDRESS = "address";
    private static final String DUBBO_TIMEOUT = "timeout";
    private static final String DUBBO_VERSION = "version";
    private static final String DUBBO_RETRIES = "retries";
    private static final String DUBBO_CLUSTER = "cluster";
    private static final String DUBBO_GROUP = "group";
    private static final String DUBBO_CONNECTIONS = "connections";
    private static final String DUBBO_LOADBALANCE = "loadbalance";
    private static final String DUBBO_ASYNC = "async";
    private static final String DUBBO_INTERFACE = "interFace";


    public DubboConfigBeanInfo() {
        super(DubboConfig.class);

        try {
            createPropertyGroup("variable_group", new String[]{VARIABLE_NAME});
            PropertyDescriptor p = property(VARIABLE_NAME);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            createPropertyGroup("registry_group", new String[]{DUBBO_REGISTRY_PROTOCOL, DUBBO_ADDRESS});
            p = property(DUBBO_REGISTRY_PROTOCOL);
            p.setValue(DEFAULT, DubboConfig.registryProtocols[1]);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(NOT_OTHER, Boolean.TRUE);
            p.setValue(TAGS, DubboConfig.registryProtocols);

            p = property(DUBBO_ADDRESS);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "${dubbo_address}");

            createPropertyGroup("rpc_protocol_group", new String[]{DUBBO_RPC_PROTOCOL});
            p = property(DUBBO_RPC_PROTOCOL);
            p.setValue(DEFAULT, DubboConfig.rpcProtocols[0]);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(NOT_OTHER, Boolean.TRUE);
            p.setValue(TAGS, DubboConfig.rpcProtocols);

            createPropertyGroup("consumer_group", new String[]{DUBBO_TIMEOUT, DUBBO_VERSION, DUBBO_RETRIES, DUBBO_CLUSTER, DUBBO_GROUP, DUBBO_CONNECTIONS, DUBBO_ASYNC, DUBBO_LOADBALANCE});
            p = property(DUBBO_TIMEOUT);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "${dubbo_timeout}");

            p = property(DUBBO_VERSION);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "${dubbo_version}");

            p = property(DUBBO_RETRIES);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "1");

            p = property(DUBBO_CLUSTER);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "failfast");

            p = property(DUBBO_GROUP);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "${dubbo_group}");

            p = property(DUBBO_CONNECTIONS);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "1");

            p = property(DUBBO_ASYNC);
            p.setValue(DEFAULT, DubboConfig.consumerAsyncs[1]);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(NOT_OTHER, Boolean.TRUE);
            p.setValue(TAGS, DubboConfig.consumerAsyncs);

            p = property(DUBBO_LOADBALANCE);
            p.setValue(DEFAULT, DubboConfig.consumerLoadbalance[0]);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(NOT_OTHER, Boolean.TRUE);
            p.setValue(TAGS, DubboConfig.consumerLoadbalance);

            createPropertyGroup("interface_group", new String[]{DUBBO_INTERFACE});
            p = property(DUBBO_INTERFACE);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
        } catch (Exception e) {
            log.error("init dubboConfig Gui error", e);
        }
    }
}
