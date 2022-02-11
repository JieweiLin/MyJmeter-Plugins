package com.ljw.jmeter.functions;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机True or False
 *
 * @author 林杰炜 linjw
 * @date 2019/1/16 10:15
 */
public class RandomBoolean extends AbstractFunction {
    private static final Logger log = LoggerFactory.getLogger(RandomBoolean.class);
    private static final List<String> DESC = Lists.newLinkedList();
    private static final String KEY = "__RandomBoolean";

    static {
        DESC.add(JMeterUtils.getResString("function_name_paropt"));
    }

    private CompoundVariable variable = null;

    @Override
    public String execute(SampleResult sampleResult, Sampler sampler) throws InvalidVariableException {
        String[] data = {"true", "false"};
        int random = ThreadLocalRandom.current().nextInt(0, 1);
        String result = data[random];
        if (this.variable != null) {
            JMeterVariables variables = getVariables();
            if ((variables != null) && (StringUtils.isNotBlank(this.variable.execute().trim()))) {
                variables.put(this.variable.execute().trim(), result);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("randomBoolean :{}", result);
        }
        return result;
    }

    @Override
    public void setParameters(Collection<CompoundVariable> collection) throws InvalidVariableException {
        checkParameterCount(collection, 0, 1);
        Object[] values = collection.toArray();
        if (values.length > 0) {
            this.variable = (CompoundVariable) values[0];
        }
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
