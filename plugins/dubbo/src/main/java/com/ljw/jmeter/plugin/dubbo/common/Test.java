package com.ljw.jmeter.plugin.dubbo.common;

import com.alibaba.dubbo.common.Constants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.ReferenceConfigBase;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.registry.RegistryService;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * @author linjw
 * @date 2019/12/19 16:49
 */
public class Test {

    public static void main(String[] args) {
        try {
            String group = "test";
            String address = "zookeeper.szy.com:2181";
            ReferenceConfig reference = new ReferenceConfig();
            reference.setApplication(new ApplicationConfig("MyDubboSample"));
            RegistryConfig registry = new RegistryConfig();
            registry.setProtocol("zookeeper");
            registry.setGroup(group);
            registry.setAddress(address);
            reference.setRegistry(registry);
            reference.setInterface("org.apache.dubbo.registry.RegistryService");
            ReferenceConfigCache cache = ReferenceConfigCache.getCache(address + "_" + group, new ReferenceConfigCache.KeyGenerator() {
                @Override
                public String generateKey(ReferenceConfigBase<?> referenceConfig) {
                    return referenceConfig.toString();
                }
            });
            RegistryService registryService = (RegistryService) cache.get(reference);
            RegistryServerSync registryServerSync = RegistryServerSync.get(address + "_" + group);
            registryService.subscribe(RegistryServerSync.SUBSCRIBE, registryServerSync);
            ConcurrentMap<String, Map<String, URL>> providerUrls = registryServerSync.getRegistryCache().get(Constants.PROVIDERS_CATEGORY);
            System.out.printf("结果:" + providerUrls.keySet().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
