package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.blueprint.RegistryAccount;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * Encapsulates DCHQ RegistryAccount related methods.
 *
 * @author Atef Ahmed
 * @see <a href="https://dchq.readme.io/docs/registryaccounts">RegistryAccount endpoint</a>
 * @since 1.0
 */
public class RegistryAccountServiceImpl extends GenericServiceImpl<RegistryAccount, ResponseEntity<List<RegistryAccount>>, ResponseEntity<RegistryAccount>>
        implements RegistryAccountService {

    public static final ParameterizedTypeReference<ResponseEntity<List<RegistryAccount>>> listTypeReference = new ParameterizedTypeReference<ResponseEntity<List<RegistryAccount>>>() {
    };
    public static final ParameterizedTypeReference<ResponseEntity<RegistryAccount>> singleTypeReference = new ParameterizedTypeReference<ResponseEntity<RegistryAccount>>() {
    };

    public static final String ENDPOINT = "registryaccounts/";

    /**
     * @param baseURI  - e.g. https://dchq.io/api/1.0/
     * @param username - registered username with DCHQ.io
     * @param password - password used with the username
     */
    public RegistryAccountServiceImpl(String baseURI, String username, String password) {
        super(baseURI, ENDPOINT, username, password,
                new ParameterizedTypeReference<ResponseEntity<List<RegistryAccount>>>() {
                },
                new ParameterizedTypeReference<ResponseEntity<RegistryAccount>>() {
                }
        );
    }

    @Override
    public ResponseEntity<List<RegistryAccount>> get() {
        return findAll();
    }

    @Override
    public ResponseEntity<RegistryAccount> findById(String id) {
        return findById(id);
    }

    @Override
    public ResponseEntity<List<RegistryAccount>> getManaged() {
        return findAll();
    }

    @Override
    public ResponseEntity<RegistryAccount> findRegistryAccountTypeById(String id) {
        return findById("accounttype/" + id);
    }

}
