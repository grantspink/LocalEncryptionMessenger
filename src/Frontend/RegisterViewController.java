
package Frontend;

import App.TheApp;
import RSA.FileManager;
import RSA.LoginManager;
import RSA.User;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class RegisterViewController extends RegisterViewDisplay
{
    boolean registerIsValid;
    
    private class CancelAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            try
            {
                TheApp.swapDisplay(new LoginViewController());
            } catch (IOException ex) {
                Logger.getLogger(RegisterViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private class RegisterAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            if(!registerIsValid)
            {
                JOptionPane.showMessageDialog
                (
                    null,
                    "To register:\n\n" +
                    "\u2022 Your username must be at least 3 characters.\n" +
                    "\u2022 Your password must be at least 6 characters. \n"
                );
                return;
            }
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new TextFileFilter());
            fileChooser.setDialogTitle("Save Private Key");
            int chooserDialog = fileChooser.showSaveDialog(RegisterViewController.this);
            if (chooserDialog == JFileChooser.APPROVE_OPTION)
            {
                String username = userTextField.getText();
                String password = new String(passwordTextField.getPassword());
                File userDirectory = new File(TheApp.fileDirectory + "\\" + username);
                userDirectory.mkdir();
                File inboxDirectory = new File(userDirectory.getAbsolutePath() + "\\" + "inbox");
                inboxDirectory.mkdir();
                File outboxDirectory = new File(userDirectory.getAbsolutePath() + "\\" + "outbox");
                outboxDirectory.mkdir();
                User newUser = new User(username, LoginManager.getSaltedHash(password), userDirectory, TheApp.bitLength);
                
                String fileName = fileChooser.getSelectedFile().toString();
                if (!fileName.endsWith(".txt"))
                    fileName += ".txt";
                File file = new File(fileName);
                FileManager.writeTextToFile(file.getAbsolutePath(), newUser.getKeyPair().getPrivateKey().toString());
                FileManager.writeTextToFile
                (
                    "passwords.txt",
                    newUser.getUsername()+"&"+
                    newUser.getPasswordHash()+"&"+
                    newUser.getUserDirectory().getAbsolutePath()+"&"+
                    newUser.getKeyPair().getPublicKey().toString()+"&"+
                    newUser.getKeyPair().getModulus().toString()+ "//"
                );
                TheApp.users.add(newUser);
                JOptionPane.showMessageDialog(null, "Registration Succesful");
                try
                {
                    TheApp.swapDisplay(new LoginViewController());
                } catch (IOException ex) {
                    Logger.getLogger(RegisterViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if (chooserDialog == JFileChooser.CANCEL_OPTION)
            {
                JOptionPane.showMessageDialog(null, "Could not register, saving of private key was cancelled");
            }
        }
    }
    
    KeyListener TextFieldKeyListener = new KeyListener()
    {
        @Override
        public void keyTyped(KeyEvent ke)
        {
            this.setRegisterButton();
            this.updateButtonColor();
        }

        @Override
        public void keyPressed(KeyEvent ke)
        {
            this.setRegisterButton();
            this.updateButtonColor();
        }

        @Override
        public void keyReleased(KeyEvent ke)
        {
            this.setRegisterButton();
            this.updateButtonColor();
        }
        
        public void setRegisterButton()
        {
            registerIsValid = userTextField.getText().length()>=3 && passwordTextField.getPassword().length>=6;
        }
        
        public void updateButtonColor()
        {
            if(registerIsValid)
            {
                registerButton.setBackground(Color.GREEN);
                registerButton.setContentAreaFilled(false);
                registerButton.setOpaque(true);
            }
            else
            {
                registerButton.setBackground(Color.RED);
                registerButton.setContentAreaFilled(false);
                registerButton.setOpaque(true);
            }
        }
    };
    
    // Initializes the functions
    public RegisterViewController() throws IOException
    {
        super();
        
        this.registerIsValid = false;
        this.cancelButton.addActionListener(new CancelAction());
        this.registerButton.addActionListener(new RegisterAction());
        this.userTextField.addKeyListener(this.TextFieldKeyListener);
        this.passwordTextField.addKeyListener(this.TextFieldKeyListener);
    }
}
