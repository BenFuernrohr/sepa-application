package com.github.sepa.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;

import com.github.sepa.debit.SepaDebitFileCreator;
import com.github.sepa.parsing.ExcelFileParser;

public class SepaApplicationMainClass {

    public static void main(String[] args) {
    	SepaApplicationMainClass mainClass = new SepaApplicationMainClass();
    	mainClass.initUi();
    }

    
    void initUi() {    	
        JFrame frame = new JFrame("SEPA Excel Transformer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,300);
        
        JTextField creditorField = new JTextField("Creditor-Id des Einziehenden:");
        JTextField ibanField = new JTextField("IBAN des Einziehenden:");
        JTextField nameField = new JTextField("Name des Einziehenden:");
        JFileChooser fileChooser = new JFileChooser("Bitte Datei auswählen:");
        
        JButton generateButton = new JButton("SEPA XML Generieren");
        JButton openFileButton = new JButton("Datei auswählen:");
        openFileButton.setEnabled(false);
        
        openFileButton.addActionListener(
        		new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						int returnValue = fileChooser.showOpenDialog(frame);
						if (returnValue == JFileChooser.APPROVE_OPTION) {
							generateButton.setEnabled(true);
						}
					}        			
        		}
		);       
        
        generateButton.addActionListener(
        		new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						SepaDebitFileCreator sepaDebitFileCreator = new SepaDebitFileCreator(creditorField.getText(), ibanField.getText(), nameField.getText());
						File file = fileChooser.getSelectedFile();
						ExcelFileParser parser = new ExcelFileParser();
						try {
							sepaDebitFileCreator.createSepaFile(file.getName() + ".xml", parser.parseFile(file), Date.from(Instant.now()));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}        			
        		}
		);
        
        
        
        
        
        frame.getContentPane().add(creditorField);
        frame.getContentPane().add(ibanField);
        frame.getContentPane().add(nameField);
        frame.getContentPane().add(openFileButton);
        frame.getContentPane().add(generateButton);
        
        frame.setVisible(true);
    }    
}
