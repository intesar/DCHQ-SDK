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
import com.dchq.schema.beans.one.security.Profile;
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
 * @contributor Saurabh B.
 * @since 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class PluginFindAllServiceTest extends AbstractServiceTest {

    private PluginService appService;

    @org.junit.Before
    public void setUp() throws Exception {
        appService = ServiceFactory.buildPluginService(rootUrl, username, password);
    }

    private Plugin plugin;
    private boolean success;
    private Plugin pluginCreated;
    private int countBeforeCreate=0,countAfterCreate=0,countAfterDelete=0;
    private String messageText;


    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"TestPlugin11", "1.1", "Dummy Script", "PERL", "Apache License 2.0", EntitlementType.CUSTOM, true, userId2, false},
        });
    }

    public PluginFindAllServiceTest(String pluginName, String version, String pluginScript, String scriptType, String license,
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
        this.success = errors;

    }
    public int testPluginPosition(String id) {

        ResponseEntity<List<Plugin>> response = appService.findAll(0, 100);

        assertNotNull(response);
        assertNotNull(response.isErrors());
        for (Message m : response.getMessages()) {
            logger.warn("[{Error while find all request}]", m.getMessageText());
            messageText = m.getMessageText();
        }
        //Check there is no error while deleting
        Assert.assertFalse(messageText, response.isErrors());

        int position=0;
        if(id!=null) {
            for (Plugin obj : response.getResults()) {
                position++;
                if(obj.getId().equals(id) ){
                    logger.info("  Object Matched in FindAll {}  at Position : {}", id, position);
                    Assert.assertEquals("Recently Created Object is not at Positon 1 :"+obj.getId(),1, position);
                }
            }
        }
        logger.info(" Total Number of Objects :{}",response.getResults().size());
        return response.getResults().size();
    }
    @org.junit.Test
    public void testFindAll() throws Exception {
        // getting Count of profile before Create
        countBeforeCreate=testPluginPosition(null);
        logger.info("Create Object with  Name [{}]", this.plugin.getName());
        ResponseEntity<Plugin> response = appService.create(plugin);

        for (Message message : response.getMessages()){
            logger.warn("Error while Create request  [{}] ", message.getMessageText());
            messageText = message.getMessageText();}

        // check response is not null
        // check response has no errors
        // check response has user entity with ID
        // check all data send

        assertNotNull(response);
        assertNotNull(response.isErrors());
        assertEquals(messageText, success, response.isErrors());

        if (!response.isErrors() && response.getResults() != null) {
            pluginCreated = response.getResults();
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
            // Find the Created Profile.
            logger.info("FindAll Object Position by Id [{}]", pluginCreated.getId());
            this.countAfterCreate = testPluginPosition(pluginCreated.getId());
            Assert.assertEquals("Count of FInd all Object between before and after create does not have diffrence of 1 for UserId :"+pluginCreated.getId(),countBeforeCreate, countAfterCreate-1);

        }

    }

    @After
    public void cleanUp() {
        logger.info("cleaning up...");

        if (pluginCreated != null) {
            ResponseEntity<Plugin> deleteResponse = appService.delete(pluginCreated.getId());
            logger.info("Deleted Object Successfully with ID [{}]", this.pluginCreated.getId());

            for (Message m : deleteResponse.getMessages()) {
                logger.warn("[{}]", m.getMessageText());
                messageText = m.getMessageText();
            }
            //Check there is no error while deleting
            Assert.assertFalse(messageText, deleteResponse.isErrors());
        }
    }
}
