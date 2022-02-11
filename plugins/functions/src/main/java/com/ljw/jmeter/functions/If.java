package com.ljw.jmeter.functions;

import com.google.common.collect.Lists;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;

import java.util.Collection;
import java.util.List;

/**
 * If方法
 *
 * @author 林杰炜 linjw
 * @date 2019/1/19 15:02
 */
public class If extends AbstractFunction {
    private static final List<String> DESC = Lists.newLinkedList();
    private static final String KEY = "__If";

    static {
        DESC.add("Actual value");
        DESC.add("Expected value");
        DESC.add("Result if actual == expected");
        DESC.add("Result if actual != expected");
        DESC.add("Name of variable in which to store the result (optional)");
    }

    private Object[] values;

    public If() {
        //do nothing
    }

    @Override
    public String execute(SampleResult sampleResult, Sampler sampler) throws InvalidVariableException {
        String actual = getParameter(0);
        String expected = getParameter(1);
        String result = null;
        if (actual.equals(expected)) {
            result = getParameter(2);
        } else {
            result = getParameter(3);
        }
        JMeterVariables variables = getVariables();
        if (values.length > 4) {
            String varName = getParameter(4).trim();
            variables.put(varName, result);
        }
        return result;
    }

    @Override
    public void setParameters(Collection<CompoundVariable> collection) throws InvalidVariableException {
        checkMinParameterCount(collection, 4);
        values = collection.toArray();
    }

    @Override
    public String getReferenceKey() {
        return KEY;
    }

    @Override
    public List<String> getArgumentDesc() {
        return DESC;
    }

    private String getParameter(int i) {
        return ((CompoundVariable) values[i]).execute();
    }
}
