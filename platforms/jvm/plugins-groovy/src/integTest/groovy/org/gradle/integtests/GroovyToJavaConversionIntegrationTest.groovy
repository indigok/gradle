/*
 * Copyright 2016 the original author or authors.
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

/*
 * Copyright 2016 the original author or authors.
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

package org.gradle.integtests

import org.gradle.integtests.fixtures.AbstractIntegrationSpec
import org.gradle.test.precondition.Requires
import org.gradle.test.preconditions.IntegTestPreconditions

class GroovyToJavaConversionIntegrationTest extends AbstractIntegrationSpec {

    @Requires(value = IntegTestPreconditions.NotEmbeddedExecutor, reason = "requires to fork a daemon for Class-Decoration")
    def "For every boolean is getter there is a get Getter"() {
        given:
        executer.requireDaemon().requireIsolatedDaemons() // We need to fork - if we do not fork Class-Decoration does not happen

        when:
        def convertedClasses = this.getClass().getResource( '/org/gradle/initialization/converted-types.txt' ).readLines()
        buildFile << """
            task checkHasGetters {
                doLast {
                    def classes = [${convertedClasses.collect { "${it}" }.join(',')}]
                    classes.each { convertedClass ->
                        def properties = org.gradle.internal.reflect.ClassInspector.inspect(convertedClass).properties
                        properties.each { prop ->
                            if (prop.getters.find { it.declaringClass == convertedClass }) {
                                println "Checking property \${prop.name} on \${convertedClass.name}"
                                if (convertedClass == org.gradle.plugins.ide.eclipse.model.AbstractClasspathEntry && prop.name == "exported") {
                                    return
                                }
                                if (convertedClass == org.gradle.plugins.signing.SigningExtension && prop.name == "required") {
                                    return
                                }
                                assert prop.getters.find { it.name.startsWith("get") }
                            }
                        }
                    }
                }
            }
        """

        then:
        succeeds 'checkHasGetters'
    }
}
