package de.dzimmermann.rcp.basicplatform.ui.util.treewizard;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.dzimmermann.rcp.basicplatform.ui.util.treewizard.TreeWizard.ButtonType;
import de.dzimmermann.rcp.basicplatform.util.PFSCoreIconProvider;

/**
 * This is a simple wrapper around a {@link TreeWizard} to make use of it within
 * a {@link Dialog}.</br>You still need to implement the <code>TreeWizard</code>
 * but some means are already prepared for your need, as to fit the wizard into
 * the dialogs bounds.</br></br><b>NOTE:</b> This <code>Dialog</code> makes
 * contributions to the {@link FormToolkit} and may be accessed through the
 * {@link #getFormToolkit()} method.
 * 
 * @author dzimmermann
 */
public abstract class TreeWizardDialog extends Dialog {

	/**
	 * This enumeration contains the different types of possible results of this
	 * shell.
	 * 
	 * @author dzimmermann
	 * 
	 */
	public static enum ResultType {
		/**
		 * Default result type and default message type (no image will be
		 * printed).
		 */
		DEFAULT,
		/**
		 * Result type for the OK button.
		 */
		OK,
		/**
		 * Result type for the CANCEL button.
		 */
		CANCEL;
	}

	/**
	 * This enumeration is used to wrap around {@link IMessageProvider}
	 * constants.
	 * 
	 * @author dzimmermann
	 * 
	 */
	public static enum MessageType {
		/**
		 * Default message type: Display no icon.
		 */
		NONE(IMessageProvider.NONE),
		/**
		 * Message type for informations. A little info image will be displayed.
		 */
		INFORMATION(IMessageProvider.INFORMATION),
		/**
		 * Message type for warnings. A little warning image will be displayed.
		 */
		WARNING(IMessageProvider.WARNING),
		/**
		 * Message type for errors. A little error image will be displayed.
		 */
		ERROR(IMessageProvider.ERROR);

		private final int iMessageProviderType;

		private MessageType(int iMessageProviderType) {
			this.iMessageProviderType = iMessageProviderType;
		}

		public int getType() {
			return iMessageProviderType;
		}
	}

	private final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());

	private final Image okImg;
	private final Image cancelImg;

	private final Image backImg;
	private final Image forwardImg;

	private final Image infoImg;
	private final Image warningImg;
	private final Image errorImg;

	protected ResultType result = ResultType.DEFAULT;
	protected Shell shell;

	protected Form form;

	protected TreeWizard treeWizard;

	/**
	 * The title of the Dialogs shell.
	 * 
	 * @return the title of the Dialog
	 */
	public abstract String getWindowTitle();

	/**
	 * The title for the Dialog internal {@link Form}. It is comparable to the
	 * Area Title of a <code>TitleAreaDialog</code>.
	 * 
	 * @return the title of the {@link Form}
	 */
	public abstract String getFormTitle();

	/**
	 * If not specified (a.k.a. <code>null</code>), a default size of 640x480
	 * will be applied.
	 * 
	 * @return any specific size
	 */
	public abstract Point getInitialSize();

	/**
	 * Both size and location within one result. Since the default rule of the
	 * method {@link #getInitialSize()} is always applicable, and checked before
	 * this method, it is not absolutely necessary to provide bounds, but if you
	 * want to set a specific location.
	 * 
	 * @return the bounds of the Dilaog
	 */
	public abstract Rectangle getInitialBounds();

	/**
	 * Returns a {@link TreeWizard} created by using a predefined parent
	 * {@link Composite} (in this case the body of the Dialogs {@link Form}).
	 * 
	 * @param parent
	 *            the parent to create the {@link TreeWizard} on
	 * @return the {@link TreeWizard} of this dialog
	 */
	public abstract TreeWizard getInternalTreeWizard(Composite parent);

	// public abstract void createContents(Shell parentShell);

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public TreeWizardDialog(Shell parent, int style) {
		super(parent, style | SWT.DIALOG_TRIM);
		setText(getWindowTitle());
		okImg = PFSCoreIconProvider.getImageByIconName("fugue_tick.png", true); //$NON-NLS-1$
		cancelImg = PFSCoreIconProvider.getImageByIconName("fugue_cross.png", //$NON-NLS-1$
				true);
		backImg = PFSCoreIconProvider.getImageByIconName("fugue_arrow-180.png", //$NON-NLS-1$
				true);
		forwardImg = PFSCoreIconProvider.getImageByIconName(
				"fugue_arrow.png", true); //$NON-NLS-1$
		infoImg = PFSCoreIconProvider.getImageByIconName("info_obj.gif", true); //$NON-NLS-1$
		warningImg = PFSCoreIconProvider.getImageByIconName("showwarn_tsk.gif", //$NON-NLS-1$
				true);
		errorImg = PFSCoreIconProvider
				.getImageByIconName("error_obj.gif", true); //$NON-NLS-1$
	}

	/**
	 * Open the dialog. If {@link #getInitialSize()} returnes <code>null</code>,
	 * a default size of 640x480 pixel is used instead.
	 * 
	 * @return the result
	 */
	public ResultType open() {

		shell = new Shell(getParent(), getStyle());

		Point point = getInitialSize();
		shell.setSize(point == null ? new Point(640, 480) : point);

		Rectangle rect = getInitialBounds();
		if (rect != null) {
			// shell.setBounds(rect);
			shell.setLocation(rect.x, rect.y);
			shell.setSize(rect.width, rect.height);
		}

		shell.setText(getText());

		createContent();
		// createContents(shell);

		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		return result;
	}

	protected void close() {
		close(ResultType.DEFAULT);
	}

	protected void close(ResultType result) {
		this.result = result;
		shell.dispose();
	}

	private void createContent() {

		shell.setLayout(new FillLayout());

		Composite container = new Composite(shell, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		formToolkit.adapt(container);
		// formToolkit.paintBordersFor(container);

		form = formToolkit.createForm(container);
		formToolkit.decorateFormHeading(form);
		// formToolkit.paintBordersFor(form);
		form.setText(getFormTitle());
		form.getBody().setLayout(new FillLayout());

		treeWizard = getInternalTreeWizard(form.getBody());
		treeWizard.setProgressBarVisible(false);
		treeWizard.adaptEclipseForms();

		Button b = treeWizard.getButton(ButtonType.OK);
		if (b != null)
			b.setImage(okImg);
		b = treeWizard.getButton(ButtonType.CANCEL);
		if (b != null)
			b.setImage(cancelImg);
		b = treeWizard.getButton(ButtonType.NEXT);
		if (b != null)
			b.setImage(forwardImg);
		b = treeWizard.getButton(ButtonType.BACK);
		if (b != null)
			b.setImage(backImg);

		setMessage(MessageType.NONE, ""); //$NON-NLS-1$
	}

	/**
	 * This method is used to display a message within the message area of the
	 * {@link Shell}s {@link Form}.
	 * 
	 * @param type
	 *            The type of the message.
	 *            <p>
	 *            See
	 *            <ul>
	 *            <li>{@link #DEFAULT}</li>
	 *            <li>{@link #INFO}</li>
	 *            <li>{@link #WARNING}</li>
	 *            <li>{@link #ERROR}</li>
	 *            </ul>
	 *            </p>
	 * @param message
	 *            The message that should be displayed.
	 */
	public void setMessage(MessageType type, String message) {
		form.setMessage(message, type.getType());
	}

	public Shell getShell() {
		return shell;
	}

	public ResultType getResult() {
		return result;
	}

	public FormToolkit getFormToolkit() {
		return formToolkit;
	}

	public Form getForm() {
		return form;
	}

	public TreeWizard getTreeWizard() {
		return treeWizard;
	}

	public Image getOkImg() {
		return okImg;
	}

	public Image getCancelImg() {
		return cancelImg;
	}

	public Image getForwardImg() {
		return forwardImg;
	}

	public Image getBackImg() {
		return backImg;
	}

	public Image getInfoImg() {
		return infoImg;
	}

	public Image getWarningImg() {
		return warningImg;
	}

	public Image getErrorImg() {
		return errorImg;
	}

	public Button getButton(ButtonType type) {
		if (treeWizard != null)
			return treeWizard.getButton(type);
		else
			return null;
	}
}
