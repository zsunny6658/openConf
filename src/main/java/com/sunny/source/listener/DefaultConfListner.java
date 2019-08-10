package com.sunny.source.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultConfListner implements ConfListner {

    private Logger log = LoggerFactory.getLogger(DefaultConfListner.class);

    @Override
    public void doBefore() {
        log.info("start to manage the conf...");
    }

    @Override
    public void doAfter() {
        log.info("manage conf success...");
    }
}
