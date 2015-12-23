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


    @org.junit.Test
    public void testGet() throws Exception {
        ResponseEntity<List<App>> responseEntity = appService.get();
        Assert.assertNotNull(responseEntity.getTotalElements());
        for (App bl : responseEntity.getResults()) {
            logger.info("Application name [{}] author [{}]", bl.getName(), bl.getCreatedBy());
        }
    }
}
