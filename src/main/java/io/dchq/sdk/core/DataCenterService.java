package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.provider.DataCenter;

import java.util.List;

/**
 * Created by atefahmed on 12/23/15.
 */
public interface DataCenterService extends GenericService<ResponseEntity<List<DataCenter>>, ResponseEntity<DataCenter>> {

    ResponseEntity<List<DataCenter>> get();
}
