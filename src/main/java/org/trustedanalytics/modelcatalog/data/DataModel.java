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

@Data
@NoArgsConstructor
public class DataModel {

    private String name;
    private String type;
    private String schemaName;
    private String schemaType;
    private String algo;
    private String algoFullName;
    private String timestamp;

    public DataModel(H2oModel h2oModel) {
        name = h2oModel.getModelId().getName();
        type = h2oModel.getModelId().getType();
        schemaName = h2oModel.getMeta().getSchemaName();
        schemaType = h2oModel.getMeta().getSchemaType();
        algo = h2oModel.getAlgo();
        algoFullName = h2oModel.getAlgoFullName();
        timestamp = h2oModel.getTimestamp();
    }
}
