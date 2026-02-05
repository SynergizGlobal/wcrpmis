package com.wcr.wcrbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.wcr.wcrbackend.common.CommonConstants;
import com.wcr.wcrbackend.common.CommonConstants2;

@Configuration
public class FileResourceConfig implements WebMvcConfigurer {
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/DESIGN_UPLOADED_FILES/**")
                .addResourceLocations(
                    "file:" + CommonConstants.DESIGN_UPLOADED_FILE_SAVING_PATH + "/"
                );
                registry.addResourceHandler("/DESIGN_REVISION_FILES/**")
                .addResourceLocations(
                    "file:" + CommonConstants.DESIGN_REVISION_FILES + "/"
                );
                registry.addResourceHandler("/P6_FILES/**")
                .addResourceLocations(
                    "file:" + CommonConstants2.P6_FILE_SAVING_PATH + "/"
                );
                
                registry.addResourceHandler("/TEMPLATE_FILES/**")
                .addResourceLocations(
                        "file:/" + CommonConstants.TEMPLATE_FILEPATH + "/"
                );

                 registry.addResourceHandler("/TEMPLATES_OLD/**")
                .addResourceLocations(
                		"file:" + CommonConstants.TEMPLATE_OLD_FILEPATH + "/"
                  );
    }

}

