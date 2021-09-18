package com.github.sepa.parsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ExcelFileParser {

    public static final String COMMA_DELIMITER = ",";
    public static final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public List<DebitTransactionDto> parseFile(File file) throws FileNotFoundException, IOException {

        List<List<String>> records = readRecords(file);
        int indexOfKontoinhaber = findIndexOf("Kontoinhaber", records);
        int indexOfIban = findIndexOf("IBAN", records);
        int indexOfMandatsreferenznummer = findIndexOf("Mandatsreferenz-Nummer", records);
        int indexOfUnterschriftsdatum = findIndexOf("Unterschriftsdatum", records);
        int indexOfVerwendungszweck = findIndexOf("Verwendungszweck", records);
        int indexOfBetrag = findIndexOf("Betrag", records);

        return records.stream().skip(1).map(record -> {
            try {
                return DebitTransactionDto.builder()
                    .accountOwner(record.get(indexOfKontoinhaber))
                    .iban(record.get(indexOfIban))
                    .mandateReferenceNumber(record.get(indexOfMandatsreferenznummer))
                    .dateOfSignature(dateFormat.parse(record.get(indexOfUnterschriftsdatum)))
                    .purposeOfUse(record.get(indexOfVerwendungszweck))
                    .amount(Double.parseDouble(record.get(indexOfBetrag).substring(0, record.get(indexOfBetrag).length()-2)))
                    .build();
            } catch (NumberFormatException | ParseException e) {
                return null;
            }
        }).collect(Collectors.toList());
    }
    

    private int findIndexOf(String headline, List<List<String>> mitglieder) {
        List<String> firstLine = mitglieder.get(0);
        for (int i = 0; i < firstLine.size(); i++) {
            if (headline.contentEquals(firstLine.get(i))) {
                return i;
            }
        }
        throw new IllegalArgumentException(String.format("Headline %s existiert nicht", headline));
    }

    private List<List<String>> readRecords(File file) throws IOException {
        List<List<String>> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                records.add(Arrays.asList(values));
            }
        }
        return records;
    }
}
