
package Frontend;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.AbstractDocument;

public abstract class RegisterViewDisplay  extends ViewDisplay
{
    public JPanel loginPanel;
    public JLabel userLabel;
    public JLabel passwordLabel;
    
    public JButton cancelButton;
    public JButton registerButton;
    
    public JTextArea userTextField;
    public JPasswordField passwordTextField;
    
    public JPanel userTextPanel;
    public JPanel passwordTextPanel;
    
    public JScrollPane userTextScroll;
    public JScrollPane passwordTextScroll;
    
    public RegisterViewDisplay()
    {
        super(300, 150, "Register", false);
        
        //////////////////////////
        
        this.userLabel = new JLabel("New Username", JLabel.LEFT);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 0;
        this.c.gridy = 0;
        this.c.fill = GridBagConstraints.HORIZONTAL;
        this.mainDisplayPane.add(this.userLabel, c);
        
        /////////////////////////
        
        this.userTextField = new JTextArea(1,10);
        AbstractDocument userDocument=(AbstractDocument)this.userTextField.getDocument();
        userDocument.setDocumentFilter(new SizeAndAlphanumericFilter(20));
        
        this.userTextScroll = new JScrollPane(this.userTextField);
        
        this.userTextPanel = new JPanel();
        this.userTextPanel.add(this.userTextScroll);
        this.userTextScroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER );
        this.userTextScroll.setHorizontalScrollBarPolicy ( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        this.userTextPanel.setLayout(new GridLayout(0, 1));
        
        this.c = new GridBagConstraints();
        this.c.gridx = 1;
        this.c.gridy = 0;
        this.c.gridwidth = 2;
        this.c.fill = GridBagConstraints.BOTH;
        this.mainDisplayPane.add(this.userTextPanel, c);
        
        /////////////////////////
        
	this.passwordLabel = new JLabel("New Password", JLabel.LEFT);
        this.passwordLabel.setPreferredSize(new Dimension(100,30));
        
        this.c = new GridBagConstraints();
        this.c.gridx = 0;
        this.c.gridy = 1;
        this.c.fill = GridBagConstraints.HORIZONTAL;
        this.mainDisplayPane.add(this.passwordLabel, c);
        
        /////////////////////////
        
	this.passwordTextField = new JPasswordField();
        AbstractDocument passwordDocument=(AbstractDocument)this.passwordTextField.getDocument();
        passwordDocument.setDocumentFilter(new SizeFilter(20));
        
        this.passwordTextScroll = new JScrollPane(this.passwordTextField);
        
        this.passwordTextPanel = new JPanel();
        this.passwordTextPanel.add(this.passwordTextScroll);
        this.passwordTextScroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER );
        this.passwordTextScroll.setHorizontalScrollBarPolicy ( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        this.passwordTextPanel.setLayout(new GridLayout(0, 1));
        
        this.c = new GridBagConstraints();
        this.c.gridx = 1;
        this.c.gridy = 1;
        this.c.gridwidth = 2;
        this.c.fill = GridBagConstraints.BOTH;
        this.mainDisplayPane.add(this.passwordTextPanel, c);
        
        /////////////////////////
        
	this.cancelButton = new JButton("Cancel");
        
        this.c = new GridBagConstraints();
        this.c.gridx = 0;
        this.c.gridy = 2;
        this.c.fill = GridBagConstraints.BOTH;
        this.mainDisplayPane.add(this.cancelButton, c);
        
        /////////////////////////
        
	this.registerButton = new JButton("Register");
        this.registerButton.setBackground(Color.RED);
        this.registerButton.setContentAreaFilled(false);
        this.registerButton.setOpaque(true);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 2;
        this.c.gridy = 2;
        this.c.fill = GridBagConstraints.BOTH;
        this.mainDisplayPane.add(this.registerButton, c);
        
        /////////////////////////
    }
}

