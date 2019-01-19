package com.ljw.jmeter.plugin.memcache.sampler;

import com.ljw.jmeter.plugin.memcache.common.GetMemcacheCommand;
import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.gui.TypeEditor;
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
    private static final String GET_MODE = "getMemcacheCommand";
    private static final String MEMCACHE_KEY = "memcacheKey";
    private static final String MEMCACHE_VALUE = "memcacheValue";
    private static final String RESULT_VARIABLE_NAME = "resultVariableName";

    public MemcacheSamplerBeanInfo() {
        super(MemcacheSampler.class);

        try {
            createPropertyGroup("variable_group", new String[]{VARIABLE_NAME});
            PropertyDescriptor p = property(VARIABLE_NAME);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
            p.setValue(DEFAULT, "");

            createPropertyGroup("memcache_group", new String[]{GET_MODE, MEMCACHE_KEY, MEMCACHE_VALUE, RESULT_VARIABLE_NAME});
            p = property(GET_MODE, GetMemcacheCommand.class);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, GetMemcacheCommand.GET.ordinal());

            p = property(MEMCACHE_KEY);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");

            p = property(MEMCACHE_VALUE, TypeEditor.TextAreaEditor);
            p.setValue(MULTILINE, Boolean.TRUE);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");

            p = property(RESULT_VARIABLE_NAME);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
        } catch (Exception e) {
            log.error("Memcache Sampler Gui initializing error");
        }
    }
}
