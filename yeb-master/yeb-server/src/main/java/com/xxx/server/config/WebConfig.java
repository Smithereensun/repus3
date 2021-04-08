package com.xxx.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * 图片保存路径，自动从yml文件中获取数据
     *   示例： E:/images/
     */

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("配置文件已经生效");
        
        String path="F:\\User\\yeb-master\\yeb-server\\src\\main\\resources\\static\\img\\";

        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:"+path);
    }
}
