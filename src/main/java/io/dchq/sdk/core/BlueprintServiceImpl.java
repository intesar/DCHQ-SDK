/*
 * Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.blueprint.Blueprint;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * Encapsulates DCHQ Blueprint endpoint calls.
 *
 * @author Intesar Mohammed
 * @see <a href="https://dchq.readme.io/docs/blueprints-1">Blueprint endpoint</a>
 * @since 1.0
 */
class BlueprintServiceImpl extends GenericServiceImpl<ResponseEntity<List<Blueprint>>, ResponseEntity<Blueprint>>
        implements BlueprintService {

    public static final ParameterizedTypeReference<ResponseEntity<List<Blueprint>>> listTypeReference = new ParameterizedTypeReference<ResponseEntity<List<Blueprint>>>() {
    };
    public static final ParameterizedTypeReference<ResponseEntity<Blueprint>> singleTypeReference = new ParameterizedTypeReference<ResponseEntity<Blueprint>>() {
    };

    public static final String ENDPOINT = "blueprints/";

    /**
     * @param baseURI  - e.g. https://dchq.io/api/1.0/
     * @param username - registered username with DCHQ.io
     * @param password - password used with the username
     */
    public BlueprintServiceImpl(String baseURI, String username, String password) {
        super(baseURI, ENDPOINT, username, password);
    }


    @Override
    public ResponseEntity<List<Blueprint>> get() {
        return get("", listTypeReference);
    }

    @Override
    public ResponseEntity<Blueprint> findById(String id) {
        return getOne(id, singleTypeReference);
    }

    @Override
    public ResponseEntity<List<Blueprint>> getManaged() {
        return get("manage", listTypeReference);
    }
    @Override
    public ResponseEntity<Blueprint> findManagedById(String id){
        return getOne(id, singleTypeReference);
    }

}
