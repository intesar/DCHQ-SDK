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
import com.dchq.schema.beans.one.container.Env;
import com.dchq.schema.beans.one.plugin.Plugin;

import com.dchq.schema.beans.one.security.EntitlementType;
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

    /**
     * Name: Not-Empty, Max_Length:Short-Text, Unique with Version
     * Version: default:1.0,
     * Description: Optional, Max_length:Large-Text
     * Script: Not-Empty, Large-Text
     * Script-Lang: default:SHELL, POWERSHELL, PERL, RUBY, PYTHON
     * License: default:EUlA, Apache License 2.0
     * Timeout: default:30, > 0, Max < ?
     * Entitlement-Type: OWNER, CUSTOM: USERS, GROUPS
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
     * InActive: Empty: false, true
     * <p/>
     * <p/>
     * Test-Cases
     * 1. Valid - name, version, script,
     */
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // Positive Test-Cases
                {"TestPlugin11", "1.1", null, "Dummy Script", "PERL", "Apache License 2.0", 30, EntitlementType.CUSTOM, true, userId2, null, new HashSet<>(Arrays.asList(new Env().withProp("prop1").withVal("val1"))), true, "General Input", false},

                // Negative Test-Cases
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

        this.plugin.setEntitlementType(entitlementType);

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

        assertThat(errors, is(equals(response.isErrors())));

        if (!response.isErrors()) {
            pluginCreated = response.getResults();
            assertNotNull(response.getResults());
            assertNotNull(response.getResults().getId());

            // Comparing objects against created Objects.
            assertEquals(plugin.getName(), pluginCreated.getName());

            // handle empty
            if (StringUtils.isEmpty(plugin.getVersion())) {
                assertEquals("1.0", pluginCreated.getVersion());
            } else {
                assertEquals(plugin.getVersion(), pluginCreated.getVersion());
            }

            assertEquals(plugin.getDescription(), pluginCreated.getDescription());

            // handle empty
            if (StringUtils.isEmpty(plugin.getLicense())) {
                assertEquals("EULA", pluginCreated.getLicense());
            } else {
                assertEquals(plugin.getLicense(), pluginCreated.getLicense());
            }

            // handle empty
            if (StringUtils.isEmpty(plugin.getTimeout())) {
                assertEquals("30", pluginCreated.getTimeout());
            } else {
                assertEquals(plugin.getTimeout(), pluginCreated.getTimeout());
            }

            assertEquals(plugin.getBaseScript(), pluginCreated.getBaseScript());
            // handle empty
            if (StringUtils.isEmpty(plugin.getScriptLang())) {
                assertEquals("SHELL", pluginCreated.getScriptLang());
            } else {
                assertEquals(plugin.getScriptLang(), pluginCreated.getScriptLang());
            }
            assertEquals(plugin.getEnvs(), pluginCreated.getEnvs());
            assertEquals(plugin.getScriptArgs(), pluginCreated.getScriptArgs());
            // TODO handle empty
            //assertEquals(plugin.getInactive(), pluginCreated.getInactive());

            assertEquals(plugin.getEntitlementType(), pluginCreated.getEntitlementType());

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
