package css.front;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.*;

public class MainGui {
    private JFrame Mframe;

    public MainGui() {
        // 创建主界面
        Mframe = new JFrame("模拟操作系统");
        Mframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Mframe.setBackground(Color.green);
        Mframe.setLayout(null); //取消默认的布局BorderLayout



        // Add ActionListener to the button
       // button.addActionListener(e -> showMessage());

        // Add the button to the content pane
        //frame.getContentPane().add(button);

        // Set the size of the JFrame
        Mframe.setSize(1280, 720);
        Mframe.setLocation(150, 50);
        Mframe.setResizable(false);
        Panel p1=new Panel();
        p1.setSize(610,300);
        p1.setBackground(Color.white);
        p1.setLocation(10,60);
        Mframe.add(p1);
//        Panel pan = new Panel(); //创建面板
//        pan.setSize(100,100);
//        pan.setBackground(Color.yellow);
//        frame.add(pan);
        Panel p2=new Panel();
        p2.setSize(610,300);
        p2.setBackground(Color.white);
        p2.setLocation(640,60);
        Mframe.add(p2);


    }

    // Method to display a message dialog
    private void showMessage() {
        JOptionPane.showMessageDialog(Mframe, "Button Clicked!");
    }

    // Method to make the GUI visible
    public void showGUI() {
        Mframe.setVisible(true);
    }
}