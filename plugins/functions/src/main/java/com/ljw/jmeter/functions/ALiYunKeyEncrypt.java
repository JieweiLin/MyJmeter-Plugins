package com.ljw.jmeter.functions;

import com.alibaba.druid.filter.config.ConfigTools;
import com.google.common.collect.Lists;
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
 * @author linjw
 * @Title ALiYun 密钥加密
 * @date 2020/3/16 13:49
 */
public class ALiYunKeyEncrypt extends AbstractFunction {
    private static final Logger log = LoggerFactory.getLogger(ALiYunKeyEncrypt.class);
    private static final List<String> DESC = Lists.newLinkedList();
    private static final String KEY = "__aliYunKeyEncrypt";
    private Object[] values;

    static {
        DESC.add("Aliyun plaintext key");
        DESC.add("Encryption key (optional)");
        DESC.add("Name of variable in which to store the result (optional)");
    }

    public ALiYunKeyEncrypt() {
        // Do nothing
    }

    @Override
    public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
        String plainText = ((CompoundVariable) values[0]).execute();
        String secretKey = ((CompoundVariable) values[1]).execute();
        String result = "";
        if (StringUtils.isBlank(plainText)) {
            return result;
        }
        try {
            if (StringUtils.isBlank(secretKey)) {
                result = ConfigTools.encrypt(plainText);
            } else {
                result = ConfigTools.encrypt(secretKey, plainText);
            }
        } catch (Exception e) {
            log.error("生成ALiYun加密密钥串失败", e);
        }
        if (values.length > 2) {
            JMeterVariables variables = getVariables();
            String varName = ((CompoundVariable) values[2]).execute().trim();
            variables.put(varName, result);
        }
        return result;
    }

    @Override
    public void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
        checkParameterCount(parameters, 1, 3);
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
