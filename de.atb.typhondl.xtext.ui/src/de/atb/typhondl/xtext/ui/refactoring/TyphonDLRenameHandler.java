package de.atb.typhondl.xtext.ui.refactoring;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.xtext.resource.EObjectAtOffsetHelper;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.utils.EditorUtils;
import org.eclipse.xtext.ui.refactoring.ui.DefaultRenameElementHandler;
import org.eclipse.xtext.ui.refactoring.ui.IRenameElementContext;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;

import com.google.inject.Inject;

import de.atb.typhondl.xtext.typhonDL.ClusterType;

/*******************************************************************************
 * Copyright (c) 2010, 2017 itemis AG (http://www.itemis.eu) and others.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
/**
 * modified by:
 * 
 * @author flug
 *
 */
@SuppressWarnings("restriction")
public class TyphonDLRenameHandler extends DefaultRenameElementHandler {

    @Inject
    private EObjectAtOffsetHelper eObjectAtOffsetHelper;

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        try {
            final XtextEditor editor = EditorUtils.getActiveXtextEditor(event);
            if (editor != null) {
                syncUtil.totalSync(preferences.isSaveAllBeforeRefactoring(),
                        renameRefactoringController.getActiveLinkedMode() == null);
                final ITextSelection selection = (ITextSelection) editor.getSelectionProvider().getSelection();
                IRenameElementContext renameElementContext = editor.getDocument()
                        .priorityReadOnly(new IUnitOfWork<IRenameElementContext, XtextResource>() {
                            @Override
                            public IRenameElementContext exec(XtextResource resource) throws Exception {
                                EObject selectedElement = eObjectAtOffsetHelper.resolveElementAt(resource,
                                        selection.getOffset());
                                if (selectedElement != null) {
                                    if (ClusterType.class.isInstance(selectedElement)) {
                                        if (!MessageDialog.openConfirm(editor.getShell(), "Refactor clustertype",
                                                "Refacoring the clustertype will change hosts and published ports.")) {
                                            return null;
                                        }
                                        ClusterTypeRefactor.refactor(selectedElement, editor, resource);
                                        return null;
                                    }
                                    IRenameElementContext renameElementContext = renameContextFactory
                                            .createRenameElementContext(selectedElement, editor, selection, resource);
                                    if (isRefactoringEnabled(renameElementContext, resource))
                                        return renameElementContext;
                                }
                                return null;
                            }
                        });
                if (renameElementContext != null) {
                    startRenameElement(renameElementContext);
                }
            }
        } catch (OperationCanceledException e) {
            // cancelled by user, ok
            return null;
        } catch (InterruptedException e) {
            // cancelled by user, ok
            return null;
        } catch (Exception exc) {
            LOG.error("Error initializing refactoring", exc);
            MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error initializing refactoring",
                    exc.getMessage() + "\nSee log for details");
        }
        return null;
    }
}
