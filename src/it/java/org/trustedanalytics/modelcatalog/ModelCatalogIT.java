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

import com.google.common.cache.LoadingCache;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.trustedanalytics.modelcatalog.data.H2oInstance;
import org.trustedanalytics.modelcatalog.data.H2oInstanceCredentials;
import org.trustedanalytics.modelcatalog.data.H2oModel;
import org.trustedanalytics.modelcatalog.data.H2oModels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, ITConfiguration.class})
@WebAppConfiguration
@IntegrationTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles({"test"})
public class ModelCatalogIT {

    @Autowired
    private H2oInstancesOperations h2oInstancesOperations;

    @Autowired
    private LoadingCache<H2oInstanceCredentials, H2oInstance> h2oInstanceCache;

    @Test
    public void modelController_getModels() throws ExecutionException {

        assertEquals(0, h2oInstanceCache.size());

        h2oInstancesOperations.getH2oInstances(UUID.fromString("ee1c60ab-1d4f-4bbb-aeba-60ea8c67bbbb"));

        H2oInstanceCredentials h2oInstanceCredentials = new H2oInstanceCredentials();
        h2oInstanceCredentials.setGuid("test-guid");

        H2oInstanceCredentials h2oInstanceCredentialsNonexistent = new H2oInstanceCredentials();
        h2oInstanceCredentialsNonexistent.setGuid("nonexistent-guid");

        assertEquals(1, h2oInstanceCache.size());
        assertEquals(null, h2oInstanceCache.getIfPresent(h2oInstanceCredentialsNonexistent));
        assertEquals(2, h2oInstanceCache.get(h2oInstanceCredentials).getH2oModels().size());
    }

    @RestController
    public static class TestController {

        @Value("${server.port}")
        public String port;

        @RequestMapping(value = "/3/Models", method = RequestMethod.GET)
        public H2oModels getModels() {

            H2oModels h2oModels = new H2oModels();
            h2oModels.setModels(new ArrayList<>());

            H2oModel h2oModel = new H2oModel();
            h2oModel.setAlgo("algo");

            H2oModel h2oModel2 = new H2oModel();
            h2oModel.setAlgo("algo2");

            h2oModels.getModels().add(h2oModel);
            h2oModels.getModels().add(h2oModel2);
            return h2oModels;
        }

        @RequestMapping(value = "/rest/credentials/organizations/{org}", method = RequestMethod.GET)
        public Collection<H2oInstanceCredentials> getCredentials(@PathVariable UUID org, @RequestParam(required = true) String service) {

            Collection<H2oInstanceCredentials> h2oInstanceCredentials = new ArrayList<>();
            H2oInstanceCredentials credentials = new H2oInstanceCredentials();
            credentials.setGuid("test-guid");
            credentials.setHostname("localhost:" + port);
            credentials.setLogin("login");
            credentials.setPassword("pass");
            credentials.setName("name");
            h2oInstanceCredentials.add(credentials);
            return h2oInstanceCredentials;
        }
    }
}
