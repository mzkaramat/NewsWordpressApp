package com.xeaphii.ghanchi.darpan.model;

/**
 * Created by Administrator on 9/9/2015.
 */
public class MemberModel {
    public String name,id,isVisible,city;
    public MemberModel( String name,String id,String isVisible){
        this.name = name;
        this.id = id;
        this.isVisible =isVisible;
    }
    public MemberModel( String name,String id,String isVisible,String city){
        this.name = name;
        this.id = id;
        this.isVisible =isVisible;
        this.city = city;
    }
}
