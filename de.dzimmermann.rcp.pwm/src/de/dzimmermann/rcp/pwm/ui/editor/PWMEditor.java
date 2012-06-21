package de.dzimmermann.rcp.pwm.ui.editor;

import java.io.IOException;
import java.util.Collection;

import javax.xml.bind.JAXBException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import de.dzimmermann.rcp.basicplatform.ui.util.Logger;
import de.dzimmermann.rcp.basicplatform.util.DirtyEditorSupport;
import de.dzimmermann.rcp.basicplatform.util.ElementUpdateSupport;
import de.dzimmermann.rcp.pwm.model.container.PWMContainer;
import de.dzimmermann.rcp.pwm.model.container.PWMContainerGroup;
import de.dzimmermann.rcp.pwm.model.content.PWMGroup;
import de.dzimmermann.rcp.pwm.model.content.PWMGroupEntry;
import de.dzimmermann.rcp.pwm.ui.component.EditorComposite;
import de.dzimmermann.rcp.pwm.util.PWMUtils;

public class PWMEditor extends EditorPart implements DirtyEditorSupport,
		ElementUpdateSupport<Object> {

	public static final String ID = PWMEditor.class.getName();

	public PWMEditor() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

		monitor.beginTask("Save PWM Database", 1);

		try {
			PWMUtils.savePWMContainer(databaseFile, container);
		} catch (JAXBException e) {
			e.printStackTrace();
			Logger.logError(e);
		} catch (IOException e) {
			e.printStackTrace();
			Logger.logError(e);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.logError(e);
		}

		monitor.worked(1);
		monitor.done();
		setNotDirty();
	}

	@Override
	public void doSaveAs() {
	}

	private IPath databaseFile;
	private PWMContainer container;

	private EditorComposite composite;

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {

		setSite(site);
		setInput(input);

		if (input.getName() != null)
			setPartName(input.getName());
		if (input.getToolTipText() != null)
			setTitleToolTip(input.getToolTipText());
		if (input.getImageDescriptor() != null)
			setTitleImage(input.getImageDescriptor().createImage());

		databaseFile = (IPath) input.getAdapter(IPath.class);

		try {
			container = PWMUtils.loadPWMContainer(databaseFile);
		} catch (IOException e) {
			throw new PartInitException(e.getMessage(), e);
		} catch (CoreException e) {
			throw new PartInitException(e.getMessage(), e);
		} catch (JAXBException e) {
			throw new PartInitException(e.getMessage(), e);
		}
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
			firePropertyChange(PWMEditor.PROP_DIRTY);
		}
	}

	@Override
	public void setNotDirty() {
		if (dirty) {
			dirty = false;
			firePropertyChange(PWMEditor.PROP_DIRTY);
		}
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {

		parent.setLayout(new FillLayout(SWT.VERTICAL));

		composite = new EditorComposite(parent, SWT.NONE, container);
	}

	@Override
	public void setFocus() {
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if (IPath.class.isAssignableFrom(adapter))
			return databaseFile;
		else if (PWMContainer.class.isAssignableFrom(adapter))
			return container;
		else if (PWMContainerGroup.class.isAssignableFrom(adapter))
			return composite.getInternalAdapter(adapter);
		else if (PWMGroup.class.isAssignableFrom(adapter))
			return composite.getInternalAdapter(adapter);
		else if (PWMGroupEntry.class.isAssignableFrom(adapter))
			return composite.getInternalAdapter(adapter);
		else if (Boolean.class.isAssignableFrom(adapter))
			return new Boolean(composite.isCurrentlyShowsContent());
		else if (String.class.isAssignableFrom(adapter))
			return composite.getInternalAdapter(adapter);
		return super.getAdapter(adapter);
	}

	@Override
	public void updateElement(Object elementToUpdate) {
		if (elementToUpdate == null)
			return;
		else if (elementToUpdate instanceof PWMContainer)
			composite.setContainer(container);
		else if (elementToUpdate instanceof PWMGroup)
			composite.updateGroupContent((PWMGroup) elementToUpdate);
		else if (elementToUpdate instanceof StructuredSelection
				&& elementToUpdate.equals(StructuredSelection.EMPTY))
			composite.clearContent();
	}

	@Override
	public void updateElements(Collection<Object> elementsToUpdate) {
		if (elementsToUpdate != null && !elementsToUpdate.isEmpty())
			updateElement(elementsToUpdate.iterator().next());
	}
}
