package io.dchq.sdk.core.machines;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.provider.DataCenter;
import com.dchq.schema.beans.one.provider.DockerServer;
import com.dchq.schema.beans.one.security.EntitlementType;
import io.dchq.sdk.core.AbstractServiceTest;
import io.dchq.sdk.core.DataCenterService;
import io.dchq.sdk.core.DockerServerService;
import io.dchq.sdk.core.ServiceFactory;
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


        return responseEntity.getResults();

    }

    public DockerServer validateProvision(DockerServer inputDocker, String action) {
        int warningCount = 0;
        waitTime = 0;
        DockerServer outDockerserver = null;
        DockerServer tempDockerserver = null;
        String serverStatus = inputDocker.getDockerServerStatus() == null ? "" :inputDocker.getDockerServerStatus().name();
        provision:
        do {

            if (wait(10000) == 0) break provision;
            ResponseEntity<DockerServer> response = dockerServerService.findById(inputDocker.getId());

            assertNotNull(response);
            assertNotNull(response.isErrors());
            assertEquals(response.getMessages().toString(), ((Boolean) createError).toString(), ((Boolean) response.isErrors()).toString());

            if (response.getResults() != null) {
                tempDockerserver = response.getResults();
                serverStatus = tempDockerserver.getDockerServerStatus().name();
                logger.info("Current Serverstatus   [{}] ", serverStatus);
                if (serverStatus.equals("CONNECTED") || serverStatus.equals("DESTROYED")) break provision;

            }
            if (serverStatus == "WARNINGS") {
                warningCount++;
                serverStatus = action;
            } else if (warningCount > 0 || warningCount < 5) {
                warningCount++;
            }


        } while (serverStatus == action);
        if (tempDockerserver.getDockerServerStatus().name().equals("CONNECTED") || tempDockerserver.getDockerServerStatus().name().equals("DESTROYED"))
            outDockerserver = tempDockerserver;
        return outDockerserver;

    }

}
