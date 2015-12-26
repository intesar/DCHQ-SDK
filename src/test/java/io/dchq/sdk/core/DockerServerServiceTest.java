package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.provider.DockerServer;
import org.junit.Assert;

import java.util.List;

/**
 * Created by atefahmed on 12/22/15.
 */
public class DockerServerServiceTest extends AbstractServiceTest{

    private DockerServerService dockerServerService;

    @org.junit.Before
    public void setUp() throws Exception{
        dockerServerService = ServiceFactory.buildDockerServerService(rootUrl, username, password);
    }



    @org.junit.Test
    public void testGet() throws Exception {
        ResponseEntity<List<DockerServer>> responseEntity = dockerServerService.get();
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
        ResponseEntity<List<DockerServer>> responseEntity = dockerServerService.getManaged();
        Assert.assertNotNull(responseEntity.getTotalElements());
        for (DockerServer bl : responseEntity.getResults()) {
            logger.info("Managed DockerServer datacenter [{}] name [{}] author [{}]", bl.getDataCenter(), bl.getName(), bl.getCreatedBy());
        }
    }

    @org.junit.Test
    public void testFindStatusById() throws Exception {
        ResponseEntity<DockerServer> responseEntity = dockerServerService.findById("2c91808651a95c4d0151c416b1491a33");
        Assert.assertNotNull(responseEntity.getResults());
        Assert.assertNotNull(responseEntity.getResults().getId());
    }

    @org.junit.Test
    public void testPingServerById() throws Exception {
        ResponseEntity<DockerServer> responseEntity = dockerServerService.findById("2c91808651a95c4d0151c416b1491a33");
        Assert.assertNotNull(responseEntity.getResults());
        Assert.assertNotNull(responseEntity.getResults().getId());
    }

    @org.junit.Test
    public void testFindManagedById() throws Exception {
        ResponseEntity<DockerServer> responseEntity = dockerServerService.findById("2c91808651a95c4d0151c416b1491a33");
        Assert.assertNotNull(responseEntity.getResults());
        Assert.assertNotNull(responseEntity.getResults().getId());
    }

    @org.junit.Test
    public void testFindMonitoredDataById() throws Exception {
        ResponseEntity<DockerServer> responseEntity = dockerServerService.findById("2c91808651a95c4d0151c416b1491a33");
        Assert.assertNotNull(responseEntity.getResults());
        Assert.assertNotNull(responseEntity.getResults().getId());
    }
}
