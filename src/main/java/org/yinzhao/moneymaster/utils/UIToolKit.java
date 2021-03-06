package org.yinzhao.moneymaster.utils;

import java.awt.*;

public final class UIToolKit {
    private UIToolKit() {
    }

    public static void centerComponent(Window window) {
        if (window != null) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            double width = screenSize.getWidth();
            double height = screenSize.getHeight();
            window.setLocation((int) (width - window.getWidth()) / 2, (int) (height - window.getHeight()) / 2);
        }
    }
}
