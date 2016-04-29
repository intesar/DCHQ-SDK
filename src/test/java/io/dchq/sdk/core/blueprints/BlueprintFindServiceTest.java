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

import com.dchq.schema.beans.base.Message;
import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.base.Visibility;
import com.dchq.schema.beans.one.blueprint.Blueprint;
import com.dchq.schema.beans.one.blueprint.BlueprintType;
import com.dchq.schema.beans.one.security.UserGroup;
import io.dchq.sdk.core.AbstractServiceTest;
import io.dchq.sdk.core.BlueprintService;
import io.dchq.sdk.core.ServiceFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
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
public class BlueprintFindServiceTest extends AbstractServiceTest {

    private BlueprintService blueprintService;

    @org.junit.Before
    public void setUp() throws Exception {
        blueprintService = ServiceFactory.buildBlueprintService(rootUrl, username, password);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Blueprint_" + (new Date()).toString(), BlueprintType.DOCKER_COMPOSE, "2.0", "LB:\n image: nginx:latest\n", Visibility.EDITABLE,  false},

        });
    }


    private Blueprint bluePrint;
    private boolean createError;
    private Blueprint bluePrintCreated;
    private Blueprint bluePrintFind;


    public BlueprintFindServiceTest(String gname, BlueprintType blueprintTpe, String version, String yaml, Visibility visible, boolean success) {
        this.bluePrint = new Blueprint().withName(gname).withBlueprintType(blueprintTpe).withVersion(version).withVisibility(visible)
                .withUserName(username);
        this.bluePrint.setYml(yaml);


        this.createError = success;


    }

    @org.junit.Test
    public void testFind() throws Exception {

        logger.info("Create Bluepring [{}]", bluePrint.getName());
        ResponseEntity<Blueprint> response = blueprintService.create(bluePrint);

        for (Message message : response.getMessages())
            logger.warn("Error while Create request  [{}] ", message.getMessageText());

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

        for (Message message : response.getMessages())
            logger.warn("Error while Find request  [{}] ", message.getMessageText());
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
        logger.info("cleaning up...");

        if (bluePrintCreated != null) {
            blueprintService.delete(bluePrintCreated.getId());
        }
    }
}