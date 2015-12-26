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
import org.junit.Assert;

import java.util.List;

/**
 * <code>BlueprintService</code> unit tests.
 *
 * @author Intesar Mohammed
 * @since 1.0
 */
public class BlueprintServiceTest extends AbstractServiceTest {

    private BlueprintService blueprintService;

    @org.junit.Before
    public void setUp() throws Exception {
        blueprintService = ServiceFactory.buildBlueprintService(rootUrl, username, password);
    }

    @org.junit.Test
    public void testGet() throws Exception {
        ResponseEntity<List<Blueprint>> responseEntity = blueprintService.get();
        Assert.assertNotNull(responseEntity.getTotalElements());
        for (Blueprint bl : responseEntity.getResults()) {
            logger.info("Blueprint type [{}] name [{}] author [{}]", bl.getBlueprintType(), bl.getName(), bl.getCreatedBy());
        }
    }

    @org.junit.Test
    public void testFindById() throws Exception {
        ResponseEntity<Blueprint> responseEntity = blueprintService.findById("4028818650d4aca10150d4bf63470003");
        Assert.assertNotNull(responseEntity.getResults());
        Assert.assertNotNull(responseEntity.getResults().getId());
    }

    @org.junit.Test
    public void testGetManaged() throws Exception {
        ResponseEntity<List<Blueprint>> responseEntity = blueprintService.getManaged();
        Assert.assertNotNull(responseEntity.getTotalElements());
        for (Blueprint bl : responseEntity.getResults()) {
            logger.info("Managed Blueprint type [{}] name [{}] author [{}]", bl.getBlueprintType(), bl.getName(), bl.getCreatedBy());
        }
    }

    @org.junit.Test
    public void testFindManagedById() throws Exception {
        ResponseEntity<Blueprint> responseEntity = blueprintService.findManagedById("402881864e1a36cc014e1a399cf90102");
        Assert.assertNotNull(responseEntity.getResults());
        Assert.assertNotNull(responseEntity.getResults().getId());
    }

    @org.junit.Test
    public void testFindStarred() throws Exception {
        ResponseEntity<List<Blueprint>> responseEntity = blueprintService.findByStarred();
        Assert.assertNotNull(responseEntity.getResults());
    }

//    @org.junit.Test
//    public void testFindYamlById() throws Exception {
//        ResponseEntity<Blueprint> responseEntity = blueprintService.findYamlById("");
//        Assert.assertNotNull(responseEntity.getResults());
//        Assert.assertNotNull(responseEntity.getResults().getId());
//    }




}