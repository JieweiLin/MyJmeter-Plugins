package com.ljw.jmeter.plugin.processor;

import com.ljw.jmeter.plugin.common.GetMode;
import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.gui.TypeEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;

/**
 * @author 林杰炜 linjw
 * @Title redis后置处理器GUI
 * @Description redis后置处理器GUI
 * @date 2018/12/18 19:39
 */
public class RedisPostProcessorBeanInfo extends BeanInfoSupport {
    private static final Logger log = LoggerFactory.getLogger(RedisPostProcessorBeanInfo.class);

    private static final String POOL_VARIABLE_NAME = "poolVariableName";
    private static final String DATABASE = "database";
    private static final String REDIS_KEY = "redisKey";
    private static final String RESULT_VARIABLE_NAMES = "resultVariableNames";
    private static final String DELIMITER = "delimiter";
    private static final String GET_MODE = "getMode";
    private static final String PARAMETER = "parameter";

    public RedisPostProcessorBeanInfo(){
        super(RedisPostProcessor.class);

        try {
            createPropertyGroup("pool_variable_group", new String[]{POOL_VARIABLE_NAME});
            PropertyDescriptor p = property(POOL_VARIABLE_NAME);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            createPropertyGroup("redis_data", new String[]{DATABASE, REDIS_KEY, RESULT_VARIABLE_NAMES, DELIMITER, GET_MODE, PARAMETER});
            p = property(DATABASE);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");

            p = property(REDIS_KEY);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");

            p = property(RESULT_VARIABLE_NAMES);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(DELIMITER);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, ",");

            p = property(GET_MODE, GetMode.class);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, GetMode.HGETALL.ordinal());

            p = property(PARAMETER, TypeEditor.TextAreaEditor);
            p.setValue(MULTILINE, Boolean.TRUE);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
        } catch (Exception e) {
            log.error("init Redis Post Processor Gui error");
        }
    }
}
