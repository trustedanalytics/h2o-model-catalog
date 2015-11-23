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
import java.util.Collection;

@Data
public class H2oInstance {

    private H2oInstanceCredentials h2oInstanceCredentials;
    private Collection<H2oModel> h2oModels;
    private H2oInstanceStatus h2oInstanceStatus;

    public H2oInstance setH2oInstanceCredentials(H2oInstanceCredentials h2oInstanceCredentials) {
        this.h2oInstanceCredentials = h2oInstanceCredentials;
        return this;
    }

    public H2oInstance setH2oModels(Collection<H2oModel> h2oModels) {
        this.h2oModels = h2oModels;
        return this;
    }

    public H2oInstance setH2oInstanceStatus(H2oInstanceStatus h2oInstanceStatus) {
        this.h2oInstanceStatus = h2oInstanceStatus;
        return this;
    }
}
