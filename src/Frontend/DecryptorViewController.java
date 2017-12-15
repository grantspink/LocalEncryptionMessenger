
package Frontend;

import RSA.Decryptor;
import java.io.IOException;

public class DecryptorViewController extends DecryptorViewDisplay
{
    public DecryptorViewController(MainViewController mainViewController) throws IOException
    {
        super(mainViewController);
        
        this.setCryptor(new Decryptor(this));
        this.addListeners();
    }
}
