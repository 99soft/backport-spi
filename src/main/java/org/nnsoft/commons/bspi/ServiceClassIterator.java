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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;

/**
 * @param <S> The type of the service to be loaded by this loader.
 * @since 1.0.1
 */
class ServiceClassIterator<S>
    implements Iterator<Class<? extends S>>
{

    /**
     * The default <code>UTF-8</code> character encoding.
     */
    private static final Charset UTF_8 = Charset.forName( "UTF-8" );

    /**
     * The class or interface representing the service being loaded.
     */
    private final Class<S> service;

    /**
     * The class loader used to locate, load, and instantiate providers.
     */
    private final ClassLoader classLoader;

    private final Enumeration<URL> serviceResources;

    /**
     * Cached providers types, in instantiation order.
     */
    private final LinkedHashMap<String, Class<? extends S>> providerTypes;

    private Iterator<String> pending = null;

    private String nextName = null;

    /**
     * @param service the class or interface representing the service being loaded.
     * @param classLoader the class loader used to locate, load, and instantiate providers.
     * @param serviceResources
     * @param providerTypes cached providers types, in instantiation order.
     */
    public ServiceClassIterator( Class<S> service, ClassLoader classLoader, Enumeration<URL> serviceResources,
                                 LinkedHashMap<String, Class<? extends S>> providerTypes )
    {
        this.service = service;
        this.classLoader = classLoader;
        this.serviceResources = serviceResources;
        this.providerTypes = providerTypes;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasNext()
    {
        if ( this.nextName != null )
        {
            return true;
        }

        while ( ( this.pending == null ) || !this.pending.hasNext() )
        {
            if ( !serviceResources.hasMoreElements() )
            {
                return false;
            }
            this.pending = parseServiceFile( this.serviceResources.nextElement() );
        }

        this.nextName = this.pending.next();
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public Class<? extends S> next()
    {
        if ( !hasNext() )
        {
            throw new NoSuchElementException();
        }
        String className = this.nextName;
        this.nextName = null;
        try
        {
            Class<?> clazz = Class.forName( className, true, this.classLoader );
            if ( !this.service.isAssignableFrom( clazz ) )
            {
                throw new ServiceConfigurationError( "Provider '" + className + "' is not assignable to Service '"
                    + this.service.getName() + "'" );
            }
            Class<? extends S> serviceClass = clazz.asSubclass( this.service );
            this.providerTypes.put( className, serviceClass );
            return serviceClass;
        }
        catch ( ClassNotFoundException e )
        {
            throw new ServiceConfigurationError( "Provider '" + className + "' not found", e );
        }
        catch ( ClassCastException e )
        {
            throw new ServiceConfigurationError( "Provider '" + className + "' is not assignable to Service '"
                + this.service.getName() + "'", e );
        }
        catch ( Throwable e )
        {
            throw new ServiceConfigurationError( "Provider '" + className + "' could not be instantiated", e );
        }
    }

    /**
     * {@inheritDoc}
     */
    public void remove()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Parse the content of the given URL as a provider-configuration file.
     *
     * @param url the URL naming the configuration file to be parsed.
     * @return a (possibly empty) iterator that will yield the provider-class names in the given configuration file that
     *         are not yet members of the returned set
     */
    private Iterator<String> parseServiceFile( URL url )
    {
        InputStream inputStream = null;
        Reader reader = null;
        try
        {
            inputStream = url.openStream();
            reader = new InputStreamReader( inputStream, UTF_8 );
            ServiceFileParser<S> serviceFileParser = new ServiceFileParser<S>( reader );
            serviceFileParser.setProviderTypes( this.providerTypes );
            serviceFileParser.parse();
            return serviceFileParser.iterator();
        }
        catch ( Exception e )
        {
            throw new ServiceConfigurationError( "An error occurred while reading service resource '" + url
                + "' for service class '" + this.service.getName() + "'", e );
        }
        finally
        {
            closeQuietly( reader );
            closeQuietly( inputStream );
        }
    }

    /**
     * Unconditionally close a {@link Closeable} element.
     *
     * @param closeable the {@link Closeable} element.
     */
    private static void closeQuietly( Closeable closeable )
    {
        if ( closeable != null )
        {
            try
            {
                closeable.close();
            }
            catch ( IOException e )
            {
                // close quietly
            }
        }
    }

}
