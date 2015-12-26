package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.provision.App;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * Encapsulates DCHQ Apps related methods.
 *
 * @author Atef Ahmed
 * @see <a href="https://dchq.readme.io/docs/applications">App endpoint</a>
 * @since 1.0
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
     * @param username - registered username with DCHQ.io
     * @param password - password used with the username
     */
    public AppServiceImpl(String baseURI, String username, String password) {
        super(baseURI, ENDPOINT, username, password);
    }

    @Override
    public ResponseEntity<List<App>> get() {
        return get("", listTypeReference);
    }

    @Override
    public ResponseEntity<App> findById(String id) {
        return getOne(id, singleTypeReference);
    }

    @Override
    public ResponseEntity<List<App>> findActive() {
        return get("active", listTypeReference);
    }

    @Override
    public ResponseEntity<List<App>> findDestroyed() {
        return get("destroyed", listTypeReference);
    }

    @Override
    public ResponseEntity<List<App>> findDeployed() {
        return get("deploy", listTypeReference);
    }

    @Override
    public ResponseEntity<App> findBackedupById(String id) {
        return getOne(id + "/backup", singleTypeReference);
    }

    @Override
    public ResponseEntity<App> findPluginById(String id) {
        return getOne(id + "/plugin", singleTypeReference);
    }

    @Override
    public ResponseEntity<App> findRolledback(String id) {
        return getOne(id + "/rollback", singleTypeReference);
    }

    @Override
    public ResponseEntity<App> findScaleOutCreate(String id) {
        return getOne(id + "/scale-out-create", singleTypeReference);
    }

    @Override
    public ResponseEntity<App> findScaleIn(String id) {
        return getOne(id + "/scale-in", singleTypeReference);
    }

    @Override
    public ResponseEntity<List<App>> findMonitored(String id) {
        return get(id + "/monitor?start=2015-08-19T03%3A58%3A57.138Z&end=2015-08-19T03%3A58%3A57.138Z", listTypeReference);
    }

}
