/**
 * Copyright (c) 2015 Intel Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.trustedanalytics.modelcatalog;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;

import feign.Feign;
import feign.Feign.Builder;
import feign.Logger;
import feign.Request;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

import org.trustedanalytics.modelcatalog.client.ServiceExposerOperations;
import org.trustedanalytics.modelcatalog.data.H2oInstance;
import org.trustedanalytics.modelcatalog.service.H2oInstanceCacheLoader;
import org.trustedanalytics.modelcatalog.data.H2oInstanceCredentials;
import org.trustedanalytics.modelcatalog.security.OAuth2TokenExtractor;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
public class ApplicationConfiguration {

    @Value("${services.service-exposer}")
    private String serviceExposerBaseUrl;

    @Value("${maximum_cache_size}")
    private long maximumCacheSize;

    @Value("${cache_expiration_time_s}")
    private long cacheExpirationTimeS;

    @Bean
    public LoadingCache<H2oInstanceCredentials, H2oInstance> getH2oInstanceCache() {
        return CacheBuilder.newBuilder()
            .maximumSize(maximumCacheSize)
            .expireAfterWrite(cacheExpirationTimeS, TimeUnit.SECONDS)
            .build(new H2oInstanceCacheLoader(clientSupplier()));
    }

    @Bean
    public Function<Authentication, String> tokenExtractor() {
        return new OAuth2TokenExtractor();
    }

    @Bean
    public ServiceExposerOperations serviceExposerOperations() {
        return clientSupplier().get().target(ServiceExposerOperations.class, serviceExposerBaseUrl);
    }

    @Bean
    public Supplier<Builder> clientSupplier() {
        return () -> Feign.builder()
            .encoder(new JacksonEncoder(objectMapper()))
            .decoder(new JacksonDecoder(objectMapper()))
            .logger(new Slf4jLogger(ApplicationConfiguration.class))
            .options(new Request.Options(30 * 1000, 10 * 1000))
            .logLevel(Logger.Level.BASIC);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
            .setPropertyNamingStrategy(new LowerCaseWithUnderscoresStrategy())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
}
