
package Frontend;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.AbstractDocument;

public abstract class LoginViewDisplay  extends ViewDisplay
{
    public JPanel loginPanel;
    public JLabel userLabel;
    public JLabel passwordLabel;
    
    public JButton loginButton;
    public JButton registerButton;
    
    public JTextArea userTextField;
    public JPasswordField passwordTextField;
    
    public JPanel userTextPanel;
    public JPanel passwordTextPanel;
    
    public JScrollPane userTextScroll;
    public JScrollPane passwordTextScroll;
    
    public LoginViewDisplay()
    {
        super(300, 150, "Login", false);
        
        //////////////////////////
        
        userLabel = new JLabel("User", JLabel.LEFT);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        mainDisplayPane.add(this.userLabel, c);
        
        /////////////////////////
        
        userTextField = new JTextArea(1,10);
        AbstractDocument userDocument=(AbstractDocument)userTextField.getDocument();
        userDocument.setDocumentFilter(new SizeFilter(20));
        
        this.userTextScroll = new JScrollPane(userTextField);
        
        this.userTextPanel = new JPanel();
        this.userTextPanel.add(this.userTextScroll);
        this.userTextScroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER );
        this.userTextScroll.setHorizontalScrollBarPolicy ( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        this.userTextPanel.setLayout(new GridLayout(0, 1));
        
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        mainDisplayPane.add(this.userTextPanel, c);
        
        /////////////////////////
        
	passwordLabel = new JLabel("Password", JLabel.LEFT);
        passwordLabel.setPreferredSize(new Dimension(100,30));
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        mainDisplayPane.add(this.passwordLabel, c);
        
        /////////////////////////
        
	passwordTextField = new JPasswordField();
        AbstractDocument passwordDocument=(AbstractDocument)passwordTextField.getDocument();
        passwordDocument.setDocumentFilter(new SizeFilter(20));
        
        this.passwordTextScroll = new JScrollPane(passwordTextField);
        
        this.passwordTextPanel = new JPanel();
        this.passwordTextPanel.add(this.passwordTextScroll);
        this.passwordTextScroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER );
        this.passwordTextScroll.setHorizontalScrollBarPolicy ( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        this.passwordTextPanel.setLayout(new GridLayout(0, 1));
        
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        mainDisplayPane.add(this.passwordTextPanel, c);
        
        /////////////////////////
        
	loginButton = new JButton("Login");
        loginButton.setEnabled(false);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.fill = GridBagConstraints.BOTH;
        mainDisplayPane.add(this.loginButton, c);
        
        /////////////////////////
        
	registerButton = new JButton("Register");
        
        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 2;
        c.fill = GridBagConstraints.BOTH;
        mainDisplayPane.add(this.registerButton, c);
        
        /////////////////////////
    }
}

