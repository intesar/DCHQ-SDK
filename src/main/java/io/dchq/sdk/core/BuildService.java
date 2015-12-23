package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.build.Build;

import java.util.List;

/**
 * Created by atefahmed on 12/22/15.
 */
public interface BuildService extends GenericService<ResponseEntity<List<Build>>, ResponseEntity<Build>> {

    ResponseEntity<List<Build>> get();

}
