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
    String validationMessage;
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Test Cluster - AA4",Boolean.FALSE,EntitlementType.ALL_BLUEPRINTS, "\nAll Input Values are normal. Malfunction in SDK",false},
                {"Test Cluster - AA40",Boolean.TRUE,EntitlementType.ALL_BLUEPRINTS,"\n Auto Flag is set to true ,\n Next required value :Machine Compose id cannot be empty", true},
                {"",Boolean.FALSE,EntitlementType.ALL_BLUEPRINTS,"\n Empty Cluster Name is Not Valid", true}
        });
    }
    // Create - Update - Delete

    public DataCenterCreateServiceTest(String clusterName,Boolean autoScaleFlag,EntitlementType blueprintType,String validationMessage,boolean success) {
        this.dataCenter = new DataCenter().withName(clusterName).withAutoScale(autoScaleFlag).withBlueprintEntitlementType(blueprintType);
        this.success=success;
        this.validationMessage=validationMessage;

    }

    @org.junit.Test
    public void testCreate() throws Exception{

        logger.info("Create Cluster with Name [{}]", dataCenter.getName());
        if(success)
            logger.info("Input for create Cluster is Expected to generate Error: [{}]", validationMessage);

        ResponseEntity<DataCenter> response = dataCenterService.create(dataCenter);
        for (Message message  : response.getMessages())
            logger.warn("Error while Create request  [{}] ",message.getMessageText());

        if(response.getResults()!=null)   this.dataCenterCreated = response.getResults();

        assertNotNull(response);
        assertNotNull(response.isErrors());
        assertEquals(validationMessage,((Boolean)success).toString(),((Boolean)response.isErrors()).toString());

        //if(!response.isErrors()  )

        if (!success) {

            assertNotNull(response.getResults());
            assertNotNull(response.getResults().getId());

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
