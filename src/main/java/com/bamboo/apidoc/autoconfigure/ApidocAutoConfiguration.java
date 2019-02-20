package com.bamboo.apidoc.autoconfigure;


import com.alibaba.fastjson.JSON;
import com.bamboo.apidoc.code.model.Project;
import com.bamboo.apidoc.extension.spring.ApidocFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Author: GuoQing
 * @Date: 2019/2/2 11:07
 * @description
 */
@EnableConfigurationProperties(ApidocProperties.class)
@Configuration
public class ApidocAutoConfiguration {


    private Project project;
    /**
     * apidoc 配置文件
     */
    private ApidocProperties properties;

    ApidocAutoConfiguration(ApidocProperties properties) {
        this.properties = properties;
    }

    /**
     * apidoc工厂bean配置
     */
    @Bean(name = "apidocFactory")
    @ConditionalOnMissingBean
    public ApidocFactoryBean apiDocFactory() {
        ApidocFactoryBean factory = new ApidocFactoryBean();
        if (!ObjectUtils.isEmpty(this.properties.getPackagePath())) {
            factory.setPackagePath(this.properties.getPackagePath());
        }

        factory.setProject(project);

        return factory;
    }

    @Bean(name = "projectInfo")
    @ConditionalOnBean(ApidocFactoryBean.class)
    public Project getProjectInfo(@Autowired ApidocFactoryBean apidocFactory) throws IOException {
        Project projectInfo = new Project();
        if (!ObjectUtils.isEmpty(this.properties.getTitle())) {
            projectInfo.setName(this.properties.getTitle());
        }
        if (!ObjectUtils.isEmpty(this.properties.getDescription())) {
            projectInfo.setDescription(this.properties.getDescription());
        }

        projectInfo.buildProjectInfoFactory(apidocFactory);
        return projectInfo;
    }
}
