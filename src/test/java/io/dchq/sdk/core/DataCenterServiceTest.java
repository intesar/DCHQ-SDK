package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.provider.DataCenter;
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
}
