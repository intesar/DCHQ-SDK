package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.provider.DockerServer;

import java.util.List;

/**
 * <code>DockerServer</code> endpoint API calls.
 *
 * @author Atef Ahmed
 * @since 1.0
 */
public interface DockerServerService extends GenericService<DockerServer, ResponseEntity<List<DockerServer>>, ResponseEntity<DockerServer>> {

    /**
     * Find the status of a <code>DockerServer</code> by id.
     *
     * @return Specific DockerServer response
     */
    ResponseEntity<DockerServer> findStatusById(String id);

    /**
     * Ping  a <code>DockerServer</code> by id.
     *
     * @return Specific DockerServer response
     */
    ResponseEntity<DockerServer> pingServerById(String id);

    /**
     * Find historical CPU, memory utilization, monitoring data of a <code>DockerServer</code> by id.
     *
     * @return Specific DockerServer response
     */
    ResponseEntity<DockerServer> findMonitoredDataById(String id);
}
