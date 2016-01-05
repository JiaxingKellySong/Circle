package client.media;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by jiaxing song on 2015/11/4.
 */
public class VideoPanel extends JPanel {
    BufferedImage image;

    public VideoPanel() {}

    public VideoPanel (BufferedImage image) {
        this.image = image;
    }

    public void setImage(BufferedImage image) { this.image = image; }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }
}