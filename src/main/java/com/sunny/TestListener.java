package com.sunny;

import com.sunny.source.listener.ConfListner;

public class TestListener implements ConfListner{
    @Override
    public void doBefore() {
        // TODO System.out.println("before");
    }

    @Override
    public void doAfter() {
    	// TODO System.out.println("after");
    }
}
