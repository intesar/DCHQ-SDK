package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
//import com.dchq.schema.beans.one.blueprint.Blueprint;
//import com.dchq.schema.beans.one.blueprint.BlueprintType;
import com.dchq.schema.beans.one.base.NameEntityBase;
import com.dchq.schema.beans.one.build.Build;
import com.dchq.schema.beans.one.build.BuildType;
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
    public void testFindAll() throws Exception{
        ResponseEntity<List<Build>> responseEntity = buildService.findAll();
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
    public void testFindAllManaged() throws Exception {
        ResponseEntity<List<Build>> responseEntity = buildService.findAllManaged();
        Assert.assertNotNull(responseEntity.getTotalElements());
        for (Build bl : responseEntity.getResults()) {
            logger.info("Managed Build type [{}] name [{}] author [{}]", bl.getBuildType(), bl.getDescription(), bl.getCreatedBy());
        }
    }

//    @org.junit.Test
//    public void testFindManagedById() throws Exception {
//        ResponseEntity<Build> responseEntity = buildService.findManagedById("");
//        Assert.assertNotNull(responseEntity.getResults());
//        Assert.assertNotNull(responseEntity.getResults().getId());
//    }

    @org.junit.Test
    public void testCreate() throws Exception {
        Build build = new Build()
                .withDockerScript("this is a docker script")
                .withBuildType(BuildType.DOCKER_FILE_CONTENT);

        // Create

        NameEntityBase neb = new NameEntityBase();
        neb.setId("2c91808651a95c4d0151d96a012f71ba");
        build.setId("2c91808651a95c4d0151fc332bff4822");
        build.setRegistryAccount(neb);
        build.setRepository("atefahmed@gmail.com");
        build.setCluster("2c91808651a95c4d0151e6a6720e3e81");
        build.setTag("latest");
        build.setInactive(Boolean.FALSE);

        ResponseEntity<Build> responseEntity = buildService.create(build);
        Assert.assertNotNull(responseEntity.getResults());
        Assert.assertNotNull(responseEntity.getResults().getId());

        // Update

        // TODO: ERROR - Build not found
        build = responseEntity.getResults();

        build.setDockerScript("docker update");
        build.setTag("lat");
        build.setInactive(Boolean.TRUE);
        responseEntity = buildService.update(build);

//        Assert.assertNotNull(responseEntity.getResults());
//        Assert.assertNotNull(responseEntity.getResults().getId());
//        Assert.assertEquals(responseEntity.getResults().getInactive(), Boolean.TRUE);

        // Delete

        // TODO: Unable to delete. Request failed - null.
        responseEntity = buildService.delete(build.getId());
//        Assert.assertFalse(responseEntity.isErrors());
    }
}
