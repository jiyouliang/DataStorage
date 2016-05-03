package com.itheima.datastorage.model;

import java.io.Serializable;

/**酒店对象
 * Created by youliang.ji on 2016/5/1.
 */
public class Hotel implements Serializable {

    private String price;//价格：价格为什么是字符串，很多公司所有返回json数据都是字符串，为了统一，当然如果你们公司返回double也没问题
    private String name;//酒店名称
    private String img;//酒店图片url

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "price='" + price + '\'' +
                ", name='" + name + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
