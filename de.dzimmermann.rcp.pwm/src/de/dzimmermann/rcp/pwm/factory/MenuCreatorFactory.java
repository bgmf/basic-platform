package de.dzimmermann.rcp.pwm.factory;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import de.dzimmermann.rcp.basicplatform.util.PFSCoreIconProvider;
import de.dzimmermann.rcp.pwm.handler.AddEntryHandler;
import de.dzimmermann.rcp.pwm.handler.AddGroupHandler;
import de.dzimmermann.rcp.pwm.handler.AddGroupHandler.AddSubGroupHandler;
import de.dzimmermann.rcp.pwm.handler.EditEntryHandler;
import de.dzimmermann.rcp.pwm.handler.EditGroupHandler;
import de.dzimmermann.rcp.pwm.handler.RemoveEntryHandler;
import de.dzimmermann.rcp.pwm.handler.RemoveGroupHandler;
import de.dzimmermann.rcp.pwm.model.content.PWMGroupEntry;
import de.dzimmermann.rcp.pwm.ui.editor.PWMEditor;
import de.dzimmermann.rcp.pwm.util.PWMStates;

public class MenuCreatorFactory {

	public static IMenuListener createPWMMenuListener(boolean group) {
		return new PWMMenuListener(group);
	}

	private static final class PWMMenuListener implements IMenuListener {

		private boolean group = false;

		public PWMMenuListener(boolean group) {
			this.group = group;
		}

		@Override
		public void menuAboutToShow(IMenuManager manager) {
			if (group)
				createGroupMenu(manager);
			else
				createContentMenu(manager);
		}

		private void createGroupMenu(IMenuManager manager) {

			IAction add = new HandlerTriggerAction(AddGroupHandler.class,
					"&Add Group", "Add a new group to the current database.",
					IAction.AS_PUSH_BUTTON,
					PFSCoreIconProvider.getImageByIconName(
							"fugue_folder--plus.png", true));
			IAction addSub = new HandlerTriggerAction(AddSubGroupHandler.class,
					"Add &Sub-Group",
					"Add a new sub-group to the current group.",
					IAction.AS_PUSH_BUTTON,
					PFSCoreIconProvider.getImageByIconName(
							"fugue_folder-bookmark.png", true));
			IAction edit = new HandlerTriggerAction(EditGroupHandler.class,
					"&Edit Group",
					"Edit an existing group of the current database.",
					IAction.AS_PUSH_BUTTON,
					PFSCoreIconProvider.getImageByIconName(
							"fugue_folder--pencil.png", true));
			IAction remove = new HandlerTriggerAction(RemoveGroupHandler.class,
					"&Remove Group",
					"Remove a new group of the current database.",
					IAction.AS_PUSH_BUTTON,
					PFSCoreIconProvider.getImageByIconName(
							"fugue_folder--minus.png", true));

			addSub.setEnabled(PWMStates.getSubgroupSelectedState());

			manager.add(add);
			manager.add(addSub);
			manager.add(edit);
			manager.add(remove);
		}

		private void createContentMenu(IMenuManager manager) {

			IAction add = new HandlerTriggerAction(AddEntryHandler.class,
					"&Add Entry",
					"Add a new entry to the current selected group.",
					IAction.AS_PUSH_BUTTON,
					PFSCoreIconProvider.getImageByIconName(
							"fugue_key--plus.png", true));
			IAction edit = new HandlerTriggerAction(EditEntryHandler.class,
					"&Edit Entry",
					"Edit an existing entry of the current selected group.",
					IAction.AS_PUSH_BUTTON,
					PFSCoreIconProvider.getImageByIconName(
							"fugue_key--pencil.png", true));
			IAction remove = new HandlerTriggerAction(RemoveEntryHandler.class,
					"&Remove Entry",
					"Remove a new entry of the current selected group.",
					IAction.AS_PUSH_BUTTON,
					PFSCoreIconProvider.getImageByIconName(
							"fugue_key--minus.png", true));

			add.setEnabled(PWMStates.getEnableEntriesState());
			edit.setEnabled(PWMStates.getEntrySelectedState());
			remove.setEnabled(PWMStates.getEntrySelectedState());

			manager.add(add);
			manager.add(edit);
			manager.add(remove);

			manager.add(new Separator());

			IAction copyUrl = new CopyToClipboardAction(
					CopyToClipboardEnum.URL,
					"Copy &URL",
					"Copy the URL of the currently selected entry into the clipboard.",
					IAction.AS_PUSH_BUTTON, PFSCoreIconProvider
							.getImageByIconName("fugue_clipboard--arrow.png",
									true));
			IAction copyName = new CopyToClipboardAction(
					CopyToClipboardEnum.USERNAME,
					"Copy User&name",
					"Copy the username of the currently selected entry into the clipboard.",
					IAction.AS_PUSH_BUTTON, PFSCoreIconProvider
							.getImageByIconName("fugue_clipboard--arrow.png",
									true));
			IAction copyPassword = new CopyToClipboardAction(
					CopyToClipboardEnum.PASSWORD,
					"Copy &Password",
					"Copy the password of the currently selected entry into the clipboard.",
					IAction.AS_PUSH_BUTTON, PFSCoreIconProvider
							.getImageByIconName("fugue_clipboard--arrow.png",
									true));

			copyUrl.setEnabled(PWMStates.getEntrySelectedState());
			copyName.setEnabled(PWMStates.getEntrySelectedState());
			copyPassword.setEnabled(PWMStates.getEntrySelectedState());

			manager.add(copyUrl);
			manager.add(copyName);
			manager.add(copyPassword);
		}
	}

	private static final class HandlerTriggerAction extends Action {

		private AbstractHandler handler;

		public HandlerTriggerAction(
				Class<? extends AbstractHandler> handlerClass, String name,
				String desc, int style, Image img) {

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

	private static final class CopyToClipboardAction extends Action {

		private final CopyToClipboardEnum type;

		public CopyToClipboardAction(CopyToClipboardEnum type, String name,
				String desc, int style, Image img) {

			super(name, style);

			this.type = type;

			setImageDescriptor(ImageDescriptor.createFromImage(img));
			setToolTipText(desc);
			setDescription(desc);
		}

		@Override
		public void run() {

			IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			IEditorPart editor = page.getActiveEditor();

			if (PWMEditor.ID.equals(editor.getEditorSite().getId())) {

				Clipboard clipboard = new Clipboard(editor.getSite().getShell()
						.getDisplay());

				PWMGroupEntry entry = (PWMGroupEntry) editor
						.getAdapter(PWMGroupEntry.class);

				switch (type) {
				case URL:
					clipboard.setContents(new Object[] { entry.getUrl() },
							new Transfer[] { TextTransfer.getInstance() });
					break;
				case USERNAME:
					clipboard.setContents(new Object[] { entry.getUsername() },
							new Transfer[] { TextTransfer.getInstance() });
					break;
				case PASSWORD:
					clipboard.setContents(new Object[] { entry.getPassword() },
							new Transfer[] { TextTransfer.getInstance() });
					break;
				}
			}
		}
	}

	private enum CopyToClipboardEnum {
		URL, USERNAME, PASSWORD;
	}
}
