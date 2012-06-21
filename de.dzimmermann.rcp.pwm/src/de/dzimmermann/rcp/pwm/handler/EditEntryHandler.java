package de.dzimmermann.rcp.pwm.handler;

public class EditEntryHandler extends AddEntryHandler {

	// XXX is there any real extension needed?
	// the AddEditEntryDialog handles it and the AddEntryHandler already
	// recognized changes to the model (not null and unequal field)

	public EditEntryHandler() {
		super.edit = true;
	}
}