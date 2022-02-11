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
 * 数据库字段解密
 *
 * @author 林杰炜 linjw
 * @date 2019/6/13 10:08
 */
public class DBDecrypt extends AbstractFunction {
    private static final Logger log = LoggerFactory.getLogger(DBDecrypt.class);
    private static final List<String> DESC = Lists.newLinkedList();
    private static final String KEY = "__dbDecrypt";
    private Object[] values;

    static {
        DESC.add("cipherText");
        DESC.add("secretKey");
        DESC.add("Name of variable in which to store the result (optional)");
    }

    public DBDecrypt() {
        //Do nothing
    }

    @Override
    public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
        String cipherText = getParameter(0);
        String secretKey = getParameter(1);
        log.info("cipherText: {}, secretKey: {}", cipherText, secretKey);
        String result = "";
        if (StringUtils.isEmpty(cipherText)) {
            return result;
        }
        try {
            result = Sm4Util.decryptEcb(secretKey, cipherText);
        } catch (Exception e) {
            log.error("字段解密出错", e);
        }
        if (values.length > 2) {
            JMeterVariables variables = getVariables();
            String varName = getParameter(2).trim();
            variables.put(varName, result);
        }
        return result;
    }

    @Override
    public void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
        checkParameterCount(parameters, 2, 3);
        values = parameters.toArray();
    }

    private String getParameter(int index) {
        return ((CompoundVariable) values[index]).execute();
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
