
package Frontend;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageViewController extends ImageViewDisplay
{
    public ImageViewController(File file) throws IOException
    {
        super(file);
    }
    
    public ImageViewController(BufferedImage image) throws IOException
    {
        super(image);
    }
}
