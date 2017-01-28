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

package io.dchq.sdk.core.blueprints;


import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import com.dchq.schema.beans.base.Message;
import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.base.Visibility;
import com.dchq.schema.beans.one.blueprint.Blueprint;
import com.dchq.schema.beans.one.blueprint.BlueprintType;
import com.dchq.schema.beans.one.security.EntitlementType;

import io.dchq.sdk.core.AbstractServiceTest;
import io.dchq.sdk.core.BlueprintService;
import io.dchq.sdk.core.ServiceFactory;

/**
 *
 * @author Abedeen.
 * @updater Jagdeep Jain
 * @since 1.0
 */

/**
 * Blueprint: Search
 * App & Machine Blueprint
 *
 */


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class BlueprintSearchServiceTest extends AbstractServiceTest {

    private BlueprintService blueprintService;
    private Blueprint bluePrint;
    private boolean error;
    private Blueprint bluePrintCreated;
    private String errorMessage;

    public BlueprintSearchServiceTest (
    		String blueprintName, 
    		BlueprintType blueprintType,
    		String version, 
    		String description,
    		String externalLink, 
    		Visibility visible, 
    		String yaml, 
			Map<String, String> customMap,
    		EntitlementType entitlementType,
    		boolean success
            )
    {
        this.bluePrint = new Blueprint().withName(blueprintName).withBlueprintType(blueprintType).withVersion(version).withDescription(description).withVisibility(visible).withUserName(username);
        this.bluePrint.setYml(yaml);
        this.bluePrint.setEntitlementType(entitlementType);
        this.bluePrint.setExternalLink(externalLink);
        this.bluePrint.setCustomizationsMap(customMap);
        this.error = success;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{      
				{ "App & Machines Blueprints Test", BlueprintType.DOCKER_COMPOSE, "5.0", "description",
						"https://dchq.io", Visibility.EDITABLE, "LB:\n image: nginx:latest", null,
						EntitlementType.NONE, false},
        });
    }
    
    @org.junit.Before
    public void setUp() throws Exception {
        blueprintService = ServiceFactory.buildBlueprintService(rootUrl, username, password);
    }
    
    @org.junit.Test
    public void testSearch() throws Exception {
        logger.info("Create Blueprint [{}]", bluePrint.getName());
        
        ResponseEntity<Blueprint> response = blueprintService.create(bluePrint);
        
        for (Message m : response.getMessages()) {
            logger.warn("[{}]", m.getMessageText());
        }
        
        if(response.getResults() != null){
            bluePrintCreated = response.getResults();
        }
        
        assertNotNull(response);
        assertNotNull(response.isErrors());
        if (!error) {
            assertNotNull(response.getResults());
            assertNotNull(response.getResults().getId());
            Assert.assertNotNull(bluePrint.getName(), bluePrintCreated.getName());
            Assert.assertNotNull(bluePrint.getBlueprintType().toString(), bluePrintCreated.getBlueprintType().toString());
            Assert.assertNotNull(bluePrint.getVersion(), bluePrintCreated.getVersion());
            Assert.assertNotNull(bluePrint.getVisibility().toString(), bluePrintCreated.getVisibility().toString());
            Assert.assertNotNull(bluePrint.getUserName(), bluePrintCreated.getUserName());
            ResponseEntity<List<Blueprint>> blueprintSearchResponseEntity = blueprintService.search(bluePrintCreated.getName(), 0, 1);
            for (Message message : blueprintSearchResponseEntity.getMessages()) {
                logger.warn("Error while Search request  [{}] ", message.getMessageText());
				//errorMessage += message.getMessageText() + "\n";
            }
            assertNotNull(blueprintSearchResponseEntity);
            assertNotNull(blueprintSearchResponseEntity.isErrors());
            // TODO: add tests for testing error message
            // assertFalse(errorMessage,blueprintSearchResponseEntity.isErrors());
            assertNotNull(blueprintSearchResponseEntity.getResults());
            assertEquals(1, blueprintSearchResponseEntity.getResults().size());
            Blueprint searchedEntity = blueprintSearchResponseEntity.getResults().get(0);
            logger.warn("Searching blueprint by name  [{}] ", searchedEntity.getName());
            assertEquals(bluePrintCreated.getId(), searchedEntity.getId());
            assertEquals(bluePrintCreated.getName(), searchedEntity.getName());
        }
    }

    @After
    public void cleanUp() {
    	if (bluePrintCreated != null) {
            logger.info("cleaning up...");
            ResponseEntity<?> response = blueprintService.delete(bluePrintCreated.getId());
            for (Message message : response.getMessages()) {
                logger.warn("Error blueprint deletion: [{}] ", message.getMessageText());
            }
        }
    }
}