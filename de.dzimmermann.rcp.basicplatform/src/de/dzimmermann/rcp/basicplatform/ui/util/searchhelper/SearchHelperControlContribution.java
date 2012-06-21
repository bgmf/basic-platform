package de.dzimmermann.rcp.basicplatform.ui.util.searchhelper;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

import de.dzimmermann.rcp.basicplatform.SSDToolPlugin;
import de.dzimmermann.rcp.basicplatform.ui.util.Logger;
import de.dzimmermann.rcp.basicplatform.util.PFSCoreIconProvider;

public class SearchHelperControlContribution extends
		WorkbenchWindowControlContribution implements Listener {

	/**
	 * If the parent is a coolbar, this control will be used to return.
	 */
	private CoolBar optionCoolBar;
	/**
	 * The coolbar item for the search options.
	 */
	private CoolItem optionCoolItem;

	/**
	 * If the parent is a toolbar, this control will be used to return.
	 */
	private ToolBar optionToolBar;
	/**
	 * The toolbar item for the search options.
	 */
	private ToolItem optionToolItem;

	/**
	 * The menu containing/displaying the options.
	 */
	private Menu optionMenu;

	/**
	 * The textbox, that will be displayed within the toolbar.
	 */
	private Text search;

	/**
	 * an image for the search option item.
	 */
	private Image img = null;

	/**
	 * This object might be <code>null</code> if this contribution has no id,
	 * otherwise it contains all the information needed to search for the data.
	 */
	private SearchData data;

	// private String[] currentSearchOptionNames;
	private String currentSearchOptionName;

	public SearchHelperControlContribution() {
		img = PFSCoreIconProvider.getImageByIconName(
				"fugue_magnifier-zoom.png", true); //$NON-NLS-1$
	}

	public SearchHelperControlContribution(String id) {
		super(id);
		img = PFSCoreIconProvider.getImageByIconName(
				"fugue_magnifier-zoom.png", true); //$NON-NLS-1$
	}

	@Override
	public void dispose() {
		if (img != null) {
			img.dispose();
		}
		super.dispose();
	}

	@Override
	protected Control createControl(Composite parent) {

		boolean useTextBox = false;

		data = getSearchData();

		String optionTextNoImg = "Option";
		String searchMessage = (data != null) ? data.getSearchMessage()
				: "Search String";

		if (parent instanceof CoolBar) {

			optionCoolBar = new CoolBar(parent, SWT.HORIZONTAL);
			optionCoolItem = new CoolItem(optionCoolBar, SWT.DROP_DOWN);

			if (img != null) {
				optionCoolItem.setImage(img);
			} else {
				optionCoolItem.setText(optionTextNoImg);
			}

			optionMenu = new Menu(optionCoolItem.getParent().getShell());
			optionCoolItem.addListener(SWT.Selection, this);

			CoolItem sepaparator = new CoolItem(optionCoolBar, SWT.SEPARATOR);
			int style = SWT.SEARCH;
			if (data != null && data.isHintCancel()) {
				style = style | SWT.CANCEL | SWT.ICON_CANCEL;
			}
			if (data != null && data.isHintSearch()) {
				style = style | SWT.ICON_SEARCH;
			}
			search = new Text(optionCoolBar, style);
			search.setMessage(searchMessage);

			if ((search.getStyle() & SWT.ICON_SEARCH) == SWT.ICON_SEARCH) {

				useTextBox = true;

				optionMenu.dispose();
				optionMenu = null;

				optionCoolItem.dispose();
				optionCoolItem = null;

				sepaparator.dispose();
				sepaparator = null;

				search.dispose();
				search = null;

				search = new Text(parent, style);
				search.setMessage(searchMessage);
				search.setSize(search.getSize().x + 100, search.getSize().y);

				optionMenu = new Menu(search);
			}

			search.addListener(SWT.DefaultSelection, this);
			search.addListener(SWT.Modify, this);

			search.addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent e) {
					search.removeListener(SWT.DefaultSelection,
							SearchHelperControlContribution.this);
					search.removeListener(SWT.Modify,
							SearchHelperControlContribution.this);
					search.removeDisposeListener(this);
				}
			});

			if (!useTextBox) {
				search.pack();

				sepaparator.setSize(search.getSize());
				sepaparator.setControl(search);

				optionCoolBar.pack();
			}

		} else if (parent instanceof ToolBar) {

			optionToolBar = new ToolBar(parent, parent.getStyle());
			optionToolItem = new ToolItem(optionToolBar, SWT.DROP_DOWN);

			if (img != null) {
				optionToolItem.setImage(img);
			} else {
				optionToolItem.setText(optionTextNoImg);
			}

			optionMenu = new Menu(optionToolItem.getParent().getShell());
			optionToolItem.addListener(SWT.Selection, this);

			ToolItem sepaparator = new ToolItem(optionToolBar, SWT.SEPARATOR);
			int style = SWT.SEARCH;
			if (data != null && data.isHintCancel()) {
				style = style | SWT.CANCEL | SWT.ICON_CANCEL;
			}
			if (data != null && data.isHintSearch()) {
				style = style | SWT.ICON_SEARCH;
			}
			search = new Text(optionToolBar, style);
			search.setMessage(searchMessage);

			if ((search.getStyle() & SWT.ICON_SEARCH) == SWT.ICON_SEARCH) {

				useTextBox = true;

				optionMenu.dispose();
				optionMenu = null;

				optionToolItem.dispose();
				optionToolItem = null;

				sepaparator.dispose();
				sepaparator = null;

				search.dispose();
				search = null;

				search = new Text(parent, style);
				search.setMessage(searchMessage);

				optionMenu = new Menu(search);
			}

			search.addListener(SWT.DefaultSelection, this);
			search.addListener(SWT.Modify, this);

			search.addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent e) {
					search.removeListener(SWT.DefaultSelection,
							SearchHelperControlContribution.this);
					search.removeListener(SWT.Modify,
							SearchHelperControlContribution.this);
					search.removeDisposeListener(this);
				}
			});

			if (!useTextBox) {
				search.setBounds(search.getBounds().x, search.getBounds().y,
						search.getBounds().width + 100, SWT.DEFAULT);
				search.pack();

				sepaparator.setWidth(search.getBounds().width + 100);
				sepaparator.setControl(search);

				optionToolBar.pack();
			}
		}

		if (useTextBox) {
			return search;
		} else if (parent instanceof CoolBar) {
			return optionCoolBar;
		} else if (parent instanceof ToolBar) {
			return optionToolBar;
		} else {
			return null;
		}
	}

	@Override
	public void handleEvent(Event event) {

		if (event.widget == search) {
			if (event.type == SWT.DefaultSelection) {
				// implement the selection handling
				if (event.detail == SWT.ICON_CANCEL) {
					// Cancel on text
				} else if (event.detail == SWT.ICON_SEARCH) {
					// ICON on text
					createOptionMenu();
					// Text item = (Text) event.widget;
					Rectangle rect = search.getBounds();
					Point pt = search.getParent().toDisplay(
							new Point(rect.x, rect.y));
					optionMenu.setLocation(pt.x, pt.y + rect.height);
					optionMenu.setVisible(true);
				} else {
					// Default selection on text
				}
			} else if (event.type == SWT.Modify) {
				if (search.getText() != null) {
					data.getHelperFactory().setSearchValue(search.getText());
				}
			}

		} else if (event.type == SWT.Selection) {

			if (event.widget instanceof MenuItem) {

				currentSearchOptionName = (String) event.widget.getData();
				data.getHelperFactory().setOptionName(
						(String) event.widget.getData());

			} else if (event.widget == optionCoolItem) {

				createOptionMenu();

				CoolItem item = (CoolItem) event.widget;
				// if (event.detail == SWT.ARROW) {
				Rectangle rect = item.getBounds();
				Point pt = item.getParent()
						.toDisplay(new Point(rect.x, rect.y));
				optionMenu.setLocation(pt.x, pt.y + rect.height);
				optionMenu.setVisible(true);
				// } else {
				// System.out.println(item.getText() + " Pressed");
				// }

			} else if (event.widget == optionToolItem) {

				createOptionMenu();

				ToolItem item = (ToolItem) event.widget;
				// if (event.detail == SWT.ARROW) {
				Rectangle rect = item.getBounds();
				Point pt = item.getParent()
						.toDisplay(new Point(rect.x, rect.y));
				optionMenu.setLocation(pt.x, pt.y + rect.height);
				optionMenu.setVisible(true);
				// } else {
				// System.out.println(item.getText() + " Pressed");
				// }

			}
		}
	}

	private void createOptionMenu() {

		if (data == null) {
			return;
		}

		List<SearchOption> options = data.getOptions();

		String[] optionNames = new String[options.size()];
		for (int i = 0; i < options.size(); i++) {
			optionNames[i] = options.get(i).getName();
		}
		String[] optionLabels = new String[options.size()];
		for (int i = 0; i < options.size(); i++) {
			optionLabels[i] = options.get(i).getLabel();
		}

		// if (currentSearchOptionNames == null
		// || !Arrays.equals(optionNames, currentSearchOptionNames)) {

		// destroy old menu
		for (MenuItem item : optionMenu.getItems()) {
			if (!item.isDisposed()) {
				try {
					item.removeListener(SWT.Selection, this);
					item.dispose();
				} catch (Exception e) {
					Logger.logError(e);
				}
			}
		}

		// reset current options
		// currentSearchOptionNames = optionNames;

		boolean oneSelected = false;
		// create new menu
		for (int i = 0; i < optionLabels.length; i++) {
			MenuItem item = new MenuItem(optionMenu, SWT.RADIO);
			item.setText(optionLabels[i]);
			if (optionNames[i].equals(currentSearchOptionName)) {
				item.setSelection(true);
				oneSelected = true;
			} else {
				item.setSelection(false);
			}
			item.setData(optionNames[i]);
			item.addListener(SWT.Selection, this);
		}

		if (!oneSelected) {
			optionMenu.getItems()[0].setSelection(true);
		}
		// } else {
		// // do something else?
		// }
	}

	private SearchData getSearchData() {
		SearchModel searchModel = SSDToolPlugin.getDefault().getSearchModel();
		SearchData data = null;
		if (searchModel != null && getId() != null) {
			data = searchModel.getTargetSearch().get(getId());
		}
		return data;
	}
}
