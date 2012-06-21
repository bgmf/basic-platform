package de.dzimmermann.rcp.pwm.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public class MenuCreatorAction extends Action {

	private AbstractHandler handler;

	public MenuCreatorAction(Class<? extends AbstractHandler> handlerClass,
			String name, String desc, int style, Image img) {

		super(name, style);

		setImageDescriptor(ImageDescriptor.createFromImage(img));
		setToolTipText(desc);
		setDescription(desc);

		try {
			handler = handlerClass.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void run() {
		if (handler != null)
			try {
				handler.execute(null);
			} catch (ExecutionException e) {
				throw new RuntimeException(e);
			}
	}
}
