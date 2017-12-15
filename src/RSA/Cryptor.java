
package RSA;

import java.util.LinkedList;
import javax.swing.SwingWorker;

public abstract class Cryptor<T> extends SwingWorker<T, String>
{
    public final LinkedList<String> progressOutput;
    
    public Cryptor()
    {
        this.progressOutput = new LinkedList<>();
    }
    
    public double printPercent(String content, double percent, int log, int count, int length)
    {
        if(log>=3)
        {
            trace(String.format( "%." + String.valueOf(log-2) + "f", percent) + "% " + content + ": " + count + " of " + length);
            return 1.0/Math.pow(10, log-2);
        }
        else
        {
            trace((int)percent + "% " + content + ": " + count + " of " + length);
            return 100/Math.pow(10, log);
        }
    }
    
    public void trace(String s)
    {
        this.progressOutput.add(s);
        this.process(this.progressOutput);
    }
}
