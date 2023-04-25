package com.progrmmingwithmati.concurrency.bank;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.IntStream;

public class App {

  public static void main(String[] args) {
    var bankAccount = new BankAccount("Matias", "USD", BigDecimal.ZERO);

    var begin = Instant.now();
    IntStream.range(0, 1000000)
            .mapToObj(i -> new BankTransaction(BankTransaction.TransactionType.CREDIT, "Matias",  BigDecimal.TEN))
            .forEach(bankAccount::processTransaction);
    var end = Instant.now();

    System.out.printf("Bank account balance: %s. Time: %s ms.\n", bankAccount.getAmount(), ChronoUnit.MILLIS.between(begin, end));

    bankAccount = new BankAccount("Matias", "USD", BigDecimal.ZERO);

    begin = Instant.now();
    IntStream.range(0, 1000000)
            .parallel()
            .mapToObj(i -> new BankTransaction(BankTransaction.TransactionType.CREDIT, "Matias",  BigDecimal.TEN))
            .forEach(bankAccount::processTransaction);
    end = Instant.now();

    System.out.printf("Bank account balance: %s. Time: %s ms.\n", bankAccount.getAmount(), ChronoUnit.MILLIS.between(begin, end));

    bankAccount = new BankAccount("Matias", "USD", BigDecimal.ZERO);

    begin = Instant.now();
    IntStream.range(0, 1000000)
            .parallel()
            .mapToObj(i -> new BankTransaction(BankTransaction.TransactionType.CREDIT, "Matias",  BigDecimal.TEN))
            .forEach(bankAccount::processTransactionSync);
    end = Instant.now();

    System.out.printf("Bank account balance: %s. Time: %s ms.\n", bankAccount.getAmount(), ChronoUnit.MILLIS.between(begin, end));

  }
}
