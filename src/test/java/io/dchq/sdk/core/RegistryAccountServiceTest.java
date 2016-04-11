package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.blueprint.AccountType;
import com.dchq.schema.beans.one.blueprint.RegistryAccount;
import com.dchq.schema.beans.one.security.EntitlementType;
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
    public void testFindAll() throws Exception {
        ResponseEntity<List<RegistryAccount>> responseEntity = registryAccountService.findAll();
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
    public void testFindAllManaged() throws Exception {
        ResponseEntity<List<RegistryAccount>> responseEntity = registryAccountService.findAllManaged();
        Assert.assertNotNull(responseEntity.getTotalElements());
        for (RegistryAccount bl : responseEntity.getResults()) {
            logger.info("Managed RegistryAccount type [{}] name [{}] author [{}]", bl.getBlueprintEntitlementType(), bl.getName(), bl.getCreatedBy());
        }
    }

//    //TODO: error is - Request failed - No enum constant com.dchq.orm.entity.blueprint.AccountType.2c91808651a95c4d0151d96a012f71ba
//    @org.junit.Test
//    public void testFindRegistryAccountTypeById() throws Exception {
//        ResponseEntity<RegistryAccount> responseEntity = registryAccountService.findRegistryAccountTypeById("2c91808651a95c4d0151d96a012f71ba");
//        Assert.assertNotNull(responseEntity.getResults());
//    }

    // Create - Update - Delete

    @org.junit.Test
    public void testCreate() throws Exception {
        RegistryAccount registryAccount = new RegistryAccount()
                .withName("Test RegistryAccount - AA6");

        // Create

        registryAccount.setId("2c91808651a95c4d0151fc332bffa96");
        registryAccount.setInactive(Boolean.FALSE);
        registryAccount.setUrl("https://registry.hub.docker.com/");
        registryAccount.setEmail("atefahmed@gmail.com");
        registryAccount.setAccountType(AccountType.DOCKER_REGISTRY);
        registryAccount.setBlueprintEntitlementType(EntitlementType.MY_BLUEPRINTS);

        ResponseEntity<RegistryAccount> responseEntity = registryAccountService.create(registryAccount);
        Assert.assertNotNull(responseEntity.getResults());
        Assert.assertNotNull(responseEntity.getResults().getId());

        // Update

        registryAccount = responseEntity.getResults();

        registryAccount.setBlueprintEntitlementType(EntitlementType.ALL_TENANT_USERS);
        registryAccount.setInactive(Boolean.TRUE);

        responseEntity = registryAccountService.update(registryAccount);

        Assert.assertNotNull(responseEntity.getResults());
        Assert.assertEquals(responseEntity.getResults().isInactive(), Boolean.TRUE);
        Assert.assertEquals(responseEntity.getResults().getBlueprintEntitlementType(), EntitlementType.ALL_TENANT_USERS);

        // Delete

        responseEntity = registryAccountService.delete(registryAccount.getId());
        Assert.assertFalse(responseEntity.isErrors());
    }
}
