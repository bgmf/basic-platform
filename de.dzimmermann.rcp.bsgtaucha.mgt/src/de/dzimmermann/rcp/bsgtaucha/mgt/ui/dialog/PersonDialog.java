package de.dzimmermann.rcp.bsgtaucha.mgt.ui.dialog;

import java.util.Calendar;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
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
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IMessage;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import de.dzimmermann.rcp.bsgtaucha.mgt.model.ObjectFactory;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.PersonType;

public class PersonDialog extends Dialog {

	private final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());

	private PersonType person;

	private Form parentForm;

	private Text nameText;
	private Text firstnameText;
	private Text streetText;
	private Text postalcodeText;
	private Text cityText;

	private Text telephoneText;
	private Text mobileText;
	private Text faxText;
	private Text emailText;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public PersonDialog(Shell parentShell, PersonType person) {
		super(parentShell);
		if (person == null) {
			person = new ObjectFactory().createPersonType();
		} else {
			PersonType tmp = new ObjectFactory().createPersonType();
			tmp.setName(person.getName());
			tmp.setFirstname(person.getFirstname());
			tmp.setStreet(person.getStreet());
			tmp.setPostalcode(person.getPostalcode());
			tmp.setCity(person.getCity());
			tmp.setBirthday(person.getBirthday());
			tmp.setActiveMember(person.isActiveMember());
			tmp.setPhone(person.getPhone());
			tmp.setMobile(person.getMobile());
			tmp.setFax(person.getFax());
			tmp.setEmail(person.getEmail());
			person = tmp;
		}
		this.person = person;
	}

	public PersonType getPerson() {
		return person;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		getShell().setText("Person anlegen oder anpassen");

		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		parentForm = formToolkit.createForm(container);
		formToolkit.paintBordersFor(parentForm);
		formToolkit.decorateFormHeading(parentForm);
		parentForm.setText("Person anlegen oder anpassen");
		parentForm.getBody().setLayout(new GridLayout(1, false));

		Section baseSection = formToolkit.createSection(parentForm.getBody(),
				Section.TITLE_BAR);
		baseSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		formToolkit.paintBordersFor(baseSection);
		baseSection.setText("Basisdaten");

		Composite composite = formToolkit
				.createComposite(baseSection, SWT.NONE);
		formToolkit.paintBordersFor(composite);
		baseSection.setClient(composite);
		composite.setLayout(new GridLayout(4, false));

		Label nameLabel = formToolkit.createLabel(composite, "Name", SWT.NONE);
		nameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		nameText = formToolkit.createText(composite, "", SWT.BORDER);
		nameText.setText("");
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		Label firstnameLabel = formToolkit.createLabel(composite, "Vorname",
				SWT.NONE);
		firstnameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		firstnameText = formToolkit.createText(composite, "", SWT.BORDER);
		GridData gd_firstnameText = new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1);
		gd_firstnameText.minimumWidth = 175;
		firstnameText.setLayoutData(gd_firstnameText);

		Label streetLabel = formToolkit.createLabel(composite, "Strasse",
				SWT.NONE);
		streetLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		streetText = formToolkit.createText(composite, "", SWT.BORDER);
		streetText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Label postalcodeLabel = formToolkit.createLabel(composite, "PLZ",
				SWT.NONE);
		postalcodeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));

		postalcodeText = formToolkit.createText(composite, "", SWT.BORDER);
		postalcodeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label cityLabel = formToolkit.createLabel(composite, "Ort", SWT.NONE);
		cityLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		cityText = formToolkit.createText(composite, "", SWT.BORDER);
		cityText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		Label birthdayLabel = formToolkit.createLabel(composite, "Geburtstag",
				SWT.NONE);
		birthdayLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
				false, 1, 1));

		final DateTime dateTime = new DateTime(composite, SWT.BORDER
				| SWT.CALENDAR);
		formToolkit.adapt(dateTime);
		formToolkit.paintBordersFor(dateTime);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		formToolkit.adapt(composite);
		formToolkit.paintBordersFor(composite);

		Section advancedSection = formToolkit.createSection(
				parentForm.getBody(), Section.TITLE_BAR);
		advancedSection.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		formToolkit.paintBordersFor(advancedSection);
		advancedSection.setText("erweiterte Daten");

		Composite advancedComposite = formToolkit.createComposite(
				advancedSection, SWT.NONE);
		formToolkit.paintBordersFor(advancedComposite);
		advancedSection.setClient(advancedComposite);
		advancedComposite.setLayout(new GridLayout(2, false));

		new Label(advancedComposite, SWT.NONE);

		final Button activeMemberCheckButton = new Button(advancedComposite,
				SWT.CHECK);
		formToolkit.adapt(activeMemberCheckButton, true, true);
		activeMemberCheckButton.setText("Aktives Mitglied");

		Label telephoneLabel = formToolkit.createLabel(advancedComposite,
				"Telefon", SWT.NONE);
		telephoneLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
				false, 1, 1));

		telephoneText = formToolkit.createText(advancedComposite, "",
				SWT.BORDER);
		telephoneText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label mobileLabel = formToolkit.createLabel(advancedComposite, "Handy",
				SWT.NONE);
		mobileLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
				false, 1, 1));

		mobileText = formToolkit.createText(advancedComposite, "", SWT.BORDER);
		gd_firstnameText.minimumWidth = 175;
		mobileText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label faxLabel = formToolkit.createLabel(advancedComposite, "Fax",
				SWT.NONE);
		faxLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false,
				1, 1));

		faxText = formToolkit.createText(advancedComposite, "", SWT.BORDER);
		faxText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		Label emailLabel = formToolkit.createLabel(advancedComposite, "E-Mail",
				SWT.NONE);
		emailLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false,
				1, 1));

		emailText = formToolkit.createText(advancedComposite, "", SWT.BORDER);
		emailText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		nameText.setText(person.getName() != null ? person.getName() : "");
		firstnameText.setText(person.getFirstname() != null ? person
				.getFirstname() : "");
		streetText
				.setText(person.getStreet() != null ? person.getStreet() : "");
		postalcodeText.setText(person.getPostalcode() != null ? person
				.getPostalcode() : "");
		cityText.setText(person.getCity() != null ? person.getCity() : "");
		if (person.getBirthday() != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(person.getBirthday());
			dateTime.setDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH));
		}

		activeMemberCheckButton
				.setSelection(person.isActiveMember() != null ? person
						.isActiveMember() : false);
		telephoneText.setText(person.getPhone() != null ? person.getPhone()
				: "");
		mobileText
				.setText(person.getMobile() != null ? person.getMobile() : "");
		faxText.setText(person.getFax() != null ? person.getFax() : "");
		emailText.setText(person.getEmail() != null ? person.getEmail() : "");

		ModifyListener modifyListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (e.widget == nameText) {
					person.setName(nameText.getText());
				} else if (e.widget == firstnameText) {
					person.setFirstname(firstnameText.getText());
				} else if (e.widget == streetText) {
					person.setStreet(streetText.getText());
				} else if (e.widget == postalcodeText) {
					person.setPostalcode(postalcodeText.getText());
				} else if (e.widget == cityText) {
					person.setCity(cityText.getText());
				} else if (e.widget == telephoneText) {
					person.setPhone(telephoneText.getText());
				} else if (e.widget == mobileText) {
					person.setMobile(mobileText.getText());
				} else if (e.widget == faxText) {
					person.setFax(faxText.getText());
				} else if (e.widget == emailText) {
					person.setEmail(emailText.getText());
				}
			}
		};
		nameText.addModifyListener(modifyListener);
		firstnameText.addModifyListener(modifyListener);
		streetText.addModifyListener(modifyListener);
		postalcodeText.addModifyListener(modifyListener);
		cityText.addModifyListener(modifyListener);
		telephoneText.addModifyListener(modifyListener);
		mobileText.addModifyListener(modifyListener);
		faxText.addModifyListener(modifyListener);
		emailText.addModifyListener(modifyListener);
		activeMemberCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				person.setActiveMember(activeMemberCheckButton.getSelection());
			}
		});
		dateTime.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Calendar c = Calendar.getInstance();
				c.set(dateTime.getYear(), dateTime.getMonth(),
						dateTime.getDay());
				person.setBirthday(c.getTime());
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
		return new Point(640, 600);
	}

	@Override
	protected void okPressed() {

		if (person.getName() == null || person.getName().isEmpty()) {
			parentForm.setMessage("Geben Sie einen Nachnamen ein!",
					IMessage.ERROR);
			return;
		}
		if (person.getFirstname() == null || person.getFirstname().isEmpty()) {
			parentForm.setMessage("Geben Sie einen Vornamen ein!",
					IMessage.ERROR);
			return;
		}
		if (person.getStreet() == null || person.getStreet().isEmpty()) {
			parentForm
					.setMessage("Geben Sie eine Strasse ein!", IMessage.ERROR);
			return;
		}
		if (person.getPostalcode() == null || person.getPostalcode().isEmpty()) {
			parentForm.setMessage("Geben Sie eine Postleitzahl ein!",
					IMessage.ERROR);
			return;
		}
		if (person.getCity() == null || person.getCity().isEmpty()) {
			parentForm.setMessage("Geben Sie eine Stadt ein!", IMessage.ERROR);
			return;
		}
		if (person.getBirthday() == null) {
			parentForm.setMessage("Wählen Sie einen gültigen Geburtstag!",
					IMessage.ERROR);
			return;
		}

		super.okPressed();
	}
}
