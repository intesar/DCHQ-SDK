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

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Abstracts class for holding test credentials.
 *
 * @author Abedeen.
 * @updater SaurabhB.
 * @since 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class UserGroupFindServiceTest extends AbstractServiceTest {


    private UserGroupService userGroupService;
    private UserGroup userGroup;
    private boolean error;
    private UserGroup userGroupCreated;
    private UserGroup userGroupFindByID;
    private String messageText;

    @org.junit.Before
    public void setUp() throws Exception {
        userGroupService = ServiceFactory.builduserGroupService(rootUrl, username, password);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Find Group", false},
                //check with Empty Group Name
                {"", false}
        });
    }

    public UserGroupFindServiceTest(String gname, boolean error) {
        this.userGroup = new UserGroup().withName(gname);
        this.error = error;

    }

    @org.junit.Test
    public void testFind() throws Exception {

        logger.info("Create Group with Group Name [{}]", userGroup.getName());
        ResponseEntity<UserGroup> response = userGroupService.create(userGroup);

        for (Message message : response.getMessages()) {
            logger.warn("Error while Create request  [{}] ", message.getMessageText());
            messageText = message.getMessageText();
        }

        assertNotNull(response);
        assertNotNull(response.isErrors());
        Assert.assertEquals(messageText ,error, response.isErrors());

        if (!response.isErrors() && response.getResults() != null) {
            userGroupCreated = response.getResults();
            assertNotNull(response.getResults());
            assertNotNull(response.getResults().getId());

            Assert.assertNotNull(userGroup.getName(), userGroupCreated.getName());

            logger.info("Find Request for Group with Group ID [{}]", userGroupCreated.getId());
            response = userGroupService.findById(userGroupCreated.getId());

            for (Message message : response.getMessages()){
                logger.warn("Error while Find request  [{}] ", message.getMessageText());
            messageText = message.getMessageText();}

            Assert.assertEquals(messageText ,error, response.isErrors());
            assertNotNull(response);
            assertNotNull(response.isErrors());

            if (!response.isErrors()) {
                userGroupFindByID=response.getResults();
                Assert.assertNotNull(response.getResults());
                assertEquals(userGroupCreated.getName(), userGroupFindByID.getName());

            }
        }

    }

    @After
    public void cleanUp() {
        logger.info("cleaning up...");

        if (userGroupCreated != null) {
           // userGroupService.delete(userGroupCreated.getId());
            ResponseEntity<UserGroup> deleteResponse = userGroupService.delete(userGroupCreated.getId());
            for (Message m : deleteResponse.getMessages()){
                logger.warn("[{}]", m.getMessageText());
                messageText = m.getMessageText();}
            Assert.assertFalse(messageText , deleteResponse.isErrors());
        }
    }
}




