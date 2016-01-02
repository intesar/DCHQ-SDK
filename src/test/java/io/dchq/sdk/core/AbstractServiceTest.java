/*
 * Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.dchq.sdk.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstracts class for holding test credentials.
 *
 * @author Intesar Mohammed
 * @since 1.0
 */
public abstract class AbstractServiceTest {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected String rootUrl = "http://104.130.163.208:33901/api/1.0/";
    protected String username = "admin@dchq.io";
    protected String password = "admin123";
//    protected String rootUrl = "http://localhost:9090/api/1.0/";
//    protected String username = "mdshannan@gmail.com";
//    protected String password = "apple123";

}
