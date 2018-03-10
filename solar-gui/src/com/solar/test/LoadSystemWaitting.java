package com.solar.test;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JWindow;

public class LoadSystemWaitting {  
    private static LoadSystemWaitting loadSystemWaitting;  
    private Image image;  
    private JWindow jw;  
    public static void main(String args[]){  
        LoadSystemWaitting.getInstance(true);  
    }  
    public static void instanceInit(){  
        if (loadSystemWaitting == null) {  
            loadSystemWaitting = new LoadSystemWaitting();  
        }  
    }  
    public static LoadSystemWaitting getInstance(boolean alwaysOnTop) {  
        if (loadSystemWaitting == null) {  
            loadSystemWaitting = new LoadSystemWaitting();  
        }  
        loadSystemWaitting.jw.setAlwaysOnTop(alwaysOnTop);  
        loadSystemWaitting.setVisible(true);  
        return loadSystemWaitting;  
    }  
  
    private JPanel getJPanel() {  
        JPanel jp = new JPanel() {  
  
            @Override  
            protected void paintComponent(Graphics g) {  
                // TODO Auto-generated method stub  
                super.paintComponent(g);  
                g.drawImage(image, 0, 0, this);  
            }  
  
        };  
        return jp;  
    }  
  
    public static void closeWindow() {  
        if (loadSystemWaitting != null) {  
            loadSystemWaitting.setVisible(false);  
        }  
    }  
    public void setVisible(boolean b){  
        jw.setVisible(b);  
    }  
  
  
    private LoadSystemWaitting() {  
        image = Toolkit.getDefaultToolkit().createImage(  
                LoadSystemWaitting.class  
                        .getResource("/resources/images/solar/fan_good.gif"));  
        jw = new JWindow();  
        jw.setSize(300, 300);  
        jw.setLayout(new BorderLayout());  
        jw.add(getJPanel(), BorderLayout.CENTER);  
        jw.setLocationRelativeTo(null);
    }  
  
}  