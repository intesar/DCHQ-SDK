/* COPYRIGHT (C) 2015 DCHQ. All Rights Reserved. */
package base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * Generic Service Implementation.
 *
 * @author Mohammed Shoukath Ali
 */
@Transactional
public class GenericServiceImpl implements GenericService {

    RestTemplate restTemplate = new RestTemplate();

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    public GenericServiceImpl() {
    }



}
