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

import feign.Feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;

import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
@Profile({"test"})
public class ITConfiguration {

    @Value("${services.service-exposer}")
    public String testUrl;

    @Bean
    public H2oInstancesOperations h2oInstancesOperations() {
        return clientSupplier.get().target(H2oInstancesOperations.class, testUrl);
    }

    @Bean
    @Primary
    public Function<Authentication, String> tokenExtractor() {
        return authentication -> "token";
    }

    @Autowired
    private Supplier<Feign.Builder> clientSupplier;
}