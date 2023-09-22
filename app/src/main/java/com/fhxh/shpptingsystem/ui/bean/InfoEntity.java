package com.fhxh.shpptingsystem.ui.bean;

/**
 * Created by  on 2022/3/11.
 */
public class InfoEntity {

    private String no;
    private String ip;
    private String bullets;
    private String personName;
    private String type;
    private String mainTextJuId;
    private String main_text_gun_type;//枪型
    private String main_text_all_num;//总发数
    private String main_text_surplus_num;//子弹数

    public String getMain_text_gun_type() {
        return main_text_gun_type;
    }

    public void setMain_text_gun_type(String main_text_gun_type) {
        this.main_text_gun_type = main_text_gun_type;
    }

    public String getMain_text_all_num() {
        return main_text_all_num;
    }

    public void setMain_text_all_num(String main_text_all_num) {
        this.main_text_all_num = main_text_all_num;
    }

    public String getMain_text_surplus_num() {
        return main_text_surplus_num;
    }

    public void setMain_text_surplus_num(String main_text_surplus_num) {
        this.main_text_surplus_num = main_text_surplus_num;
    }

    private boolean isStart;

    public String getMainTextJuId() {
        return mainTextJuId;
    }

    public void setMainTextJuId(String mainTextJuId) {
        this.mainTextJuId = mainTextJuId;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getBullets() {
        return bullets;
    }

    public void setBullets(String bullets) {
        this.bullets = bullets;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getIsStart() {
        return isStart;
    }

    public void setIsStart(boolean isStart) {
        this.isStart = isStart;
    }
}
