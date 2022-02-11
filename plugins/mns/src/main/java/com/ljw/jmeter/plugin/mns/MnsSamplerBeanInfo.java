package com.ljw.jmeter.plugin.mns;

import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.gui.TypeEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;

/**
 * @author 林杰炜 linjw
 *  Mns采样器GUI
 * @Description Mns采样器GUI
 * @date 2018/12/6 10:03
 */
public class MnsSamplerBeanInfo extends BeanInfoSupport {

    private static final Logger log = LoggerFactory.getLogger(MnsSamplerBeanInfo.class);

    private static final String VARIABLE_NAME = "variableName";
    private static final String GET_MODE = "getMode";
    private static final String QUEUE_OR_TOPIC_NAME = "queueOrTopicName";
    private static final String PUSH_CONTENT = "pushContent";
    private static final String RESULT_VARIABLE_NAME = "resultVariableName";

    public MnsSamplerBeanInfo(){
        super(MnsSampler.class);

        try {
            createPropertyGroup("variable_group", new String[]{VARIABLE_NAME});
            PropertyDescriptor p = property(VARIABLE_NAME);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            createPropertyGroup("push_mns", new String[]{GET_MODE, QUEUE_OR_TOPIC_NAME, PUSH_CONTENT, RESULT_VARIABLE_NAME});
            p = property(GET_MODE);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, MnsSampler.QUEUE);
            p.setValue(NOT_OTHER, Boolean.TRUE);
            p.setValue(TAGS, new String[]{
                    MnsSampler.QUEUE,
                    MnsSampler.TOPIC
            });

            p = property(QUEUE_OR_TOPIC_NAME);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");

            p = property(PUSH_CONTENT, TypeEditor.TextAreaEditor);
            p.setValue(MULTILINE, Boolean.TRUE);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(RESULT_VARIABLE_NAME);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
        } catch (Exception e) {
            log.error("init MnsSamplerGui error!");
        }
    }
}
