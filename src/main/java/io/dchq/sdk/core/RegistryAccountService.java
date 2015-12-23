package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.blueprint.RegistryAccount;

import java.util.List;

/**
 * Created by atefahmed on 12/23/15.
 */
public interface RegistryAccountService extends GenericService<ResponseEntity<List<RegistryAccount>>, ResponseEntity<RegistryAccount>> {

    ResponseEntity<List<RegistryAccount>> get();
}
