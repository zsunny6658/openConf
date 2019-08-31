package com.sunny.commom.listener.impl;

import com.sunny.commom.listener.ConfProcessListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultConfProcessListener implements ConfProcessListener {

    private Logger log = LoggerFactory.getLogger(DefaultConfProcessListener.class);

    @Override
    public void doBefore() {
        log.info("start to manage the conf...");
    }

    @Override
    public void doAfter() {
        log.info("manage conf success...");
    }
}
