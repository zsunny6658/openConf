package com.sunny.sample;

import com.sunny.commom.listener.SourceListener;

public class SampleSourceListener implements SourceListener{
    @Override
    public void doBefore() {
        System.out.println("test source listener before");
    }

    @Override
    public void doAfter() {
        System.out.println("test source listener after");
    }
}
