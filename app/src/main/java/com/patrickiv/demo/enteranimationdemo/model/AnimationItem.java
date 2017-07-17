package com.patrickiv.demo.enteranimationdemo.model;

/**
 * Created by Patrick Ivarsson on 7/17/17.
 */
public class AnimationItem {
    private final String mName;
    private final int mResourceId;

    public AnimationItem(String name, int resourceId) {
        mName = name;
        mResourceId = resourceId;
    }

    public String getName() {
        return mName;
    }

    public int getResourceId() {
        return mResourceId;
    }
}
