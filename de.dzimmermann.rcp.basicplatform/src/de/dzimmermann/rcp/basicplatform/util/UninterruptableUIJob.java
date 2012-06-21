package de.dzimmermann.rcp.basicplatform.util;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.progress.UIJob;

public abstract class UninterruptableUIJob extends UIJob {

	public UninterruptableUIJob(Display jobDisplay, String name) {
		super(jobDisplay, name);
	}

	public UninterruptableUIJob(String name) {
		super(name);
	}
}
