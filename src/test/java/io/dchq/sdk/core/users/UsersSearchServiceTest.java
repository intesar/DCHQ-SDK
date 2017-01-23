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

package io.dchq.sdk.core.users;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import com.dchq.schema.beans.base.Message;
import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.security.Users;

import io.dchq.sdk.core.AbstractServiceTest;
import io.dchq.sdk.core.ServiceFactory;
import io.dchq.sdk.core.UserService;

/**
 * <code>UsersService</code> Integration Tests.
 *
 * @author Intesar Mohammed
 * @since 1.0
 *        <p/>
 *        Users: Search (ROLE_USER for sharing)
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class UsersSearchServiceTest extends AbstractServiceTest {

    private UserService service;
    private Users users;
    private boolean createError;
    private Users userCreated;
    private String errorMessage;

    @org.junit.Before
    public void setUp() throws Exception {
        service = ServiceFactory.buildUserService(rootUrl, username, password);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
    	int userId = getRandomUserId();
        return Arrays.asList(new Object[][]{
                {"fn", "ln","user"+userId , "user"+userId+"@dchq.io", "pass1234", "", false},
        });
    }

    public UsersSearchServiceTest(String fn, String ln, String username, String email, String pass, String errorMessage, boolean success) {
        this.users = new Users().withFirstname(fn).withLastname(ln).withUsername(username).withEmail(email).withPassword(pass);
        this.users.setInactive(false);
        this.createError = success;
        this.errorMessage = errorMessage;
    }

    @Test
    public void testSearch() {
        logger.info("Create user fn [{}] ln [{}] username [{}]", users.getFirstname(), users.getLastname(), users.getUsername());
        ResponseEntity<Users> response = service.create(users);
        for (Message message : response.getMessages()) {
            logger.warn("Error while Create request  [{}] ", message.getMessageText());
            errorMessage+=message.getMessageText()+"\n";
        }
        if (!response.isErrors() && response.getResults()!=null) {
            this.userCreated = response.getResults();
            logger.info("Created Object Successfully with id  [{}] ",userCreated.getId());
        }
        // check response is not null
        // check response has no errors
        // check response has user entity with ID
        // check all data send
        assertNotNull(response);
        assertNotNull(response.isErrors());
        assertEquals(errorMessage, createError, response.isErrors());
        if (!createError) {
            assertNotNull(response.getResults());
            assertNotNull(response.getResults().getId());
            assertEquals(users.getFirstname(), response.getResults().getFirstname());
            assertEquals(users.getLastname(), response.getResults().getLastname());
            assertEquals(users.getUsername(), response.getResults().getUsername());
            assertEquals(users.getEmail(), response.getResults().getEmail());
            // Password should always be empty
            assertThat("", is(response.getResults().getPassword()));
        }
        logger.warn("Search Object wth username  [{}] ",userCreated.getUsername());
        ResponseEntity<List<Users>> userSearchResponseEntity = service.search(userCreated.getUsername(), 0, 1);
        errorMessage="";
        for (Message message : userSearchResponseEntity.getMessages()) {
            logger.warn("Error while Create request  [{}] ", message.getMessageText());
            errorMessage+=message.getMessageText()+"\n";
        }
        assertNotNull(userSearchResponseEntity);
        assertNotNull(userSearchResponseEntity.isErrors());
        assertFalse(errorMessage,userSearchResponseEntity.isErrors());
        assertNotNull(userSearchResponseEntity.getResults());
        assertEquals(1, userSearchResponseEntity.getResults().size());
        Users searchedEntity = userSearchResponseEntity.getResults().get(0);
        assertEquals(userCreated.getId(), searchedEntity.getId());
        assertEquals(userCreated.getUsername(), searchedEntity.getUsername());
    }

    @After
    public void cleanUp() {
		logger.info("cleaning up...");
		if (userCreated != null) {
			service.delete(userCreated.getId());
			logger.info("Deleted Object Successfully by Id {}", userCreated.getId());
		}
    }


}