/*
 * Copyright 2010-2012 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.jet.codegen.intrinsics;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jet.codegen.ExpressionCodegen;
import org.jetbrains.jet.codegen.JetTypeMapper;
import org.jetbrains.jet.codegen.StackValue;
import org.jetbrains.jet.lang.psi.JetCallExpression;
import org.jetbrains.jet.lang.psi.JetExpression;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.InstructionAdapter;

import java.util.Arrays;
import java.util.List;

public class StupidSync implements IntrinsicMethod {
    @Override
    public StackValue generate(ExpressionCodegen codegen, InstructionAdapter v, Type expectedType, @Nullable PsiElement element, @Nullable List<JetExpression> arguments, StackValue receiver) {
        codegen.pushMethodArguments((JetCallExpression)element, Arrays.asList(JetTypeMapper.TYPE_OBJECT, JetTypeMapper.TYPE_FUNCTION0));
        v.invokestatic("jet/runtime/Intrinsics", "stupidSync", "(Ljava/lang/Object;Ljet/Function0;)Ljava/lang/Object;");
        StackValue.onStack(JetTypeMapper.TYPE_OBJECT).put(expectedType, v);
        return StackValue.onStack(expectedType);
    }
}