package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.inbox.Message;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * Encapsulates DCHQ user Message calls.
 *
 * @author Atef Ahmed
 * @see <a href="https://dchq.readme.io/docs/messagesid">Message endpoint</a>
 * @since 1.0
 */
public class MessageServiceImpl extends GenericServiceImpl<Message, ResponseEntity<List<Message>>, ResponseEntity<Message>>
        implements MessageService {

    public static final ParameterizedTypeReference<ResponseEntity<List<Message>>> listTypeReference = new ParameterizedTypeReference<ResponseEntity<List<Message>>>() {
    };
    public static final ParameterizedTypeReference<ResponseEntity<Message>> singleTypeReference = new ParameterizedTypeReference<ResponseEntity<Message>>() {
    };

    public static final String ENDPOINT = "messages/";

    /**
     * @param baseURI  - e.g. https://dchq.io/api/1.0/
     * @param username - registered username with DCHQ.io
     * @param password - password used with the username
     */
    public MessageServiceImpl(String baseURI, String username, String password) {
        super(baseURI, ENDPOINT, username, password,
                new ParameterizedTypeReference<ResponseEntity<List<Message>>>() {
                },
                new ParameterizedTypeReference<ResponseEntity<Message>>() {
                }
        );
    }


    @Override
    public ResponseEntity<Message> findById(String id) {
        return findById(id);
    }
}
