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

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * A simple service-provider loading facility.
 *
 * @param <S> The type of the service to be loaded by this loader.
 * @version $Id: ServiceLoader.java 28 2010-07-31 21:09:10Z simone.tripodi $
 */
public final class ServiceLoader<S>
    implements Iterable<S>
{

    private static final String SERVICE_PREFIX = "META-INF/services/";

    /**
     * Creates a new service loader for the given service type, using the current thread's
     * {@linkplain java.lang.Thread#getContextClassLoader context class loader}.
     *
     * @param <S> The type of the service to be loaded by this loader.
     * @param service The class or interface representing the service being loaded.
     * @return A new service loader.
     */
    public static <S> ServiceLoader<S> load( Class<S> service )
    {
        return load( service, Thread.currentThread().getContextClassLoader() );
    }

    /**
     * Creates a new service loader for the given service type and class loader.
     *
     * @param <S> The type of the service to be loaded by this loader.
     * @param service The class or interface representing the service being loaded.
     * @param classLoader The class loader used to locate, load, and instantiate providers.
     * @return A new service loader.
     */
    public static <S> ServiceLoader<S> load( Class<S> service, ClassLoader classLoader )
    {
        if ( service == null )
        {
            throw new IllegalArgumentException( "Parameter 'service' must not be null" );
        }
        if ( classLoader == null )
        {
            throw new IllegalArgumentException( "Parameter 'classLoader' must not be null" );
        }
        return new ServiceLoader<S>( service, classLoader );
    }

    /**
     * Creates a new service loader for the given service type, using the extension class loader.
     *
     * @param <S> The type of the service to be loaded by this loader.
     * @param service The class or interface representing the service being loaded.
     * @return A new service loader.
     */
    public static <S> ServiceLoader<S> loadInstalled( Class<S> service )
    {
        ClassLoader parent = ClassLoader.getSystemClassLoader();
        ClassLoader current = null;
        while ( parent != null )
        {
            current = parent;
            parent = parent.getParent();
        }
        return load( service, current );
    }

    /**
     * Cached services types, in instantiation order.
     */
    private final LinkedHashMap<String, Class<? extends S>> servicesTypes =
        new LinkedHashMap<String, Class<? extends S>>();

    /**
     * Cached services, in instantiation order.
     */
    private final LinkedHashMap<Class<? extends S>, S> services = new LinkedHashMap<Class<? extends S>, S>();

    /**
     * The class or interface representing the service being loaded.
     */
    private final Class<S> service;

    /**
     * The class loader used to locate, load, and instantiate providers.
     */
    private final ClassLoader classLoader;

    /**
     * The current lazy-lookup class iterator.
     */
    private ServiceClassIterator<S> serviceClassIterator;

    /**
     * This class can't be instantiate directly, use static methods instead.
     *
     * @param service the class or interface representing the service being loaded.
     * @param classLoader the class loader used to locate, load, and instantiate providers.
     */
    private ServiceLoader( Class<S> service, ClassLoader classLoader )
    {
        this.service = service;
        this.classLoader = classLoader;
        this.reload();
    }

    /**
     * Clear this loader's provider cache so that all providers will be reloaded.
     */
    public void reload()
    {
        this.servicesTypes.clear();
        this.services.clear();

        String fullName = SERVICE_PREFIX + this.service.getName();
        try
        {
            Enumeration<URL> serviceResources = this.classLoader.getResources( fullName );
            this.serviceClassIterator =
                new ServiceClassIterator<S>( this.service, this.classLoader, serviceResources, this.servicesTypes );
        }
        catch ( IOException e )
        {
            throw new ServiceConfigurationError( "An error occurred while loading '" + fullName
                + "' Service resource(s) from clabackport-spiath", e );
        }
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<S> iterator()
    {
        return new ServiceInstancesIterator<S>( this.services.entrySet().iterator(),
                                                new ServiceIterator<S>( this.typesIterator(), this.services ) );
    }

    /**
     * Returns an iterator over a set of elements of type {@code Class<? extends S>}.
     *
     * @return an iterator over a set of elements of type {@code Class<? extends S>}.
     * @since 1.0.1
     */
    public Iterator<Class<? extends S>> typesIterator()
    {
        return new ServiceTypesIterator<S>( this.servicesTypes.entrySet().iterator(), this.serviceClassIterator );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return this.getClass().getName() + "[" + this.service.getName() + "]";
    }

}