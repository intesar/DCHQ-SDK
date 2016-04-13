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

import com.dchq.schema.beans.base.Message;
import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.blueprint.Blueprint;
import com.dchq.schema.beans.one.security.Users;
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
 * @author Intesar Mohammed
 * @since 1.0
 * <p/>
 * Users:
 * Create (annonymous & ENABLED, ROLE_TENANT_ADMIN)
 * valid
 * invalid: username, email, role, pass
 * Read   (ROLE_USER for sharing)
 * Update (SELF, ROLE_TENANT_ADMIN)
 * invalid: dup username, email, role, pass
 * Delete (ROLE_TENANT_ADMIN)
 * Disabled (ROLE_TENANT_ADMIN)
 * Change ROLE (ROLE_TENANT_ADMIN)
 * Change Password (SELF, ROLE_TENANT_ADMIN)
 * Search (ROLE_USER for sharing)
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class UsersFindAllServiceTest extends AbstractServiceTest {

    private UserService service;

    @org.junit.Before
    public void setUp() throws Exception {
        service = ServiceFactory.buildUserService(rootUrl, username, password);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"fn", "ln", "ituser1", "ituser1@dchq.io", "pass1234",false},

        });
    }

    private Users users;
    private boolean success;
    private Users userCreated;
    private int countBeforeCreate=0,countAfterCreate=0,countAfterDelete=0;


    public UsersFindAllServiceTest(String fn, String ln, String username, String email, String pass,  boolean success) {
        this.users = new Users().withFirstname(fn).withLastname(ln).withUsername(username).withEmail(email).withPassword(pass);
        this.success = success;

    }



    public int testGetUserFromFindAll(String id) {

        ResponseEntity<List<Users>> response = service.findAll();

        String errors = "";
        for (Message message : response.getMessages())
            errors += ("Error while Find request  " + message.getMessageText() + "\n");


        assertNotNull(response);
        assertNotNull(response.isErrors());
        assertThat(false, is(equals(response.isErrors())));
        int position=0;
        if(id!=null) {
            for (Users obj : response.getResults()) {
                position++;
if(obj.getId().equals(id) ){
    logger.info(" User Object Matched in FindAll {}  at Position : {}", id, position);
    assertEquals("Recently Created User is not at Positon 1 :"+obj.getId(),1, position);
}
            }
        }

        logger.info(" Total Number of Users :{}",response.getResults().size());

return response.getResults().size();
    }

    @Test
    public void testFindAll() {

        countBeforeCreate=testGetUserFromFindAll(null);
        logger.info("Create user fn [{}] ln [{}] username [{}]", users.getFirstname(), users.getLastname(), users.getUsername());

        ResponseEntity<Users> response = service.create(users);

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

            this.userCreated = response.getResults();
            logger.info("Create request successfully completed for user fn [{}] ln [{}] username [{}]", userCreated.getFirstname(), userCreated.getLastname(), userCreated.getUsername());
            assertEquals(users.getFirstname(), response.getResults().getFirstname());
            assertEquals(users.getLastname(), response.getResults().getLastname());
            assertEquals(users.getUsername(), response.getResults().getUsername());
            assertEquals(users.getEmail(), response.getResults().getEmail());

            // Password should always be empty
            assertThat("", is(response.getResults().getPassword()));


            logger.info("FindAll User by Id [{}]", userCreated.getId());
            this.countAfterCreate = testGetUserFromFindAll(userCreated.getId());
            assertEquals("Count of FInd all user between before and after create does not have diffrence of 1 for UserId :"+userCreated.getId(),countBeforeCreate, countAfterCreate-1);

        }

    }

    @After
    public void cleanUp() {
        logger.info("cleaning up...");

        if (userCreated != null) {
            service.delete(userCreated.getId());
            logger.info("Find All Users After Delete  User by Id {}",userCreated.getId());
            countAfterDelete=testGetUserFromFindAll(null);
            assertEquals("Count of FInd all user between before and after delete are not same for UserId :"+userCreated.getId(),countBeforeCreate, countAfterDelete);
        }
    }


}