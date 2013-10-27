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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import com.google.common.base.Strings;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

/**
 *
 * @author saden
 */
public class WebArchiveBuilder implements ArchiveBuilder {

    private final Set<Class> classResources;
    private final Set<String> packageResources;
    private final Set<String> classPathResources;
    private final Set<String> manifestResources;
    private final Set<String> webInfResources;
    private final Set<String> libResources;
    private String webXmlFile;
    private String pomFile;
    private String archiveName;

    WebArchiveBuilder() {
        this.classResources = new HashSet<>();
        this.packageResources = new HashSet<>();
        this.classPathResources = new HashSet<>();
        this.manifestResources = new HashSet<>();
        this.webInfResources = new HashSet<>();
        this.libResources = new HashSet<>();
        this.archiveName = "test.war";
    }

    public void setArchiveName(String archiveName) {
        this.archiveName = archiveName;
    }

    @Override
    public WebArchiveBuilder addPackageResources(String... resources) {
        checkNotNull(resources, "resources parameter is null");
        packageResources.addAll(Arrays.asList(resources));

        return this;
    }

    @Override
    public WebArchiveBuilder addClassPathResources(String... resources) {
        checkNotNull(resources, "resources parameter is null");
        classPathResources.addAll(Arrays.asList(resources));

        return this;
    }

    public WebArchiveBuilder addWebInfResource(String... resources) {
        checkNotNull(resources, "resources parameter is null");
        webInfResources.addAll(Arrays.asList(resources));

        return this;
    }

    @Override
    public WebArchiveBuilder addClassResources(Class... resources) {
        checkNotNull(resources, "resources parameter is null");
        classResources.addAll(Arrays.asList(resources));

        return this;
    }

    @Override
    public WebArchiveBuilder addManifestResources(String... resources) {
        checkNotNull(resources, "resources parameter is null");
        manifestResources.addAll(Arrays.asList(resources));

        return this;
    }

    public WebArchiveBuilder addLibraries(String... resources) {
        checkNotNull(resources, "resources parameter is null");
        libResources.addAll(Arrays.asList(resources));

        return this;
    }

    public WebArchiveBuilder addWebXmlFile(String webXmlFile) {
        checkArgument(!Strings.isNullOrEmpty(webXmlFile), "webXmlFile parameter is null or empty");
        this.webXmlFile = webXmlFile;

        return this;
    }

    public WebArchiveBuilder addPomFile(String pomFile) {
        checkArgument(!Strings.isNullOrEmpty(pomFile), "pomFile parameter is null or empty");
        this.pomFile = pomFile;

        return this;
    }

    @Override
    public WebArchive build() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, archiveName);

        for (String resource : classPathResources) {
            archive.addAsResource(resource);
        }

        for (String resource : packageResources) {
            archive.addPackages(true, resource);
        }

        for (String resource : webInfResources) {
            archive.addAsWebInfResource(resource);
        }

        for (Class resource : classResources) {
            archive.addClasses(resource);
        }

        for (String resource : manifestResources) {
            archive.addAsManifestResource(resource);
        }

        for (String resource : libResources) {
            archive.addAsLibraries(resource);
        }

        checkState(!Strings.isNullOrEmpty(webXmlFile),
                "web.xml file is not set. Add it using addWebXmlResource(..) before calling build()");

        archive.setWebXML(webXmlFile);

        if (!Strings.isNullOrEmpty(pomFile)) {
            File[] libs = Maven.resolver()
                    .offline()
                    .loadPomFromFile(pomFile)
                    .importRuntimeDependencies()
                    .asFile();

            archive.addAsLibraries(libs);
        }

        return archive;
    }
}
