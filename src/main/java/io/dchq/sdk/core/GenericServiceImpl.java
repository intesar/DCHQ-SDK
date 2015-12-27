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
import org.springframework.http.*;
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
abstract class GenericServiceImpl<E, RL, RO> implements GenericService<E, RL, RO> {

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

    /**
     *
     */
    private ParameterizedTypeReference<RL> listTypeReference;

    /**
     *
     */
    private ParameterizedTypeReference<RO> singleTypeReference;

    protected GenericServiceImpl(String baseURI, String endpoint, String username, String password, ParameterizedTypeReference<RL> listTypeReference, ParameterizedTypeReference<RO> singleTypeReference) {
        this.baseURI = baseURI;
        this.endpoint = endpoint;
        this.username = username;
        this.password = password;
        this.template = new RestTemplate();
        this.listTypeReference = listTypeReference;
        this.singleTypeReference = singleTypeReference;
    }


    protected String getManagedEndpoint() {
        return "manage/";
    }

    /**
     * Executes GET request
     *
     * @return
     */
    @Override
    public RL findAll() {

        String authHeader = buildAuth();

        String url = baseURI + endpoint;
        URI uri = getUri(url);

        RequestEntity request = getBasicRequestEntity(authHeader, uri);

        org.springframework.http.ResponseEntity<RL> res =
                template.exchange(
                        url,
                        HttpMethod.GET,
                        request,
                        listTypeReference
                );

        return res.getBody();
    }


    /**
     * Executes GET request
     *
     * @param requestParams - not null
     * @return
     */
    @Override
    public RO findById(String requestParams) {

        String authHeader = buildAuth();

        String url = baseURI + endpoint + requestParams;
        URI uri = getUri(url);

        RequestEntity request = getBasicRequestEntity(authHeader, uri);

        org.springframework.http.ResponseEntity<RO> res =
                template.exchange(
                        url,
                        HttpMethod.GET,
                        request,
                        singleTypeReference
                );

        return res.getBody();
    }

    /**
     * Executes GET request
     *
     * @return
     */
    @Override
    public RL findAllManaged() {

        String authHeader = buildAuth();

        String url = baseURI + endpoint + getManagedEndpoint();
        URI uri = getUri(url);

        RequestEntity request = getBasicRequestEntity(authHeader, uri);

        org.springframework.http.ResponseEntity<RL> res =
                template.exchange(
                        url,
                        HttpMethod.GET,
                        request,
                        listTypeReference
                );

        return res.getBody();
    }


    /**
     * Executes GET request
     *
     * @param requestParams - not null
     * @return
     */
    @Override
    public RO findManagedById(String requestParams) {

        String authHeader = buildAuth();

        String url = baseURI + endpoint + getManagedEndpoint() + requestParams;
        URI uri = getUri(url);

        RequestEntity request = getBasicRequestEntity(authHeader, uri);

        org.springframework.http.ResponseEntity<RO> res =
                template.exchange(
                        url,
                        HttpMethod.GET,
                        request,
                        singleTypeReference
                );

        return res.getBody();
    }

    /**
     * Executes POST request
     *
     * @param entity - not null
     * @return
     */
    @Override
    public RO create(E entity) {

        String url = baseURI + endpoint;
        URI uri = getUri(url);

        HttpHeaders map = getHttpHeaders();

        //set your entity to send
        HttpEntity<E> requestEntity = new HttpEntity<>(entity, map);


        org.springframework.http.ResponseEntity<RO> res =
                template.exchange(
                        url,
                        HttpMethod.POST,
                        requestEntity,
                        singleTypeReference
                );

        return res.getBody();
    }


    /**
     * Executes PUT request
     *
     * @param entity - not null
     * @return
     */
    @Override
    public RO update(E entity) {

        String url = baseURI + endpoint + "id";
        URI uri = getUri(url);

        HttpHeaders map = getHttpHeaders();

        //set your entity to send
        HttpEntity<E> requestEntity = new HttpEntity<>(entity, map);


        org.springframework.http.ResponseEntity<RO> res =
                template.exchange(
                        url,
                        HttpMethod.PUT,
                        requestEntity,
                        singleTypeReference
                );

        return res.getBody();
    }

    /**
     * Executes DELETE request
     *
     * @param id - not null
     * @return
     */
    @Override
    public RO delete(String id) {

        String url = baseURI + endpoint + id;
        URI uri = getUri(url);

        HttpHeaders map = getHttpHeaders();

        //set your entity to send
        HttpEntity<E> requestEntity = new HttpEntity<>(map);


        org.springframework.http.ResponseEntity<RO> res =
                template.exchange(
                        url,
                        HttpMethod.DELETE,
                        requestEntity,
                        singleTypeReference
                );

        return res.getBody();
    }


    private HttpHeaders getHttpHeaders() {
        HttpHeaders map = new HttpHeaders();
        map.add(HttpHeaders.AUTHORIZATION, buildAuth());
        map.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        map.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return map;
    }

    private RequestEntity getBasicRequestEntity(String authHeader, URI uri) {
        return RequestEntity.get(uri)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", authHeader)
                .build();
    }


    private URI getUri(String url) {
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return uri;
    }


    private String buildAuth() {
        String auth = username + ":" + password;
        byte[] encodedAuth = org.apache.commons.codec.binary.Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")));
        return "Basic " + new String(encodedAuth);
    }


}
