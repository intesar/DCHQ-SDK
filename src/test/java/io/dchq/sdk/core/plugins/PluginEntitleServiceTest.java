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
package io.dchq.sdk.core.plugins;

import com.dchq.schema.beans.base.Message;
import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.base.NameEntityBase;
import com.dchq.schema.beans.one.base.UsernameEntityBase;
import com.dchq.schema.beans.one.plugin.Plugin;
import com.dchq.schema.beans.one.security.EntitlementType;
import io.dchq.sdk.core.AbstractServiceTest;
import io.dchq.sdk.core.PluginService;
import io.dchq.sdk.core.ServiceFactory;
import org.apache.commons.lang3.RandomStringUtils;
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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.core.Is.is;

/**
 * @author Intesar Mohammed
 * @since 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class PluginEntitleServiceTest extends AbstractServiceTest {

    private PluginService pluginService;
    private PluginService pluginService2;
    private String messageText;

    @org.junit.Before
    public void setUp() throws Exception {
        pluginService = ServiceFactory.buildPluginService(rootUrl, username, password);
        pluginService2 = ServiceFactory.buildPluginService(rootUrl, username2, password2);
    }

    private Plugin plugin;
    private boolean errors;
    private Plugin pluginCreated;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"TestPlugin1111", "1.1", "Dummy Script", "PERL", "Apache License 2.0", EntitlementType.CUSTOM, true, userId2, false},
         /**       {"TestPlugin11111", "1.1", "Dummy Script", "PERL", "Apache License 2.0", EntitlementType.CUSTOM, false, USER_GROUP, false},
                {"TestPlugin2", "1.1", "Dummy Script", "PERL", "EULA", EntitlementType.OWNER, false, null, false},
                {"TestPlugin3", "1.1", "Dummy Script", "PERL", "EULA", EntitlementType.OWNER, false, "", false}
         **/
        });
    }

    public PluginEntitleServiceTest(String pluginName, String version, String pluginScript, String scriptType, String license,
                                    EntitlementType entitlementType, boolean isEntitlementTypeUser, String entitledUserId, boolean errors) {

        // random pluginname
        String prefix = RandomStringUtils.randomAlphabetic(3);
        pluginName = prefix + "-" + pluginName;

        this.plugin = new Plugin();
        this.plugin.setName(pluginName);
        this.plugin.setVersion(version);
        this.plugin.setBaseScript(pluginScript);
        this.plugin.setScriptLang(scriptType);
        this.plugin.setLicense(license);

        this.plugin.setEntitlementType(entitlementType);


        if (!StringUtils.isEmpty(entitledUserId) && isEntitlementTypeUser) {
            UsernameEntityBase entitledUser = new UsernameEntityBase().withId(entitledUserId);
            List<UsernameEntityBase> entiledUsers = new ArrayList<>();
            entiledUsers.add(entitledUser);
            this.plugin.setEntitledUsers(entiledUsers);
        } else if (!StringUtils.isEmpty(entitledUserId)) { // assume user-group
            NameEntityBase entitledUser = new NameEntityBase().withId(entitledUserId);
            List<NameEntityBase> entiledUsers = new ArrayList<>();
            entiledUsers.add(entitledUser);
            this.plugin.setEntitledUserGroups(entiledUsers);
        }

        this.errors = errors;

    }

    @org.junit.Test
    /**
     * 1. User1 creates a Plugin
     * 2. Shares with User2
     * 3. Validate User2 can access the plugins
     */
    public void testEntitle() throws Exception {

        logger.info("Creating plugins [{}]", this.plugin.getName());
        ResponseEntity<Plugin> response = pluginService.create(plugin);

        assertNotNull(response);
        assertNotNull(response.isErrors());

        if (response.isErrors()) {
            for (Message m : response.getMessages()) {
                logger.warn("[{}]", m.getMessageText());
                messageText = m.getMessageText();
            }
            //check for errors
            Assert.assertEquals(messageText ,errors, response.isErrors());
        }

        pluginCreated = response.getResults();
        assertNotNull(response.getResults());
        assertNotNull(response.getResults().getId());
        Assert.assertNotNull(plugin.getName(), pluginCreated.getName());

        // valid User2 can access plugins
        if (plugin.getEntitlementType() == EntitlementType.CUSTOM && !StringUtils.isEmpty(userId2)) {
            ResponseEntity<Plugin> entitledResponse = pluginService2.findById(pluginCreated.getId());

            assertNotNull(entitledResponse);
            assertNotNull(entitledResponse.isErrors());
            Assert.assertThat(false, is(equals(entitledResponse.isErrors())));

            assertNotNull(entitledResponse.getResults());

            assertEquals(pluginCreated.getId(), entitledResponse.getResults().getId());
        } else if (plugin.getEntitlementType() == EntitlementType.OWNER) {
            ResponseEntity<Plugin> entitledResponse = pluginService2.findById(pluginCreated.getId());
            assertNotNull(entitledResponse);
            assertNotNull(entitledResponse.isErrors());
            //Assert.assertThat(true, is(equals(entitledResponse.isErrors())));
            assertNull(entitledResponse.getResults());
        }


    }

    @After
    public void cleanUp() {
        logger.info("cleaning up...");

        if (pluginCreated != null) {
            ResponseEntity<Plugin> deleteResponse  =   pluginService.delete(pluginCreated.getId());

            for (Message m : deleteResponse.getMessages()){
                logger.warn("[{}]", m.getMessageText());
                messageText = m.getMessageText();}

            //check for errors
            Assert.assertFalse(messageText ,deleteResponse.isErrors());
        }
    }
}
