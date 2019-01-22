package com.ljw.jmeter.plugin.decoded.sampler;

import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.gui.TypeEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;

/**
 * @author 林杰炜 linjw
 * @Title token解码采样器Gui
 * @Description token解码采样器Gui
 * @date 2019/1/21 16:58
 */
public class TokenDecodedSamplerBeanInfo extends BeanInfoSupport {
    private static final Logger log = LoggerFactory.getLogger(TokenDecodedSamplerBeanInfo.class);
    private static final String VARIABLE_NAME = "variableName";
    private static final String TOKEN = "token";

    public TokenDecodedSamplerBeanInfo(){
        super(TokenDecodedSampler.class);

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
        } catch (Exception e){
            log.error("init Token Decoded Sampler Gui Error", e);
        }
    }
}
