package com.ljw.jmeter.plugin.decoded.config;

import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.gui.TypeEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;

/**
 * @author 林杰炜 linjw
 *  Token解码配置元件Gui
 * @Description Token解码配置元件Gui
 * @date 2019/1/21 10:36
 */
public class TokenDecodedConfigBeanInfo extends BeanInfoSupport {
    private static final Logger log = LoggerFactory.getLogger(TokenDecodedConfigBeanInfo.class);
    private static final String VARIABLE_NAME = "variableName";
    private static final String TOKEN = "token";

    public TokenDecodedConfigBeanInfo() {
        super(TokenDecodedConfig.class);

        try {
            createPropertyGroup("variable_group", new String[]{VARIABLE_NAME});
            PropertyDescriptor p = property(VARIABLE_NAME);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            createPropertyGroup("data_group", new String[]{TOKEN});
            p = property(TOKEN, TypeEditor.TextAreaEditor);
            p.setValue(MULTILINE, Boolean.TRUE);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
        } catch (Exception e) {
            log.error("init Token Decoded Config Gui Error", e);
        }
    }
}
