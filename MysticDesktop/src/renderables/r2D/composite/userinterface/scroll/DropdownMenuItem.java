package renderables.r2D.composite.userinterface.scroll;

import blindmystics.input.CurrentInput;
import blindmystics.util.input.Updates;

/**
 * Created by P3TE on 4/08/2016.
 */
public abstract class DropdownMenuItem implements Updates {
    private String menuItemName;

    public DropdownMenuItem(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public abstract void onSelect();

    public abstract boolean enabled();

    public String getMenuItemName() {
        return menuItemName;
    }

    @Override
    public void update(CurrentInput input, float delta) {
        //Do nothing.
    }
}
