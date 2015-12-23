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
    public void testGet() throws Exception {
        ResponseEntity<List<DataCenter>> responseEntity = dataCenterService.get();
        Assert.assertNotNull(responseEntity.getTotalElements());
        for (DataCenter bl : responseEntity.getResults()) {
            logger.info("DataCenter name [{}] author [{}]", bl.getName(), bl.getCreatedBy());
        }
    }

}
