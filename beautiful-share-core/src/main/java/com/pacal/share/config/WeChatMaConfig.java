package com.pacal.share.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties(WxMiaProperties.class)
public class WeChatMaConfig {
    private final WxMiaProperties wxMiaProperties;

    public WeChatMaConfig(WxMiaProperties wxMiaProperties) {
        this.wxMiaProperties = wxMiaProperties;
    }

    @Bean
    public WxMaService wxMaService() {
        WxMaService maService = new WxMaServiceImpl();

        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid( this.wxMiaProperties.getAppId() );
        config.setSecret( this.wxMiaProperties.getAppSecret() );
        config.setToken( this.wxMiaProperties.getToken() );
        config.setAesKey( this.wxMiaProperties.getAesKey() );
        config.setMsgDataFormat( this.wxMiaProperties.getMsgDataFormat() );

        maService.setWxMaConfig( config );

        return maService;
    }
}