import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;

public class Designer {
    private JButton runButton;
    private JButton initButton;
    private JButton clearButton;
    private JTextField retryTimesTextField;
    private JSpinner retryTimesSpinner;
    private JSpinner restTimeSpinner;
    private JTextPane HostName;
    private JTextPane IP;
    private JTextPane Status;
    private JPanel jp_top;
    private JPanel jp_center;
    private JPanel jp_down;
    private JPanel jp_total;
    private JTextField restTimeTextField;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Designer");
        frame.setContentPane(new Designer().jp_total);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
