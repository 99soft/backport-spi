package org.nnsoft.commons.bspi;

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

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * @param <S> The type of the service to be loaded by this loader.
 */
final class ServiceIterator<S>
    implements Iterator<S>
{

    private final Iterator<Class<? extends S>> typesIterator;

    private final LinkedHashMap<Class<? extends S>, S> providers;

    public ServiceIterator( Iterator<Class<? extends S>> typesIterator, LinkedHashMap<Class<? extends S>, S> providers )
    {
        this.typesIterator = typesIterator;
        this.providers = providers;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasNext()
    {
        return typesIterator.hasNext();
    }

    /**
     * {@inheritDoc}
     */
    public S next()
    {
        Class<? extends S> serviceClass = typesIterator.next();
        try
        {
            S provider = serviceClass.newInstance();
            providers.put( serviceClass, provider );
            return provider;
        }
        catch ( Exception e )
        {
            throw new ServiceConfigurationError( "Provider '" + serviceClass.getName() + "' could not be instantiated",
                                                 e );
        }
    }

    /**
     * {@inheritDoc}
     */
    public void remove()
    {
        throw new UnsupportedOperationException();
    }

}
