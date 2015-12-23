package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.build.Build;
import org.junit.Assert;

import java.util.List;

/**
 * Created by atefahmed on 12/22/15.
 */
public class BuildServiceTest extends AbstractServiceTest{

    private BuildService buildService;

    @org.junit.Before
    public void setUp() throws Exception{
        buildService = ServiceFactory.buildBuildService(rootUrl, username, password);
    }

    @org.junit.Test
    public void testGet() throws Exception{
        ResponseEntity<List<Build>> responseEntity = buildService.get();
        Assert.assertNotNull(responseEntity.getTotalElements());
        for (Build bl : responseEntity.getResults()) {
            logger.info("Build repository [{}] buildType [{}] author [{}]", bl.getRepository() ,bl.getBuildType(), bl.getCreatedBy());
        }
    }
}
