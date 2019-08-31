package com.sunny.sample;

import com.sunny.commom.listener.ConfProcessListener;

public class SampleConfProcessListener implements ConfProcessListener {
    @Override
    public void doBefore() {
        System.out.println("before");
    }

    @Override
    public void doAfter() {
        System.out.println("after");
    }
}
