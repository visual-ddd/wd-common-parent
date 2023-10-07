package com.wakedata.common.core.location;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 使用工具类前需要在配置文件加入qq.map.accessKey
 * @author pengxu
 * @Date 2019/4/10. 
 */
@Component
public class LocationUtil {
    private static Logger logger = LoggerFactory.getLogger(LocationUtil.class);
    private static final String QQ_MAP_URL = "https://apis.map.qq.com/ws/geocoder/v1/?location=%s,%s&get_poi=1&key=%s";
    private static final String QQ_MAP_ADDRESS_POI = "https://apis.map.qq.com/ws/geocoder/v1/?address=%s&key=%s";
    private static final String QQ_MAP_IP_TO_ADDRESS = "https://apis.map.qq.com/ws/location/v1/ip?ip=%s&key=%s";

    //http默认超时5s
    private static final Integer DEFAULT_TIMEOUT = 5000;

    /**
     * https://lbs.qq.com/webservice_v1/guide-gcoder.html
     * 地址解析
     * @param location
     * @return
     */
    public static LocationToPoiDO locationToPoi(String location){
        LocationToPoiDO resultDTO = new LocationToPoiDO();
        if(StringUtils.isEmpty(location)){
            logger.error("locationToPoi参数为空");
            return null;
        }
        String jsonObjectStr = HttpUtil.get(String.format(QQ_MAP_ADDRESS_POI,location, QqMapConfig.getAccessKey()), DEFAULT_TIMEOUT);
        if(StringUtils.isEmpty(jsonObjectStr)){
            logger.error("请求腾讯地图返回为空:" + location);
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonObjectStr);
        JSONObject resultJson = jsonObject.getJSONObject("result");
        if(null == resultJson){
            logger.error("请求腾讯地图结果失败:" + location + jsonObject.getString("message"));
            return null;
        }
        JSONObject locationObj = resultJson.getJSONObject("location");
        resultDTO.setLat(locationObj.getString("lat"));
        resultDTO.setLng(locationObj.getString("lng"));
        return resultDTO;
    }
    /**
     * https://lbs.qq.com/webservice_v1/guide-gcoder.html
     * 逆地址解析
     * @param lng
     * @param lat
     * @return
     */
    public static PoiToLocationDO poiToLocation(String lat , String lng){
        PoiToLocationDO resultDTO = new PoiToLocationDO();
        if(StringUtils.isEmpty(lng) || StringUtils.isEmpty(lat)){
            logger.error("locationToPoi参数为空");
            return null;
        }
        String jsonObjectStr = HttpUtil.get(String.format(QQ_MAP_URL,lat,lng, QqMapConfig.getAccessKey()), DEFAULT_TIMEOUT);
        if(StringUtils.isEmpty(jsonObjectStr)){
            logger.error("请求腾讯地图返回为空");
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonObjectStr);
        JSONObject resultJson = jsonObject.getJSONObject("result");
        if(null == resultJson){
            logger.error("请求腾讯地图结果失败："+ jsonObject.getString("message"));
            return null;
        }
        resultDTO.setAddress(resultJson.getString("address"));
        JSONObject titleObject = resultJson.getJSONObject("formatted_addresses");
        if(null != titleObject){
            resultDTO.setTitle(titleObject.getString("recommend"));
        }
        return resultDTO;
    }
    /**
     * https://lbs.qq.com/webservice_v1/guide-gcoder.html
     * 逆地址解析
     * @param lng
     * @param lat
     * @return
     */
    public static AddrComponentDO poiToAddrComponent(String lat , String lng){
        if(StringUtils.isEmpty(lng) || StringUtils.isEmpty(lat)){
            logger.error("locationToPoi参数为空");
            return null;
        }
        String jsonObjectStr = HttpUtil.get(String.format(QQ_MAP_URL,lat,lng, QqMapConfig.getAccessKey()), DEFAULT_TIMEOUT);
        if(StringUtils.isEmpty(jsonObjectStr)){
            logger.error("请求腾讯地图返回为空");
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonObjectStr);
        JSONObject resultJson = jsonObject.getJSONObject("result");
        if(null == resultJson){
            logger.error("请求腾讯地图结果失败："+ jsonObject.getString("message"));
            return null;
        }
        String addrComp = resultJson.getString("address_component");
        if(null == addrComp){
            logger.error("请求腾讯地图结果失败："+ jsonObject.getString("message"));
            return null;
        }
        return JSON.parseObject(addrComp,AddrComponentDO.class);
    }

    /**
     * https://apis.map.qq.com/ws/location/v1/ip
     * ip解释成地址
     */
    public static IpInterpretationResultDO ipToAddressComponent(String ip) {
        //拼接URL访问腾讯地图的接口
        String jsonObjectStr = HttpUtil.get(String.format(QQ_MAP_IP_TO_ADDRESS, ip, QqMapConfig.getAccessKey()));
        if(StringUtils.isEmpty(jsonObjectStr)){
            logger.error("请求腾讯地图返回为空");
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonObjectStr);
        JSONObject resultJson = jsonObject.getJSONObject("result");
        //接收错误码和错误码描述,当访问腾讯地图接口不成功的时候,返回的结果resultJson一定是空的
        if(null == resultJson){
            logger.error("请求腾讯地图结果失败："+ jsonObject.getString("message"));
            return null;
        }

        String location = resultJson.getString("location");
        String adInfo = resultJson.getString("ad_info");
        //接收经纬度
        LocationToPoiDO locationToPoiDO = JSON.parseObject(location, LocationToPoiDO.class);
        //接收返回的定位行政区划信息（国家、省、市、区、行政区划代码）
        IpInterpretationResultAdInfoDO ipInterpretationResultAdInfoDO = JSON.parseObject(adInfo, IpInterpretationResultAdInfoDO.class);

        //返回的结果实体
        IpInterpretationResultDO ipInterpretationResultDO = new IpInterpretationResultDO();
        //记录实际发送请求的ip、经纬度、行政区划信息
        ipInterpretationResultDO.setIp(resultJson.getString("ip"));
        ipInterpretationResultDO.setLocation(locationToPoiDO);
        ipInterpretationResultDO.setAdInfo(ipInterpretationResultAdInfoDO);
        return ipInterpretationResultDO;
    }
}
