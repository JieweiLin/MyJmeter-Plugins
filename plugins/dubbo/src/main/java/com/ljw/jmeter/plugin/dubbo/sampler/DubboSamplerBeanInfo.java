package com.ljw.jmeter.plugin.dubbo.sampler;

import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.gui.TypeEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;

/**
 * @author 林杰炜 linjw
 * @Title dubbo采样器Gui
 * @Description dubbo采样器Gui
 * @date 2018/12/14 17:28
 */
public class DubboSamplerBeanInfo extends BeanInfoSupport {

    private static final Logger log = LoggerFactory.getLogger(DubboSamplerBeanInfo.class);
    private static final String VARIABLE_NAME = "variableName";
    private static final String DUBBO_METHOD = "method";
    private static final String DUBBO_ARGS = "args";

    public DubboSamplerBeanInfo() {
        super(DubboSampler.class);

        try {
            createPropertyGroup("variable_group", new String[]{VARIABLE_NAME});
            PropertyDescriptor p = property(VARIABLE_NAME);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            createPropertyGroup("method_group", new String[]{DUBBO_METHOD, DUBBO_ARGS});
            p = property(DUBBO_METHOD);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(DUBBO_ARGS, TypeEditor.TextAreaEditor);
            p.setValue(MULTILINE, Boolean.TRUE);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
        } catch (Exception e) {
            log.error("init DubboSamplerGui error!", e);
        }
    }
}
