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
package io.dchq.sdk.core.groups;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.blueprint.Blueprint;
import com.dchq.schema.beans.one.security.UserGroup;
import io.dchq.sdk.core.AbstractServiceTest;
import io.dchq.sdk.core.ServiceFactory;
import io.dchq.sdk.core.UserGroupService;
import org.junit.Assert;

import java.util.List;

/**
 * Created by tahsin on 12/24/15.
 */
/**
 * Abstracts class for holding test credentials.
 *
 * @author Tahsin Usmani
 * @since 1.0
 */
public class UserGroupServiceTest extends AbstractServiceTest {


    private UserGroupService userGroupService;

    @org.junit.Before
    public void setUp() throws Exception {
        userGroupService = ServiceFactory.builduserGroupService(rootUrl, username, password);
    }

    @org.junit.Test
    public void testFindAll() throws Exception {
        ResponseEntity<List<UserGroup>> responseEntity = userGroupService.findAll();
        Assert.assertNotNull(responseEntity.getTotalElements());
        for (UserGroup userGroup : responseEntity.getResults()) {
            logger.info("UserGroup id [{}] name [{}] author [{}]", userGroup.getId(), userGroup.getName(), userGroup.getCreatedBy());
        }

    }


    @org.junit.Test
    public void testFindById() throws Exception {
        ResponseEntity<UserGroup> responseEntity = userGroupService.findById("2c918086520b7ce90152119662282165");
        Assert.assertNotNull(responseEntity.getResults());
        Assert.assertNotNull(responseEntity.getResults().getId());
    }


    @org.junit.Test
    public void testCreate() throws Exception {

        UserGroup userGroup = new UserGroup();

        // Create
        userGroup.setName("Tahsin Group");

        ResponseEntity<UserGroup> responseEntity = userGroupService.create(userGroup);
        Assert.assertNotNull(responseEntity.getResults());
        Assert.assertNotNull(responseEntity.getResults().getId());

        // ------

        userGroup = responseEntity.getResults();

        // Update

        userGroup.setName("Tahsin Group3");
        responseEntity = userGroupService.update(userGroup);

        Assert.assertNotNull(responseEntity.getResults());
        Assert.assertNotNull(responseEntity.getResults().getName(), "Tahsin Group3");

        // Delete
        responseEntity = userGroupService.delete(userGroup.getId());
        Assert.assertFalse(responseEntity.isErrors());

    }
}




