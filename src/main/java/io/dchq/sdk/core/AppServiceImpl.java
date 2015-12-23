package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.provision.App;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * Created by atefahmed on 12/23/15.
 */
public class AppServiceImpl extends GenericServiceImpl<ResponseEntity<List<App>>, ResponseEntity<App>>
        implements AppService{

    public static final ParameterizedTypeReference<ResponseEntity<List<App>>> listTypeReference = new ParameterizedTypeReference<ResponseEntity<List<App>>>() {
    };
    public static final ParameterizedTypeReference<ResponseEntity<App>> singleTypeReference = new ParameterizedTypeReference<ResponseEntity<App>>() {
    };

    public static final String ENDPOINT = "apps/";

    /**
     * @param baseURI  - e.g. https://dchq.io/api/1.0/
     * @param username
     * @param password
     */
    public AppServiceImpl(String baseURI, String username, String password) {
        super(baseURI, ENDPOINT, username, password);
    }

    @Override
    public ResponseEntity<List<App>> get() {
        return get("", listTypeReference);
    }

}
