import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

public class Designer {
    private JButton runButton;
    private JButton initButton;
    private JTextPane hostName;
    private JPanel jp_top;
    private JPanel jp_center;
    private JPanel jp_down;
    private JPanel jp_total;
    private JTextField restTimeTextField;
    private JButton lookUpButton;
    private JTextPane FalseHostName;
    //    private JTextPane textMail;
    private JButton initialParamButton;
    private Thread thread;
    private Watcher watcher = new Watcher();
    boolean isRunning = false;
    private int num = 0;
    private int wrongNum = 0;
    public JFrame frame;
    ArrayList<String> receiveEmail = new ArrayList<String>();
    ArrayList<String> trueHostName = new ArrayList<String>();
    ArrayList<String> falseHostName = new ArrayList<String>();


    static public String transMail = "";
    static public String transPasswd = "";
    static ArrayList<String> receiveMail = new ArrayList<String>();
    static public String DNSIP = "";
    static public boolean setOK = false;

    public Designer() {

        frame = new JFrame("DNSMonitor by肖祎杰 v1.2");
        ImageIcon icon = new ImageIcon(this.getClass().getResource("images/AHCmccLogo.jpg"));
        frame.setIconImage(icon.getImage());
        frame.setResizable(false);
        frame.setContentPane(jp_total);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
//        textMail.setText("ahcmccdnsmonitor@126.com");
        restTimeTextField.setText("已轮询次数(5s一次)：0次    解析存在错误次数：" + wrongNum + "次");//清空轮询次数

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                textMail.setEditable(false);
                if (setOK == true) {
                    JFileChooser jfc = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(".xls/.xlsx", "xls", "xlsx");//建立过滤器，只显示xls和xlsx
                    jfc.setFileFilter(filter);
                    jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    jfc.showDialog(new JLabel(), "确认");
                    File file = jfc.getSelectedFile();
                    if (file == null) {
                        JOptionPane.showMessageDialog(null, "您未选择文件", "错误", JOptionPane.WARNING_MESSAGE);
                    } else {
                        String filePath = file.getAbsolutePath();
                        if (filePath.endsWith("xlsx") || filePath.endsWith("xls")) {
                            thread = new MyThread(hostName, filePath);
                            thread.start();
                            isRunning = true;
                            runButton.setText("Running");
                            runButton.setEnabled(false);
                            lookUpButton.setEnabled(false);
                        } else {
                            JOptionPane.showMessageDialog(null, "您选择的文件不是Excel表格式", "错误", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "您未设置运行参数，请点击setting", "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        initButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initialParamButton.setEnabled(true);
                if (setOK == true) {
                    if (isRunning == true) {
                        thread.stop();
//                    mp3Player.clip.stop();
                        runButton.setText("run");
                        runButton.setEnabled(true);
                        lookUpButton.setEnabled(true);
                    } else
                        isRunning = false;
                    hostName.setText("");
                    FalseHostName.setText("");
                    num = 0;
                    wrongNum = 0;
                    restTimeTextField.setText("已轮询次数(5s一次)：0次    解析存在错误次数：" + wrongNum + "次");//清空轮询次数
//                textMail.setEditable(true);
                } else {
                    JOptionPane.showMessageDialog(null, "您未设置运行参数，请点击setting", "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        lookUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (setOK == true) {
                    JFileChooser jfc = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(".xls/.xlsx", "xls", "xlsx");//建立过滤器，只显示xls和xlsx
                    jfc.setFileFilter(filter);
                    jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    jfc.showDialog(new JLabel(), "确认");
                    File file = jfc.getSelectedFile();
                    ArrayList<Object> ip = new ArrayList<Object>();
                    ArrayList<Object> hostName = new ArrayList<Object>();
                    ArrayList<ArrayList<Object>> excel = new ArrayList<ArrayList<Object>>();
                    if (file == null) {
                        JOptionPane.showMessageDialog(null, "您未选择文件", "错误", JOptionPane.WARNING_MESSAGE);
                    } else {
                        String filePath = file.getAbsolutePath();
                        if (filePath.endsWith("xlsx") || filePath.endsWith("xls")) {
                            ArrayList<ArrayList<Object>> result = ExcelUtil.readExcel(file);
                            for (int i = 0; i < result.size(); i++) {
                                hostName.add(result.get(i).get(0).toString());
                                try {
                                    String provider = Designer.DNSIP;
//                                    System.out.println(provider);
                                    Hashtable env = new Hashtable();
                                    env.put("java.naming.factory.initial",
                                            "com.sun.jndi.dns.DnsContextFactory");
                                    // 指定DNS
                                    env.put(Context.PROVIDER_URL, "dns://" + provider);
                                    env.put("com.sun.jndi.dns.timeout.initial", "1000");// 连接时间
                                    env.put("com.sun.jndi.dns.timeout.retries", "3");// 连接次数
                                    DirContext ictx = new InitialDirContext(env);
                                    Attributes attrs = ictx.getAttributes(hostName.get(i).toString(), new String[]{"A"});
                                    for (Enumeration e1 = attrs.getAll(); e1.hasMoreElements(); ) {
                                        Attribute a = (Attribute) e1.nextElement();
//                                    System.out.println(a);
                                        int size = a.size();
                                        String iplook = "";
                                        for (int j = 0; j < size; j++) {
                                            // MX string has priority (lower better) followed by associated
                                            // mailserver
                                            // A string is just IP
                                            iplook = iplook + ((String) a.get(j)) + ";";
                                            System.out.println(iplook);
                                        }// end inner for
                                        ip.add(iplook);
                                    }// end outer for
                                } catch (NamingException e2) {
                                    ip.add("error");
                                }
                            }
                            excel.add(hostName);
                            excel.add(ip);
                            ExcelUtil.writeExcelRight(excel, "D:/test.xls");
                            JOptionPane.showMessageDialog(null, "文件已写入D:/test.sls", "成功", JOptionPane.WARNING_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "您选择的文件不是Excel表格式", "错误", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "您未设置运行参数，请点击setting", "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        initialParamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                Setting set = new Setting(frame);
                initialParamButton.setEnabled(false);
            }
        });
    }

//    public void show() throws Exception {
//    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        jp_total = new JPanel();
        jp_total.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        jp_top = new JPanel();
        jp_top.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        jp_total.add(jp_top, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(400, 300), null, new Dimension(400, 300), 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        jp_top.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        hostName = new JTextPane();
        hostName.setEditable(false);
        hostName.setText("");
        scrollPane1.setViewportView(hostName);
        final JScrollPane scrollPane2 = new JScrollPane();
        jp_top.add(scrollPane2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        FalseHostName = new JTextPane();
        FalseHostName.setEditable(false);
        scrollPane2.setViewportView(FalseHostName);
        jp_center = new JPanel();
        jp_center.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        jp_total.add(jp_center, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(400, 100), null, new Dimension(400, 100), 0, false));
        runButton = new JButton();
        Font runButtonFont = this.$$$getFont$$$(null, -1, 26, runButton.getFont());
        if (runButtonFont != null) runButton.setFont(runButtonFont);
        runButton.setForeground(new Color(-1891838));
        runButton.setHideActionText(false);
        runButton.setText("run");
        jp_center.add(runButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(150, 100), 0, false));
        initButton = new JButton();
        Font initButtonFont = this.$$$getFont$$$(null, -1, 26, initButton.getFont());
        if (initButtonFont != null) initButton.setFont(initButtonFont);
        initButton.setText("init");
        jp_center.add(initButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(150, 100), 0, false));
        lookUpButton = new JButton();
        Font lookUpButtonFont = this.$$$getFont$$$(null, -1, 20, lookUpButton.getFont());
        if (lookUpButtonFont != null) lookUpButton.setFont(lookUpButtonFont);
        lookUpButton.setText("LookUp");
        jp_center.add(lookUpButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(150, 100), 0, false));
        jp_down = new JPanel();
        jp_down.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        jp_total.add(jp_down, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(400, 100), new Dimension(400, 100), new Dimension(400, 100), 0, false));
        restTimeTextField = new JTextField();
        restTimeTextField.setEditable(false);
        restTimeTextField.setText("");
        jp_down.add(restTimeTextField, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(400, 50), new Dimension(400, 50), new Dimension(400, 50), 0, false));
        initialParamButton = new JButton();
        Font initialParamButtonFont = this.$$$getFont$$$(null, -1, 20, initialParamButton.getFont());
        if (initialParamButtonFont != null) initialParamButton.setFont(initialParamButtonFont);
        initialParamButton.setHideActionText(false);
        initialParamButton.setText("Setting");
        jp_down.add(initialParamButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return jp_total;
    }

    class MyThread extends Thread {
        JTextPane hostName;
        String excelRouting;

        public MyThread(JTextPane hostName, String excelRouting) {
            this.hostName = hostName;
            this.excelRouting = excelRouting;
        }

        private ArrayList<String> convertMaptoList(Map<String, String> mymap) {
            ArrayList<String> param = new ArrayList<String>();
            for (String web : mymap.keySet()) {
                param.add(web + "->" + mymap.get(web));
            }
            return param;
        }

        public void run() {
            while (true) {
                trueHostName.clear();
                falseHostName.clear();
                String reading_true = "", reading_false = "";
                ArrayList<Map<String, String>> total = watcher.dnsLookup(excelRouting);
//                System.out.println(total);
                trueHostName = convertMaptoList(total.get(0));
                falseHostName = convertMaptoList(total.get(1));
                for (int i = 0; i < trueHostName.size(); i++) {
                    reading_true = reading_true + trueHostName.get(i) + "\n";
                }
                for (int i = 0; i < falseHostName.size(); i++) {
                    reading_false = reading_false + falseHostName.get(i) + "\n";
                }
                hostName.setForeground(Color.green);
                hostName.setText(reading_true);
                FalseHostName.setText("");
                //System.out.print(hostName.getText());
                if (reading_false != "") {
                    FalseHostName.setForeground(Color.RED);
                    FalseHostName.setText(reading_false);
                    MP3Player mp3Player = new MP3Player(this.getClass().getResource("alarm.mp3"));
                    wrongNum++;
                    if (wrongNum % 120 == 0)
                        MailTrans.SendEmail("ahcmccdnsmonitor@126.com", "dnstrans2017", receiveEmail, FalseHostName.getText());
//                    System.out.println("Wrong");
                }//如果解析结果不同，产生告警音
                restTimeTextField.setText("已轮询次数(5s一次)：" + ++num + "次    解析存在错误次数：" + wrongNum + "次");//显示轮询次数
                try {
                    sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
