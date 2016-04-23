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
package io.dchq.sdk.core.providers;

import com.dchq.schema.beans.base.Message;
import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.blueprint.AccountType;
import com.dchq.schema.beans.one.blueprint.RegistryAccount;
import com.dchq.schema.beans.one.security.EntitlementType;
import com.dchq.schema.beans.one.security.UserGroup;
import com.dchq.schema.beans.one.security.Users;
import io.dchq.sdk.core.AbstractServiceTest;
import io.dchq.sdk.core.RegistryAccountService;
import io.dchq.sdk.core.ServiceFactory;
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
import static org.junit.Assert.assertEquals;

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
public class CloudProviderFindAllServiceTest extends AbstractServiceTest {
    private RegistryAccountService registryAccountService;

    @org.junit.Before
    public void setUp() throws Exception {
        registryAccountService = ServiceFactory.buildRegistryAccountService(rootUrl, username, password);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Rackspace US First testAccount", "dchqinc", Boolean.FALSE, AccountType.RACKSPACE, "7b1fa480664b4823b72abed54ebb9b0f", false},

        });
    }

    private RegistryAccount registryAccount;
    private boolean success;
    private RegistryAccount registryAccountCreated;
    private int countBeforeCreate = 0, countAfterCreate = 0, countAfterDelete = 0;

    public CloudProviderFindAllServiceTest(String name, String rackspaceName, Boolean isActive, AccountType rackspaceType, String Password, boolean success) {
        this.registryAccount = new RegistryAccount().withName(name).withUsername(rackspaceName).withInactive(isActive).withAccountType(rackspaceType).withPassword(Password);
        this.success = success;


    }

    public int testRegistryAccountPosition(String id) {

        ResponseEntity<List<RegistryAccount>> response = registryAccountService.findAll();

        String errors = "";
        for (Message message : response.getMessages())
            errors += ("Error while Find All request  " + message.getMessageText() + "\n");


        assertNotNull(response);
        assertNotNull(response.isErrors());
        assertThat(false, is(equals(response.isErrors())));
        int position = 0;
        if (id != null) {
            for (RegistryAccount obj : response.getResults()) {
                position++;
                if (obj.getId().equals(id)) {
                    logger.info("  Object Matched in FindAll {}  at Position : {}", id, position);
                    assertEquals("Recently Created Object is not at Positon 1 :" + obj.getId(), 1, position);
                }
            }
        }

        logger.info(" Total Number of Objects :{}", response.getResults().size());

        return response.getResults().size();
    }

    @org.junit.Test
    public void testFindAll() throws Exception {

        logger.info("Count of Cloud Provider before Create Cloudprovider with  Account with Name [{}]", registryAccount.getName());
        countBeforeCreate = testRegistryAccountPosition(null);

        logger.info("Create Registry Account with Name [{}]", registryAccount.getName());

        if (success)
            logger.info("Expecting Error while Create Registry Account with Name [{}]", registryAccount.getName());

        ResponseEntity<RegistryAccount> response = registryAccountService.create(registryAccount);

        String tempMessage = "";
        for (Message message : response.getMessages()) {
            logger.warn("Error while Create Cloutprovider  [{}] ", message.getMessageText());
            tempMessage += ("\n Error while Create Cloutprovider  :" + message.getMessageText());
        }


        if (response.getResults() != null) {
            this.registryAccountCreated = response.getResults();
            logger.info(" Registry Account Created with Name [{}] and ID [{}]", registryAccountCreated.getName(), registryAccountCreated.getId());
        }

        assertNotNull(response);
        assertNotNull(response.isErrors());
        assertEquals(tempMessage, ((Boolean) success).toString(), ((Boolean) response.isErrors()).toString());

        if (!success) {

            assertNotNull(response.getResults());
            assertNotNull(response.getResults().getId());

            assertEquals("UserName is invalid, when compared with input UserName @ Creation Time ", registryAccount.getUsername(), registryAccountCreated.getUsername());
            assertEquals(registryAccount.getInactive(), registryAccountCreated.getInactive());
            assertEquals(registryAccount.getAccountType(), registryAccountCreated.getAccountType());
            assertEquals(registryAccount.getAccountType(), registryAccountCreated.getAccountType());
            // Password should always be empty
            assertThat("Password is not Expected in Response. ", "password-hidden", is(registryAccountCreated.getPassword()));

            // gettnig Count of objects after creating Object
            logger.info("FindAll User RegistryAccount by Id [{}]", registryAccountCreated.getId());
            this.countAfterCreate = testRegistryAccountPosition(registryAccountCreated.getId());
            assertEquals("Count of FInd all RegistryAccount between before and after create does not have diffrence of 1 for UserId :" + registryAccountCreated.getId(), countBeforeCreate, countAfterCreate - 1);


        }
    }

    @After
    public void cleanUp() {
        logger.info("cleaning up...");

        if (registryAccountCreated != null) {
            registryAccountService.delete(registryAccountCreated.getId());
        }
        logger.info("Find All RegistryAccount After Delete  User by Id {}", registryAccountCreated.getId());
        countAfterDelete = testRegistryAccountPosition(null);
        assertEquals("Count of FInd all RegistryAccount between before and after delete are not same for UserId :" + registryAccountCreated.getId(), countBeforeCreate, countAfterDelete);
    }
}
