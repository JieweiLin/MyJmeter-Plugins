package com.ljw.jmeter.plugin.ons;

import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 林杰炜 linjw
 * @Title Ons配置元件Gui
 * @Description Ons配置元件Gui
 * @date 2018/12/6 11:03
 */
public class OnsConfigBeanInfo extends BeanInfoSupport {

    private static final Logger log = LoggerFactory.getLogger(OnsConfigBeanInfo.class);

    private static final String VARIABLE_NAME = "variableName";
    private static final String ACCESS_KEY = "accessKey";
    private static final String SECRET_KEY = "secretKey";
    private static final String SENDMSG_TIMEOUT = "sendMsgTimeout";
    private static final String ONS_ADDR = "onsAddr";

    public OnsConfigBeanInfo() {
        super(OnsConfig.class);

        createPropertyGroup("variable_group", new String[]{});
    }
}
