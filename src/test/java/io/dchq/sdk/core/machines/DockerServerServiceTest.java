package io.dchq.sdk.core.machines;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.provider.DataCenter;
import com.dchq.schema.beans.one.provider.DockerServer;
import io.dchq.sdk.core.AbstractServiceTest;
import io.dchq.sdk.core.DockerServerService;
import io.dchq.sdk.core.ServiceFactory;
import org.junit.Assert;

import java.util.List;

/**
 * Created by atefahmed on 12/22/15.
 */
public class DockerServerServiceTest extends AbstractServiceTest {

    private DockerServerService dockerServerService;

    @org.junit.Before
    public void setUp() throws Exception {
        dockerServerService = ServiceFactory.buildDockerServerService(rootUrl, username, password);
    }


    @org.junit.Test
    public void testFindAll() throws Exception {
        ResponseEntity<List<DockerServer>> responseEntity = dockerServerService.findAll();
        Assert.assertNotNull(responseEntity.getTotalElements());
        for (DockerServer bl : responseEntity.getResults()) {
            logger.info("DockerServer name [{}] author [{}]", bl.getName(), bl.getCreatedBy());
        }
    }

    @org.junit.Test
    public void testFindById() throws Exception {
        ResponseEntity<DockerServer> responseEntity = dockerServerService.findById("2c91808651a95c4d0151c416b1491a33");
        Assert.assertNotNull(responseEntity.getResults());
        Assert.assertNotNull(responseEntity.getResults().getId());
    }

    @org.junit.Test
    public void testGetManaged() throws Exception {
        ResponseEntity<List<DockerServer>> responseEntity = dockerServerService.findAllManaged();
        Assert.assertNotNull(responseEntity.getTotalElements());
        for (DockerServer bl : responseEntity.getResults()) {
            logger.info("Managed DockerServer datacenter [{}] name [{}] author [{}]", bl.getDataCenter(), bl.getName(), bl.getCreatedBy());
        }
    }

    @org.junit.Test
    public void testFindStatusById() throws Exception {
        ResponseEntity<DockerServer> responseEntity = dockerServerService.findStatusById("2c91808651a95c4d0151c416b1491a33");
        Assert.assertNotNull(responseEntity.getResults().getSysInfo().getResourceStatus());
    }

    //TODO: There is an issue with ResponseEntity
//    @org.junit.Test
//    public void testPingServerById() throws Exception {
//        ResponseEntity<DockerServer> responseEntity = dockerServerService.pingServerById("2c91808651a95c4d0151c416b1491a33");
//        Assert.assertNotNull(responseEntity.getResults());
//    }

    //TODO: There is no method in DockerServerRESTOneController.java
//    @org.junit.Test
//    public void testFindManagedById() throws Exception {
//        ResponseEntity<DockerServer> responseEntity = dockerServerService.findManagedById("2c91808651a95c4d0151c416b1491a33");
//        Assert.assertNotNull(responseEntity.getResults());
//        Assert.assertNotNull(responseEntity.getResults().getId());
//    }

    // TODO: Fix json format in DockerServerServiceImpl - Error: Cannot deserialize instance of object out of START_ARRAY token in Spring Webservice
//    @org.junit.Test
//    public void testFindMonitoredDataById() throws Exception {
//        ResponseEntity<DockerServer> responseEntity = dockerServerService.findMonitoredDataById("2c91808651a95c4d0151c416b1491a33");
//        Assert.assertNotNull(responseEntity.getResults());
//    }

    // Create - Update - Delete
    @org.junit.Test
    public void testCreate() throws Exception {

        DataCenter dc = new DataCenter();
        dc.setId("2c91808651a95c4d0151e6a6720e3e81");

        DockerServer dockerServer = new DockerServer()
                .withName("Test Docker Server - AA5")
                .withDataCenter(dc);

        // Create

        dockerServer.setInactive(Boolean.FALSE);
        dockerServer.setRegion("IAD");
        dockerServer.setImageId("IAD/5ed162cc-b4eb-4371-b24a-a0ae73376c73");
        dockerServer.setSize(1);

        ResponseEntity<DockerServer> responseEntity = dockerServerService.create(dockerServer);
        Assert.assertNotNull(responseEntity.getResults());
        Assert.assertNotNull(responseEntity.getResults().getId());

        // Update

        dockerServer = responseEntity.getResults();

        dockerServer.setName("Test Docker Server - AA6");

        responseEntity = dockerServerService.update(dockerServer);
        Assert.assertNotNull(responseEntity.getResults());
        Assert.assertNotNull(responseEntity.getResults().getName(), "Test Docker Server - AA6");

        // Delete

        responseEntity = dockerServerService.delete(dockerServer.getId());
        Assert.assertFalse(responseEntity.isErrors());
    }
}
