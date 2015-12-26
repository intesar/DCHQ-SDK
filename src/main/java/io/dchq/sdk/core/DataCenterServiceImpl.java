package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.provider.DataCenter;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * Encapsulates DCHQ DataCenter related methods.
 *
 * @author Atef Ahmed
 * @see <a href="https://dchq.readme.io/docs/datacenters">DataCenter endpoint</a>
 * @since 1.0
 */
public class DataCenterServiceImpl extends GenericServiceImpl<ResponseEntity<List<DataCenter>>, ResponseEntity<DataCenter>>
        implements DataCenterService{

    public static final ParameterizedTypeReference<ResponseEntity<List<DataCenter>>> listTypeReference = new ParameterizedTypeReference<ResponseEntity<List<DataCenter>>>() {
    };
    public static final ParameterizedTypeReference<ResponseEntity<DataCenter>> singleTypeReference = new ParameterizedTypeReference<ResponseEntity<DataCenter>>() {
    };

    public static final String ENDPOINT = "datacenters/";

    /**
     * @param baseURI  - e.g. https://dchq.io/api/1.0/
     * @param username - registered username with DCHQ.io
     * @param password - password used with the username
     */
    public DataCenterServiceImpl(String baseURI, String username, String password) {
        super(baseURI, ENDPOINT, username, password);
    }

    @Override
    public ResponseEntity<List<DataCenter>> get() {
        return get("", listTypeReference);
    }

    @Override
    public ResponseEntity<DataCenter> findById(String id) {
        return getOne(id, singleTypeReference);
    }

    @Override
    public ResponseEntity<List<DataCenter>> getManaged() {
        return get("manage", listTypeReference);
    }


}
