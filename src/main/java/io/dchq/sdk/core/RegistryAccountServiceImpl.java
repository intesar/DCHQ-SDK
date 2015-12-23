package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.blueprint.RegistryAccount;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * Created by atefahmed on 12/23/15.
 */
public class RegistryAccountServiceImpl extends GenericServiceImpl<ResponseEntity<List<RegistryAccount>>, ResponseEntity<RegistryAccount>>
        implements RegistryAccountService{

    public static final ParameterizedTypeReference<ResponseEntity<List<RegistryAccount>>> listTypeReference = new ParameterizedTypeReference<ResponseEntity<List<RegistryAccount>>>() {
    };
    public static final ParameterizedTypeReference<ResponseEntity<RegistryAccount>> singleTypeReference = new ParameterizedTypeReference<ResponseEntity<RegistryAccount>>() {
    };

    public static final String ENDPOINT = "registryaccounts/";

    /**
     * @param baseURI  - e.g. https://dchq.io/api/1.0/
     * @param username
     * @param password
     */
    public RegistryAccountServiceImpl(String baseURI, String username, String password) {
        super(baseURI, ENDPOINT, username, password);
    }

    @Override
    public ResponseEntity<List<RegistryAccount>> get() {
        return get("", listTypeReference);
    }

}
