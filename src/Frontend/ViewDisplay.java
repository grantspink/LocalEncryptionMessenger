
package Frontend;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public abstract class ViewDisplay extends JFrame
{
    public Container mainDisplayPane;
    public GridBagConstraints c;
    
    public ViewDisplay(int width, int height, String name, boolean resizable)
    {
        this.setSize(width, height);
        this.setTitle(name);
        this.setVisible(true);
        this.setResizable(resizable);
        this.mainDisplayPane = getContentPane();
        this.mainDisplayPane.setLayout(new GridBagLayout());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
    
    public class ResizablePanel extends JPanel
    {

        private final Image originalImage;
        private Image scaled;

        public ResizablePanel(Image originalImage)
        {
            this.originalImage = originalImage;
            this.invalidate();
        }
        
        @Override
        public Dimension getPreferredSize()
        {
            if(this.originalImage==null)
                return super.getPreferredSize();
            else
                return new Dimension(this.originalImage.getWidth(this), this.originalImage.getHeight(this));
        }

        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Image newImage = null;
            if(this.scaled!=null)
                newImage = this.scaled;
            else if(this.originalImage!=null)
                newImage = this.originalImage;

            if(newImage!=null)
            {
                g.drawImage
                (
                    newImage,
                    (this.getWidth()-newImage.getWidth(this))/2,
                    (this.getHeight()-newImage.getHeight(this))/2,
                    this
                );
            }
        }

        @Override
        public void invalidate()
        {
            this.scaled = this.getScaledImage();
            super.invalidate();
        }

        protected BufferedImage getScaledImage()
        {
            GraphicsConfiguration graphicConfig = getGraphicsConfiguration();
            if (graphicConfig == null)
                graphicConfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

            BufferedImage originalImage = graphicConfig.createCompatibleImage(this.originalImage.getWidth(this), this.originalImage.getHeight(this), Transparency.TRANSLUCENT);
            originalImage.coerceData(true);
            
            Graphics2D originalGraphic = originalImage.createGraphics();
            originalGraphic.drawImage(this.originalImage, 0, 0, this);
            originalGraphic.dispose();
            
            double scaleCoefficient = Math.max
            ((double)this.getSize().height/(double)this.originalImage.getHeight(this),
                (double)this.getSize().width/(double)this.originalImage.getWidth(this)
            );
            
            BufferedImage returnImage = originalImage;
            BufferedImage scaledImage;
            int endWidth = (int)Math.round(originalImage.getWidth()*scaleCoefficient);
            int endHeight = (int)Math.round(originalImage.getHeight()*scaleCoefficient);
            int startWidth = originalImage.getWidth();
            int startHeight = originalImage.getHeight();
            Graphics2D scaledGraphic;

            if(scaleCoefficient > 1.0d || endHeight <= 0 && endWidth <= 0 && scaleCoefficient <= 1.0d) 
                return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            
            do
            {
                if (scaleCoefficient <= 1.0d)
                {
                    if (startWidth > endWidth)
                    {
                        startWidth /= 2;
                        if (startWidth < endWidth)
                            startWidth = endWidth;
                    }
                    if (startHeight > endHeight)
                    {
                        startHeight /= 2;
                        if (startHeight < endHeight)
                            startHeight = endHeight;
                    }
                    scaledImage = new BufferedImage(Math.max(startWidth, 1), Math.max(startHeight, 1), BufferedImage.TYPE_INT_ARGB);
                }
                else
                {
                    if (startWidth < endWidth)
                    {
                        startWidth *= 2;
                        if (startWidth > endWidth)
                            startWidth = endWidth;
                    }
                    if (startHeight < endHeight)
                    {
                        startHeight *= 2;
                        if (startHeight > endHeight)
                            startHeight = endHeight;
                    }
                    scaledImage = new BufferedImage(startWidth, startHeight, BufferedImage.TYPE_INT_ARGB);
                }

                scaledGraphic = scaledImage.createGraphics();
                scaledGraphic.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                scaledGraphic.drawImage(returnImage, 0, 0, startWidth, startHeight, null);
                scaledGraphic.dispose();

                returnImage = scaledImage;
            } while (startWidth != endWidth || startHeight != endHeight);
            return returnImage;
        }
    }
    
    public class ImagePanel extends JPanel
    {
        private Image image;
        private ImageObserver imageObserver;
        private File imageFile;
        
        public void setImage(File file)
        {
            this.imageFile = file;
            String filename = file.getAbsolutePath();
            ImageIcon icon = new ImageIcon(filename);
            this.image = icon.getImage();
            this.imageObserver = icon.getImageObserver();
        }
        
        public Image getImage(){ return this.image; }
        public File getImageFile(){ return this.imageFile; }
        
        public void removeImage()
        {
            this.image = null;
            this.imageObserver = null;
        }
        
        @Override
        public void paint(Graphics g)
        {
            super.paint(g) ;
            g.drawImage(this.image,  0, 0, getWidth(), getHeight(), this.imageObserver);
        }
    }
    
    public class BitPanel extends JPanel
    {
        private final int position;
        public BitPanel(int position)
        {
            this.position = position;
        }
        
        public int getPosition(){ return this.position; }
    }
    
    public class SizeFilter extends DocumentFilter
    {
        public final int maxLength;
 
        public SizeFilter(int maxLength)
        {
            this.maxLength = maxLength;
        }
 
        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offs, String text, AttributeSet a)
        throws BadLocationException
        {
            if ((fb.getDocument().getLength() + text.length()) < this.maxLength)
                super.insertString(fb, offs, text, a);
            else
            {
                this.showSizeError();
                Toolkit.getDefaultToolkit().beep();
            }
        }
     
        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet a)
        throws BadLocationException
        {
            if ((fb.getDocument().getLength() + text.length() - length) < this.maxLength)
                super.replace(fb, offset, length, text, a);
            else
            {
                this.showSizeError();
                Toolkit.getDefaultToolkit().beep();
            }
        }
        
        private void showSizeError()
        {
            JOptionPane.showMessageDialog(null, "Input is too large");
        }
    }
    
    public class SizeAndNumberFilter extends SizeFilter
    {
        public SizeAndNumberFilter(int maxLength)
        {
            super(maxLength);
        }
        
        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String text, AttributeSet a) throws BadLocationException
        {
            if(numberTest(text))
                super.insertString(fb, offset, text, a);
            else
                showNumberError();
        }
        
        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet a) throws BadLocationException
        {
            if(numberTest(text))
                super.replace(fb, offset, length, text, a);
            else
                showNumberError();
        }
        
        private boolean numberTest(String text)
        {
            try
            {
                Integer.parseInt(text);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        
        private void showNumberError()
        {
            JOptionPane.showMessageDialog(null, "You must enter a number");
        }
    }
    
    public class SizeAndAlphanumericFilter extends SizeFilter
    {
        public SizeAndAlphanumericFilter(int maxLength)
        {
            super(maxLength);
        }
        
        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException
        {
            if(isAlphanumeric(text))
                super.insertString(fb, offset, text, attr);
            else
                showAlphanumericError();
        }
        
        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException
        {
            if(isAlphanumeric(text))
                super.replace(fb, offset, length, text, attrs);
            else
                showAlphanumericError();
        }
        
        private boolean isAlphanumeric(String text)
        {
            return text.matches("[A-Za-z0-9]+");
        }
        
        private void showAlphanumericError()
        {
            JOptionPane.showMessageDialog(null, "You must enter a letter or number");
        }
    }
    
    public class TextFileFilter extends FileFilter
    {
        @Override
        public boolean accept(File f)
        {
            if(f.isDirectory())
            {
                return true;
            }
            return f.getName().endsWith(".txt");
        }
 
        @Override
        public String getDescription()
        {
            return "Text files (*.txt)";
        }
    }
    
    public class ImageFileFilter extends FileFilter
    {
        @Override
        public boolean accept(File f)
        {
            if(f.isDirectory())
            {
                return true;
            }
            return f.getName().endsWith(".png");
        }
 
        @Override
        public String getDescription()
        {
            return "Text files (*.png)";
        }
    }
    
    public static int[] listToIntArray(List<Integer> integers)
    {
        int[] arr = new int[integers.size()];
        for (int i = 0; i < arr.length; i++)
            arr[i] = integers.get(i);
        return arr;
    }
}
