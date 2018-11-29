package com.ljw.jmeter.plugin.memcache.sampler;

import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;

/**
 * @author 林杰炜 linjw
 * @Title Memcache采样器GUI
 * @Description Memcache采样器GUI
 * @date 2018/11/29 18:48
 */
public class MemcacheSamplerBeanInfo extends BeanInfoSupport {

    private static final Logger log = LoggerFactory.getLogger(MemcacheSamplerBeanInfo.class);

    private static final String VARIABLE_NAME = "variableName";

    public MemcacheSamplerBeanInfo() {
        super(MemcacheSampler.class);

        try {
            createPropertyGroup("variable_group", new String[]{VARIABLE_NAME});
            PropertyDescriptor p = property(VARIABLE_NAME);
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
            p.setValue(DEFAULT, "");

        } catch (Exception e) {
            log.error("Memcache Sampler Gui initializing error");
        }
    }
}
