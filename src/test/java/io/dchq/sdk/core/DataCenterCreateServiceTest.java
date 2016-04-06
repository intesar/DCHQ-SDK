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
import com.dchq.schema.beans.one.provider.DataCenter;
import com.dchq.schema.beans.one.security.EntitlementType;
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
public class DataCenterCreateServiceTest extends AbstractServiceTest{

    private DataCenterService dataCenterService;

    @org.junit.Before
    public void setUp() throws Exception{
        dataCenterService = ServiceFactory.buildDataCenterService(rootUrl, username, password);
    }

    DataCenter dataCenter;
    DataCenter dataCenterCreated;
    boolean success;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Test Cluster - AA4",Boolean.FALSE,EntitlementType.ALL_BLUEPRINTS, false},
                {"Test Cluster - AA4",Boolean.TRUE,EntitlementType.ALL_BLUEPRINTS, true},
                {"",Boolean.FALSE,EntitlementType.ALL_BLUEPRINTS, true}
        });
    }
    // Create - Update - Delete

    public DataCenterCreateServiceTest(String clusterName,Boolean autoScaleFlag,EntitlementType blueprintType,boolean success) {
        this.dataCenter = new DataCenter().withName(clusterName).withAutoScale(autoScaleFlag).withBlueprintEntitlementType(blueprintType);
        this.success=success;

    }

    @org.junit.Test
    public void testCreate() throws Exception{

        // Create
        logger.info("Create Cluster with Name [{}]", dataCenter.getName());
        ResponseEntity<DataCenter> response = dataCenterService.create(dataCenter);
        if(response.isErrors())
            logger.warn("Message from Server... {}",response.getMessages().get(0).getMessageText());
        assertNotNull(response);
        assertNotNull(response.isErrors());
        assertThat(success, is(equals(response.isErrors())));

        if (!success) {

            assertNotNull(response.getResults());
            assertNotNull(response.getResults().getId());

            this.dataCenterCreated = response.getResults();

            assertEquals(dataCenter.getName(), dataCenterCreated.getName());
            assertEquals(dataCenter.getEntitledBlueprint(), dataCenterCreated.getEntitledBlueprint());
            assertEquals(dataCenter.isAutoScale(), dataCenterCreated.isAutoScale());

        }


    }
    @After
    public void cleanUp() {
        logger.info("cleaning up...");

        if (!success) {
            dataCenterService.delete(dataCenterCreated.getId());
        }
    }
}
