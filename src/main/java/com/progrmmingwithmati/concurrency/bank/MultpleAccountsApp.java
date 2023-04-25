package com.progrmmingwithmati.concurrency.bank;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class MultpleAccountsApp {

  public static void main(String[] args) {

    System.out.println("loading methods");
    runParallelExample();
    runSequentialExample();

    System.out.println("Executing sequential");
    runSequentialExample();
    runSequentialExample();
    runSequentialExample();
    runSequentialExample();
    runSequentialExample();
    runSequentialExample();

    System.out.println("Executing parallel");
    runParallelExample();
    runParallelExample();
    runParallelExample();
    runParallelExample();
    runParallelExample();
    runParallelExample();

  }

  private static void runParallelExample() {
    Instant end;
    Instant begin;
    List<BankAccount> bankAccounts;
    bankAccounts = List.of(
            new BankAccount("Matias", "USD", BigDecimal.ZERO),
            new BankAccount("Pablo", "USD", BigDecimal.ZERO),
            new BankAccount("German", "USD", BigDecimal.ZERO)
    );

    begin = Instant.now();
    List<BankAccount> finalBankAccounts1 = bankAccounts;
    IntStream.range(0, 1000000)
            .mapToObj(i -> createBankTransaction())
            .map(t -> getAccountAndTransaction(finalBankAccounts1, t))
            .forEach(AccountAndTransaction::processTransactionSync);

    end = Instant.now();

    System.out.printf("Bank accounts balance: %s. Time: %s ms.\n", bankAccounts, Duration.between(begin, end).toMillis());
  }

  private static void runSequentialExample() {
    var bankAccounts = List.of(
            new BankAccount("Matias", "USD", BigDecimal.ZERO),
            new BankAccount("Pablo", "USD", BigDecimal.ZERO),
            new BankAccount("German", "USD", BigDecimal.ZERO)
    );

    var begin = Instant.now();
    IntStream.range(0, 1000000)
            .sequential()
            .mapToObj(i -> createBankTransaction())
            .map(t -> getAccountAndTransaction(bankAccounts, t))
            .forEach(AccountAndTransaction::processTransaction);
    var end = Instant.now();

    System.out.printf("Bank accounts balance: %s. Time: %s ms.\n", bankAccounts,  Duration.between(begin, end).toMillis());
  }

  private static AccountAndTransaction getAccountAndTransaction(List<BankAccount> bankAccounts, BankTransaction t) {
    return bankAccounts.stream().filter(ba -> ba.getUserId().equals(t.userId())).findAny().map(ba -> new AccountAndTransaction(t, ba)).orElseThrow();
  }

  private static BankTransaction createBankTransaction() {
    var ids = List.of("Matias", "Pablo", "German");
    return new BankTransaction(BankTransaction.TransactionType.CREDIT, ids.get(ThreadLocalRandom.current().nextInt(0, 3)), BigDecimal.TEN);
  }

  record AccountAndTransaction(
          BankTransaction transaction,
          BankAccount account
  ) {
    public void processTransaction() {
      account.processTransaction(transaction);
    }

    public void processTransactionSync() {
      account.processTransactionSync(transaction);
    }
  }
}
