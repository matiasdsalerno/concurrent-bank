package com.progrmmingwithmati.concurrency.bank;

import java.math.BigDecimal;

public record BankTransaction(
        TransactionType type,
        String userId,
        BigDecimal amount
) {

  public enum TransactionType {
    DEBIT, CREDIT
  }
}

