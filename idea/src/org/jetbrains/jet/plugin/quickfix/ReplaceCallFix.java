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

package org.jetbrains.jet.plugin.quickfix;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.lang.psi.*;
import org.jetbrains.jet.plugin.JetBundle;

/**
 * @author svtk
 */
public class ReplaceCallFix implements IntentionAction {
    private final boolean toSafe;

    public ReplaceCallFix(boolean toSafe) {
        this.toSafe = toSafe;
    }


    @NotNull
    @Override
    public String getText() {
        return toSafe ? JetBundle.message("replace.with.safe.call") : JetBundle.message("replace.with.dot.call");
    }

    @NotNull
    @Override
    public String getFamilyName() {
        return toSafe ? JetBundle.message("replace.with.safe.call") : JetBundle.message("replace.with.dot.call");
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
        if (file instanceof JetFile) {
            return getCallExpression(editor, (JetFile) file) != null;
        }
        return false;
    }

    private JetQualifiedExpression getCallExpression(@NotNull Editor editor, @NotNull JetFile file) {
        final PsiElement elementAtCaret = file.findElementAt(editor.getCaretModel().getOffset());
        return PsiTreeUtil.getParentOfType(elementAtCaret, toSafe ? JetDotQualifiedExpression.class : JetSafeQualifiedExpression.class);
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        JetQualifiedExpression callExpression = getCallExpression(editor, (JetFile) file);
        assert callExpression != null;

        JetExpression selector = callExpression.getSelectorExpression();
        if (selector != null) {
            JetQualifiedExpression newElement = (JetQualifiedExpression) JetPsiFactory.createExpression(
                    project, callExpression.getReceiverExpression().getText() + (toSafe ? "?." : ".") + selector.getText());

            callExpression.replace(newElement);
        }
    }

    @Override
    public boolean startInWriteAction() {
        return true;
    }
}
