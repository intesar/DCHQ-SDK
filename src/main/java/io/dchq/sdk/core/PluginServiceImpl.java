package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.plugin.Plugin;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * Created by atefahmed on 12/23/15.
 */
public class PluginServiceImpl extends GenericServiceImpl<ResponseEntity<List<Plugin>>, ResponseEntity<Plugin>>
        implements PluginService{

    public static final ParameterizedTypeReference<ResponseEntity<List<Plugin>>> listTypeReference = new ParameterizedTypeReference<ResponseEntity<List<Plugin>>>() {
    };
    public static final ParameterizedTypeReference<ResponseEntity<Plugin>> singleTypeReference = new ParameterizedTypeReference<ResponseEntity<Plugin>>() {
    };

    public static final String ENDPOINT = "plugins/";

    /**
     * @param baseURI  - e.g. https://dchq.io/api/1.0/
     * @param username
     * @param password
     */
    public PluginServiceImpl(String baseURI, String username, String password) {
        super(baseURI, ENDPOINT, username, password);
    }

    @Override
    public ResponseEntity<List<Plugin>> get() {
        return get("", listTypeReference);
    }

}
