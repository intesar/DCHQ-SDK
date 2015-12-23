package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.provision.App;

import java.util.List;

/**
 * Created by atefahmed on 12/23/15.
 */
public interface AppService extends GenericService<ResponseEntity<List<App>>, ResponseEntity<App>> {

    ResponseEntity<List<App>> get();
}
