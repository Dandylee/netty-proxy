package com.yy.xh.example.proxy;

import lombok.Data;

/**
 * ProxyConfig
 *
 * @Date 2021/4/1
 * @Author Dandy
 */
@Data
public class ProxyConfig {

    private int localPort;

    private String remoteHost;

    private int remotePort;
}
