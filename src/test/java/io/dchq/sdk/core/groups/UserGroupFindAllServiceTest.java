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
import com.dchq.schema.beans.one.security.Users;
import io.dchq.sdk.core.AbstractServiceTest;
import io.dchq.sdk.core.ServiceFactory;
import io.dchq.sdk.core.UserGroupService;
import org.hamcrest.core.Is;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * <code>UsersService</code> Integration Tests.
 *
 * @author c b          bIntesar Mohammed
 * @since 1.0
 * <p/>
 * UserGroup:
Create (ROLE_ORG_ADMIN)
invalid: dup name
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class UserGroupFindAllServiceTest extends AbstractServiceTest {

    private UserGroupService service;

    @org.junit.Before
    public void setUp() throws Exception {
        service = ServiceFactory.builduserGroupService(rootUrl, username, password);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Tahsin Group",  false},

        });
    }

    private UserGroup userGroup;
    private boolean success;
    private UserGroup userGroupCreated;
    private int countBeforeCreate=0,countAfterCreate=0,countAfterDelete=0;


    public UserGroupFindAllServiceTest(String gname, boolean success) {
        this.userGroup = new UserGroup().withName(gname);

        this.success = success;

    }



    public int testGroupPosition(String id) {

        ResponseEntity<List<UserGroup>> response = service.findAll();

        String errors = "";
        for (Message message : response.getMessages())
            errors += ("Error while Find All request  " + message.getMessageText() + "\n");


        assertNotNull(response);
        assertNotNull(response.isErrors());
        assertThat(false, is(equals(response.isErrors())));
        int position=0;
        if(id!=null) {
            for (UserGroup obj : response.getResults()) {
                position++;
                if(obj.getId().equals(id) ){
                    logger.info(" User Group Object Matched in FindAll {}  at Position : {}", id, position);
                    assertEquals("Recently Created User Group is not at Positon 1 :"+obj.getId(),1, position);
                }
            }
        }

        logger.info(" Total Number of User Groups :{}",response.getResults().size());

        return response.getResults().size();
    }

    @Test
    public void testFindAll() {

        countBeforeCreate=testGroupPosition(null);
        logger.info("Create Group with Group Name [{}]", userGroup.getName());
        ResponseEntity<UserGroup> response = service.create(userGroup);

        for (Message message : response.getMessages())
            logger.warn("Error while Create request  [{}] ", message.getMessageText());

        // check response is not null
        // check response has no errors
        // check response has user entity with ID
        // check all data send

        assertNotNull(response);
        assertNotNull(response.isErrors());
        assertThat(success, is(equals(response.isErrors())));

        if (!success) {

            assertNotNull(response.getResults());
            assertNotNull(response.getResults().getId());

            this.userGroupCreated = response.getResults();
            logger.info("Create request successfully completed for user Group Name [{}]",userGroupCreated.getName());
            assertEquals(userGroupCreated.getName(), userGroupCreated.getName());



            logger.info("FindAll User Group by Id [{}]", userGroupCreated.getId());
            this.countAfterCreate = testGroupPosition(userGroupCreated.getId());
            assertEquals("Count of FInd all user between before and after create does not have diffrence of 1 for UserId :"+userGroupCreated.getId(),countBeforeCreate, countAfterCreate-1);

        }

    }

    @After
    public void cleanUp() {
        logger.info("cleaning up...");

        if (userGroupCreated != null) {
            service.delete(userGroupCreated.getId());
            logger.info("Find All Users After Delete  User by Id {}",userGroupCreated.getId());
            countAfterDelete=testGroupPosition(null);
            assertEquals("Count of FInd all user between before and after delete are not same for UserId :"+userGroupCreated.getId(),countBeforeCreate, countAfterDelete);
        }
    }


}