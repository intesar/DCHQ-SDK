package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.provider.DockerServer;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * Encapsulates DCHQ DockerServer related methods.
 *
 * @author Atef Ahmed
 * @see <a href="https://dchq.readme.io/docs/dockerservers">DockerServer endpoint</a>
 * @since 1.0
 */
public class DockerServerServiceImpl extends GenericServiceImpl<DockerServer, ResponseEntity<List<DockerServer>>, ResponseEntity<DockerServer>>
        implements DockerServerService {

    public static final ParameterizedTypeReference<ResponseEntity<List<DockerServer>>> listTypeReference = new ParameterizedTypeReference<ResponseEntity<List<DockerServer>>>() {
    };
    public static final ParameterizedTypeReference<ResponseEntity<DockerServer>> singleTypeReference = new ParameterizedTypeReference<ResponseEntity<DockerServer>>() {
    };

    public static final String ENDPOINT = "dockerservers/";

    /**
     * @param baseURI  - e.g. https://dchq.io/api/1.0/
     * @param username - registered username with DCHQ.io
     * @param password - password used with the username
     */
    public DockerServerServiceImpl(String baseURI, String username, String password) {
        super(baseURI, ENDPOINT, username, password,
                new ParameterizedTypeReference<ResponseEntity<List<DockerServer>>>() {
                },
                new ParameterizedTypeReference<ResponseEntity<DockerServer>>() {
                }
        );
    }

    @Override
    public ResponseEntity<List<DockerServer>> get() {
        return findAll();
    }

    @Override
    public ResponseEntity<DockerServer> findById(String id) {
        return findById(id);
    }

    @Override
    public ResponseEntity<List<DockerServer>> getManaged() {
        return findAll();
    }

    @Override
    public ResponseEntity<DockerServer> findStatusById(String id) {
        return findById(id + "/status");
    }

    @Override
    public ResponseEntity<DockerServer> pingServerById(String id) {
        return findById(id + "/ping");
    }

    @Override
    public ResponseEntity<DockerServer> findManagedById(String id) {
        return findById("manage/" + id);
    }

    @Override
    public ResponseEntity<DockerServer> findMonitoredDataById(String id) {
        return findById(id + "/monitor");
    }


}
