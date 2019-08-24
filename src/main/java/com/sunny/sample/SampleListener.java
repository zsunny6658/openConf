package com.sunny.sample;

import com.sunny.commom.listener.ConfProcessListner;

public class SampleListener implements ConfProcessListner {
    @Override
    public void doBefore() {
        System.out.println("before");
    }

    @Override
    public void doAfter() {
        System.out.println("after");
    }
}
