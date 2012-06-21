package de.dzimmermann.rcp.basicplatform.util;

import org.eclipse.core.runtime.jobs.Job;

public abstract class UninterruptableJob extends Job {

	public UninterruptableJob(String name) {
		super(name);
	}
}
