package de.dzimmermann.rcp.bsgtaucha.mgt.ui.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IMessage;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.dzimmermann.rcp.bsgtaucha.mgt.model.ObjectFactory;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.WorkType;

public class WorkDialog extends Dialog {

	private final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());

	private WorkType work;

	private Form parentForm;

	private Button affectBandButton;
	private Text nameText;
	private Text descrText;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public WorkDialog(Shell parentShell, WorkType work) {
		super(parentShell);
		if (work == null) {
			work = new ObjectFactory().createWorkType();
		} else {
			WorkType tmp = new ObjectFactory().createWorkType();
			tmp.setAffectBand(work.isAffectBand());
			tmp.setName(work.getName());
			tmp.setDescription(work.getDescription());
			work = tmp;
		}
		this.work = work;
	}

	public WorkType getwork() {
		return work;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		getShell().setText("Arbeitsdefinition anlegen oder anpassen");

		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		parentForm = formToolkit.createForm(container);
		formToolkit.paintBordersFor(parentForm);
		formToolkit.decorateFormHeading(parentForm);
		parentForm.setText("Arbeitsdefinition anlegen oder anpassen");
		parentForm.getBody().setLayout(new GridLayout(1, false));

		Composite composite = formToolkit.createComposite(parentForm.getBody(),
				SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		formToolkit.paintBordersFor(composite);
		composite.setLayout(new GridLayout(2, false));
		new Label(composite, SWT.NONE);

		affectBandButton = new Button(composite, SWT.CHECK);
		formToolkit.adapt(affectBandButton, true, true);
		affectBandButton.setText("Betrifft ein oder mehrere Bänder");

		Label nameLabel = formToolkit.createLabel(composite, "Bezeichnung",
				SWT.NONE);
		nameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		nameText = formToolkit.createText(composite, "", SWT.NONE);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		Label descrLabel = formToolkit.createLabel(composite,
				"Kurzbeschreibung", SWT.NONE);
		descrLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		descrText = formToolkit.createText(composite, "", SWT.NONE);
		GridData gd_firstnameText = new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1);
		gd_firstnameText.minimumWidth = 175;
		descrText.setLayoutData(gd_firstnameText);

		affectBandButton.setSelection(work.isAffectBand() != null ? work
				.isAffectBand() : false);
		nameText.setText(work.getName() != null ? work.getName() : "");
		descrText.setText(work.getDescription() != null ? work.getDescription()
				: "");

		ModifyListener modifyListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (e.widget == nameText) {
					work.setName(nameText.getText());
				} else if (e.widget == descrText) {
					work.setDescription(descrText.getText());
				}
			}
		};
		nameText.addModifyListener(modifyListener);
		descrText.addModifyListener(modifyListener);
		affectBandButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				work.setAffectBand(affectBandButton.getSelection());
			}
		});

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(475, 250);
	}

	@Override
	protected void okPressed() {

		if (work.getName() == null || work.getName().isEmpty()) {
			parentForm.setMessage("Geben Sie eine Bezeichnung ein!",
					IMessage.ERROR);
			return;
		}
		if (work.getDescription() == null || work.getDescription().isEmpty()) {
			parentForm.setMessage("Geben Sie einen Vornamen ein!",
					IMessage.WARNING);
			boolean agree = MessageDialog
					.openQuestion(
							getShell(),
							"Fortfahren?",
							"Sie haben keine Kurzbeschreibung eingegeben. Sind Sie sicher, dass Sie fortfahren möchten?");
			if (!agree)
				return;
		}

		super.okPressed();
	}
}
