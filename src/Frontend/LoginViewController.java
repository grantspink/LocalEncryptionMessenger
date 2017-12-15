
package Frontend;

import App.TheApp;
import RSA.LoginManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class LoginViewController extends LoginViewDisplay
{
    private class LoginAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            LoginManager.checkValidLogin(userTextField.getText(), String.valueOf(passwordTextField.getPassword()));
            if(LoginManager.getValidLogin())
            {
                try
                {
                    TheApp.swapDisplay(new MainViewController());
                }
                catch (IOException ex)
                {
                    Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Invalid login");
            }
        }
    }
    
    private class RegisterAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            try
            {
                TheApp.swapDisplay(new RegisterViewController());
            }
            catch (IOException ex)
            {
                Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    KeyListener TextFieldKeyListener = new KeyListener()
    {
        @Override
        public void keyTyped(KeyEvent ke)
        {
            if( userTextField.getText().length()<3 ||
                passwordTextField.getPassword().length<6)
                loginButton.setEnabled(false);
            else
                loginButton.setEnabled(true);
        }

        @Override
        public void keyPressed(KeyEvent ke)
        {
            if( userTextField.getText().length()<3 ||
                passwordTextField.getPassword().length<6)
                loginButton.setEnabled(false);
            else
                loginButton.setEnabled(true);
        }

        @Override
        public void keyReleased(KeyEvent ke)
        {
            if( userTextField.getText().length()<3 ||
                passwordTextField.getPassword().length<6)
                loginButton.setEnabled(false);
            else
                loginButton.setEnabled(true);
        }
    };
    
    // Initializes the functions
    public LoginViewController() throws IOException
    {
        super();
        
        this.loginButton.addActionListener(new LoginAction());
        this.registerButton.addActionListener(new RegisterAction());
        this.userTextField.addKeyListener(this.TextFieldKeyListener);
        this.passwordTextField.addKeyListener(this.TextFieldKeyListener);
    }
}
