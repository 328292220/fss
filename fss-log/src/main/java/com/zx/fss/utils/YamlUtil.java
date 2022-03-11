package com.zx.fss.utils;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.zx.fss.properties.LogNacosProperties;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.util.*;

@Slf4j
public class YamlUtil {
    private LogNacosProperties logNacosProperties;
    private String dataId;
    private String groupId;

    private ConfigService configService;

//    @Builder
//    public static YamlUtil builder(NacosProperties nacosProperties, String dataId, String groupId){
//        return new YamlUtil(nacosProperties, dataId, groupId);
//    }

    @Builder
    private YamlUtil(LogNacosProperties logNacosProperties, String dataId, String groupId) {
        this.logNacosProperties = logNacosProperties;
        this.dataId = dataId;
        this.groupId = groupId;
        this.configService = getConfigService();
    }





    /**
     * 修改配置文件内容
     * @return
     */
    public  boolean update(Map<String,Object> map){
        try {
            //获取配置文件值
            String confStr = configService.getConfig(dataId,groupId,3000);
            Yaml yaml = new Yaml();
            //读入文件
            Map result = yaml.load(confStr);
            log.info("yaml转map:{}",result);

            Set<String> set = map.keySet();
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()){
                String fullKey = iterator.next();
                Object value = map.get(fullKey);
                //处理数据
                List<Map<String,Object>> list = new ArrayList<>();
                list.add(result);
                String[] keys = fullKey.split("\\.");
                for (int i = 0; i < keys.length; i++) {
                    Map<String, Object> currentMap = list.get(list.size() - 1);
                    Object object = currentMap.get(keys[i]);
                    if(object instanceof Map){
                        Map<String,Object> temp = (Map) object;
                        list.add(temp);
                    }else {
                        //最后一个一定是String
                        currentMap.put(keys[i],value);
                    }
                }
                log.info("修改map值后数据:{}",result);
            }

            confStr = yaml.dumpAsMap(result);
            log.info("map转成yaml:{}",confStr);
            boolean status = configService.publishConfig(dataId, groupId, confStr, "yaml");
            log.info("修改nacos配置是否成功:{}",status);
            return status;
        }catch (NacosException e){
            log.error("YamlUtil.update异常",e.getMessage());
            return false;
        }


    }
    public  void update(String fullKey,Object value){
        try {
            //获取配置文件值
            String confStr = configService.getConfig(dataId,groupId,3000);

            Yaml yaml = new Yaml();
            //读入文件
            Map result = yaml.load(confStr);
            log.info("获取nacos配置文件值:{}",result);

            //处理数据
            List<Map<String,Object>> list = new ArrayList<>();
            list.add(result);
            String[] keys = fullKey.split("\\.");
            for (int i = 0; i < keys.length; i++) {
                Map<String, Object> currentMap = list.get(list.size() - 1);
                Object object = currentMap.get(keys[i]);
                if(object instanceof Map){
                    Map<String,Object> temp = (Map) object;
                    list.add(temp);
                }else {
                    //最后一个一定是String
                    currentMap.put(keys[i],value);
                }
            }
            log.info("处理数据nacos配置文件值:{}",result);
            confStr = yaml.dumpAsMap(result);
            log.info("修改nacos配置文件值后转成String:{}",confStr);
            boolean status = configService.publishConfig(dataId, groupId, confStr, "yaml");
            log.info("修改nacos配置:{}",status);
        } catch (NacosException e) {
            log.error("YamlUtil.update异常",e.getMessage());
            return;
        }


    }

    /**
     * 获取nacos配置对象
     * @return
     * @throws NacosException
     */
    private ConfigService getConfigService(){
        Properties properties = new Properties();
        if(logNacosProperties != null){
            properties.setProperty("serverAddr", logNacosProperties.getServerAddr());
            properties.setProperty("namespace", logNacosProperties.getNamespace());
            properties.setProperty("username", logNacosProperties.getUserName());
            properties.setProperty("password", logNacosProperties.getPassword());
        }else {
            properties.setProperty("serverAddr", "127.0.0.1:8848");
            properties.setProperty("namespace", "d038fa42-7ca1-4d55-9a35-97873f5109c9");
            properties.setProperty("username", "nacos");
            properties.setProperty("password", "nacos");
        }
        //获取Configsetvice
        ConfigService configService = null;
        try {
            configService = NacosFactory.createConfigService(properties);
        } catch (NacosException e) {
            log.error("YamlUtil.update异常",e.getMessage());
        }
        return configService;
    }


}
