package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.base.PkEntityBase;
import com.dchq.schema.beans.one.blueprint.Blueprint;
import com.dchq.schema.beans.one.provision.App;
import com.dchq.schema.beans.one.provision.ProvisionState;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by atefahmed on 12/23/15.
 */
public class AppServiceTest extends AbstractServiceTest {

    private AppService appService;
    private BlueprintService blueprintService;

    @org.junit.Before
    public void setUp() throws Exception {
        appService = ServiceFactory.buildAppService(rootUrl, username, password);
        blueprintService = ServiceFactory.buildBlueprintService(rootUrl, username, password);
    }

    //TODO: An exception is thrown, even on dchq.readme.io
//    @org.junit.Test
//    public void testFindAll() throws Exception {
//        ResponseEntity<List<App>> responseEntity = appService.findAll();
//        Assert.assertNotNull(responseEntity.getResults());
//
//        for (App bl : responseEntity.getResults()) {
//            logger.info("Application name [{}] author [{}]", bl.getName(), bl.getCreatedBy());
//        }
//    }


    @org.junit.Test
    public void testFindById() throws Exception {
        ResponseEntity<App> responseEntity = appService.findById("2c91808651a95c4d0151d8f0a1116edb");
        Assert.assertNotNull(responseEntity.getResults());
//        Assert.assertNotNull(responseEntity.getResults().getId());
    }

    @org.junit.Test
    public void testFindActive() throws Exception {
        ResponseEntity<List<App>> responseEntity = appService.findActive();
        Assert.assertNotNull(responseEntity.getTotalElements());
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

    // TODO: Check passing params in AppServiceImpl
//    @org.junit.Test
//    public void testMonitorStats() throws Exception {
//        ResponseEntity<List<App>> responseEntity = appService.monitorStats("2c91808651a95c4d0151d8f0a1116edb");
//        Assert.assertNotNull(responseEntity.getResults());
//    }

    @Test
    public void testDeploy() {
        // run blueprint post build/push
        ResponseEntity<Blueprint> blueprintResponseEntity = blueprintService.findById("2c9180875380891901538106ad851650");
        Blueprint blueprint = blueprintResponseEntity.getResults();

        blueprint.setName("Deployed from SDK");
        blueprint.setReason("Tests");
        blueprint.setTags("DEV");

        Map<String, String> map = new HashMap<String, String>();
        map.put("MyApp.image", "intesar/apache:latest-im");
        blueprint.setCustomizationsMap(map);


        //blueprint.setDatacenter(new PkEntityBase().setId(""););

        // Deploy based on blueprintId
        //ResponseEntity<App> appResponseEntity = appService.deploy("40288184537c9f6d01537ca663850018");

        // Deploying using blueprint object
        ResponseEntity<App> appResponseEntity = appService.deploy(blueprint);
        App app = appResponseEntity.getResults();

        if (app != null) {
            while ( (app.getProvisionState() != ProvisionState.RUNNING) && (app.getProvisionState() != ProvisionState.PROVISIONING_FAILED)) {
                logger.info("Found app [{}] with status [{}]", app.getName(), app.getProvisionState());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    logger.warn(e.getLocalizedMessage(), e);
                }
                app = appService.findById(app.getId()).getResults();
            }
            logger.info("Fished provisioning app [{}] with status [{}]", app.getName(), app.getProvisionState());
        }

        // Destroy app
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            logger.warn(e.getLocalizedMessage(), e);
        }
        appService.destroy(app.getId());
    }

}
