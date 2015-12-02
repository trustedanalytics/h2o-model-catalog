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
package org.trustedanalytics.modelcatalog.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.google.common.cache.LoadingCache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.trustedanalytics.modelcatalog.client.ServiceExposerOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.trustedanalytics.modelcatalog.data.DataModelProvider;
import org.trustedanalytics.modelcatalog.data.H2oInstance;
import org.trustedanalytics.modelcatalog.data.H2oInstanceCredentials;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
public class ModelController {

    private static final String SERVICE = "h2o";
    private final Function<Authentication, String> tokenExtractor;
    private final ServiceExposerOperations serviceExposerOperations;
    private final LoadingCache<H2oInstanceCredentials, H2oInstance> h2oInstanceCache;

    @Autowired
    public ModelController(Function<Authentication, String> tokenExtractor,
                           ServiceExposerOperations serviceExposerOperations,
                           LoadingCache<H2oInstanceCredentials, H2oInstance> h2oInstanceCache) {
        this.tokenExtractor = tokenExtractor;
        this.serviceExposerOperations = serviceExposerOperations;
        this.h2oInstanceCache = h2oInstanceCache;
    }

    @RequestMapping(value = "/rest/v1/analytics/h2o/models/organizations/{org}", method = GET, produces = APPLICATION_JSON_VALUE)
    public Collection<DataModelProvider> getH2oModels(@PathVariable("org") UUID org) {

        return serviceExposerOperations.getAllCredentials("bearer " + tokenExtractor.apply(SecurityContextHolder.getContext().getAuthentication()), org, SERVICE).stream()
            .map(h2oInstanceCache::getUnchecked)
            .map(h2oInstance -> new DataModelProvider(h2oInstance))
            .collect(Collectors.toList());
    }
}
