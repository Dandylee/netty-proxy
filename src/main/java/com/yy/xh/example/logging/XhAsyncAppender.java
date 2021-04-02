package com.yy.xh.example.logging;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * XhAsyncAppender
 *
 * @Date 2021/3/12
 * @Author Dandy
 */
public class XhAsyncAppender extends AsyncAppender{

    public XhAsyncAppender(){}

    /**
     * 只丢弃debug级别的日志
     * @param event
     * @return
     */
    protected boolean isDiscardable(ILoggingEvent event) {
        Level level = event.getLevel();
        return level.toInt() <= 10000;
    }

}
