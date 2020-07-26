package app;

import app.ui.ApplicationMainPanel;

/**
 * Application entry point.
 */
public class Application {
    /**
     * @param args Application program arguments (which are ignored).
     */
    public static void main(String[] args) {
        new ApplicationMainPanel().setVisible(true);
    }
}
