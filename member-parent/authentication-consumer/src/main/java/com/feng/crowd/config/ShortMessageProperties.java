package com.feng.crowd.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Component
@ConfigurationProperties(prefix = "shortmessage")
public class ShortMessageProperties {

    private  String host;
    private String path;
    private String method;
    private String appcode;


}

