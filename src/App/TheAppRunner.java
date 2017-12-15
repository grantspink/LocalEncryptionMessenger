
package App;

public class TheAppRunner
{
    public static void main(String[] args)
    {
        try  
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Metal".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex)
        {
            System.err.println(ex);
        }

        java.awt.EventQueue.invokeLater(new TheApp());
    }
}
