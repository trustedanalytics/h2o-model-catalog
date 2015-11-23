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

@Data
public class H2oInstanceCredentials {

    private String name;
    private String password;
    private String login;
    private String hostname;
    private String guid;

    @Override
    public int hashCode() {
        return guid.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof H2oInstanceCredentials)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        H2oInstanceCredentials other = (H2oInstanceCredentials)obj;
        return guid.equals(other.getGuid());
    }
}
