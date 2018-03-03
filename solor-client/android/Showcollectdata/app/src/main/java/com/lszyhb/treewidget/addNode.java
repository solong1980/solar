package com.lszyhb.treewidget;

import java.io.Serializable;

/**
 * Created by kkk8199 on 12/28/17.
 */

public  class addNode implements Serializable {

    private String name; //记录省.市.县的名字
    private int id;   //记录节点id

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name=name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }
}
