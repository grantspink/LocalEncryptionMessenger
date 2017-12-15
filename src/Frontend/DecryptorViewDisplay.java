
package Frontend;

import java.io.IOException;

public abstract class DecryptorViewDisplay  extends CryptorViewDisplay
{
    public DecryptorViewDisplay(MainViewController mainViewController) throws IOException
    {
        super(mainViewController, "Decrypt");
        
        this.pack();
    }
}

