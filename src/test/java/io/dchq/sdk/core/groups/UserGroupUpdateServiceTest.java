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
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static junit.framework.TestCase.assertNotNull;


/**
 * Created by Abedeen on 04/05/16.
 */

/**
 * Abstracts class for holding test credentials.
 *
 * @updater SaurabhB.
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
                //positive case
               {"My Group12", " Group Updated12", false},
                //Updating with Special chars
                {"Test for SpecialChars", "@#$%^", true},
                // Updating with Empty group names
     //           {"Test for Updating with Empty", "", true}

        });
    }

    private UserGroup userGroup;
    private boolean error;
    private UserGroup userGroupCreated;
    private UserGroup userGroupDeleted;
    private String updatedGroupName;
    private String messageText;

    public UserGroupUpdateServiceTest(String gname, String updatedGourpName, boolean error) {
        // random group Name
        String prefix = RandomStringUtils.randomAlphabetic(3);
        gname = prefix + gname;

        this.userGroup = new UserGroup().withName(gname);
        this.updatedGroupName = updatedGourpName;
        this.error = error;

    }

    @org.junit.Test
    public void testUpdate() throws Exception {

        //Creating new user Group
        logger.info("Create Group with Group Name [{}]", userGroup.getName());
        ResponseEntity<UserGroup> response = userGroupService.create(userGroup);

        for (Message message : response.getMessages()){
            logger.warn("Error while Create request  [{}] ", message.getMessageText());
        messageText = message.getMessageText();}
           assertNotNull(response);
           assertNotNull(response.isErrors());
           Assert.assertNotNull(((Boolean) false).toString(), ((Boolean) response.isErrors()).toString());
        Assert.assertFalse(messageText , response.isErrors());

        if (!response.isErrors() && response.getResults() != null) {
            userGroupCreated = response.getResults();
            assertNotNull(response.getResults());
            assertNotNull(response.getResults().getId());

            Assert.assertNotNull(userGroup.getName(), userGroupCreated.getName());
            userGroupCreated.setName(this.updatedGroupName);

            //Updating User Group
            logger.info("Update Request for Group with Group Name [{}]", userGroup.getName());
            response = userGroupService.update(userGroupCreated);

            for (Message message : response.getMessages()){
                logger.warn("Error while Update request  [{}] ", message.getMessageText());
            messageText = message.getMessageText();
            }
       //     Assert.assertNotNull(((Boolean) error).toString(), ((Boolean) response.isErrors()).toString());
            assertNotNull(response);
          //  assertNotNull(response.isErrors());
            if (response.isErrors()){
                Assert.fail(messageText);
            }
            else {
                Assert.assertNotNull(response.getResults());
                Assert.assertNotNull(response.getResults().getName(), updatedGroupName);
            }
        }
    }

    @After
    public void cleanUp() {
        logger.info("cleaning up...");

        if (userGroupCreated != null) {
            ResponseEntity<UserGroup> deleteResponse =  userGroupService.delete(userGroupCreated.getId());
            if (deleteResponse.getResults() != null)
                userGroupDeleted = deleteResponse.getResults();
            for (Message m : deleteResponse.getMessages()){
                logger.warn("[{}]", m.getMessageText());
                messageText = m.getMessageText();}
            Assert.assertFalse(messageText ,deleteResponse.isErrors());
        }
    }
}




