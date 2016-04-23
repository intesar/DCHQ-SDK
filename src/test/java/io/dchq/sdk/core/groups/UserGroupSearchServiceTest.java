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
import com.dchq.schema.beans.one.security.UserGroup;
import io.dchq.sdk.core.AbstractServiceTest;
import io.dchq.sdk.core.ServiceFactory;
import io.dchq.sdk.core.UserGroupService;
import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertNotNull;

/**
 * @author Intesar Mohammed
 * @since 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class UserGroupSearchServiceTest extends AbstractServiceTest {


    private UserGroupService userGroupService;

    @org.junit.Before
    public void setUp() throws Exception {
        userGroupService = ServiceFactory.builduserGroupService(rootUrl, username, password);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"TestGroup2", false}
        });
    }

    private UserGroup userGroup;
    private boolean errors;
    private UserGroup userGroupCreated;

    public UserGroupSearchServiceTest(String gname, boolean errors) {
        this.userGroup = new UserGroup().withName(gname);
        this.errors = errors;

    }

    @org.junit.Test
    public void testSearch() throws Exception {

        logger.info("Creating Group with Group Name [{}]", userGroup.getName());
        ResponseEntity<UserGroup> response = userGroupService.create(userGroup);

        if (response.isErrors())
            logger.warn("Message from Server... {}", response.getMessages().get(0).getMessageText());

        assertNotNull(response);
        assertNotNull(response.isErrors());

        Assert.assertNotNull(((Boolean) errors).toString(), ((Boolean) response.isErrors()).toString());
        if (!response.isErrors() && response.getResults() != null) {
            userGroupCreated = response.getResults();
            assertNotNull(response.getResults());
            assertNotNull(response.getResults().getId());
            Assert.assertNotNull(userGroup.getName(), userGroupCreated.getName());
        }

        // let's search for the group
        ResponseEntity<List<UserGroup>> userGroupsResponseEntity = userGroupService.search(userGroup.getName(), 0, 1);
        assertNotNull(userGroupsResponseEntity);
        assertNotNull(userGroupsResponseEntity.isErrors());
        assertFalse(userGroupsResponseEntity.isErrors());

        assertNotNull(userGroupsResponseEntity.getResults());
        assertEquals(1, userGroupsResponseEntity.getResults().size());

        UserGroup searchedEntity = userGroupsResponseEntity.getResults().get(0);
        assertEquals(userGroupCreated.getId(), searchedEntity.getId());
        assertEquals(userGroupCreated.getName(), searchedEntity.getName());


    }

    @After
    public void cleanUp() {
        logger.info("cleaning up...");

        if (!errors) {
            userGroupService.delete(userGroupCreated.getId());
        }
    }
}




