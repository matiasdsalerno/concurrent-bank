package com.progrmmingwithmati.concurrency.bank;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BankAccount {

  private String userId;
  private String currency;
  private BigDecimal amount;

  public BankAccount(String userId, String currency, BigDecimal amount) {
    this.userId = userId;
    this.currency = currency;
    this.amount = amount;
  }

  public void processTransaction(BankTransaction transaction) {
    switch (transaction.type()) {
      case CREDIT -> amount = amount.add(transaction.amount());
      case DEBIT -> amount = amount.subtract(transaction.amount());
    }
  }

  public synchronized void processTransactionSync(BankTransaction transaction) {
    switch (transaction.type()) {
      case CREDIT -> amount = amount.add(transaction.amount());
      case DEBIT -> amount = amount.subtract(transaction.amount());
    }
  }

}
