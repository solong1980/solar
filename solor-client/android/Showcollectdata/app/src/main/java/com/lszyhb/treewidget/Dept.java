package com.lszyhb.treewidget;

/**
 * Created by kkk8199 on 2017/12/12.
 *
 * @description 部门类（继承Node），此处的泛型Integer是因为ID和parentID都为int
 * ，如果为String传入泛型String即可
 */
public class Dept extends Node<Integer>{

    private int id;//自己ID
    private int parentId;//父亲节点ID
    private String name;//名称
    private String location;//位置
    private long project_id;

    public Dept() {
    }

    public Dept(int id, int parentId,long location_id,String location ,String name) {
        this.id = id;
        this.parentId = parentId;
        this.project_id=location_id;
        this.location =location;
        this.name = name;
    }

    /**
     * 此处返回节点ID
     * @return
     */
    @Override
    public Integer get_id() {
        return id;
    }

    /**
     * 此处返回父亲节点ID
     * @return
     */
    @Override
    public Integer get_parentId() {
        return parentId;
    }

    @Override
    public String get_label() {
        return name;
    }

    @Override
    public long get_projectid() {
        return project_id;
    }

    @Override
    public String get_location() {
        return location;
    }

    @Override
    public boolean parent(Node dest) {
        if (id == ((Integer)dest.get_parentId()).intValue()){
            return true;
        }
        return false;
    }

    @Override
    public boolean child(Node dest) {
        if (parentId == ((Integer)dest.get_id()).intValue()){
            return true;
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
