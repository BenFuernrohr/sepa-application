package com.github.sepa.parsing;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DebitTransactionDto {
    private final String accountOwner;
    private final String iban;
    private final String mandateReferenceNumber;
    private final Date dateOfSignature;
    private final String purposeOfUse;
    private final double amount;
}
