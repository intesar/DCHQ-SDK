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
import com.dchq.schema.beans.one.base.NameEntityBase;
import com.dchq.schema.beans.one.base.UsernameEntityBase;
import com.dchq.schema.beans.one.blueprint.AccountType;
import com.dchq.schema.beans.one.blueprint.RegistryAccount;
import com.dchq.schema.beans.one.plugin.Plugin;
import com.dchq.schema.beans.one.security.EntitlementType;
import com.dchq.schema.beans.one.security.Users;
import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static junit.framework.Assert.assertNull;
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
public class CloudProviderFindAllEntitledServiceTest extends AbstractServiceTest {
    private RegistryAccountService registryAccountService, registryAccountService2;

    @org.junit.Before
    public void setUp() throws Exception {
        registryAccountService = ServiceFactory.buildRegistryAccountService(rootUrl, username, password);
        registryAccountService2 = ServiceFactory.buildRegistryAccountService(rootUrl, username2, password2);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Rackspace US 1 testAccount", "dchqinc", Boolean.FALSE, AccountType.RACKSPACE, "7b1fa480664b4823b72abed54ebb9b0f", EntitlementType.CUSTOM, true, userId2, "General Input", false},
                {"Rackspace US 1 testAccount", "dchqinc", Boolean.FALSE, AccountType.RACKSPACE, "7b1fa480664b4823b72abed54ebb9b0f", EntitlementType.CUSTOM, false, USER_GROUP, "General Input", false},
                {"Rackspace US 1 testAccount", "dchqinc", Boolean.FALSE, AccountType.RACKSPACE, "7b1fa480664b4823b72abed54ebb9b0f", EntitlementType.OWNER, false, null, "General Input", false},
                {"Rackspace US 1 testAccount", "dchqinc", Boolean.FALSE, AccountType.RACKSPACE, "7b1fa480664b4823b72abed54ebb9b0f", EntitlementType.OWNER, false, "", "General Input", false}
        });
    }

    private RegistryAccount registryAccount;
    private boolean createError;
    private RegistryAccount registryAccountCreated;
    private String validationMssage;

    public CloudProviderFindAllEntitledServiceTest(String name, String rackspaceName, Boolean isActive, AccountType rackspaceType, String Password, EntitlementType blueprintType, boolean isEntitlementTypeUser, String entitledUserId, String validationMessage, boolean success) {
        this.registryAccount = new RegistryAccount().withName(name).withUsername(rackspaceName).withInactive(isActive).withAccountType(rackspaceType).withPassword(Password);
        if (!StringUtils.isEmpty(entitledUserId) && isEntitlementTypeUser) {
            UsernameEntityBase entitledUser = new UsernameEntityBase().withId(entitledUserId);
            List<UsernameEntityBase> entiledUsers = new ArrayList<>();
            entiledUsers.add(entitledUser);
            this.registryAccount.setEntitledUsers(entiledUsers);
        } else if (!StringUtils.isEmpty(entitledUserId)) { // assume user-group
            NameEntityBase entitledUser = new NameEntityBase().withId(entitledUserId);
            List<NameEntityBase> entiledUsers = new ArrayList<>();
            entiledUsers.add(entitledUser);
            this.registryAccount.setEntitledUserGroups(entiledUsers);
        }
        this.createError = success;
        this.validationMssage = validationMssage;
    }

    @org.junit.Test
    public void testFindAllEntitled() throws Exception {
        logger.info("Create Registry Account with Name [{}]", registryAccount.getName());

        if (createError)
            logger.info("Expecting Error while Create Registry Account with Name [{}]", registryAccount.getName());

        ResponseEntity<RegistryAccount> response = registryAccountService.create(registryAccount);


        if (response.getResults() != null) this.registryAccountCreated = response.getResults();

        if (response.isErrors())
            logger.warn("Message from Server... {}", response.getMessages().get(0).getMessageText());


        assertNotNull(response);
        assertNotNull(response.isErrors());
        assertEquals(validationMssage, ((Boolean) createError).toString(), ((Boolean) response.isErrors()).toString());

        if (!createError) {
            this.registryAccountCreated = response.getResults();
            logger.info(" Registry Account Created with Name [{}] and ID [{}]", registryAccountCreated.getName(), registryAccountCreated.getId());
            assertNotNull(response.getResults());
            assertNotNull(response.getResults().getId());


            assertEquals(registryAccount.getUsername(), registryAccountCreated.getUsername());
            assertEquals(registryAccount.getInactive(), registryAccountCreated.getInactive());
            assertEquals(registryAccount.getAccountType(), registryAccountCreated.getAccountType());
            assertEquals(registryAccount.getAccountType(), registryAccountCreated.getAccountType());

            // Password should always be empty
            assertThat("password-hidden", is(registryAccountCreated.getPassword()));

            logger.info("  Search Object  entitled in FindAll {}  ", registryAccountCreated.getId());
            ResponseEntity<List<RegistryAccount>> entitledResponse = registryAccountService2.findAll();
            String errors = "";
            for (Message message : response.getMessages())
                errors += ("Error while Find All request  " + message.getMessageText() + "\n");


            assertNotNull(entitledResponse);
            assertNotNull(entitledResponse.isErrors());
            assertThat(false, is(equals(entitledResponse.isErrors())));
            int position = 0;
            for (RegistryAccount obj : entitledResponse.getResults()) {
                position++;
                if (obj.getId().equals(registryAccountCreated.getId())) {
                    logger.info("  Object Matched in FindAll {}  at Position : {}", registryAccountCreated.getId(), position);
                    assertEquals("Recently Created Object is not at Positon 1 :" + obj.getId(), 1, position);
                }
            }


            logger.info(" Total Number of Objects :{}", entitledResponse.getResults().size());

            // valid User2 can access plugin
          /*  if (registryAccount.getEntitlementType() == EntitlementType.CUSTOM && !StringUtils.isEmpty(userId2)) {


                assertNotNull(entitledResponse.getResults());

                junit.framework.Assert.assertEquals(registryAccountCreated.getId(), entitledResponse.getResults().getId());
            } else if (registryAccount.getEntitlementType() == EntitlementType.OWNER) {
                ResponseEntity<RegistryAccount> entitledResponse = registryAccountService2.findById(registryAccountCreated.getId());
                assertNotNull(entitledResponse);
                assertNotNull(entitledResponse.isErrors());
                //Assert.assertThat(true, is(equals(entitledResponse.isErrors())));
                assertNull(entitledResponse.getResults());
            }*/


        }
    }

    @After
    public void cleanUp() {
        logger.info("cleaning up...");

        if (registryAccountCreated != null) {
            registryAccountService.delete(registryAccountCreated.getId());
        }
    }
}
