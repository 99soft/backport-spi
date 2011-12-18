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
 * @version $Id: ServiceInstancesIterator.java 25 2010-07-31 20:47:21Z simone.tripodi $
 * @param <S>
 */
final class ServiceInstancesIterator<S>
    implements Iterator<S>
{

    private final Iterator<Entry<Class<? extends S>, S>> knownProviders;

    private final ServiceIterator<S> serviceIterator;

    public ServiceInstancesIterator( Iterator<Entry<Class<? extends S>, S>> knownProviders,
                                     ServiceIterator<S> serviceIterator )
    {
        this.knownProviders = knownProviders;
        this.serviceIterator = serviceIterator;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasNext()
    {
        if ( this.knownProviders.hasNext() )
        {
            return true;
        }
        return this.serviceIterator.hasNext();
    }

    /**
     * {@inheritDoc}
     */
    public S next()
    {
        if ( this.knownProviders.hasNext() )
        {
            return this.knownProviders.next().getValue();
        }
        return this.serviceIterator.next();
    }

    /**
     * {@inheritDoc}
     */
    public void remove()
    {
        throw new UnsupportedOperationException();
    }

}
