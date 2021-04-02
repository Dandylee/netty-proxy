package com.yy.xh.example.proxy;

import lombok.Data;
import org.springframework.aop.framework.ProxyConfig;

import java.util.List;

/**
 * ProxyConfig
 *
 * @Date 2021/4/1
 * @Author Dandy
 */
@Data
public class ProxyConfigs {

    private List<ProxyConfig> proxyConfigList;

}
