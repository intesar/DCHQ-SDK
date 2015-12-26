package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.plugin.Plugin;
import org.junit.Assert;

import java.util.List;

/**
 * Created by atefahmed on 12/23/15.
 */
public class PluginServiceTest extends AbstractServiceTest {

    private PluginService appService;

    @org.junit.Before
    public void setUp() throws Exception{
        appService = ServiceFactory.buildPluginService(rootUrl, username, password);
    }


    @org.junit.Test
    public void testGet() throws Exception {
        ResponseEntity<List<Plugin>> responseEntity = appService.get();
        Assert.assertNotNull(responseEntity.getTotalElements());
        for (Plugin bl : responseEntity.getResults()) {
            logger.info("Plugin name [{}] author [{}]", bl.getName(), bl.getCreatedBy());
        }
    }

    @org.junit.Test
    public void testFindById() throws Exception {
        ResponseEntity<Plugin> responseEntity = appService.findById("2c91808651a95c4d0151d9915b687345");
        Assert.assertNotNull(responseEntity.getResults());
        Assert.assertNotNull(responseEntity.getResults().getId());
    }

    @org.junit.Test
    public void testGetManaged() throws Exception {
        ResponseEntity<List<Plugin>> responseEntity = appService.getManaged();
        Assert.assertNotNull(responseEntity.getTotalElements());
        for (Plugin bl : responseEntity.getResults()) {
            logger.info("Managed Plugin type [{}] name [{}] author [{}]", bl.getInactive(), bl.getName(), bl.getCreatedBy());
        }
    }

    @org.junit.Test
    public void testFindManagedById() throws Exception {
        ResponseEntity<Plugin> responseEntity = appService.findManagedById("2c91808651a95c4d0151d9915b687345");
        Assert.assertNotNull(responseEntity.getResults());
        Assert.assertNotNull(responseEntity.getResults().getId());
    }

//    @org.junit.Test
//    public void testFindStarred() throws Exception {
//        ResponseEntity<List<Plugin>> responseEntity = appService.findByStarred();
//        Assert.assertNotNull(responseEntity.getResults());
//    }

}
