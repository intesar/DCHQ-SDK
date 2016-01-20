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

/**
 * Abstracts and provides infrastructure to all API calls.
 *
 * @author Intesar Mohammed
 * @since 1.0
 */
interface GenericService<E, RL, RO> {

    /**
     * Finds all <code>E</code>.
     *
     * @return - Response
     */
    RL findAll();

    RL findAll(int page, int size);

    /**
     * Find <code>E</code> by id.
     *
     * @param id - id to look for
     * @return - Response
     */
    RO findById(String id);

    /**
     * Finds all managed <code>E</code>.
     *
     * @return - Response
     */
    RL findAllManaged();

    /**
     * Find managed <code>E</code> by id.
     *
     * @param id - id to look for
     * @return - Response
     */
    RO findManagedById(String id);

    /**
     * Create <code>E</code>.
     *
     * @param entity - Object
     * @return
     */
    RO create(E entity);

    /**
     * Delete <code>E</code> by id.
     *
     * @param id - Entity id
     * @return
     */
    RO delete(String id);

    /**
     * Update <code>E</code>
     *
     * @param entity - Entity
     * @return
     */
    RO update(E entity);

    // TODO - search
}
