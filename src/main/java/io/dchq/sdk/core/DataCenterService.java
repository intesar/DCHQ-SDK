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
public interface DataCenterService extends GenericService<DataCenter, ResponseEntity<List<DataCenter>>, ResponseEntity<DataCenter>> {

}
