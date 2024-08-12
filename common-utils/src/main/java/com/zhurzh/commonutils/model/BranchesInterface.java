package com.zhurzh.commonutils.model;


public interface BranchesInterface {
//    private String path;
    String isActiveAndGetButtonName(Body body);
    void execute(Body body);
}