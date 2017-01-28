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

import java.util.Arrays;
import java.util.Collection;
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
 * @author Abedeen.
 * @updater Jagdeep Jain
 * @since 1.0
 */

/**
 * Blueprint: Find
 * App & Machine Blueprint
 *
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class BlueprintFindServiceTest extends AbstractServiceTest {

    private BlueprintService blueprintService;
    private boolean success;
    private Blueprint bluePrintCreated;
    private Blueprint bluePrintFind;
    private Blueprint bluePrint;

    public BlueprintFindServiceTest (
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
        this.success = success;
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
    public void testFind() throws Exception {
        logger.info("Create Bluepring [{}]", bluePrint.getName());
        ResponseEntity<Blueprint> response = blueprintService.create(bluePrint);
        
        for (Message message : response.getMessages()) {
            logger.warn("Error [{}] ", message.getMessageText());
        }
        
        assertNotNull(response);
        assertNotNull(response.isErrors());
        
        if (!response.isErrors() && response.getResults() != null) {
            bluePrintCreated = response.getResults();
            logger.info("Create Blueprint completed Successfully [{}]", bluePrintCreated.getName());
            assertNotNull(response.getResults());
            assertNotNull(response.getResults().getId());
            Assert.assertNotNull(bluePrint.getName(), bluePrintCreated.getName());
            Assert.assertNotNull(bluePrint.getBlueprintType().toString(), bluePrintCreated.getBlueprintType().toString());
            Assert.assertNotNull(bluePrint.getVersion(), bluePrintCreated.getVersion());
            Assert.assertNotNull(bluePrint.getVisibility().toString(), bluePrintCreated.getVisibility().toString());
            Assert.assertNotNull(bluePrint.getUserName(), bluePrintCreated.getUserName());
        }
        
        logger.info("Find Blueprint for ID {} ",bluePrintCreated.getId());
        response = blueprintService.findById(bluePrintCreated.getId());
        
        for (Message message : response.getMessages()) {
            logger.warn("Error while Find request  [{}] ", message.getMessageText());
        }
        
        Assert.assertNotNull(((Boolean) false).toString(), ((Boolean) response.isErrors()).toString());
        assertNotNull(response);
        assertNotNull(response.isErrors());
        
        if (!response.isErrors() && response.getResults() != null) {
            bluePrintFind = response.getResults();
            logger.info("Find Blueprint completed Successfully [{}]", bluePrintCreated.getId());
            assertNotNull(response.getResults());
            assertNotNull(response.getResults().getId());
            Assert.assertNotNull(bluePrintCreated.getId(), bluePrintFind.getId());
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