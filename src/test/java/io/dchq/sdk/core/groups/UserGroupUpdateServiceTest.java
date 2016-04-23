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

import com.dchq.schema.beans.base.Message;
import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.blueprint.Blueprint;
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

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by Abedeen on 04/05/16.
 */

/**
 * Abstracts class for holding test credentials.
 *
 * @author Abedeen.
 * @since 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class UserGroupUpdateServiceTest extends AbstractServiceTest {


    private UserGroupService userGroupService;

    @org.junit.Before
    public void setUp() throws Exception {
        userGroupService = ServiceFactory.builduserGroupService(rootUrl, username, password);
        
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Tahsin Group", " Tahsin Group Updated", false},
                // checking Empty group names
                {"Test for Updating with Empty", "", true}
        });
    }

    private UserGroup userGroup;
    private boolean success;
    private UserGroup userGroupCreated;
    private String updatedGourpName;

    public UserGroupUpdateServiceTest(String gname, String updatedGourpName, boolean success) {
        this.userGroup = new UserGroup().withName(gname);
        this.updatedGourpName = updatedGourpName;
        this.success = success;

    }

    @org.junit.Test
    public void testUpdate() throws Exception {

        logger.info("Create Group with Group Name [{}]", userGroup.getName());
        ResponseEntity<UserGroup> response = userGroupService.create(userGroup);

        for (Message message : response.getMessages())
            logger.warn("Error while Create request  [{}] ", message.getMessageText());

        assertNotNull(response);
        assertNotNull(response.isErrors());
        Assert.assertNotNull(((Boolean) false).toString(), ((Boolean) response.isErrors()).toString());

        if (!response.isErrors() && response.getResults() != null) {
            userGroupCreated = response.getResults();
            assertNotNull(response.getResults());
            assertNotNull(response.getResults().getId());


            Assert.assertNotNull(userGroup.getName(), userGroupCreated.getName());
            userGroupCreated.setName(this.updatedGourpName);

            logger.info("Update Request for Group with Group Name [{}]", userGroup.getName());
            response = userGroupService.update(userGroupCreated);

            for (Message message : response.getMessages())
                logger.warn("Error while Update request  [{}] ", message.getMessageText());
            Assert.assertNotNull(((Boolean) success).toString(), ((Boolean) response.isErrors()).toString());
            assertNotNull(response);
            assertNotNull(response.isErrors());

            if (!response.isErrors()) {
                Assert.assertNotNull(response.getResults());
                Assert.assertNotNull(response.getResults().getName(), updatedGourpName);
            }


        }

    }

    @After
    public void cleanUp() {
        logger.info("cleaning up...");

        if (userGroupCreated != null) {
            userGroupService.delete(userGroupCreated.getId());
        }
    }
}




