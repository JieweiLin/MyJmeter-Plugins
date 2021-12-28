package com.ljw.jmeter.functions;

import com.google.common.collect.Lists;
import com.ljw.jmeter.utils.Sm4Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

/**
 * @author 林杰炜 linjw
 * @Title 数据库字段加密
 * @Description 数据库字段加密
 * @date 2019/6/12 9:17
 */
public class DBEncrypt extends AbstractFunction {
    private static final Logger log = LoggerFactory.getLogger(DBEncrypt.class);
    private static final List<String> DESC = Lists.newLinkedList();
    private static final String KEY = "__dbEncrypt";

    static {
        DESC.add("plainText");
        DESC.add("secretKey");
        DESC.add("Name of variable in which to store the result (optional)");
    }

    private Object[] values;

    public DBEncrypt() {
    }

    @Override
    public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
        String plainText = getParameter(0);
        String secretKey = getParameter(1);
        log.info("plainText: {}, secretKey: {}", plainText, secretKey);
        String result = "";
        if (StringUtils.isEmpty(plainText)) {
            return result;
        }
        try {
            result = Sm4Util.encryptEcb(secretKey, plainText);
        } catch (Exception e) {
            log.error("字段加密出错", e);
        }
        if (values.length > 2) {
            JMeterVariables variables = getVariables();
            String varName = getParameter(2).trim();
            variables.put(varName, result);
        }
        return result;
    }

    private String getParameter(int index) {
        return ((CompoundVariable) values[index]).execute();
    }

    @Override
    public void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
        checkParameterCount(parameters, 2, 3);
        values = parameters.toArray();
    }

    @Override
    public String getReferenceKey() {
        return KEY;
    }

    @Override
    public List<String> getArgumentDesc() {
        return DESC;
    }
}
