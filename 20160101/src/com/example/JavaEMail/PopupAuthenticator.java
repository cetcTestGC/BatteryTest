package com.example.JavaEMail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class PopupAuthenticator extends Authenticator{
	
	private String userName;  
    private String password;  
    
    public PopupAuthenticator(String userName, String password)  
    {  
        this.userName = userName;  
        this.password = password;  
    }  
    
    public PasswordAuthentication getPasswordAuthentication() {  
        return new PasswordAuthentication(userName, password);  
    }
}
