package com.wakedata.common.core.location;

/**
 * @author pengxu
 * @Date 2018/12/20.
 */
public class PoiToLocationDO {

    /**
     * location : {"lat":39.984154,"lng":116.30749}
     * address : 北京市海淀区北四环西路66号
     * formatted_addresses : {"recommend":"海淀区中关村中国技术交易大厦","rough":"海淀区中关村中国技术交易大厦"}
     * address_component : {"nation":"中国","province":"北京市","city":"北京市","district":"海淀区","street":"北四环西路","street_number":"北四环西路66号"}
     */

    /**
     * 详细地址
     */
    private String address;
    /**
     * 地址名称
     */
    private String title;


    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }
}
