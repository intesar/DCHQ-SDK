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
public class UsersServiceTest extends AbstractServiceTest {

    private UserService service;

    @org.junit.Before
    public void setUp() throws Exception {
        service = ServiceFactory.buildUserService(rootUrl, username, password);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"fn", "ln", "ituser1", "ituser1@dchq.io", "pass1234", false},
                {"fn", "ln", "ituser2", "ituser2@dchq.io", "pass1234", false},
                // TODO: validate password
                {"fn", "ln", "ituser2", "ituser3@dchq.io", "", false},
                {"", "", "ituser2", "ituser4@dchq.io", "", false}
        });
    }

    private Users users;
    private boolean success;
    private Users userCreated;

    public UsersServiceTest(String fn, String ln, String username, String email, String pass, boolean success) {
        this.users = new Users().withFirstname(fn).withLastname(ln).withUsername(username).withEmail(email).withPassword(pass);
        this.success = success;

    }

    //@org.junit.Test
    @Ignore
    public void testGet() throws Exception {
        ResponseEntity<List<Users>> responseEntity = service.findAll(0, 2000);
        Assert.assertNotNull(responseEntity.getTotalElements());
        for (Users obj : responseEntity.getResults()) {
            logger.info("User email [{}] first-name [{}] last-name [{}]", obj.getEmail(), obj.getFirstname(), obj.getLastname());
        }
    }

    @Test
    public void testCreate() {

        logger.info("Create user fn [{}] ln [{}] username [{}]", users.getFirstname(), users.getLastname(), users.getUsername());
        ResponseEntity<Users> response = service.create(users);

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

            assertEquals(users.getFirstname(), response.getResults().getFirstname());
            assertEquals(users.getLastname(), response.getResults().getLastname());
            assertEquals(users.getUsername(), response.getResults().getUsername());
            assertEquals(users.getEmail(), response.getResults().getEmail());

            // Password should always be empty
            assertThat("", is(response.getResults().getPassword()));
        }
    }

    @After
    public void cleanUp() {
        logger.info("cleaning up...");

        if (!success) {
            service.delete(userCreated.getId());
        }
    }


}