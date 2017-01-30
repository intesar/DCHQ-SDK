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
import com.dchq.schema.beans.one.plugin.Plugin;

import com.dchq.schema.beans.one.security.EntitlementType;
import com.dchq.schema.beans.one.security.Users;
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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;


import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by Abedeen on 04/05/16.
 */

/**
 * Abstracts class for holding test credentials.
 *
 * @author Abedeen.
 * @contributor Saurabh B.
 * @since 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class PluginSearchServiceTest extends AbstractServiceTest {

    private PluginService appService;

    @org.junit.Before
    public void setUp() throws Exception {
        appService = ServiceFactory.buildPluginService(rootUrl, username, password);
    }

    private Plugin plugin;
    private boolean createError;
    private Plugin pluginCreated;
    private String errorMessage;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"TestPluginSe", "1.1", "Dummy Script", "PERL", "Apache License 2.0", EntitlementType.CUSTOM, true, userId2, false},
        });
    }

    public PluginSearchServiceTest(String pluginName, String version, String pluginScript, String scriptType, String license,
                                   EntitlementType entitlementType, boolean isEntitlementTypeUser, String entitledUserId, boolean errors) {
        // random pluginname
        String prefix = RandomStringUtils.randomAlphabetic(3);
        pluginName = prefix + pluginName;

        this.plugin = new Plugin();
        this.plugin.setName(pluginName);
        this.plugin.setVersion(version);
        this.plugin.setBaseScript(pluginScript);
        this.plugin.setScriptLang(scriptType);
        this.plugin.setLicense(license);
        this.plugin.setEntitlementType(entitlementType);
        this.createError = errors;


    }

    @org.junit.Test
    public void testSearch() throws Exception {

        logger.info("Create Object with Name [{}]", this.plugin.getName());
        ResponseEntity<Plugin> response = appService.create(plugin);


        for (Message m : response.getMessages()) {
            logger.warn("Message from Server...{} ", m.getMessageText());
            errorMessage = m.getMessageText();
        }

        //Through Error message if error boolean value doesn't match.
        Assert.assertEquals(errorMessage,createError , response.isErrors());

        if(response.getResults() != null){
            pluginCreated = response.getResults();
            logger.info("Created Object Successfully with Name [{}]", this.plugin.getName());
        }

        assertNotNull(response);
        assertNotNull(response.isErrors());
        assertEquals(errorMessage,((Boolean) createError).toString(), ((Boolean) response.isErrors()).toString());

        if (!createError) {

            assertNotNull(response.getResults());
            assertNotNull(response.getResults().getId());

            // Comparing objects agains created Objects.
            Assert.assertNotNull(plugin.getName(), pluginCreated.getName());
            Assert.assertNotNull(plugin.getName(),pluginCreated.getName());
            Assert.assertNotNull(plugin.getVersion(),pluginCreated.getVersion());
            Assert.assertNotNull(plugin.getBaseScript(),pluginCreated.getBaseScript());
            Assert.assertNotNull(plugin.getScriptLang(),pluginCreated.getScriptLang());
            Assert.assertNotNull(plugin.getLicense(),pluginCreated.getLicense());
            Assert.assertNotNull(plugin.getEntitlementType().toString(),pluginCreated.getEntitlementType().toString());

            logger.warn("Search Object wth username  [{}] ",pluginCreated.getName());

            ResponseEntity<List<Plugin>> pluginSearchResponseEntity = appService.search(pluginCreated.getName(), 0, 1);
             errorMessage="";
            for (Message message : pluginSearchResponseEntity.getMessages()) {
                logger.warn("Error while Create request  [{}] ", message.getMessageText());
                errorMessage+=message.getMessageText()+"\n";
            }

            assertNotNull(pluginSearchResponseEntity);
            assertNotNull(pluginSearchResponseEntity.isErrors());
            assertFalse(errorMessage,pluginSearchResponseEntity.isErrors());

            assertNotNull(pluginSearchResponseEntity.getResults());
            Assert.assertEquals(1, pluginSearchResponseEntity.getResults().size());

            Plugin searchedEntity = pluginSearchResponseEntity.getResults().get(0);
            Assert.assertEquals(pluginCreated.getId(), searchedEntity.getId());
     //       Assert.assertEquals(pluginCreated.getName(), searchedEntity.getName());

        }

    }

   @After
    public void cleanUp() {
        logger.info("cleaning up...");

        if (pluginCreated != null) {
            ResponseEntity<Plugin> deleteResponse  =  appService.delete(pluginCreated.getId());
            logger.info("Deleted Object Successfully with ID [{}]", this.pluginCreated.getId());

            for (Message m : deleteResponse.getMessages()){
                logger.warn("[{}]", m.getMessageText());
                errorMessage = m.getMessageText();}
            //Check there is no error while deleting
            Assert.assertFalse(errorMessage ,deleteResponse.isErrors());
        }
    }
}
