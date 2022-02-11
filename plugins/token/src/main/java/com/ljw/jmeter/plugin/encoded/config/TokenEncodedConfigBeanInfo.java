package com.ljw.jmeter.plugin.encoded.config;

import com.ljw.jmeter.plugin.common.AlgorithmEnum;
import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.gui.TypeEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;

/**
 * @author 林杰炜 linjw
 *  token编码配置元件Gui
 * @Description token编码配置元件Gui
 * @date 2019/1/19 17:07
 */
public class TokenEncodedConfigBeanInfo extends BeanInfoSupport {
    private static final Logger log = LoggerFactory.getLogger(TokenEncodedConfigBeanInfo.class);
    private static final String VARIABLE_NAME = "variableName";
    private static final String DATA = "data";
    private static final String TOKEN_SECRET = "tokenSecret";
    private static final String ALGORITHM = "algorithm";

    public TokenEncodedConfigBeanInfo() {
        super(TokenEncodedConfig.class);

        try {
            createPropertyGroup("variable_group", new String[]{VARIABLE_NAME});
            PropertyDescriptor p = property(VARIABLE_NAME);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            createPropertyGroup("data_group", new String[]{ALGORITHM, TOKEN_SECRET, DATA});
            p = property(ALGORITHM, AlgorithmEnum.class);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, AlgorithmEnum.HS256.ordinal());

            p = property(TOKEN_SECRET);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "debug-U^Xr%w&TYX8Gee9");

            p = property(DATA, TypeEditor.TextAreaEditor);
            p.setValue(MULTILINE, Boolean.TRUE);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
        } catch (Exception e) {
            log.error("init Token Encoded Config Gui Error", e);
        }
    }
}
