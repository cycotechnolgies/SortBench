
package com.cyco.uni.sortbench;

import com.cyco.uni.sortbench.ui.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                // Use the default Swing look and feel if unavailable.
            }

            new MainFrame().setVisible(true);
        });
    }
}
