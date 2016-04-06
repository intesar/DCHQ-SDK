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
import com.dchq.schema.beans.one.provider.DockerServer;
import com.dchq.schema.beans.one.security.EntitlementType;
import org.junit.After;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static junit.framework.TestCase.assertNotNull;
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
public class DockerServerCreateServiceTest extends AbstractServiceTest{

    private DockerServerService dockerServerService;

    @org.junit.Before
    public void setUp() throws Exception{
        dockerServerService = ServiceFactory.buildDockerServerService(rootUrl, username, password);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Test RACKSPACE SERVER ",Boolean.FALSE,"DFW","general1-1" , "DFW/6d833af9-9e31-4d4e-a4ea-1f7f3a4d4406"  ,1,"2c918086531095990153e05c4ac30f24","RACKSPACE",false},

        });
    }
    // Create - Update - Delete

    public DockerServerCreateServiceTest(String serverName,Boolean activeFlag,String region,String hardwareID, String image,int size,String endpoint,String endpointTpe,boolean success) {
        this.dockerServer = new DockerServer().withDatacenter(getDataCenter("Test Cluster - AAA",Boolean.FALSE, EntitlementType.ALL_BLUEPRINTS)).withName(serverName)
                .withInactive(activeFlag).withRegion(region).withImageId(image).withSize(size).withEndpoint(endpoint).withEndpointType(endpointTpe).withHardwareId(hardwareID);

        this.success=success;
    }

    public DataCenter getDataCenter(String datacenterName, boolean autoScale, EntitlementType entitlementType){
        logger.info("Create Cluster with Name [{}]", datacenterName);
        this.dataCenterService = ServiceFactory.buildDataCenterService(rootUrl, username, password);
        DataCenter dt =new DataCenter().withName(datacenterName).withAutoScale(autoScale).withBlueprintEntitlementType(entitlementType);
        ResponseEntity<DataCenter> responseEntity = dataCenterService.create(dt);
        if(responseEntity.isErrors())
            logger.warn("Message from Server... {}",responseEntity.getMessages().get(0).getMessageText());
        assertEquals(false, responseEntity.isErrors());

        return responseEntity.getResults();
    }

    DockerServer dockerServer;
    DockerServer dockerServerCreated;
    DataCenterService  dataCenterService;
    boolean success;

    @org.junit.Test
    public void testCreate() throws Exception {
        logger.info("Create Machine with Name [{}]", dockerServer.getName());
        ResponseEntity<DockerServer> response = dockerServerService.create(dockerServer);

        if(response.isErrors())
            logger.warn("Message from Server... {}",response.getMessages().get(0).getMessageText());

        if  (response.getTotalElements()==null) {
            logger.info("No Response for  Machine [{}]", dockerServer.getName());
            assertNotNull(response.getTotalElements());
        }
        else if (!success ) {

            assertNotNull(response.getResults());
            assertNotNull(response.getResults().getId());

            this.dockerServerCreated = response.getResults();

            assertEquals(dockerServer.getName(), dockerServerCreated.getName());
            assertEquals(dockerServer.isInactive(), dockerServerCreated.isInactive());
            assertEquals(dockerServer.getRegion(), dockerServerCreated.getRegion());
            assertEquals(dockerServer.getImageId(), dockerServerCreated.getImageId());
            assertEquals(dockerServer.getSize(), dockerServerCreated.getSize());
            assertEquals(dockerServer.getDataCenter(), dockerServerCreated.getDataCenter());

        }


    }
    @After
    public void cleanUp() {
        logger.info("cleaning up...");

        if (!success) {
            if(dockerServerCreated!=null)
            dockerServerService.delete(dockerServerCreated.getId());
            if (dockerServer.getDataCenter()!=null)
            dataCenterService.delete(dockerServer.getDataCenter().getId());

        }
    }

}
