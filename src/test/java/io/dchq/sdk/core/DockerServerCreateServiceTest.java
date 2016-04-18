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
import com.dchq.schema.beans.one.blueprint.RegistryAccount;
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
import java.util.Date;
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
public class DockerServerCreateServiceTest extends AbstractServiceTest {

    private DockerServerService dockerServerService;

    @org.junit.Before
    public void setUp() throws Exception {
        dockerServerService = ServiceFactory.buildDockerServerService(rootUrl2, username, password);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Test_RACKSPACE_SERVER2 ", Boolean.FALSE, "DFW", "general1-1", "DFW/6d833af9-9e31-4d4e-a4ea-1f7f3a4d4406", 1, "2c918086531095990153e05c4ac30f24", "RACKSPACE",400000, false},

        });
    }
    // Create - Update - Delete

    public DockerServerCreateServiceTest(String serverName, Boolean activeFlag, String region, String hardwareID, String image, int size, String endpoint, String endpointTpe, int tinout,boolean success) {
        this.dockerServer = new DockerServer().withDatacenter(getDataCenter("Test_Cluster_"+(new Date().toString()), Boolean.FALSE, EntitlementType.ALL_BLUEPRINTS)).withName(serverName)
                .withInactive(activeFlag).withRegion(region).withImageId(image).withSize(size).withEndpoint(endpoint).withEndpointType(endpointTpe).withHardwareId(hardwareID);
        maxWait=tinout;


        this.success = success;
    }

    public DataCenter getDataCenter(String datacenterName, boolean autoScale, EntitlementType entitlementType) {
        logger.info("Create Cluster with Name [{}]", datacenterName);
        this.dataCenterService = ServiceFactory.buildDataCenterService(rootUrl2, username, password);
        DataCenter dt = new DataCenter().withName(datacenterName).withAutoScale(autoScale).withBlueprintEntitlementType(entitlementType);
        ResponseEntity<DataCenter> responseEntity = dataCenterService.create(dt);
        if (responseEntity.isErrors())
            logger.warn("Message from Server... {}", responseEntity.getMessages().get(0).getMessageText());
        assertEquals(false, responseEntity.isErrors());
        return responseEntity.getResults();


      //  return dataCenterService.findById("2c9180865310959901541d78a7fd3322").getResults();
    }

    DockerServer dockerServer;
    DockerServer dockerServerCreated,dockerServerProvisioning;
    DataCenterService dataCenterService;
    boolean success;
    ResponseEntity<List<DockerServer>> dockerServerResponseEntity ;
    String serverID;
    int maxWait;
    @org.junit.Test
    public void testCreate() throws Exception {
        logger.info("Create Machine with Name [{}]", dockerServer.getName());
        ResponseEntity<DockerServer> response = dockerServerService.create(dockerServer);
        String errorMessage = "";
        for (Message message : response.getMessages()) {
            logger.warn("Error while Create request  [{}] ", message.getMessageText());
        }



        if (response.getTotalElements() == null) {
            logger.info("No Response for  Machine [{}]", dockerServer.getName());

//            assertNotNull(response.getTotalElements());
            Thread.sleep(7000);
             dockerServerResponseEntity = dockerServerService.search(dockerServer.getName(), 0, 1);
            errorMessage = "";
            for (Message message : dockerServerResponseEntity.getMessages()) {
                logger.warn("Error while Create request  [{}] ", message.getMessageText());
                errorMessage += message.getMessageText() + "\n";
            }
            if( dockerServerResponseEntity.getResults()!=null){
//                assertNotNull(response.getResults());
          //      assertNotNull(response.getResults().getId());

                String serverStatus="";
                for (DockerServer searchDocker : dockerServerResponseEntity.getResults()) {
                    serverID=searchDocker.getId();
                    serverStatus= searchDocker.getDockerServerStatus()==null?"":searchDocker.getDockerServerStatus().name();
                }
int waitTime=0,warningCount=0;
                provision :do{
                    logger.info("Waiting for 10 seconds  for Docker Server to Initilize with ID [{}] ", serverID);
                    if(maxWait<waitTime)  break provision;
                    Thread.sleep(10000);
                    waitTime+=10000;
                    logger.info("Checking Serverstatus  ID [{}] ",serverID);
                    dockerServerProvisioning=dockerServerService.findById(serverID).getResults();

                    serverStatus=dockerServerProvisioning.getDockerServerStatus().name();
                    if(serverStatus=="WARNINGS" || serverStatus.equals("CONNECTED") ) warningCount++;
                    if(warningCount<5 && !serverStatus.equals("CONNECTED")) serverStatus="PROVISIONING";


                }while( serverStatus=="PROVISIONING");
                Thread.sleep(10000);
                waitTime+=10000;
                logger.info("Time Wait during Provisioning [{}] Minutes ",(waitTime/1000/60));

                assertEquals( "CONNECTED",serverStatus);
              /*  if(dockerServerProvisioning.getDockerServerStatus().name().equals("PROVISIONING"))
                    dockerServerCreated=dockerServerProvisioning;*/


            }





        }

       /* if (!success) {

            assertNotNull(dockerServerResponseEntity.getResults());
            assertNotNull(dockerServerCreated.getId());

            this.dockerServerCreated = response.getResults();

            assertEquals(dockerServer.getName(), dockerServerCreated.getName());
            assertEquals(dockerServer.isInactive(), dockerServerCreated.isInactive());
            assertEquals(dockerServer.getRegion(), dockerServerCreated.getRegion());
            assertEquals(dockerServer.getImageId(), dockerServerCreated.getImageId());
            assertEquals(dockerServer.getSize(), dockerServerCreated.getSize());
            assertEquals(dockerServer.getDataCenter(), dockerServerCreated.getDataCenter());

        }*/


    }

    @After
    public void cleanUp() {
        logger.info("cleaning up...");

        if (!success) {

            if (dockerServerProvisioning != null)
                dockerServerService.delete(dockerServerProvisioning.getId());

            //assertNotNull("Unable to Delete Object after Test ",dockerServerService.findById(dockerServerProvisioning.getId()).getResults());
            dockerServerService.delete(dockerServerProvisioning.getId());
            assertEquals("Unable to Delete Object after Test ",0,testRegistryAccountPosition(dockerServerProvisioning.getId()));
            if (dockerServer.getDataCenter() != null)
                dataCenterService.delete(dockerServer.getDataCenter().getId());

        }
    }
    public int testRegistryAccountPosition(String id) {

        ResponseEntity<List<DockerServer>> response = dockerServerService.findAll();

        String errors = "";
        for (Message message : response.getMessages())
            errors += ("Error while Find All request  " + message.getMessageText() + "\n");


        assertNotNull(response);
        assertNotNull(response.isErrors());
        assertThat(false, is(equals(response.isErrors())));
        int position=0;
        if(id!=null) {
            for (DockerServer obj : response.getResults()) {

                if(obj.getDataCenterName( ).equals("AbedeenCluster")){
                    position=1;
                  //  logger.info("  Object Matched in FindAll {}  at Position : {}", id, position);
                 //   assertEquals("Recently Created Object is not at Positon 1 :"+obj.getId(),1, position);
                }
            }
        }

        logger.info(" Total Number of Objects :{}",response.getResults().size());

        return position;
    }

}
