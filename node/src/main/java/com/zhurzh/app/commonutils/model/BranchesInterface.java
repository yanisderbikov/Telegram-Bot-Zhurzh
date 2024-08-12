package com.zhurzh.app.commonutils.model;


import javax.annotation.Nullable;

public interface BranchesInterface {
//    private String path;
    @Nullable
    String isActiveAndGetButtonName(Body body);
    void execute(Body body);
}