package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.blueprint.RegistryAccount;
import org.junit.Assert;

import java.util.List;

/**
 * Created by atefahmed on 12/23/15.
 */
public class RegistryAccountServiceTest extends AbstractServiceTest {

    private RegistryAccountService registryAccountService;

    @org.junit.Before
    public void setUp() throws Exception {
        registryAccountService = ServiceFactory.buildRegistryAccountService(rootUrl, username, password);
    }


    @org.junit.Test
    public void testGet() throws Exception {
        ResponseEntity<List<RegistryAccount>> responseEntity = registryAccountService.get();
        Assert.assertNotNull(responseEntity.getTotalElements());
        for (RegistryAccount bl : responseEntity.getResults()) {
            logger.info("RegistryAccount name [{}] author [{}]", bl.getName(), bl.getCreatedBy());
        }
    }

}
