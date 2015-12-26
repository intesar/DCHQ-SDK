package io.dchq.sdk.core;

import com.dchq.schema.beans.one.provider.DockerServer;
import java.util.List;
import com.dchq.schema.beans.base.ResponseEntity;

/**
 * <code>DockerServer</code> endpoint API calls.
 *
 * @author Atef Ahmed
 * @since 1.0
 */
public interface DockerServerService extends GenericService<ResponseEntity<List<DockerServer>>, ResponseEntity<DockerServer>> {

    /**
     * Get hosts page all <code>DockerServer</code> owned by the logged in user
     *
     * @return List of ResponseEntity of type DockerServer
     */
    ResponseEntity<List<DockerServer>> get();

    /**
     * Get managed/authored <code>DockerServer</code>.
     *
     * @return DockerServers response
     */
    ResponseEntity<List<DockerServer>> getManaged();

    /**
     * Find <code>DockerServer</code> by id.
     *
     * @return Specific DockerServer response
     */
    ResponseEntity<DockerServer> findById(String id);

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
     * Find a managed <code>DockerServer</code> by id.
     *
     * @return Specific DockerServer response
     */
    ResponseEntity<DockerServer> findManagedById(String id);

    /**
     * Find historical CPU, memory utilization, monitoring data of a <code>DockerServer</code> by id.
     *
     * @return Specific DockerServer response
     */
    ResponseEntity<DockerServer> findMonitoredDataById(String id);

}
