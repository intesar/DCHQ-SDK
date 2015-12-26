package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.build.Build;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * This class encapsulates Build related methods
 *
 * @author Atef Ahmed
 * @see <a href="https://dchq.readme.io/docs/builds">Build endpoint</a>
 * @since 1.0
 */
public class BuildServiceImpl extends GenericServiceImpl<ResponseEntity<List<Build>>, ResponseEntity<Build>>
        implements BuildService{

    public static final ParameterizedTypeReference<ResponseEntity<List<Build>>> listTypeReference = new ParameterizedTypeReference<ResponseEntity<List<Build>>>() {
    };
    public static final ParameterizedTypeReference<ResponseEntity<Build>> singleTypeReference = new ParameterizedTypeReference<ResponseEntity<Build>>() {
    };

    public static final String ENDPOINT = "builds/";

    /**
     * @param baseURI  - e.g. https://dchq.io/api/1.0/
     * @param username - registered username with DCHQ.io
     * @param password - password used with the username
     */
    public BuildServiceImpl(String baseURI, String username, String password) {
        super(baseURI, ENDPOINT, username, password);
    }

    @Override
    public ResponseEntity<List<Build>> get() {
        return get("", listTypeReference);
    }

    @Override
    public ResponseEntity<Build> findById(String id) {
        return getOne(id, singleTypeReference);
    }

    @Override
    public ResponseEntity<List<Build>> getManaged() {
        return get("manage", listTypeReference);
    }

    @Override
    public ResponseEntity<Build> findManagedById(String id){
        return getOne("manage/" + id, singleTypeReference);
    }

}
