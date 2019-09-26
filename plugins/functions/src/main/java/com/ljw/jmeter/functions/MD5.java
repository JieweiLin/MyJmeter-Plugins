package com.ljw.jmeter.functions;

import com.google.common.collect.Lists;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.util.JOrphanUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;

/**
 * @author 林杰炜 linjw
 * @Title 计算MD5值
 * @Description 计算MD5值
 * @date 2019/1/17 15:50
 */
public class MD5 extends AbstractFunction {
    private static final List<String> desc = Lists.newLinkedList();
    private static final String KEY = "__MD5";
    static {
        desc.add("String to calculate MD5 hash");
        desc.add("Name of variable in which to store the result (optional)");
    }
    private Object[] values;

    @Override
    public String execute(SampleResult sampleResult, Sampler sampler) throws InvalidVariableException {
        JMeterVariables variables = getVariables();
        String str = ((CompoundVariable)values[0]).execute();
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            return "Error creating digest: " + e;
        }
        String result = JOrphanUtils.baToHexString(digest.digest(str.getBytes()));
        if (values.length > 1){
            variables.put(((CompoundVariable)values[1]).execute().trim(), result);
        }
        return result;
    }

    @Override
    public void setParameters(Collection<CompoundVariable> collection) throws InvalidVariableException {
        checkMinParameterCount(collection, 1);
        values = collection.toArray();
    }

    @Override
    public String getReferenceKey() {
        return KEY;
    }

    @Override
    public List<String> getArgumentDesc() {
        return desc;
    }
}
