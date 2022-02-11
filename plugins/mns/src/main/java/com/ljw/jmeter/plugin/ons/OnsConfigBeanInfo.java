package com.ljw.jmeter.plugin.ons;

import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;

/**
 * @author 林杰炜 linjw
 *  Ons配置元件Gui
 * @Description Ons配置元件Gui
 * @date 2018/12/6 11:03
 */
public class OnsConfigBeanInfo extends BeanInfoSupport {

    private static final Logger log = LoggerFactory.getLogger(OnsConfigBeanInfo.class);

    private static final String VARIABLE_NAME = "variableName";
    private static final String ACCESS_KEY = "accessKey";
    private static final String SECRET_KEY = "secretKey";
    private static final String SEND_MSG_TIMEOUT = "sendMsgTimeout";
    private static final String GET_MODE = "getMode";
    private static final String ADDR = "addr";

    public OnsConfigBeanInfo() {
        super(OnsConfig.class);

        try {
            createPropertyGroup("variable_group", new String[]{VARIABLE_NAME});
            PropertyDescriptor p = property(VARIABLE_NAME);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            createPropertyGroup("ons_config", new String[]{ACCESS_KEY, SECRET_KEY, SEND_MSG_TIMEOUT, GET_MODE, ADDR});
            p = property(ACCESS_KEY);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");

            p = property(SECRET_KEY);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");

            p = property(SEND_MSG_TIMEOUT);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "3000");

            p = property(GET_MODE);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, OnsConfig.ONS_ADDR_MODULE);
            p.setValue(NOT_OTHER, Boolean.TRUE);
            p.setValue(TAGS, new String[]{
                    OnsConfig.ONS_ADDR_MODULE,
                    OnsConfig.NAMESRV_ADDR_MODULE
            });

            p = property(ADDR);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
        } catch (Exception e) {
            log.error("init OnsConfigGui error");
        }
    }
}
