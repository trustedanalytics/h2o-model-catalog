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
package org.trustedanalytics.modelcatalog.service;

import com.google.common.cache.CacheLoader;

import feign.Feign.Builder;
import feign.FeignException;
import feign.auth.BasicAuthRequestInterceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.trustedanalytics.modelcatalog.client.H2oOperations;
import org.trustedanalytics.modelcatalog.data.H2oInstance;
import org.trustedanalytics.modelcatalog.data.H2oInstanceCredentials;
import org.trustedanalytics.modelcatalog.data.H2oInstanceStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class H2oInstanceCacheLoader extends CacheLoader<H2oInstanceCredentials, H2oInstance> {

    private static final Logger LOGGER = LoggerFactory.getLogger(H2oInstanceCacheLoader.class);
    private final Supplier<Builder> clientSupplier;
    private final Map<String, H2oOperations> h2oClientsPool;

    @Autowired
    public H2oInstanceCacheLoader(Supplier<Builder> clientSupplier) {
        this.h2oClientsPool = new HashMap<>();
        this.clientSupplier = clientSupplier;
    }

    @Override
    public H2oInstance load(H2oInstanceCredentials h2oInstanceCredentials) {
        return getH2oInstance(h2oClientsPool.computeIfAbsent(h2oInstanceCredentials.getGuid(), key -> clientSupplier.get()
                .requestInterceptor(new BasicAuthRequestInterceptor(h2oInstanceCredentials.getLogin(), h2oInstanceCredentials.getPassword()))
                .target(H2oOperations.class, "http://" + h2oInstanceCredentials.getHostname())))
            .setH2oInstanceCredentials(h2oInstanceCredentials);
    }

    private H2oInstance getH2oInstance(H2oOperations client) {
        try {
            return new H2oInstance()
                .setH2oModels(client.getModels().getModels())
                .setH2oInstanceStatus(H2oInstanceStatus.OK);
        } catch (FeignException e) {
            LOGGER.error("Could not read data models for instance ", e);
            return new H2oInstance()
                .setH2oModels(Collections.emptyList())
                .setH2oInstanceStatus(H2oInstanceStatus.UNAUTHORIZED);
        } catch (Exception e) {
            LOGGER.error("Unexpected failure ", e);
            return new H2oInstance()
                .setH2oModels(Collections.emptyList())
                .setH2oInstanceStatus(H2oInstanceStatus.FAILED);
        }
    }
}
