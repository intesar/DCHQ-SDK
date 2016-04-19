package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.provider.DataCenter;
import com.dchq.schema.beans.one.provider.DockerServer;
import com.dchq.schema.beans.one.security.EntitlementType;
import org.junit.Assert;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by abed on 19/4/16.
 */
public class DockerServerTest extends AbstractServiceTest {
    DataCenterService dataCenterService;
    DataCenter datacenterCreated;
    DockerServerService dockerServerService;
    DockerServer dockerServer;
    DockerServer dockerServerCreated, dockerServerProvisioning;
    boolean createError;
    ResponseEntity<List<DockerServer>> dockerServerResponseEntity;


    public DataCenter getDataCenter(String datacenterName, boolean autoScale, EntitlementType entitlementType) {
        logger.info("Create Cluster with Name [{}]", datacenterName);
        this.dataCenterService = ServiceFactory.buildDataCenterService(rootUrl, username, password);
        DataCenter dt = new DataCenter().withName(datacenterName).withAutoScale(autoScale).withBlueprintEntitlementType(entitlementType);

        ResponseEntity<DataCenter> responseEntity = dataCenterService.create(dt);
        if (responseEntity.isErrors())
            logger.warn("Message from Server... {}", responseEntity.getMessages().get(0).getMessageText());
        assertEquals(false, responseEntity.isErrors());
        return responseEntity.getResults();

    }
    public DockerServer validateProvision(DockerServer inputDocker, String action)throws Exception{
        int warningCount=0;
        waitTime=0;
        DockerServer outDockerserver=null;
        String serverStatus=inputDocker.getDockerServerStatus()==null?"":inputDocker.getDockerServerStatus().name();
        provision:
        do {

            Assert.assertEquals("",1,wait(10000));
            ResponseEntity<DockerServer>  response = dockerServerService.findById(inputDocker.getId());

            assertNotNull(response);
            assertNotNull(response.isErrors());
            assertEquals(response.getMessages().toString(), ((Boolean) createError).toString(), ((Boolean) response.isErrors()).toString());

            if(response.getResults()!=null) {
                outDockerserver=response.getResults();
                serverStatus = outDockerserver.getDockerServerStatus().name();
                logger.info("Current Serverstatus   [{}] ", serverStatus);
                if (serverStatus.equals("CONNECTED") ||serverStatus.equals("DESTROYED")) return outDockerserver;

            }
            if (serverStatus == "WARNINGS" ){ warningCount++; serverStatus = action;}
            else if (warningCount>0 ||warningCount < 5 ){
                warningCount++;
            }


        } while (serverStatus == action);
        Assert.assertEquals("",1,wait(10000));
        return outDockerserver;

    }
}
