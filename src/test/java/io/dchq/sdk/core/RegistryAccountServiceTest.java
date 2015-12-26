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

    @org.junit.Test
    public void testFindById() throws Exception {
        ResponseEntity<RegistryAccount> responseEntity = registryAccountService.findById("2c91808651a95c4d0151d96a012f71ba");
        Assert.assertNotNull(responseEntity.getResults());
        Assert.assertNotNull(responseEntity.getResults().getId());
    }

    @org.junit.Test
    public void testGetManaged() throws Exception {
        ResponseEntity<List<RegistryAccount>> responseEntity = registryAccountService.getManaged();
        Assert.assertNotNull(responseEntity.getTotalElements());
        for (RegistryAccount bl : responseEntity.getResults()) {
            logger.info("Managed RegistryAccount type [{}] name [{}] author [{}]", bl.getBlueprintEntitlementType(), bl.getName(), bl.getCreatedBy());
        }
    }

    //TODO: need to figure out
//    @org.junit.Test
//    public void testFindRegistryAccountTypeById() throws Exception {
//        ResponseEntity<RegistryAccount> responseEntity = registryAccountService.findRegistryAccountTypeById("2c91808651a95c4d0151d96a012f71ba");
//        Assert.assertNotNull(responseEntity.getResults());
//    }

}
