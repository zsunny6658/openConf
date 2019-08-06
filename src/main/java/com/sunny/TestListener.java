package com.sunny;

import com.sunny.source.listener.ConfListner;

public class TestListener implements ConfListner {
    @Override
    public void doBefore() {
        System.out.println("before");
    }

    @Override
    public void doAfter() {
        System.out.println("after");
    }
}
