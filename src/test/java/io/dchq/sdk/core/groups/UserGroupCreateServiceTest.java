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
//import io.dchq.sdk.core.util.StringGenenerator;
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
 * Abstracts class for holding test credentials.
 * Make Sure User has rights to create groups
 *
 * @author Abedeen.
 * @since 1.0
 * @updater SaurabhB.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class UserGroupCreateServiceTest extends AbstractServiceTest {


    private UserGroupService userGroupService;

    @org.junit.Before
    public void setUp() throws Exception {
        userGroupService = ServiceFactory.builduserGroupService(rootUrl, username, password);
    }

    /*
    * Name: Not-Empty, Max_Length:Short-Text, Unique per System.
    * user Ids: Empty,Valid User id
    * */
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"TestGroup1212", false, false},
                // Passing " as group Name
               {"1\"My Group1\"", true, false},
                // Group Names with  Max Short Text :255 Charcters Passed
           /*     {"FwqkRRVOH2tuh8iigqZWTngTgLYzpcaqVahyLQqAXvzUhPpbN4qFz2TeeZASJUtC4x1nsFzP9cOkNAcFuHEGysRR6VafWArGW1jkWiWXD6CUfpkhwPoGNhIkcWLOqRrO7aqDifoZ8EGWKNHY49vTCKZ1jOI2JbZVToQeQGAERFJSlby4o2vc131o2wTFqMnp4KIwhjVQ97PBFjOxJhfnd9a5PAxNHLYBvnzTcCK45uGBiZhu3ubWOr6yM1s28pY", false, false},
                {"  TestGroup   1", false, false},
                {"_TestGroup   1", false, false},
                {"1", false, false},
                {"–—¡¿\\\"“”'‘’\\\"« »\\\"", false, false},
                {"&¢©÷><µ·¶±€£®§™¥°", false, false},
                {"2.00005", false, false},
                {" TestGroup    1", false, false},
                // Group Name Length 256.
                {"tQ9ukuIEBiYsSGkM1cRfES7DctIaE1W3GJ3K4WCQQxwYcNPy6NArpf2RFCEUXfmmmRkMVsvkh3TDQwWdxcyuWbbzX8xgxcfX6XwvCqVkbLE7rQ348EInhBNkIupRSvsMKaR51KFrVS7cNMi1WmJsNxWA3vEaKczJ2EHSauHx7Rs3Ln8UiEcjazU2qluzdaoQCTNBayw4VFJAAPVFHLG3wNV9OPjRUj39mNjCZBsZQJI1g2NYw6gQ1qkhqNOcWeFw", false, true},
                // checking Empty group names
                {"", false, true} */
        });
    }

    private UserGroup userGroup;
    private boolean error;
    private UserGroup userGroupCreated;
    private UserGroup userGroupDeleted;
    private String messageText;

    public UserGroupCreateServiceTest(String gname, boolean isInActive, boolean success) {
        this.userGroup = new UserGroup().withName(gname).withInactive(isInActive);
        this.error = success;
    }
    @org.junit.Test
    public void testCreate() throws Exception {

        logger.info("Create Group with Group Name [{}]", userGroup.getName());
        ResponseEntity<UserGroup> response = userGroupService.create(userGroup);

        if (response.getResults() != null)
            userGroupCreated = response.getResults();

        assertNotNull(response);
        assertNotNull(response.isErrors());
        for (Message m : response.getMessages()){
            logger.warn("[{}]", m.getMessageText());
            messageText = m.getMessageText();}

        Assert.assertEquals(messageText ,error, response.isErrors());


        if (!response.isErrors()) {
            assertNotNull("Response is null ", response.getResults());
            assertNotNull("Group Id is null", response.getResults().getId());
            Assert.assertEquals("Group Name does not match input value", userGroup.getName(), userGroupCreated.getName());
            Assert.assertEquals("User group Active/Inactive status does not match input Value", userGroup.getInactive(), userGroupCreated.getInactive());

        }
    }
   @After
    public void cleanUp() {
        logger.info("cleaning up...");

        if (userGroupCreated != null) {
            ResponseEntity<UserGroup> deleteResponse  =  userGroupService.delete(userGroupCreated.getId());
            if (deleteResponse.getResults() != null)
                userGroupDeleted = deleteResponse.getResults();
            for (Message m : deleteResponse.getMessages()){
                logger.warn("[{}]", m.getMessageText());
                 messageText = m.getMessageText();}
                 Assert.assertFalse(messageText ,deleteResponse.isErrors());
    //        Assert.assertEquals(messageText ,error, deleteResponse.isErrors());
        }
    }
}




