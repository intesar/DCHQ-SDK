package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.blueprint.Blueprint;
import com.dchq.schema.beans.one.provision.App;
import org.springframework.core.ParameterizedTypeReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates DCHQ Apps related methods.
 *
 * @author Atef Ahmed
 * @author Intesar Mohammed
 * @see <a href="https://dchq.readme.io/docs/applications">App endpoint</a>
 * @since 1.0
 */
public class AppServiceImpl extends GenericServiceImpl<App, ResponseEntity<List<App>>, ResponseEntity<App>>
        implements AppService {

    public static final ParameterizedTypeReference<ResponseEntity<List<App>>> listTypeReference = new ParameterizedTypeReference<ResponseEntity<List<App>>>() {
    };
    public static final ParameterizedTypeReference<ResponseEntity<App>> singleTypeReference = new ParameterizedTypeReference<ResponseEntity<App>>() {
    };

    public static final String ENDPOINT = "apps/";

    /**
     * @param baseURI  - e.g. https://dchq.io/api/1.0/
     * @param username - registered username with DCHQ.io
     * @param password - password used with the username
     */
    public AppServiceImpl(String baseURI, String username, String password) {
        super(baseURI, ENDPOINT, username, password,
                new ParameterizedTypeReference<ResponseEntity<List<App>>>() {
                },
                new ParameterizedTypeReference<ResponseEntity<App>>() {
                }
        );
    }

    @Override
    public ResponseEntity<List<App>> findActive() {
        return findAll("active", listTypeReference);
    }

    @Override
    public ResponseEntity<List<App>> findDestroyed() {
        return findAll("destroyed", listTypeReference);
    }

    @Override
    public ResponseEntity<List<App>> findDeployed() {
        return findAll();
    }

    @Override
    public ResponseEntity<App> findBackedupById(String id) {
        return findById(id + "/backup");
    }

    @Override
    public ResponseEntity<App> findPluginById(String id) {
        return findById(id + "/plugin");
    }

    @Override
    public ResponseEntity<App> findRolledback(String id) {
        return findById(id + "/rollback");
    }

    @Override
    public ResponseEntity<App> findScaleOutCreate(String id) {
        return findById(id + "/scale-out-create");
    }

    @Override
    public ResponseEntity<App> findScaleIn(String id) {
        return findById(id + "/scale-in");
    }

    @Override
    public ResponseEntity<List<App>> findMonitored(String id) {
        return findAll();
    }

    @Override
    public ResponseEntity<App> deploy(String blueprintId) {
        return super.doPost("", "/deploy/" + blueprintId);
    }

    @Override
    public ResponseEntity<App> deploy(Blueprint blueprint) {
        return super.doPost(blueprint, "/deploy");
    }

    @Override
    public ResponseEntity<App> destroy(String appId) {
        return super.doPost(new ArrayList<>(), "/" + appId + "/destroy");
    }

}
