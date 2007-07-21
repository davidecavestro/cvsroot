import com.davidecavestro.common.gui.CompositeIcon;
import com.davidecavestro.common.gui.VTextIcon;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class Test implements SwingConstants {
	public static void main(String[] args) {
		new Test();
    }

	public Test() {
		JFrame fr = new JFrame("Test");
		fr.getContentPane().add(testComponent());
		fr.setSize(new Dimension(600, 400));
		fr.show();
	}
	JComponent testComponent() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,3));
		panel.add(makeTabpane(null, sEnglish, VTextIcon.ROTATE_DEFAULT));
		panel.add(makeTabpane(new Font("Osaka", 0, 12), sJapanese, VTextIcon.ROTATE_DEFAULT));
		panel.add(makeTabpane(null, sEnglish, VTextIcon.ROTATE_NONE));
		return panel;
	}
	
	JTabbedPane makeTabpane(Font font, String[] strings, int rotateHint) {
		JTabbedPane panel = new JTabbedPane(LEFT);
		Icon graphicIcon = UIManager.getIcon("FileView.computerIcon");
		if (font != null)
			panel.setFont(font);
		for (int i = 0; i < strings.length; i++) {
			VTextIcon textIcon = new VTextIcon(panel, strings[i], rotateHint);
			CompositeIcon icon = new CompositeIcon(graphicIcon, textIcon);
			panel.addTab(null, icon, makePane());
		}
		return panel;
	}
	
	JPanel makePane() {
		JPanel p = new JPanel();
		p.setOpaque(false);
		return p;
	}
	
	static String[] sEnglish = {"Apple", "Java", "OS X"};
	static String[] sJapanese = {"\u65e5\u672c\u8a9e", "\u5c45\u3068\u3001\u304d\u3087\u3068"};
}

