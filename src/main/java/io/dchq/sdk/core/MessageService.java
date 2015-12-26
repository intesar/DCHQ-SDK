package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.inbox.Message;

import java.util.List;

/**
 * <code>Message</code> API calls.
 *
 * @author Atef Ahmed
 * @since 1.0
 */
public interface MessageService extends GenericService<ResponseEntity<List<Message>>, ResponseEntity<Message>>{

    /**
     * Find <code>Message</code> by id.
     *
     * @return Specific Message response
     */
    ResponseEntity<Message> findById(String id);

}
