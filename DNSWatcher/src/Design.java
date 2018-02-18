import sun.font.TrueTypeFont;

import javax.swing.*;
import java.awt.*;

public class Design {
    private JFrame frame;
    private JPanel jp_HostName;
    private JPanel jp_IPAddr;
    private JPanel jp_Status;

    public Design(){
        initialize();
    }

    public void initialize(){
        /*
         *主窗体初始化
         */
        frame = new JFrame();
        frame.setBounds(0, 0, 600, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setTitle("DNS Monitor(by xiaoyijie)");
        frame.setLocationRelativeTo(null);
        //frame.pack();
        frame.setVisible(true);
        /**
         * jp_HostName窗体设置，最左边
         */
        jp_HostName =new JPanel();
        //jp_HostName.setSize(200,500);
        jp_HostName.setBackground(Color.pink);
        jp_HostName.setVisible(true);
        String[] a={"1","2"};
        JComboBox jcb1= new JComboBox(a);
        JButton mb = new JButton("test");
        mb.setVisible(true);
        jp_HostName.add(mb);
        /**
         * 主窗体添加参数
         */
        frame.add(BorderLayout.EAST,mb);
    }
}
