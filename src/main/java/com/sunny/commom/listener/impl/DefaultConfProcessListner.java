package com.sunny.commom.listener.impl;

import com.sunny.commom.listener.ConfProcessListner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultConfProcessListner implements ConfProcessListner {

    private Logger log = LoggerFactory.getLogger(DefaultConfProcessListner.class);

    @Override
    public void doBefore() {
        log.info("start to manage the conf...");
    }

    @Override
    public void doAfter() {
        log.info("manage conf success...");
    }
}
