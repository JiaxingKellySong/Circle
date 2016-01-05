package test;

import org.junit.Test;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by jiaxing song on 2015/11/18.
 */
public class UITest {

    @Test
    public void testJSlider() {
        JFrame frame = new JFrame("Test");
        JPanel panel = new JPanel();
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 100, 1);

        addListener(slider);

        panel.add(slider);
        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(320,240);
        frame.setVisible(true);
        while(true) {}
    }

    private void addListener(final JSlider slider) {
        slider.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println(slider.getValue());
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }
}
