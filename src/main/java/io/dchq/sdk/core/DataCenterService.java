package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.provider.DataCenter;

import java.util.List;

/**
 * <code>DataCenter</code> endpoint API calls.
 *
 * @author Atef Ahmed
 * @since 1.0
 */
public interface DataCenterService extends GenericService<ResponseEntity<List<DataCenter>>, ResponseEntity<DataCenter>> {

    /**
     * Get clusters page entitled <code>DataCenter</code>
     * @return ResponseEntity, list of data centers.
     */
    ResponseEntity<List<DataCenter>> get();

    /**
     * Find <code>DataCenter</code> by id.
     *
     * @return Specific DataCenter response
     */
    ResponseEntity<DataCenter> findById(String id);

    /**
     * Get managed/authored <code>DataCenter</code>.
     *
     * @return DataCenter response
     */
    ResponseEntity<List<DataCenter>> getManaged();
}
