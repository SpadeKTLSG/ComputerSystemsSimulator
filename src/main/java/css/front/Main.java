package css.front;

// Main.java
public class Main {
    public static void main(String[] args) {
        // 运行图形化主界面线程
        javax.swing.SwingUtilities.invokeLater(() -> {
            // 创建图形化界面
            MainGui gui = new MainGui();
            // 设置面板可见
            gui.showGUI();
        });
    }
}