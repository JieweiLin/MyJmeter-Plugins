package com.ljw.jmeter.plugin.memcache.config;

import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;

/**
 * @author 林杰炜 linjw
 * @Title Memcache配置元件GUI
 * @Description Memcache配置元件GUI
 * @date 2018/11/29 17:05
 */
public class MemcacheConfigBeanInfo extends BeanInfoSupport {
    private static final Logger log = LoggerFactory.getLogger(MemcacheConfigBeanInfo.class);
    private static final String VARIABLE_NAME = "variableName";
    private static final String USER_NAME = "userName";
    private static final String PASSWORD = "password";
    private static final String HOST = "host";
    private static final String PORT = "port";

    public MemcacheConfigBeanInfo() {
        super(MemcacheConfig.class);

        try {
            createPropertyGroup("variable_group", new String[]{VARIABLE_NAME});
            PropertyDescriptor p = property(VARIABLE_NAME);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            createPropertyGroup("memcache_config", new String[]{USER_NAME, PASSWORD, HOST, PORT});
            p = property(USER_NAME);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");

            p = property(PASSWORD);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");

            p = property(HOST);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");

            p = property(PORT);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
        } catch (Exception e) {
            log.error("Memcache Config GUI initializing error");
        }
    }
}
