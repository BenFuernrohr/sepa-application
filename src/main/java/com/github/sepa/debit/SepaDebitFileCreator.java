package com.github.sepa.debit;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.java.sepaxml.SEPA;
import org.java.sepaxml.SEPABankAccount;
import org.java.sepaxml.SEPADirectDebit;
import org.java.sepaxml.SEPATransaction;
import org.java.sepaxml.SEPATransaction.Currency;

import com.github.sepa.parsing.DebitTransactionDto;

public class SepaDebitFileCreator {

    private final String creditorID;

    private final SEPABankAccount receiver;
    
    public SepaDebitFileCreator(String creditorID, String creditorIban, String creditorBIC, String creditorName) {
        this.receiver = new SEPABankAccount(creditorIban, creditorBIC, creditorName);
        this.creditorID = creditorID;
    }
    
    public void createSepaFile(String filename, List<DebitTransactionDto> transactionList, Date dueDate) throws IOException {
    
        final List<SEPATransaction> transactions = new ArrayList<SEPATransaction>();
        
        for(DebitTransactionDto transactionDto : transactionList){
            transactions.add(new SEPATransaction(
                new SEPABankAccount(transactionDto.getIban(), transactionDto.getAccountOwner()), 
                BigDecimal.valueOf(transactionDto.getAmount()), 
                transactionDto.getPurposeOfUse(), 
                dueDate, 
                transactionDto.getMandateReferenceNumber(), 
                transactionDto.getDateOfSignature(), 
                Currency.EUR, 
                "Überweisung")); //TODO: what is remittance?
        }

    final SEPA sepa = new SEPADirectDebit(receiver, transactions, creditorID);
    FileUtils.writeStringToFile(new File(filename), sepa.toString(), Charset.defaultCharset());
    }
}
