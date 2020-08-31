package com.inventrax_pepsi.model;

/**
 * Created by android on 3/12/2016.
 */
public class OutletMenuItem {

    private int menuItemIcon;
    private String menuText;

    public OutletMenuItem() {
    }

    public OutletMenuItem(int menuItemIcon, String menuText) {
        this.menuItemIcon = menuItemIcon;
        this.menuText = menuText;
    }

    public int getMenuItemIcon() {
        return menuItemIcon;
    }

    public void setMenuItemIcon(int menuItemIcon) {
        this.menuItemIcon = menuItemIcon;
    }

    public String getMenuText() {
        return menuText;
    }

    public void setMenuText(String menuText) {
        this.menuText = menuText;
    }
}
