package de.dzimmermann.rcp.basicplatform.ui.util;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

import de.dzimmermann.rcp.basicplatform.model.Task;
import de.dzimmermann.rcp.basicplatform.util.PFSCoreIconProvider;

public class TaskTreeLabelProvider extends CellLabelProvider implements
		ILabelProvider, IColorProvider, IFontProvider {

	public TaskTreeLabelProvider() {
	}

	// default cell text

	@Override
	public Image getImage(Object element) {

		Task task = (Task) element;

		if (task.getImage() != null) {
			return task.getImage();
		}

		if (task.getImageString() != null && !task.getImageString().isEmpty())
			try {
				return PFSCoreIconProvider.getImageByIconName(
						task.getImageString(), task.isEnabled());
			} catch (Exception e) {
				String message = "Could not load the Image "
						+ task.getImageString()
						+ " from a TaskTree-based plugin.\nMessage was: "
						+ e.getMessage();
				Logger.logWarning(message);
			}

		return null;
	}

	@Override
	public String getText(Object element) {
		return ((Task) element).getName();
	}

	@Override
	public Color getBackground(Object element) {
		return null;
	}

	@Override
	public Color getForeground(Object element) {
		return null;
	}

	// font for the cell

	@Override
	public Font getFont(Object element) {
		return null;
	}

	// tooltip for the current cell

	@Override
	public int getToolTipDisplayDelayTime(Object object) {
		return 2;
	}

	@Override
	public int getToolTipTimeDisplayed(Object object) {
		return 0;
	}

	@Override
	public Point getToolTipShift(Object object) {
		return super.getToolTipShift(object);
	}

	@Override
	public int getToolTipStyle(Object object) {
		return SWT.SHADOW_NONE;
	}

	@Override
	public String getToolTipText(Object element) {
		String description = ((Task) element).getDescription();
		return description != null && !description.isEmpty() ? description
				: ((Task) element).getName();
	}

	@Override
	public Image getToolTipImage(Object object) {
		return getImage(object);
	}

	@Override
	public Font getToolTipFont(Object object) {
		return super.getToolTipFont(object);
	}

	@Override
	public Color getToolTipBackgroundColor(Object object) {
		return super.getToolTipBackgroundColor(object);
	}

	@Override
	public Color getToolTipForegroundColor(Object object) {
		return super.getToolTipForegroundColor(object);
	}

	@Override
	public void update(ViewerCell cell) {
		cell.setBackground(getBackground(cell.getElement()));
		cell.setFont(getFont(cell.getElement()));
		cell.setForeground(getForeground(cell.getElement()));
		cell.setImage(getImage(cell.getElement()));
		// XXX it is unused and creates errors in RAP
		// cell.setStyleRanges(null);
		cell.setText(getText(cell.getElement()));
	}

	// other stuff

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

}
