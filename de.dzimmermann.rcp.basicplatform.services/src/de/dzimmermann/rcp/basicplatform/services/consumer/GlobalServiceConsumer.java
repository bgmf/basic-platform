package de.dzimmermann.rcp.basicplatform.services.consumer;

import org.osgi.service.component.ComponentContext;

import de.dzimmermann.rcp.basicplatform.services.IAvailablePluginService;
import de.dzimmermann.rcp.basicplatform.services.IGroupService;

public class GlobalServiceConsumer {

	private IAvailablePluginService service;
	private IGroupService service2;

	private ComponentContext componentContext;

	public GlobalServiceConsumer() {
	}

	// Method will be used by DS to set the service
	public synchronized void setAvailablePlugins(IAvailablePluginService service) {
		this.service = service;
	}

	// Method will be used by DS to unset the service
	public synchronized void unsetAvailablePlugins(
			IAvailablePluginService service) {
		if (this.service == service) {
			this.service = null;
		}
	}

	public IAvailablePluginService getService() {
		return service;
	}

	// Method will be used by DS to set the service
    public synchronized void setGroupService(IGroupService service) {
            this.service2 = service;
    }

    // Method will be used by DS to unset the service
    public synchronized void unsetGroupService(IGroupService service) {
            if (this.service2 == service) {
                    this.service2 = null;
            }
    }
     
    public IGroupService getService2() {
            return service2;
    }

	protected void activate(ComponentContext componentContext) {
		this.componentContext = componentContext;
	}

	protected void deactivate(ComponentContext componentContext) {
		this.componentContext = null;
	}
}
