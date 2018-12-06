package com.ljw.jmeter.plugin.mns;

import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;

/**
 * @author 林杰炜 linjw
 * @Title Mns配置元件GUI
 * @Description Mns配置元件GUI
 * @date 2018/12/5 13:49
 */
public class MnsConfigBeanInfo extends BeanInfoSupport {

    private static final Logger log = LoggerFactory.getLogger(MnsConfigBeanInfo.class);

    private static final String VARIABLE_NAME = "variableName";
    private static final String ACCESS_KEY = "accessKey";
    private static final String SECRET_KEY = "secretKey";
    private static final String ACCOUNT_ENDPOINT = "accountEndpoint";

    public MnsConfigBeanInfo(){
        super(MnsConfig.class);

        try {
            createPropertyGroup("variable_group", new String[]{VARIABLE_NAME});
            PropertyDescriptor p = property(VARIABLE_NAME);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            createPropertyGroup("mns_config", new String[]{ACCESS_KEY, SECRET_KEY, ACCOUNT_ENDPOINT});

            p = property(ACCESS_KEY);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(SECRET_KEY);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(ACCOUNT_ENDPOINT);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
        } catch (Exception e) {
            log.error("init MnsConfigGui error", e);
        }
    }
}
