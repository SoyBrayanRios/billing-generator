package com.jtc.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.jtc.app.primary.entity.Alliance;
import com.jtc.app.primary.entity.Frequency;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {
	
	@Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        HttpMethod[] theUnsupportedActions = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE};
        //Disable Http methods for Product: PUT, POST and DELETE
        config.getExposureConfiguration()
                .forDomainType(Alliance.class)
                .withItemExposure(((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)))
                .withCollectionExposure(((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)));

        //Disable Http methods for ProductCategory: PUT, POST and DELETE
        config.getExposureConfiguration()
                .forDomainType(Frequency.class)
                .withItemExposure(((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)))
                .withCollectionExposure(((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)));
    }
	
}
