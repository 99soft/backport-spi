/*
 *    Copyright 2010 The backport-spi Team
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
package com.googlecode.bspi;

import org.testng.annotations.Test;

import com.googlecode.bspi.ServiceLoader;
import com.googlecode.bspi.ServiceConfigurationError;

/**
 * 
 * @version $Id: ServiceLoaderTestCase.java 29 2010-07-31 21:13:11Z simone.tripodi $
 */
public final class ServiceLoaderTestCase {

    @Test
    public void verifyServiceLoading() throws Exception {
        ServiceLoader<TestService> testServiceLoader = ServiceLoader.load(TestService.class);

        assert testServiceLoader.typesIterator().next() != null;
        assert testServiceLoader.typesIterator().next() != null;

        assert testServiceLoader.iterator().next() != null;
        assert testServiceLoader.iterator().next() != null;
    }

    @Test(expectedExceptions = { ServiceConfigurationError.class })
    public void serviceNotFound() throws Exception {
        ServiceLoader<TestService2> testService2Loader = ServiceLoader.load(TestService2.class);
        testService2Loader.iterator().next();
    }

    @Test(expectedExceptions = { ServiceConfigurationError.class })
    public void notAssignableServiceLoading() throws Exception {
        ServiceLoader<TestService3> testServiceLoader = ServiceLoader.load(TestService3.class);
        testServiceLoader.iterator().next();
    }

}