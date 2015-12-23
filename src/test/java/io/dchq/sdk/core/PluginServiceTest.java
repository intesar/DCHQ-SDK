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
}
