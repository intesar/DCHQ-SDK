package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.build.Build;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * Created by atefahmed on 12/22/15.
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
     * @param username
     * @param password
     */
    public BuildServiceImpl(String baseURI, String username, String password) {
        super(baseURI, ENDPOINT, username, password);
    }

    @Override
    public ResponseEntity<List<Build>> get() {
        return get("", listTypeReference);
    }

}
