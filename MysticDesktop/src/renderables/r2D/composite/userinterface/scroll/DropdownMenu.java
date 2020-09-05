package renderables.r2D.composite.userinterface.scroll;

import blindmystics.input.CurrentInput;
import blindmystics.util.input.mouse.ButtonStatus;
import loader.LoadedObjectHandler;
import javax.vecmath.Vector2f;
import renderables.r2D.composite.TextButton;
import renderables.r2D.composite.TopLevelInterface;
import renderables.r2D.composite.UserInterface;
import renderables.r2D.composite.userinterface.ScrollList;
import renderables.r2D.simple.Button;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Created by P3TE on 4/08/2016.
 */
public class DropdownMenu extends UserInterface {

    private TextButton menuNameButton;
    private ScrollList dropdownList;
    private ArrayList<VisibleDropdownOption> menuItems = new ArrayList<>();
    private Vector2f dropdownMenuItemSize;
    private ArrayList<VisibleDropdownOption> loadingMenuItems = new ArrayList<>();

    public DropdownMenu(String menuName, Vector2f relativePosition, Vector2f size, float rotation) {
        super(relativePosition, size, rotation);

        dropdownList = addComponentToTop(new ScrollList(new Vector2f(0, -size.y), new Vector2f(size.x, 0f), 0));
        dropdownList.setVisible(false);

        this.menuNameButton = addComponentToTop(
                new TextButton(menuName, Button.BUTONIFY_TEXTURE, new Vector2f(), size, 0) {
                    @Override
                    protected void onRelease() {
                        super.onRelease();
                        toggleMenu();
                    }
                });
        //At first it isn't enabled as there is nothing within the list.
        menuNameButton.setEnabled(false);

        dropdownMenuItemSize = new Vector2f(size);
    }

    public class VisibleDropdownOption extends TextButton {
        private DropdownMenuItem dropdownMenuItem;
        public VisibleDropdownOption(DropdownMenuItem dropdownMenuItem, Vector2f size) {
            super(dropdownMenuItem.getMenuItemName(), BUTONIFY_TEXTURE, new Vector2f(), size, 0);
            this.dropdownMenuItem = dropdownMenuItem;
        }

        @Override
        public boolean isEnabled() {
            return dropdownMenuItem.enabled();
        }

        @Override
        protected void onRelease() {
            super.onRelease();
            dropdownMenuItem.onSelect();
            setMenuVisible(false);
        }
    }

    @Override
    public void completeLoad(LoadedObjectHandler<?> handler) {
        super.completeLoad(handler);
        TopLevelInterface.addComponentToTopLayer(dropdownList);
    }

    public void addDropdownOption(DropdownMenuItem dropdownMenuItem) {
        VisibleDropdownOption newMenuItem = new VisibleDropdownOption(dropdownMenuItem, dropdownMenuItemSize);
        menuItems.add(newMenuItem);
        loadingMenuItems.add(newMenuItem);
        dropdownList.addComponentToList(newMenuItem);
        dropdownList.setRelY(-((dropdownList.getSize().y + size.y) / 2f));
    }

    public void removeDropdownOption(int index) {
        menuItems.remove(index);
        dropdownList.remove(index);
        dropdownList.setRelY(-((dropdownList.getSize().y + size.y) / 2f));
    }

    public void toggleMenu() {
        setMenuVisible(!dropdownList.isVisible());
    }

    public void openMenu() {
        setMenuVisible(true);
    }

    public void closeMenu() {
        setMenuVisible(false);
    }

    private void setMenuVisible(boolean isVisible) {
        dropdownList.setVisible(isVisible);
    }

    @Override
    public void update(CurrentInput input, float delta) {
        super.update(input, delta);
        if (dropdownList.isVisible()) {
            if (input.getLeftMouse() == ButtonStatus.JUST_PRESSED) {
                if (!input.receivedMouseEvent(dropdownList)
                        && !input.receivedMouseEvent(menuNameButton)) {
                    //Close the menu.
                    setMenuVisible(false);
                }
            }
            //Consume the back handler, and close the menu.
            if (input.consumeBackButtonHit()) {
                //Close the menu.
                setMenuVisible(false);
            }
        }
        if (dropdownList.isVisible()) {
            for (VisibleDropdownOption visibleDropdownOption : menuItems) {
                visibleDropdownOption.dropdownMenuItem.update(input, delta);
            }
        }
        handleNewlyLoadedMenuItems();
    }

    private void handleNewlyLoadedMenuItems() {
        if (loadingMenuItems.isEmpty()) {
            return;
        }
        ListIterator<VisibleDropdownOption> loadingDropdownOptionIterator = loadingMenuItems.listIterator();
        while (loadingDropdownOptionIterator.hasNext()) {
            VisibleDropdownOption loadingDropdownOption = loadingDropdownOptionIterator.next();
            if (loadingDropdownOption.isLoaded()) {
                loadingDropdownOptionIterator.remove();
                //Update the widths.
                float menuWidth = loadingDropdownOption.getTextWidth() + 10;
                if (menuWidth > dropdownMenuItemSize.x) {
                    //Update the size.
                    dropdownMenuItemSize.x = menuWidth;
                    for (VisibleDropdownOption visibleDropdownOption : menuItems) {
                        visibleDropdownOption.setWidth(dropdownMenuItemSize.x);
                        visibleDropdownOption.setHeight(dropdownMenuItemSize.y);
                    }
                }
                dropdownList.setItemListSize(dropdownMenuItemSize.x);
                dropdownList.setRelX(((dropdownList.getSize().x - size.x) / 2f));
                //Now that there is an element within the list, enable to button.
                menuNameButton.setEnabled(true);
            }
        }
    }

    protected VisibleDropdownOption getMenuItem(int index) {
        return menuItems.get(index);
    }

    protected int getMenuSize(){
        return menuItems.size();
    }
}
