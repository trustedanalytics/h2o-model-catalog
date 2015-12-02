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
package org.trustedanalytics.modelcatalog.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class DataModelProvider {

    private H2oInstanceStatus h2oInstanceStatus;
    private String name;
    private String password;
    private String login;
    private String hostname;
    private String guid;
    private Collection<DataModel> dataModels;

    public DataModelProvider(H2oInstance h2oInstance) {
        h2oInstanceStatus = h2oInstance.getH2oInstanceStatus();
        name = h2oInstance.getH2oInstanceCredentials().getName();
        password = h2oInstance.getH2oInstanceCredentials().getPassword();
        login = h2oInstance.getH2oInstanceCredentials().getLogin();
        hostname = h2oInstance.getH2oInstanceCredentials().getHostname();
        guid = h2oInstance.getH2oInstanceCredentials().getGuid();
        dataModels = h2oInstance.getH2oModels()
            .stream()
            .map(DataModel::new)
            .collect(Collectors.toList());
    }
}
