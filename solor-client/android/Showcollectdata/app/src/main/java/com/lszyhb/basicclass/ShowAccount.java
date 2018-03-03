package com.lszyhb.basicclass;

import java.util.List;

/**
 * Created by kkk8199 on 11/20/17.
 */

public class ShowAccount extends ShowAbtAuth {
    private Long id;
    private String account;
    private String name;// 用户姓名
    private String phone;// 手机号码
    private String oldPhone;//旧手机号
    private String email;// 邮箱地址
    private int type;// 用户类型
    private List<ShowAccountLocation> locations;
    private List<ShowProject> projects;//项目
    private String password;
    private Boolean savePwd;
    private String locationIds;

    private String vcode;// 验证码

    private String createTime;

    private int status;//审核状态,10是待审核,50是审核通过,60审核不通过

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getlocationIds(){
        return locationIds;
    }

    public void setlocationlds(String locationlds) {
        this.locationIds = locationIds;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Boolean getSavePwd() {
        return savePwd;
    }

    public void setSavePwd(Boolean savePwd) {
        this.savePwd = savePwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOldPhone() { return oldPhone; }

    public void setOldPhone(String oldPhone)  {
        this.oldPhone = oldPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public List<ShowAccountLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<ShowAccountLocation> locations) {
        this.locations = locations;
    }

    public List<ShowProject> getProjects() {
        return projects;
    }

    public void setProjects(List<ShowProject> projects) {
        this.projects = projects;
    }


}
