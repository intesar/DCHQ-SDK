package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.provider.DataCenter;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * Created by atefahmed on 12/23/15.
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
     * @param username
     * @param password
     */
    public DataCenterServiceImpl(String baseURI, String username, String password) {
        super(baseURI, ENDPOINT, username, password);
    }

    @Override
    public ResponseEntity<List<DataCenter>> get() {
        return get("", listTypeReference);
    }
    
}
