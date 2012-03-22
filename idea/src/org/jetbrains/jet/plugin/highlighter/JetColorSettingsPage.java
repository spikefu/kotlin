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

package org.jetbrains.jet.plugin.highlighter;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.plugin.JetIconProvider;

import javax.swing.*;
import java.util.Collections;
import java.util.Map;

public class JetColorSettingsPage implements ColorSettingsPage {
    @Override
    public Icon getIcon() {
        return JetIconProvider.KOTLIN_ICON;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new JetHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return "/* Block comment */\n" +
               "import kotlin.util.*\n" +
               "                               Bad characters: \\n #\n" +
               "val globalConst = 0\n" +
               "/**\n" +
               " * Doc comment here for `SomeClass`\n" +
               " * @see Iterator#next()\n" +
               " */\n" +
               "[Annotation]\n" +
               "public class SomeClass<out T : Runnable>(param : ATrait, reassignedParam : Array<Int>, val paramProperty: String?) { // some comment\n" +
               "  private field : T {\n" +
               "    return null\n" +
               "  }\n" +
               "  private open unusedField : Double = 12345.67890\n" +
               "  private anotherString : UnknownType = \"$field Another\\nStrin\\g\";\n" +
               "\n" +
               "  {\n" +
               "    paramProperty.?length ?: 33\n" +
               "    val localVal : Int = \"IntelliJ\" // Error, incompatible types\n" +
               "    println(anotherString + field + localVar + globalConst)\n" +
               "    val time = Date.parse(\"1.2.3\") // Method is deprecated\n" +
               "    var reassignedValue = \"\" as Int\n" +
               "    reassignedValue++\n" +
               "    object : Runnable {\n" +
               "      override fun() {\n" +
               "        val a = localVar\n" +
               "      }\n" +
               "    }\n" +
               "    reassignedParam = Array<Int>(2)\n" +
               "    reassignedParam[0] = 1\n" +
               "    reassignedParam.foreach @lit {\n" +
               "      if (it == 0) return@lit\n" +
               "      println(it + localVar)\n" +
               "    }\n" +
               "  }\n" +
               "}\n" +
               "trait ATrait {\n" +
               "  fun memberFun(param : (Int) -> Int)\n" +
               "}\n" +
               "abstract class SomeAbstractClass {\n" +
               "}";
    }

    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return Collections.emptyMap();
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors() {
        return new AttributesDescriptor[]{
            new AttributesDescriptor(JetHighlightingColors.JET_KEYWORD.getExternalName(), JetHighlightingColors.JET_KEYWORD)
        };
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Kotlin";
    }
}