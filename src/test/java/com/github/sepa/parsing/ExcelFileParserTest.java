package com.github.sepa.parsing;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.junit.Test;

public class ExcelFileParserTest {

    private ExcelFileParser sut = new ExcelFileParser();
    
    private DebitTransactionDto debitTransactionDto1;
    private DebitTransactionDto debitTransactionDto2;
    
    @Test
    public void parserShouldParseFileCorrectly() throws FileNotFoundException, IOException, ParseException {
        initComparisonDtos();
        File testFile = new File(this.getClass().getResource("/TestExcelWorkbook.csv").getFile());
        
        List<DebitTransactionDto> result = sut.parseFile(testFile);
        
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualToComparingFieldByField(debitTransactionDto1);
        assertThat(result.get(1)).isEqualToComparingFieldByField(debitTransactionDto2);
    }

    private void initComparisonDtos() throws ParseException {
        debitTransactionDto1 = DebitTransactionDto.builder()
            .accountOwner("Max Mustermann")
            .iban("DE23 2004 1133 0008 3033 07")
            .mandateReferenceNumber("Mandatsreferenz-Nummer")
            .dateOfSignature(ExcelFileParser.dateFormat.parse("3.5.2021"))
            .purposeOfUse("Verwendungszweck")
            .amount(123.45)
            .build();
        
        debitTransactionDto2 = DebitTransactionDto.builder()
            .accountOwner("Nadine Neumann")
            .iban("DE12 3005 2244 0052 4044 08")
            .mandateReferenceNumber("Miete 145")
            .dateOfSignature(ExcelFileParser.dateFormat.parse("3.6.2021"))
            .purposeOfUse("Miete")
            .amount(333.56)
            .build();
        
    }
}
