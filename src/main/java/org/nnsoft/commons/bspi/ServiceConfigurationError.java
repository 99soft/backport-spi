/*
 *    Copyright 2010-2011 The 99 Software Foundation
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.nnsoft.commons.bspi;

/**
 * Error thrown when something goes wrong while loading a service provider.
 *
 * @version $Id: ServiceConfigurationError.java 20 2010-07-31 19:30:53Z simone.tripodi $
 */
public final class ServiceConfigurationError
    extends Error
{

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new instance with the specified message.
     *
     * @param message the message.
     * @since 1.0.1
     */
    public ServiceConfigurationError( String message )
    {
        super( message );
    }

    /**
     * Constructs a new instance with the specified message and cause.
     *
     * @param message the message.
     * @param cause the cause.
     */
    public ServiceConfigurationError( String message, Throwable cause )
    {
        super( message, cause );
    }

}
