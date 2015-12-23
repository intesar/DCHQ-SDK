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



}
