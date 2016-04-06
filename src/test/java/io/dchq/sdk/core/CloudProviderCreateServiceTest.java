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
import com.dchq.schema.beans.one.blueprint.AccountType;
import com.dchq.schema.beans.one.blueprint.RegistryAccount;
import com.dchq.schema.beans.one.security.EntitlementType;
import com.dchq.schema.beans.one.security.Users;
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
public class CloudProviderCreateServiceTest  extends AbstractServiceTest {
    private RegistryAccountService registryAccountService;

    @org.junit.Before
    public void setUp() throws Exception {
        registryAccountService = ServiceFactory.buildRegistryAccountService(rootUrl, username, password);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                { "Rackspace US 1 testAccount","dchqinc" ,Boolean.FALSE,AccountType.RACKSPACE,"7b1fa480664b4823b72abed54ebb9b0f", false},
                { "Rackspace US 2 Empty testAccount","" ,Boolean.FALSE,AccountType.RACKSPACE,"7b1fa480664b4823b72abed54ebb9b0f", true},
                { "","dchqinc3" ,Boolean.FALSE,AccountType.RACKSPACE,"7b1fa480664b4823b72abed54ebb9b0f", true},
                { "Rackspace US with empty password ","dchqinc" ,Boolean.FALSE,AccountType.RACKSPACE,"", true}
        });
    }

    private RegistryAccount registryAccount;
    private boolean success;
    private RegistryAccount registryAccountCreated;

    public CloudProviderCreateServiceTest(String name,String rackspaceName, Boolean isActive, AccountType rackspaceType,String Password, boolean success) {
        this.registryAccount = new RegistryAccount().withName(name).withUsername(rackspaceName).withInactive(isActive).withAccountType(rackspaceType).withPassword(Password);
        this.success = success;

    }

    @org.junit.Test
    public void testCreate() throws Exception {
        logger.info("Create Registry Account with Name [{}]", registryAccount.getName());

        if(success)
        logger.info("Expecting Error while Create Registry Account with Name [{}]", registryAccount.getName());

        ResponseEntity<RegistryAccount> response = registryAccountService.create(registryAccount);

        if(response.isErrors())
            logger.warn("Message from Server... {}",response.getMessages().get(0).getMessageText());

        boolean tempSuccess=success;
        if(success && !response.isErrors()){
            success=false;
            this.registryAccountCreated = response.getResults();
        }
        assertNotNull(response);
        assertNotNull(response.isErrors());
        assertEquals(((Boolean)tempSuccess).toString(), ((Boolean)response.isErrors()).toString());

        if (!tempSuccess) {
            this.registryAccountCreated = response.getResults();
            logger.info(" Registry Account Created with Name [{}] and ID [{}]", registryAccountCreated.getName(),registryAccountCreated.getId());
            assertNotNull(response.getResults());
            assertNotNull(response.getResults().getId());




            assertEquals(registryAccount.getUsername(), registryAccountCreated.getUsername());
            assertEquals(registryAccount.isInactive(), registryAccountCreated.isInactive());
            assertEquals(registryAccount.getAccountType(), registryAccountCreated.getAccountType());
            assertEquals(registryAccount.getAccountType(), registryAccountCreated.getAccountType());

            // Password should always be empty
            assertThat("password-hidden", is(registryAccountCreated.getPassword()));

        }else if(!response.isErrors()){
            success=false;
            this.registryAccountCreated = response.getResults();
        }
    }

    @After
    public void cleanUp() {
        logger.info("cleaning up...");

        if (!success) {
            registryAccountService.delete(registryAccountCreated.getId());
        }
    }
}
