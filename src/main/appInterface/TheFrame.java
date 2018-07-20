package main.appInterface;

import main.sqlUtils.Connector;

import javax.swing.*;
import java.awt.*;

/**
 * MainFrame class incorporates all the components of the frame, the control panel, the drawing area and the gallery panel.
 * It is displayed via a BorderLayout.
 */
public class TheFrame extends JFrame {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new Connector();
                Interface frame = new Interface();

                frame.init();

                // Use the close operations on the frame so it can be resizable and can be closed
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setMinimumSize(new Dimension(950,550));
                frame.pack();
                frame.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
}
