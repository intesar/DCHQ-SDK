package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.provider.DockerServer;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * Created by atefahmed on 12/22/15.
 */
public class DockerServerServiceImpl extends GenericServiceImpl<ResponseEntity<List<DockerServer>>, ResponseEntity<DockerServer>>
        implements DockerServerService{

    public static final ParameterizedTypeReference<ResponseEntity<List<DockerServer>>> listTypeReference = new ParameterizedTypeReference<ResponseEntity<List<DockerServer>>>() {
    };
    public static final ParameterizedTypeReference<ResponseEntity<DockerServer>> singleTypeReference = new ParameterizedTypeReference<ResponseEntity<DockerServer>>() {
    };

    public static final String ENDPOINT = "dockerservers/";

    /**
     * @param baseURI  - e.g. https://dchq.io/api/1.0/
     * @param username
     * @param password
     */
    public DockerServerServiceImpl(String baseURI, String username, String password) {
        super(baseURI, ENDPOINT, username, password);
    }

    @Override
    public ResponseEntity<List<DockerServer>> get() {
        return get("", listTypeReference);
    }

}
