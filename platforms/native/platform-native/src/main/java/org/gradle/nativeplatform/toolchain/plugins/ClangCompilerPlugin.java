/*
 * Copyright 2014 the original author or authors.
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

package org.gradle.nativeplatform.toolchain.plugins;

import org.gradle.api.Incubating;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.internal.Cast;
import org.gradle.nativeplatform.plugins.NativeComponentPlugin;
import org.gradle.nativeplatform.toolchain.Clang;
import org.gradle.nativeplatform.toolchain.NativeToolChainRegistry;
import org.gradle.nativeplatform.toolchain.internal.NativeToolChainRegistryInternal;
import org.gradle.nativeplatform.toolchain.internal.clang.ClangToolChain;
import org.jspecify.annotations.NullMarked;

/**
 * A {@link Plugin} which makes the <a href="http://clang.llvm.org">Clang</a> compiler available for compiling C/C++ code.
 */
@Incubating
@NullMarked
public abstract class ClangCompilerPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(NativeComponentPlugin.class);
        NativeToolChainRegistryInternal toolChainRegistry = Cast.uncheckedCast(project.getExtensions().getByType(NativeToolChainRegistry.class));
        toolChainRegistry.registerBinding(Clang.class, ClangToolChain.class);
        toolChainRegistry.registerDefaultToolChain(ClangToolChain.DEFAULT_NAME, Clang.class);
    }
}
