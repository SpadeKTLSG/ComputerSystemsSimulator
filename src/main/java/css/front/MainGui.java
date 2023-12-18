package css.front;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class MainGui {
    private JFrame Mframe;
    private JTree pathTree;

    private JPanel ramPanel;
    private Color[] ram;
    private JLabel timeLabel;

    private JPanel diskPanel;
    private Color[] disk;
    public MainGui() {
        // 创建主界面
        Mframe = new JFrame("模拟操作系统");
        Mframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Mframe.setLayout(null); //取消默认的布局BorderLayout
        Mframe.setSize(1280, 720);
        Mframe.setLocation(150, 50);
        Mframe.setResizable(false);
        Mframe.setBackground(Color.white);

        JPanel p1=new JPanel();
        p1.setSize(600,310);
        p1.setBackground(Color.white);
        p1.setLocation(10,50);

        // 使用流式布局，左对齐，水平和垂直间隔均为20
        p1.setLayout(new FlowLayout(FlowLayout.LEADING, 20, 10));
        p1.setBorder(new TitledBorder(new EtchedBorder(), "进程管理"));

        JPanel ready=createWindow("就绪队列");
        JPanel execute=createWindow("执行指令");
        JPanel blocking=createWindow("阻塞队列");
        JLabel process=new JLabel("运行进程:");
        JTextField out_text = new JTextField();
        out_text.setEditable(false);
        out_text.setFocusable(false);
        out_text.setPreferredSize(new Dimension(230, 30));
        out_text.setBackground(Color.white);
        JLabel time_slice=new JLabel("时间片");
        JTextField Ttime_slice = new JTextField();
        Ttime_slice.setEditable(false);
        Ttime_slice.setFocusable(false);
        Ttime_slice.setPreferredSize(new Dimension(170, 30));
        Ttime_slice.setBackground(Color.white);

        p1.add(ready);
        p1.add(blocking);
        p1.add(execute);
        p1.add(ready);
        p1.add(process);
        p1.add(out_text);
        p1.add(time_slice);
        p1.add(Ttime_slice);

        Mframe.add(p1);

        //显示时间
        JPanel timepanel=new JPanel();
        timepanel.setSize(610,40);
        timepanel.setBackground(Color.white);
        timepanel.setLocation(640,10);
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        updateTime();  // 初始化时间
        timepanel.add(timeLabel);
        Mframe.add(timepanel);



        JPanel p2=new JPanel();
        p2.setSize(610,300);
        p2.setBackground(Color.white);
        p2.setLocation(640,60);
        p2.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        JPanel treepanel=new JPanel();
        treepanel.setSize(560,150);
        treepanel.setBackground(Color.white);
        treepanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        treepanel.setBorder(new TitledBorder(new EtchedBorder(), "目录结构"));
        // 创建根节点
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");

        // 创建子节点
        DefaultMutableTreeNode folderA = new DefaultMutableTreeNode("Folder A");
        DefaultMutableTreeNode folderB = new DefaultMutableTreeNode("Folder B");
        DefaultMutableTreeNode fileC = new DefaultMutableTreeNode("File C");

        // 将子节点添加到根节点
        root.add(folderA);
        root.add(folderB);
        root.add(fileC);

        // 创建树
        pathTree = new JTree(root);
        pathTree.setPreferredSize(new Dimension(560, 200));

        // 添加树的选择事件监听器
        pathTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                // 获取选择的路径
                TreePath selectedPath = e.getNewLeadSelectionPath();
                if (selectedPath != null) {
                    // 在控制台输出选择的路径
                    System.out.println("Selected Path: " + selectedPath);
                }
            }
        });

        // 把pathtree添加到panel
        treepanel.add(pathTree);
        JTextField input_text = new JTextField("命令行");
        input_text.setPreferredSize(new Dimension(360, 30));
        input_text.setBackground(Color.white);
        p2.add(treepanel);
        p2.add(input_text);
        Mframe.add(p2);

        JPanel p3=new JPanel();
        p3.setSize(290,310);
        p3.setBackground(Color.white);
        p3.setLocation(10,370);
        p3.setBorder(new TitledBorder(new EtchedBorder(), "内存"));
        ramPanel = new JPanel();

        // 初始化硬盘颜色数组
        ram = new Color[64];
        initializeram(ram);

        updateRam(); // 初始更新硬盘视图


        p3.add(ramPanel);
        Mframe.add(p3);

        JPanel p4 =new JPanel();
        p4.setSize(200,310);
        p4.setBackground(Color.white);
        p4.setLocation(310,370);
        p4.setLayout(new FlowLayout(FlowLayout.LEADING));
        p4.setBorder(new TitledBorder(new EtchedBorder(), "外围设备"));

        JLabel A1=new JLabel("A1:");
        JPanel deviceA1= device("");
        p4.add(A1);
        p4.add(deviceA1);

        JLabel A2=new JLabel("A2:");
        JPanel deviceA2= device("");
        p4.add(A2);
        p4.add(deviceA2);

        JLabel B1=new JLabel("B1:");
        JPanel deviceB1= device("");
        p4.add(B1);
        p4.add(deviceB1);

        JLabel B2=new JLabel("B2:");
        JPanel deviceB2= device("");
        p4.add(B2);
        p4.add(deviceB2);

        JLabel C=new JLabel("C  :");
        JPanel deviceC= device("");
        p4.add(C);
        p4.add(deviceC);
        Mframe.add(p4);


        JPanel p5 =new JPanel();
        p5.setSize(90,2310);
        p5.setBackground(Color.white);
        p5.setLocation(520,370);
        p5.setLayout(new FlowLayout(FlowLayout.LEADING, 20, 10));
        p5.setBorder(new TitledBorder(new EtchedBorder(), "图例"));
        JPanel legend1=legend("未占用",Color.gray);

        JPanel legend2=legend("占用",Color.green);

        JPanel legend3=legend("正在运行",Color.yellow);

        JPanel legend4=legend("系统占用", new Color(139,69,19));

        p5.add(legend1);
        p5.add(legend2);
        p5.add(legend3);
        p5.add(legend4);

        Mframe.add(p5);

        JPanel p6=new JPanel();
        p6.setSize(610,310);
        p6.setBackground(Color.white);
        p6.setLocation(640,370);
        p6.setBorder(new TitledBorder(new EtchedBorder(), "磁盘"));
        p6.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        diskPanel = new JPanel();

        // 初始化硬盘颜色数组
        disk = new Color[128];
        initializeram(disk);
        updateDisk(); // 初始更新硬盘视图
        p6.add(diskPanel);
        Mframe.add(p6);


        // 设置定时器，每隔一段时间更新视图
        Timer timer = new Timer(1000, e -> {
            initializeram(ram); // 随机改变硬盘颜色
            updateRam(); // 更新硬盘视图
            initializeram(disk); // 随机改变硬盘颜色
            updateDisk(); // 更新硬盘视图
            updateTime();
        });
        timer.start();

    }

    //需求：根据实参标颜色的方式表示磁盘使用情况，
    private void initializeram(Color[] color) {
        Random random = new Random();

        for (int i = 0; i < color.length; i++) {
            int rand = random.nextInt(4); // 随机选择一种颜色
            switch (rand) {
                case 0:
                    color[i] = Color.GRAY; // 未占用，灰色
                    break;
                case 1:
                    color[i] = Color.GREEN; // 占用，绿色
                    break;
                case 2:
                    color[i] = Color.YELLOW; // 正在运行，黄色
                    break;
                case 3:
                    color[i] = new Color(139, 69, 19); // 系统占用，褐色
                    break;
            }
        }
    }
    private void updateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(new Date());
        timeLabel.setText(  formattedDate);
    }
    private static JPanel createWindow(String label) {
        JPanel window = new JPanel(new BorderLayout());
        window.setBackground(Color.white);

        // 添加标签
        JLabel windowLabel = new JLabel(label, SwingConstants.CENTER);
        windowLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        windowLabel.setBackground(Color.white);
        window.add(windowLabel, BorderLayout.NORTH);

        // 创建窗口内容面板
        JPanel contentPanel = new JPanel();
        contentPanel.setPreferredSize(new Dimension(170, 210));
        contentPanel.setBackground(Color.white);
        contentPanel.setBorder(new LineBorder(new LineBorder(Color.black, 10).getLineColor()));

        // 将内容面板添加到窗口
        window.add(contentPanel, BorderLayout.CENTER);

        return window;
    }
    private static JPanel legend(String label,Color i ) {
        JPanel window = new JPanel(new BorderLayout());
        window.setBackground(Color.white);

        // 添加标签
        JLabel windowLabel = new JLabel(label, SwingConstants.CENTER);
        window.add(windowLabel, BorderLayout.NORTH);

        // 创建窗口内容面板
        JPanel legendPanel = new JPanel();
        legendPanel.setPreferredSize(new Dimension(30, 30));
        legendPanel.setBackground(i);
        legendPanel.setBorder(new LineBorder(new LineBorder(Color.black, 10).getLineColor()));

        // 将内容面板添加到窗口
        window.add(legendPanel, BorderLayout.CENTER);
        return window;
    }

    private static JPanel device(String s) {
        JPanel window = new JPanel(new FlowLayout());
        window.setBackground(Color.white);

        // 创建窗口内容面板
        JPanel devicepanel = new JPanel();
        devicepanel.setPreferredSize(new Dimension(40, 40));
        devicepanel.setBackground(Color.LIGHT_GRAY);
        devicepanel.setBorder(new LineBorder(new LineBorder(Color.black, 10).getLineColor()));
        System.out.println("已运行");
        window.add(devicepanel, FlowLayout.LEFT);

        //创建显示框
        JTextField devicetext=new JTextField(s);
        devicetext.setPreferredSize(new Dimension(100, 40));
        devicetext.setBorder(new LineBorder(new LineBorder(Color.black, 10).getLineColor()));
        System.out.println("已运行");
        devicetext.setEditable(false);
        devicetext.setFocusable(false);
        devicetext.setBackground(Color.white);
        window.add(devicetext,FlowLayout.CENTER);

        return window;
    }
    private void updateRam() {
        ramPanel.removeAll();
        GridLayout gl = new GridLayout(8,8,5,5);
        ramPanel.setLayout(gl);
        ramPanel.setBackground(Color.white);


        // 根据硬盘颜色数组创建并添加盒子
        for (Color color : ram) {
            JPanel box = new JPanel();
            box.setPreferredSize(new Dimension(30, 30));
            box.setBackground(color);
            box.setBorder(new LineBorder(new LineBorder(Color.black, 10).getLineColor()));
            ramPanel.add(box);
        }

        ramPanel.revalidate(); // 重新验证布局
        ramPanel.repaint(); // 重绘界面
    }


    private void updateDisk() {
        diskPanel.removeAll(); // 移除之前颜色
        GridLayout gl = new GridLayout(8,16,5,5);
        diskPanel.setLayout(gl);
        diskPanel.setBackground(Color.white);

        // 根据硬盘颜色数组创建并添加盒子
        for (int i = 0; i < disk.length; i++) {
            JPanel box = new JPanel();
            box.setPreferredSize(new Dimension(30, 30));
            box.setBackground(disk[i]);
            box.setBorder(new LineBorder(new LineBorder(Color.black, 10).getLineColor()));
            diskPanel.add(box);
        }

        diskPanel.revalidate(); // 重新验证布局
        diskPanel.repaint(); // 重绘界面
    }

    

    // 显示窗口
    public void showGUI() {
        Mframe.setVisible(true);
    }
}