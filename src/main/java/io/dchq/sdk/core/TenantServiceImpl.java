/*
 * Copyright 2017 the original author or authors.
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
import com.dchq.schema.beans.one.security.Tenant;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * Encapsulates DCHQ Tenant calls.
 *
 * @author Intesar Mohammed
 * @see <a href="https://dchq.readme.io/docs/tenant-1">Tenant endpoint</a>
 * @since 1.0
 */
public class TenantServiceImpl extends GenericServiceImpl<Tenant, ResponseEntity<List<Tenant>>, ResponseEntity<Tenant>>
        implements TenantService {

    public static final ParameterizedTypeReference<ResponseEntity<List<Tenant>>> listTypeReference = new ParameterizedTypeReference<ResponseEntity<List<Tenant>>>() {
    };
    public static final ParameterizedTypeReference<ResponseEntity<Tenant>> singleTypeReference = new ParameterizedTypeReference<ResponseEntity<Tenant>>() {
    };

    public static final String ENDPOINT = "tenants/";

    /**
     * @param baseURI  - e.g. https://dchq.io/api/1.0/
     * @param username - registered username with DCHQ.io
     * @param password - password used with the username
     */
    public TenantServiceImpl(String baseURI, String username, String password) {
        super(baseURI, ENDPOINT, username, password,
                new ParameterizedTypeReference<ResponseEntity<List<Tenant>>>() {
                },
                new ParameterizedTypeReference<ResponseEntity<Tenant>>() {
                }
        );
    }

    @Override
    public ResponseEntity<List<Tenant>> get() {
        return findAll();
    }
}
