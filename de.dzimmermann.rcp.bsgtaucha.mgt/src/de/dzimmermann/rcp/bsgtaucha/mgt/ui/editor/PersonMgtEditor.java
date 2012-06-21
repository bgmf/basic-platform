package de.dzimmermann.rcp.bsgtaucha.mgt.ui.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import de.dzimmermann.rcp.basicplatform.ui.util.Logger;
import de.dzimmermann.rcp.basicplatform.util.DirtyEditorSupport;
import de.dzimmermann.rcp.basicplatform.util.ElementUpdateSupport;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.RootType;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.localfile.LocalFileModel;
import de.dzimmermann.rcp.bsgtaucha.mgt.ui.component.PersonComposite;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.BSGTauchaUtils;

public class PersonMgtEditor extends EditorPart implements DirtyEditorSupport,
		ElementUpdateSupport<LocalFileModel>, PropertyChangeListener {

	public static final String ID = PersonMgtEditor.class.getName();

	public PersonMgtEditor() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		save(monitor);
	}

	private final static Set<String> editors2update = new HashSet<String>();
	static {
		// editors2update.add(PersonMgtEditor.ID);
		editors2update.add(TasksMgtEditor.ID);
		editors2update.add(BandMgtEditor.ID);
		editors2update.add(WorkEntryEditor.ID);
	}

	@SuppressWarnings("unchecked")
	private void save(IProgressMonitor monitor) {
		try {
			BSGTauchaUtils.saveModel(model.getModel(), model.getFileStore()
					.toLocalFile(EFS.NONE, monitor));
			setNotDirty();
		} catch (Exception e) {
			Logger.logError(e);
			MessageDialog
					.openError(
							getSite().getShell(),
							"Fehler beim Speichern",
							"Beim Speichern Ihrer Änderungen sind Fehler aufgetreten. Bitte wenden Sie sich an den Entwickler.");
		}
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		for (IEditorReference editor : page.getEditorReferences()) {
			if (editors2update.contains(editor.getId())) {
				IEditorPart e = editor.getEditor(false);
				if (e != null && e instanceof ElementUpdateSupport) {
					((ElementUpdateSupport<LocalFileModel>) e)
							.updateElement(model);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateElement(LocalFileModel elementToUpdate) {

		if (elementToUpdate == null)
			return;

		model = elementToUpdate;
		((ElementUpdateSupport<RootType>) composite).updateElement(model
				.getModel());
	}

	@Override
	public void updateElements(Collection<LocalFileModel> elementsToUpdate) {
		if (elementsToUpdate == null || elementsToUpdate.isEmpty())
			return;
		updateElement(elementsToUpdate.iterator().next());
	}

	@Override
	public void doSaveAs() {
	}

	private LocalFileModel model;

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(input.getName());
		setTitleImage(input.getImageDescriptor() != null ? input
				.getImageDescriptor().createImage() : null);
		model = (LocalFileModel) input.getAdapter(LocalFileModel.class);
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	private PersonComposite composite;

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FormLayout());

		composite = new PersonComposite(parent, SWT.NONE, this,
				model != null ? model.getModel() : null);
		FormData fd_composite = new FormData();
		fd_composite.top = new FormAttachment(0);
		fd_composite.left = new FormAttachment(0);
		fd_composite.right = new FormAttachment(100);
		fd_composite.bottom = new FormAttachment(100);
		composite.setLayoutData(fd_composite);

		composite.addListener(this);
	}

	@Override
	public void setFocus() {

	}

	private boolean dirty = false;

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public void setDirty() {
		if (!dirty) {
			dirty = true;
			firePropertyChange(BandMgtEditor.PROP_DIRTY);
		}
	}

	@Override
	public void setNotDirty() {
		if (dirty) {
			dirty = false;
			firePropertyChange(BandMgtEditor.PROP_DIRTY);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (PersonComposite.ROOTMODEL_CHANGED_MESSAGE.equals(evt
				.getPropertyName())) {
			model.setModel((RootType) evt.getNewValue());
			setDirty();
		}
	}
}
