
package Frontend;

import RSA.ImageEncryptor;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class ImageViewDisplay  extends ViewDisplay
{
    public ResizablePanel imagePanel;
    public BufferedImage imageImage;
    
    public ImageViewDisplay(File imageFile) throws IOException
    {
        super(500, 500, "Image Preview", true);
        
        imageImage = ImageIO.read(imageFile);
        imagePanel = new ResizablePanel(imageImage);
        this.setLayout(new BorderLayout());
        this.mainDisplayPane.add(imagePanel);
    }
    
    public ImageViewDisplay(BufferedImage imageFile) throws IOException
    {
        super(500, 500, "Image Preview", true);
        
        imageImage = ImageEncryptor.imageCopy(imageFile);
        imagePanel = new ResizablePanel(imageImage);
        this.setLayout(new BorderLayout());
        this.mainDisplayPane.add(imagePanel);
    }
}

