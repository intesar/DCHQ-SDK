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
import com.dchq.schema.beans.one.container.Env;
import com.dchq.schema.beans.one.plugin.Plugin;

import com.dchq.schema.beans.one.security.EntitlementType;
import io.dchq.sdk.core.AbstractServiceTest;
import io.dchq.sdk.core.PluginService;
import io.dchq.sdk.core.ServiceFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import org.springframework.util.StringUtils;

import java.util.*;


import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * PluginService create tests
 *
 * @author Abedeen
 * @author Intesar Mohammed
 * @since 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class PluginCreateServiceTest extends AbstractServiceTest {

    private PluginService appService;

    @org.junit.Before
    public void setUp() throws Exception {
        appService = ServiceFactory.buildPluginService(rootUrl, username, password);
    }

    private Plugin plugin;
    private boolean errors;
    private Plugin pluginCreated;
    private String validityMessage;
    private Boolean isEntitlementTypeUser;

    /**
     * Name: Not-Empty, Max_Length:Short-Text, Unique with Version per owner
     * Version: default:1.0,
     * Description: Optional, Max_length:Large-Text
     * Script: Not-Empty, Large-Text
     * Script-Lang: default:SHELL, POWERSHELL, PERL, RUBY, PYTHON
     * License: default:EUlA, Apache License 2.0
     * Timeout: default:30, > 0, Max < ?
     * Entitlement-Type: default:OWNER, CUSTOM: USERS, GROUPS
     * Entitled-Users:
     * Valid user_id
     * Entitled-Groups
     * Valid group_id
     * Arguments: Optional
     * ScriptArgs: Optional
     * ENV: Optional
     * prop:  Not-Empty
     * val: Not-Empty
     * eVal: value should be ignored
     * hidden: default: false, true
     * InActive: default: false, true
     * <p/>
     * <p/>
     * Test-Cases
     * 1. Valid - name, version, script,
     */
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // Positive Test-Cases
                // Script-Lang
                {"TestPlugin11", "1.3", "Description", "Dummy Script", "SHELL", "Apache License 2.0", 30, EntitlementType.CUSTOM, true, userId2, null, new HashSet<>(Arrays.asList(new Env().withProp("prop1").withVal("val1"))), true, "General Input", false},
                {"TestPlugin11", "1.3", "Description", "Dummy Script", "PERL", "Apache License 2.0", 30, EntitlementType.CUSTOM, true, userId2, null, new HashSet<>(Arrays.asList(new Env().withProp("prop1").withVal("val1"))), true, "General Input", false},
                {"TestPlugin11", "1.3", "Description", "Dummy Script", "POWERSHELL", "Apache License 2.0", 30, EntitlementType.CUSTOM, true, userId2, null, new HashSet<>(Arrays.asList(new Env().withProp("prop1").withVal("val1"))), true, "General Input", false},
                {"TestPlugin11", "1.3", "Description", "Dummy Script", "RUBY", "Apache License 2.0", 30, EntitlementType.CUSTOM, true, userId2, null, new HashSet<>(Arrays.asList(new Env().withProp("prop1").withVal("val1"))), true, "General Input", false},
                {"TestPlugin11", "1.3", "Description", "Dummy Script", "PYTHON", "Apache License 2.0", 30, EntitlementType.CUSTOM, true, userId2, null, new HashSet<>(Arrays.asList(new Env().withProp("prop1").withVal("val1"))), true, "General Input", false},
                {"TestPlugin11", "1.3", "", "Dummy Script", "PYTHON", "Apache License 2.0", 30, EntitlementType.CUSTOM, true, userId2, null, new HashSet<>(Arrays.asList(new Env().withProp("prop1").withVal("val1"))), true, "General Input", false},
                {"TestPlugin11", "1.3", null, "Dummy Script", "PYTHON", "Apache License 2.0", 30, EntitlementType.CUSTOM, true, userId2, null, new HashSet<>(Arrays.asList(new Env().withProp("prop1").withVal("val1"))), true, "General Input", false},
                {"TestPlugin11", "", "", "Dummy Script", "PYTHON", "Apache License 2.0", 30, EntitlementType.CUSTOM, true, userId2, null, new HashSet<>(Arrays.asList(new Env().withProp("prop1").withVal("val1"))), true, "General Input", false},
                {"TestPlugin11", null, "", "Dummy Script", "PYTHON", "Apache License 2.0", 30, EntitlementType.CUSTOM, true, userId2, null, new HashSet<>(Arrays.asList(new Env().withProp("prop1").withVal("val1"))), true, "General Input", false},
                {"TestPlugin11", null, "", "Dummy Script", "", "Apache License 2.0", 30, EntitlementType.CUSTOM, true, userId2, null, new HashSet<>(Arrays.asList(new Env().withProp("prop1").withVal("val1"))), true, "General Input", false},
                {"TestPlugin11", null, "", "Dummy Script", null, "Apache License 2.0", 30, EntitlementType.CUSTOM, true, userId2, null, new HashSet<>(Arrays.asList(new Env().withProp("prop1").withVal("val1"))), true, "General Input", false},
                {"TestPlugin11", null, "", "Dummy Script", null, "", 30, EntitlementType.CUSTOM, true, userId2, null, new HashSet<>(Arrays.asList(new Env().withProp("prop1").withVal("val1"))), true, "General Input", false},
                {"TestPlugin11", null, "", "Dummy Script", null, null, 30, EntitlementType.CUSTOM, true, userId2, null, new HashSet<>(Arrays.asList(new Env().withProp("prop1").withVal("val1"))), true, "General Input", false},
                {"TestPlugin11", null, "", "Dummy Script", null, null, 30, EntitlementType.CUSTOM, true, userId2, null, null, true, "General Input", false},
                {"TestPlugin11", null, "", "Dummy Script", null, null, 30, EntitlementType.OWNER, true, userId2, null, null, true, "General Input", false},
                // License
                {"TestPlugin11", "1.3", "Description", "Dummy Script", "SHELL", "EULA", 30, EntitlementType.CUSTOM, true, userId2, null, new HashSet<>(Arrays.asList(new Env().withProp("prop1").withVal("val1"))), true, "General Input", false},
                // Negative Test-Cases
                // name
                {"", "1.2", "Description", "Dummy Script", "SHELL", "Apache License 2.0", 30, EntitlementType.CUSTOM, true, userId2, null, new HashSet<>(Arrays.asList(new Env().withProp("prop1").withVal("val1"))), true, "General Input", true},
                {null, "1.2", "Description", "Dummy Script", "SHELL", "Apache License 2.0", 30, EntitlementType.CUSTOM, true, userId2, null, new HashSet<>(Arrays.asList(new Env().withProp("prop1").withVal("val1"))), true, "General Input", true},
                // script-lang
                {"TestPlugin11", "1.2", "Description", "Dummy Script", "invalid", "Apache License 2.0", 30, EntitlementType.CUSTOM, true, userId2, null, new HashSet<>(Arrays.asList(new Env().withProp("prop1").withVal("val1"))), true, "General Input", true},
                {"TestPlugin11", "1.2", "Description", "Dummy Script", "shell", "Apache License 2.0", 30, EntitlementType.CUSTOM, true, userId2, null, new HashSet<>(Arrays.asList(new Env().withProp("prop1").withVal("val1"))), true, "General Input", true},
                {"TestPlugin11", "1.2", "Description", "Dummy Script", "powershell", "Apache License 2.0", 30, EntitlementType.CUSTOM, true, userId2, null, new HashSet<>(Arrays.asList(new Env().withProp("prop1").withVal("val1"))), true, "General Input", true},
                {"TestPlugin11", "1.2", "Description", "Dummy Script", "perl", "Apache License 2.0", 30, EntitlementType.CUSTOM, true, userId2, null, new HashSet<>(Arrays.asList(new Env().withProp("prop1").withVal("val1"))), true, "General Input", true},
                {"TestPlugin11", "1.2", "Description", "Dummy Script", "ruby", "Apache License 2.0", 30, EntitlementType.CUSTOM, true, userId2, null, new HashSet<>(Arrays.asList(new Env().withProp("prop1").withVal("val1"))), true, "General Input", true},
                {"TestPlugin11", "1.2", "Description", "Dummy Script", "python", "Apache License 2.0", 30, EntitlementType.CUSTOM, true, userId2, null, new HashSet<>(Arrays.asList(new Env().withProp("prop1").withVal("val1"))), true, "General Input", true},

                // script
                {"TestPlugin11", "1.2", "Description", "", "SHELL", "Apache License 2.0", 30, EntitlementType.CUSTOM, true, userId2, null, new HashSet<>(Arrays.asList(new Env().withProp("prop1").withVal("val1"))), true, "General Input", true},
                {"TestPlugin11", "1.2", "Description", null, "SHELL", "Apache License 2.0", 30, EntitlementType.CUSTOM, true, userId2, null, new HashSet<>(Arrays.asList(new Env().withProp("prop1").withVal("val1"))), true, "General Input", true},
        });
    }

    public PluginCreateServiceTest(String pluginName, String version, String description, String pluginScript, String scriptType, String license,
                                   Integer timeout, EntitlementType entitlementType, boolean isEntitlementTypeUser, String entitledUserId,
                                   String scriptArgs, Set<Env> envs, Boolean inactive,
                                   String validityMessage, boolean errors) {
        this.plugin = new Plugin();
        this.plugin.setName(pluginName);
        this.plugin.setVersion(version);
        this.plugin.setDescription(description);

        this.plugin.setBaseScript(pluginScript);
        this.plugin.setScriptLang(scriptType);

        this.plugin.setLicense(license);
        this.plugin.setTimeout(timeout);

        this.plugin.setEnvs(envs);
        this.plugin.setScriptArgs(scriptArgs);

        this.isEntitlementTypeUser = isEntitlementTypeUser;
        this.plugin.setEntitlementType(entitlementType);
        if (EntitlementType.CUSTOM == entitlementType && isEntitlementTypeUser) {
            this.plugin.setEntitledUsers(new ArrayList<UsernameEntityBase>(Arrays.asList(new UsernameEntityBase().withId(entitledUserId))));
        } else if (EntitlementType.CUSTOM == entitlementType && !isEntitlementTypeUser) {
            this.plugin.setEntitledUserGroups(new ArrayList<NameEntityBase>(Arrays.asList(new NameEntityBase().withId(entitledUserId))));
        }

        this.plugin.setInactive(inactive);


        this.errors = errors;
        this.validityMessage = validityMessage;

    }

    @org.junit.Test
    public void testCreate() throws Exception {

        logger.info("Creating Plugin with name [{}]", this.plugin.getName());
        ResponseEntity<Plugin> response = appService.create(plugin);

        if (response.isErrors()) {
            for (Message m : response.getMessages())
                logger.warn("[{}]", m.getMessageText());
        }

        assertNotNull(response);
        assertNotNull(response.isErrors());

        assertEquals(errors, response.isErrors());

        if (!response.isErrors()) {
            pluginCreated = response.getResults();
            assertNotNull(response.getResults());
            assertNotNull(response.getResults().getId());

            // name
            assertEquals(plugin.getName(), pluginCreated.getName());

            // version
            if (StringUtils.isEmpty(plugin.getVersion())) {
                assertEquals("1.0", pluginCreated.getVersion());
            } else {
                assertEquals(plugin.getVersion(), pluginCreated.getVersion());
            }

            assertEquals(plugin.getDescription(), pluginCreated.getDescription());
            assertEquals(plugin.getBaseScript(), pluginCreated.getBaseScript());

            // license
            if (StringUtils.isEmpty(plugin.getLicense())) {
                assertEquals("EULA", pluginCreated.getLicense());
            } else {
                assertEquals(plugin.getLicense(), pluginCreated.getLicense());
            }

            // timeout
            if (StringUtils.isEmpty(plugin.getTimeout())) {
                assertEquals("30", pluginCreated.getTimeout());
            } else {
                assertEquals(plugin.getTimeout(), pluginCreated.getTimeout());
            }

            assertEquals(plugin.getBaseScript(), pluginCreated.getBaseScript());
            // script-lang
            if (StringUtils.isEmpty(plugin.getScriptLang())) {
                assertEquals("SHELL", pluginCreated.getScriptLang());
            } else {
                assertEquals(plugin.getScriptLang(), pluginCreated.getScriptLang());
            }
            assertEquals(plugin.getEnvs(), pluginCreated.getEnvs());
            assertEquals(plugin.getScriptArgs(), pluginCreated.getScriptArgs());

            assertEquals(plugin.getEntitlementType(), pluginCreated.getEntitlementType());

            assertEquals(plugin.getInactive(), pluginCreated.getInactive());

            assertEquals(plugin.getEntitlementType(), pluginCreated.getEntitlementType());
            if (EntitlementType.CUSTOM == plugin.getEntitlementType() && isEntitlementTypeUser) {
                assertEquals(plugin.getEntitledUsers(), pluginCreated.getEntitledUsers());
            } else if (EntitlementType.CUSTOM == plugin.getEntitlementType() && !isEntitlementTypeUser) {
                assertEquals(plugin.getEntitledUserGroups(), pluginCreated.getEntitledUserGroups());
            }


        }

    }

    @After
    public void cleanUp() {
        logger.info("cleaning up...");

        if (pluginCreated != null) {
            appService.delete(pluginCreated.getId());
        }
    }
}
