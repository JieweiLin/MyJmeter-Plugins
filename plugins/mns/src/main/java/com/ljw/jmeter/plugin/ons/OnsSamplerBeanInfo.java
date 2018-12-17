package com.ljw.jmeter.plugin.ons;

import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.gui.TypeEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;

/**
 * @author 林杰炜 linjw
 * @Title Ons采样器GUI
 * @Description Ons采样器GUI
 * @date 2018/12/17 14:16
 */
public class OnsSamplerBeanInfo extends BeanInfoSupport {
    private static final Logger log = LoggerFactory.getLogger(OnsSamplerBeanInfo.class);

    private static final String VARIABLE_NAME = "variableName";
    private static final String ONS_TOPIC = "onsTopic";
    private static final String ONS_TAG = "onsTag";
    private static final String ONS_KEY = "onsKey";
    private static final String PUSH_CONTENT = "pushContent";
    private static final String RESULT_VARIABLE_NAME = "resultVariableName";

    public OnsSamplerBeanInfo(){
        super(OnsSampler.class);

        try {
            createPropertyGroup("variable_group", new String[]{VARIABLE_NAME});
            PropertyDescriptor p = property(VARIABLE_NAME);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            createPropertyGroup("push_ons", new String[]{ONS_TOPIC, ONS_TAG, ONS_KEY, PUSH_CONTENT, RESULT_VARIABLE_NAME});
            p = property(ONS_TOPIC);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");

            p = property(ONS_TAG);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");

            p = property(ONS_KEY);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");

            p = property(PUSH_CONTENT, TypeEditor.TextAreaEditor);
            p.setValue(MULTILINE, Boolean.TRUE);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");

            p = property(RESULT_VARIABLE_NAME);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
        } catch (Exception e) {
            log.error("init OnsSamplerGui error");
        }
    }
}
