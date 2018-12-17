package com.ljw.jmeter.plugin.dubbo.sampler;

import com.alibaba.dubbo.rpc.service.GenericException;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ljw.jmeter.plugin.dubbo.common.ClassUtils;
import com.ljw.jmeter.plugin.dubbo.common.JsonUtils;
import com.ljw.jmeter.plugin.dubbo.common.MethodArgument;
import com.ljw.jmeter.plugin.dubbo.common.Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 林杰炜 linjw
 * @Title dubbo采样器
 * @Description dubbo采样器
 * @date 2018/12/14 17:27
 */
public class DubboSampler extends AbstractSampler implements TestBean, Serializable, TestStateListener {
    private static final Logger log = LoggerFactory.getLogger(DubboSampler.class);

    private String variableName;
    private String method;
    private String args;

    @Override
    public SampleResult sample(Entry e) {
        SampleResult result = new SampleResult();
        result.setSampleLabel(getName());
        result.sampleStart();
        JMeterVariables variables = getThreadContext().getVariables();
        result.setSamplerData(getSampleData(variables));
        result.setResponseData(JsonUtils.getJson(callDubbo(variables, result)).getBytes(Charset.forName("utf-8")));
        result.setDataType(SampleResult.TEXT);
        result.setResponseOK();
        result.setResponseMessageOK();
        result.sampleEnd();
        return result;
    }

    private Object callDubbo(JMeterVariables variables, SampleResult res) {
        try {
            GenericService genericService = (GenericService) variables.getObject(variableName);
            String[] parameterTypes = null;
            Object[] parameterValues = null;
            List<MethodArgument> args = getMethodArgs();
            List<String> paramterTypeList = new ArrayList<String>();
            List<Object> parameterValuesList = new ArrayList<Object>();
            for (MethodArgument arg : args) {
                ClassUtils.parseParameter(paramterTypeList, parameterValuesList, arg);
            }
            parameterTypes = paramterTypeList.toArray(new String[paramterTypeList.size()]);
            parameterValues = parameterValuesList.toArray(new Object[parameterValuesList.size()]);
            Object obj = null;
            Result result = new Result();
            try {
                obj = genericService.$invoke(method, parameterTypes, parameterValues);
                result.setCode(10000);
                result.setBody(obj);
                res.setSuccessful(true);
            } catch (GenericException e) {
                log.error("{}接口调用异常：", method, e);
                res.setSuccessful(false);
                obj = e.getMessage();
                result.setCode(-1);
                result.setBody(obj);
            }
            return result;
        } catch (Exception e) {
            log.error("{}接口未知异常：", method, e);
            res.setSuccessful(false);
            return e;
        }
    }

    private String getSampleData(JMeterVariables variables) {
        StringBuilder sb = new StringBuilder();
        sb.append(variables.get(variableName + "_samplerData"));
        sb.append("Method: ").append(method).append("\n");
        sb.append("Method Args: ").append(getMethodArgs().toString());
        return sb.toString();
    }

    private List<MethodArgument> getMethodArgs() {
        List<MethodArgument> result = null;
        try {
            String methodArgs = getArgs();
            if (log.isDebugEnabled()) {
                log.debug(getName() + "\nargs:" + methodArgs);
            }
            int size = 0;
            JSONArray jsonArray = null;
            result = Lists.newArrayList();
            if (StringUtils.isNotBlank(methodArgs)) {
                try {
                    jsonArray = JSON.parseArray(methodArgs);
                } catch (Exception e) {
                    jsonArray = JSON.parseArray("[]");
                }
                if (jsonArray.size() > 0) {
                    size = jsonArray.size();
                }
            }
            for (int i = 0; i < size; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.keySet().size() == 1) {
                    for (String key : jsonObject.keySet()) {
                        String paramType = key;
                        String paramValue = "";
                        if (paramType.equals("String") || paramType.equals("string") || paramType.equals("java.lang.String")) {
                            paramValue = (String) jsonObject.get(key);
                        } else {
                            paramValue = JsonUtils.getJson(jsonObject.get(key));
                        }
                        MethodArgument argument = new MethodArgument(paramType, paramValue);
                        result.add(argument);
                    }
                }
            }
        } catch (Exception e) {
            log.info("getMethodArgs error:" + e);
        }
        return result;
    }

    @Override
    public void testStarted() {

    }

    @Override
    public void testStarted(String host) {

    }

    @Override
    public void testEnded() {

    }

    @Override
    public void testEnded(String host) {

    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }
}
