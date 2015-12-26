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



//    @org.junit.Test
//    public void testFindById() throws Exception {
//        ResponseEntity<Build> responseEntity = buildService.findById("");
//        Assert.assertNotNull(responseEntity.getResults());
//        Assert.assertNotNull(responseEntity.getResults().getId());
//    }

    @org.junit.Test
    public void testGetManaged() throws Exception {
        ResponseEntity<List<Build>> responseEntity = buildService.getManaged();
        Assert.assertNotNull(responseEntity.getTotalElements());
        for (Build bl : responseEntity.getResults()) {
//            logger.info("Managed Build type [{}] name [{}] author [{}]", bl.getBuildType(), bl.getName(), bl.getCreatedBy());
        }
    }

//    @org.junit.Test
//    public void testFindManagedById() throws Exception {
//        ResponseEntity<Build> responseEntity = buildService.findManagedById("");
//        Assert.assertNotNull(responseEntity.getResults());
//        Assert.assertNotNull(responseEntity.getResults().getId());
//    }
}
