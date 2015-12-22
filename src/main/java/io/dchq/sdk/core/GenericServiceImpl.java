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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

/**
 * Abstracts low level rest calls.
 *
 * @author Intesar Mohammed
 * @since 1.0
 */
abstract class GenericServiceImpl<RL, RO> implements GenericService<RL, RO> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Represents the DCHQ base URL
     * <p>e.g. https://dchq.io/api/1.0/</p>
     */
    private String baseURI;

    /**
     * Represents the DCHQ endpoint
     * <p>e.g. blueprint</p>
     */
    private String endpoint;

    /**
     * Represents the authentication username
     * <p>e.g. admin@example.com</p>
     */
    private String username;

    /**
     * Represents the authentication password
     * <p>e.g. some-password</p>
     */
    private String password;

    /**
     * Spring RestTemplate
     */
    private RestTemplate template;

    public GenericServiceImpl(String baseURI, String endpoint, String username, String password) {
        this.baseURI = baseURI;
        this.endpoint = endpoint;
        this.username = username;
        this.password = password;
        this.template = new RestTemplate();
    }


    /**
     * @param requestParams
     * @param typeReference
     * @return
     */
    @Override
    public RL get(String requestParams, ParameterizedTypeReference<RL> typeReference) {

        String authHeader = buildAuth();

        String url = baseURI + endpoint + requestParams;
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        RequestEntity request = RequestEntity.get(uri)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", authHeader)
                .build();

        org.springframework.http.ResponseEntity<RL> res =
                template.exchange(
                        url,
                        HttpMethod.GET,
                        request,
                        typeReference
                );

        return res.getBody();
    }

    private String buildAuth() {
        String auth = username + ":" + password;
        byte[] encodedAuth = org.apache.commons.codec.binary.Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")));
        return "Basic " + new String(encodedAuth);
    }


}
