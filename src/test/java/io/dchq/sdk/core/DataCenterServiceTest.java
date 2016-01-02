package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.provider.DataCenter;
import com.dchq.schema.beans.one.security.EntitlementType;
import org.junit.Assert;

import java.util.List;

/**
 * Created by atefahmed on 12/23/15.
 */
public class DataCenterServiceTest extends AbstractServiceTest{

    private DataCenterService dataCenterService;

    @org.junit.Before
    public void setUp() throws Exception{
        dataCenterService = ServiceFactory.buildDataCenterService(rootUrl, username, password);
    }

    @org.junit.Test
    public void testFindAll() throws Exception {
        ResponseEntity<List<DataCenter>> responseEntity = dataCenterService.findAll();
        Assert.assertNotNull(responseEntity.getTotalElements());
    }

    @org.junit.Test
    public void testFindById() throws Exception {
        ResponseEntity<DataCenter> responseEntity = dataCenterService.findById("2c91808651a95c4d0151e6a6720e3e81");
        Assert.assertNotNull(responseEntity.getResults());
        Assert.assertNotNull(responseEntity.getResults().getId());
    }

    @org.junit.Test
    public void testFindAllManaged() throws Exception {
        ResponseEntity<List<DataCenter>> responseEntity = dataCenterService.findAllManaged();
        Assert.assertNotNull(responseEntity.getTotalElements());
    }

    // Create - Update - Delete

    @org.junit.Test
    public void testCreate() throws Exception {
        DataCenter dataCenter = new DataCenter().withName("Test Cluster - AA4");

        // Create

        dataCenter.setId("2c91808651a95c4d0151fc332bff4333");
        dataCenter.setInactive(Boolean.FALSE);
        dataCenter.setAutoScale(Boolean.FALSE);
        dataCenter.setBlueprintEntitlementType(EntitlementType.ALL_BLUEPRINTS);

        ResponseEntity<DataCenter> responseEntity = dataCenterService.create(dataCenter);
        Assert.assertNotNull(responseEntity.getResults());
        Assert.assertNotNull(responseEntity.getResults().getId());

        // Update

        dataCenter = responseEntity.getResults();

        dataCenter.setBlueprintEntitlementType(EntitlementType.ALL_TENANT_USERS);

        responseEntity = dataCenterService.update(dataCenter);

        Assert.assertNotNull(responseEntity.getResults());
        Assert.assertEquals(responseEntity.getResults().getBlueprintEntitlementType(), EntitlementType.ALL_TENANT_USERS);
        Assert.assertNotNull(responseEntity.getResults().getId());

        // Delete

        // TODO: Unable to delete. Request failed - null.
        responseEntity = dataCenterService.delete(dataCenter.getId());
        Assert.assertFalse(responseEntity.isErrors());
    }
}
