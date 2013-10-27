/* 
 * Copyright 2013 Sharmarke Aden.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codepoint.test.web.util;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

/**
 *
 * @author saden
 */
public class JavaArchiveBuilder implements ArchiveBuilder {

    public static final String DEFAULT_JAR_NAME = "test.jar";

    private final Set<Class> classResources;
    private final Set<String> packageResources;
    private final Set<String> classPathResources;
    private final Set<String> manifestResources;
    private String archiveName;

    JavaArchiveBuilder() {
        this.classResources = new HashSet<>();
        this.packageResources = new HashSet<>();
        this.classPathResources = new HashSet<>();
        this.manifestResources = new HashSet<>();
        this.archiveName = DEFAULT_JAR_NAME;
    }

    public void setArchiveName(String archiveName) {
        this.archiveName = archiveName;
    }

    @Override
    public JavaArchiveBuilder addPackageResources(String... resources) {
        checkNotNull(resources, "resources parameter is null");
        packageResources.addAll(Arrays.asList(resources));

        return this;
    }

    @Override
    public JavaArchiveBuilder addClassPathResources(String... resources) {
        checkNotNull(resources, "resources parameter is null");
        classPathResources.addAll(Arrays.asList(resources));

        return this;
    }

    @Override
    public JavaArchiveBuilder addManifestResources(String... resources) {
        checkNotNull(resources, "resources parameter is null");
        manifestResources.addAll(Arrays.asList(resources));

        return this;
    }

    @Override
    public JavaArchiveBuilder addClassResources(Class... resources) {
        checkNotNull(resources, "resources parameter is null");
        classResources.addAll(Arrays.asList(resources));

        return this;
    }

    @Override
    public JavaArchive build() {
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class, archiveName);

        for (String resource : classPathResources) {
            archive.addAsResource(resource);
        }

        for (String resource : packageResources) {
            archive.addPackages(true, resource);
        }

        for (String resource : manifestResources) {
            archive.addAsManifestResource(resource);
        }

        for (Class resource : classResources) {
            archive.addClasses(resource);
        }

        return archive;
    }
}
