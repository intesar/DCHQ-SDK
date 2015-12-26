package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.provision.App;
import org.junit.Assert;

import java.util.List;

/**
 * Created by atefahmed on 12/23/15.
 */
public class AppServiceTest extends AbstractServiceTest {

    private AppService appService;

    @org.junit.Before
    public void setUp() throws Exception{
        appService = ServiceFactory.buildAppService(rootUrl, username, password);
    }

    //TODO: An exception is thrown, even on dchq.readme.io
//    @org.junit.Test
//    public void testGet() throws Exception {
//        ResponseEntity<List<App>> responseEntity = appService.get();
//        Assert.assertNotNull(responseEntity.getResults());
//
//        for (App bl : responseEntity.getResults()) {
//            logger.info("Application name [{}] author [{}]", bl.getName(), bl.getCreatedBy());
//        }
//    }


    @org.junit.Test
    public void testFindById() throws Exception {
        ResponseEntity<App> responseEntity = appService.findById("2c91808651a95c4d0151c413f13c1a26");
        Assert.assertNotNull(responseEntity.getResults());
        Assert.assertNotNull(responseEntity.getResults().getId());
    }

    @org.junit.Test
    public void testFindActive() throws Exception {
        ResponseEntity<List<App>> responseEntity = appService.findActive();
        Assert.assertNotNull(responseEntity.getResults());
    }


    @org.junit.Test
    public void testFindDestroyed() throws Exception {
        ResponseEntity<List<App>> responseEntity = appService.findDestroyed();
        Assert.assertNotNull(responseEntity.getResults());
    }

    //TODO: An exception is thrown
//    @org.junit.Test
//    public void testFindDeployed() throws Exception {
//        ResponseEntity<List<App>> responseEntity = appService.findDeployed();
//        Assert.assertNotNull(responseEntity.getResults());
//    }

    //TODO: An exception is thrown
//    @org.junit.Test
//    public void testFindBackedupById() throws Exception {
//        ResponseEntity<App> responseEntity = appService.findBackedupById("2c91808651a95c4d0151d8ef771a6ed4");
//        Assert.assertNotNull(responseEntity.getResults());
//    }

    @org.junit.Test
    public void testFindPluginById() throws Exception {
        ResponseEntity<App> responseEntity = appService.findPluginById("2c91808651a95c4d0151d8f0a1116edb");
        Assert.assertNotNull(responseEntity.getResults());
    }

    @org.junit.Test
    public void testFindRolledback() throws Exception {
        ResponseEntity<App> responseEntity = appService.findRolledback("2c91808651a95c4d0151d8f0a1116edb");
        Assert.assertNotNull(responseEntity.getResults());
    }

    @org.junit.Test
    public void testFindScaleOutCreate() throws Exception {
        ResponseEntity<App> responseEntity = appService.findScaleOutCreate("2c91808651a95c4d0151d8f0a1116edb");
        Assert.assertNotNull(responseEntity.getResults());
    }

    @org.junit.Test
    public void testFindScaleIn() throws Exception {
        ResponseEntity<App> responseEntity = appService.findScaleIn("2c91808651a95c4d0151d8f0a1116edb");
        Assert.assertNotNull(responseEntity.getResults());
    }

    // TODO: Check passing params in impl
//    @org.junit.Test
//    public void testMonitorStats() throws Exception {
//        ResponseEntity<List<App>> responseEntity = appService.monitorStats("2c91808651a95c4d0151d8f0a1116edb");
//        Assert.assertNotNull(responseEntity.getResults());
//    }


}
