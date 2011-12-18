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

import java.util.Iterator;
import java.util.Map.Entry;

/**
 * @version $Id: ServiceTypesIterator.java 26 2010-07-31 20:48:25Z simone.tripodi $
 * @param <S>
 */
final class ServiceTypesIterator<S>
    implements Iterator<Class<? extends S>>
{

    private final Iterator<Entry<String, Class<? extends S>>> knownServicesTypes;

    private final ServiceClassIterator<S> serviceClassIterator;

    public ServiceTypesIterator( Iterator<Entry<String, Class<? extends S>>> knownServicesTypes,
                                 ServiceClassIterator<S> serviceClassIterator )
    {
        this.knownServicesTypes = knownServicesTypes;
        this.serviceClassIterator = serviceClassIterator;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasNext()
    {
        if ( this.knownServicesTypes.hasNext() )
        {
            return true;
        }
        return this.serviceClassIterator.hasNext();
    }

    /**
     * {@inheritDoc}
     */
    public Class<? extends S> next()
    {
        if ( this.knownServicesTypes.hasNext() )
        {
            return this.knownServicesTypes.next().getValue();
        }
        return this.serviceClassIterator.next();
    }

    /**
     * {@inheritDoc}
     */
    public void remove()
    {
        throw new UnsupportedOperationException();
    }

}
