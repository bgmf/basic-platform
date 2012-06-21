package de.dzimmermann.rcp.basicplatform.ui.component;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.dzimmermann.rcp.basicplatform.util.DirtyEditorSupport;

/**
 * This helper class can be used to create a {@link Composite} which is
 * adaptable to a {@link FormToolkit}, that means, you can decide, whether you
 * want to use it in a EclipseForms look and feel or a common SWT one.
 * 
 * @author danielz
 * @version 0.1
 * @since PFSRCPCore V0.2.4
 */
public abstract class CompositeToFormAdapter implements DirtyEditorSupport {

	/**
	 * This private flag decides, whether the current composite is allready
	 * "dirty", in a Eclipse editor language that means, a save is necessarry.
	 */
	protected boolean dirty = false;

	/**
	 * Use a parent component - most likely an Eclipse editor - to set it's
	 * dirty state. The target editor of couse need to implement
	 * {@link DirtyEditorSupport} as well. Addtionally, use the
	 * {@link CompositeToFormAdapter}s {@link #setDirty()} and
	 * {@link #setNotDirty()} methods in you implementation of the
	 * {@link #createContent()} method, if necessarry.
	 */
	protected final DirtyEditorSupport parentComponent;

	/**
	 * This is th composite, which is to display either in a EclispeForms or SWT
	 * context.
	 */
	protected Composite compositeToAdapt;

	/**
	 * This {@link FormToolkit} is used to create the EclipseForms look and
	 * feel, when it's not <code>null</code>.<br>
	 * see {@link #adaptComposite()}
	 */
	protected final FormToolkit formToolkit;

	/**
	 * This is the default constructor where all necessarry information is
	 * passed to this class.<br>
	 * It depends on your needs, if you use a {@link FormToolkit}, a
	 * {@link DirtyEditorSupport} or at least a boolean that defines, that the
	 * parent is allready dirty, but there must always be a {@link Composite}
	 * (see {@link #parentComponent}) present! If no {@link Composite} is
	 * specified, you will receive an {@link IllegalArgumentException}!
	 * 
	 * @param compositeToAdapt
	 *            The {@link Composite} on which the content will appear.
	 * @param formToolkit
	 *            If not <code>null</code>, this {@link FormToolkit} will be
	 *            used to give the {@link #compositeToAdapt} the EclipseForms
	 *            look and feel.<br>
	 *            see {@link #adaptComposite()}
	 * @param parentComponent
	 *            If there's a parent implementing the
	 *            {@link DirtyEditorSupport} interface, you can pass it to this
	 *            class.
	 * @param parentComponentIsDirty
	 *            Predefine the dirty-state of this class (see {@link #dirty},
	 *            {@link #setDirty()} and {@link #setNotDirty()})
	 * 
	 * @throws IllegalArgumentException
	 *             When no {@link Composite} was given (<code>null</code> )
	 */
	public CompositeToFormAdapter(Composite compositeToAdapt,
			FormToolkit formToolkit, DirtyEditorSupport parentComponent,
			boolean parentComponentIsDirty) throws IllegalArgumentException {

		if (this.compositeToAdapt == null)
			throw new IllegalArgumentException(
					"You need to specify a composite on which you can place your widgets!");

		this.compositeToAdapt = compositeToAdapt;
		this.formToolkit = formToolkit;
		this.parentComponent = parentComponent;

		if (parentComponentIsDirty)
			setDirty();

		createContent();
	}

	/**
	 * This constructor uses the defaul one, by creating a {@link FormToolkit}
	 * on demand by interpreting the useFormToolkit parameter.<br>
	 * Be aware, that you will receive a {@link IllegalArgumentException} if you
	 * do not pass a {@link Composite} to this class.
	 * 
	 * @param compositeToAdapt
	 *            The {@link Composite} on which the content will appear.
	 * @param useFormToolkit
	 *            If this parameter is set to <code>true</code>, a new
	 *            {@link FormToolkit} will be created and passed to this class,
	 *            otherwise will be left as <code>null</code><br>
	 *            see {@link #adaptComposite()}
	 * @param parentComponent
	 *            If there's a parent implementing the
	 *            {@link DirtyEditorSupport} interface, you can pass it to this
	 *            class.
	 * @param parentComponentIsDirty
	 *            Predefine the dirty-state of this class (see {@link #dirty},
	 *            {@link #setDirty()} and {@link #setNotDirty()})
	 * 
	 * @throws IllegalArgumentException
	 *             When no {@link Composite} was given (<code>null</code> )
	 */
	public CompositeToFormAdapter(Composite compositeToAdapt,
			boolean useFormToolkit, DirtyEditorSupport parentComponent,
			boolean parentComponentIsDirty) throws IllegalArgumentException {
		this(compositeToAdapt, useFormToolkit ? new FormToolkit(Display
				.getDefault()) : null, parentComponent, parentComponentIsDirty);
	}

	/**
	 * This is a more simple constructor, ignoring a parents possible
	 * {@link DirtyEditorSupport} implementation. Most likely in this case you
	 * need no references to the {@link #isDirty()}, {@link #setDirty()} and
	 * {@link #setNotDirty()} methods.<br>
	 * This contructor passes a allready existing {@link FormToolkit} to this
	 * class or can set it to <code>null</code> if no {@link FormToolkit} is
	 * needed. <br>
	 * Be aware, that you will receive a {@link IllegalArgumentException} if you
	 * do not pass a {@link Composite} to this class.
	 * 
	 * @param compositeToAdapt
	 *            The {@link Composite} on which the content will appear.
	 * @param formToolkit
	 *            If not <code>null</code>, this {@link FormToolkit} will be
	 *            used to give the {@link #compositeToAdapt} the EclipseForms
	 *            look and feel.<br>
	 *            see {@link #adaptComposite()}
	 * 
	 * @throws IllegalArgumentException
	 *             When no {@link Composite} was given (<code>null</code> )
	 */
	public CompositeToFormAdapter(Composite compositeToAdapt,
			FormToolkit formToolkit) throws IllegalArgumentException {
		this(compositeToAdapt, formToolkit, null, false);
	}

	/**
	 * This is a more simple constructor, ignoring a parents possible
	 * {@link DirtyEditorSupport} implementation. Most likely in this case you
	 * need no references to the {@link #isDirty()}, {@link #setDirty()} and
	 * {@link #setNotDirty()} methods.<br>
	 * This constructor need no passed {@link FormToolkit}, because it will
	 * create one on demand by interpreting the useFormToolkit parameter. <br>
	 * Be aware, that you will receive a {@link IllegalArgumentException} if you
	 * do not pass a {@link Composite} to this class.
	 * 
	 * @param compositeToAdapt
	 *            The {@link Composite} on which the content will appear.
	 * @param formToolkit
	 *            If not <code>null</code>, this {@link FormToolkit} will be
	 *            used to give the {@link #compositeToAdapt} the EclipseForms
	 *            look and feel.<br>
	 *            see {@link #adaptComposite()}
	 * 
	 * @throws IllegalArgumentException
	 *             When no parent {@link Composite} was given (<code>null</code>
	 *             ) <br>
	 *             Be aware, that you will receive a
	 *             {@link IllegalArgumentException} if you do not pass a parent
	 *             Composite to this class.
	 * 
	 * @param compositeToAdapt
	 *            The parent {@link Composite} on which the content will appear.
	 * @param useFormToolkit
	 *            If this parameter is set to <code>true</code>, a new
	 *            {@link FormToolkit} will be created and passed to this class,
	 *            otherwise will be left as <code>null</code><br>
	 *            see {@link #adaptComposite()}
	 * 
	 * @throws IllegalArgumentException
	 *             When no {@link Composite} was given (<code>null</code> )
	 */
	public CompositeToFormAdapter(Composite compositeToAdapt,
			boolean useFormToolkit) throws IllegalArgumentException {
		this(compositeToAdapt, useFormToolkit ? new FormToolkit(Display
				.getDefault()) : null, null, false);
	}

	/**
	 * Set this {@link CompositeToFormAdapter} to be dirty, most likely to be
	 * used in you own implementation within the {@link #createContent()}
	 * method.
	 */
	@Override
	public void setDirty() {
		if (!dirty) {
			dirty = true;
			if (parentComponent != null)
				parentComponent.setDirty();
		}
	}

	/**
	 * Set this {@link CompositeToFormAdapter} to be not dirty, most likely to
	 * be used in you own implementation within the {@link #createContent()}
	 * method.
	 */
	@Override
	public void setNotDirty() {
		if (dirty) {
			dirty = false;
			if (parentComponent != null)
				parentComponent.setNotDirty();
		}
	}

	/**
	 * This method simply returns the dirty state of this class by returning the
	 * current {@link #dirty} value.
	 * 
	 * @return the current {@link #dirty} state
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * This method can be used to obtain the composite on which the content
	 * should appear. The use is recommended only for the
	 * {@link #createContent()} method.
	 * 
	 * @return the composite to which the content will be added to
	 */
	protected Composite getCompositeToAdapt() {
		return compositeToAdapt;
	}

	/**
	 * This method created the {@link FormToolkit} adaption, if the
	 * {@link #formToolkit} attribute is not <code>null</code>.
	 * 
	 * @return returns the plain {@link Composite} (simple SWT optics) when the
	 *         {@link #formToolkit} parameter is <code>null</code>, otherwise it
	 *         will try to render the composite in a EclipseForms way
	 */
	public Composite adaptComposite() {

		if (formToolkit != null) {

			formToolkit.adapt(compositeToAdapt);
			formToolkit.paintBordersFor(compositeToAdapt);

			if (compositeToAdapt.getChildren().length > 0)
				adaptCompositesChildren(formToolkit, compositeToAdapt
						.getChildren());
		}

		return compositeToAdapt;
	}

	/**
	 * This static method created the {@link FormToolkit} adaption, if the
	 * formToolkit parameter is not <code>null</code>.
	 * 
	 * @param compositeToAdapt
	 *            the {@link Composite} to adapt to EclipseForms
	 * @param formToolkit
	 *            the {@link FormToolkit} which will process the adaption
	 * 
	 * @return returns the plain {@link Composite} (simple SWT optics) when the
	 *         {@link #formToolkit} parameter is <code>null</code>, otherwise it
	 *         will try to render the composite in a EclipseForms way
	 */
	public static Composite adaptComposite(Composite compositeToAdapt,
			FormToolkit formToolkit) {

		if (formToolkit != null) {

			formToolkit.adapt(compositeToAdapt);
			formToolkit.paintBordersFor(compositeToAdapt);

			if (compositeToAdapt.getChildren().length > 0)
				adaptCompositesChildren(formToolkit, compositeToAdapt
						.getChildren());
		}

		return compositeToAdapt;
	}

	/**
	 * This is a local helper method to cecursivley adapt all given children of
	 * an other {@link Composite}.
	 * 
	 * @param children
	 *            the array of children, passed to this method from another
	 *            composite
	 */
	private static void adaptCompositesChildren(FormToolkit formToolkit,
			Control[] children) {

		for (Control c : children) {

			if (c instanceof Composite) {

				if (((Composite) c).getChildren().length > 0)
					adaptCompositesChildren(formToolkit, ((Composite) c)
							.getChildren());

				formToolkit.adapt((Composite) c);
				formToolkit.paintBordersFor((Composite) c);

			} else
				formToolkit.adapt(c, true, true);
		}
	}

	/**
	 * This method need to be implemented by classes which extends this
	 * {@link CompositeToFormAdapter} class.<br>
	 * The content of the {@link #compositeToAdapt} will be created here.
	 */
	protected abstract void createContent();
}
