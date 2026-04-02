package rus.cheremisin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        ConcurrentBank bank = new ConcurrentBank();

        // Создание счетов
        BankAccount account1 = bank.createAccount(1000);
        BankAccount account2 = bank.createAccount(500);

        // Перевод между счетами
        Thread transferThread1 = new Thread(() -> bank.transfer(account1, account2, 200));
        Thread transferThread2 = new Thread(() -> bank.transfer(account2, account1, 100));
        Thread transferThread3 = new Thread(() -> bank.transfer(null, account1, 100));
        Thread transferThread4 = new Thread(() -> bank.transfer(account2, null, 100));
        Thread transferThread5 = new Thread(() -> bank.transfer(account2, null, 0));

        transferThread1.start();
        transferThread2.start();
        transferThread3.start();
        transferThread4.start();
        transferThread5.start();

        try {
            transferThread1.join();
            transferThread2.join();
            transferThread3.join();
            transferThread4.join();
            transferThread5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Вывод общего баланса
        System.out.println("Total balance: " + bank.getTotalBalance());
    }
}

class ConcurrentBank {

    private List<BankAccount> accountList = new ArrayList<>();

    public BankAccount createAccount(int initialAmount) {
        BankAccount newAccount = new BankAccount(initialAmount);
        accountList.add(newAccount);
        return newAccount;
    }

    public void transfer(BankAccount account1, BankAccount account2, int amount) {
        if (amount > 0) {
            System.out.println("Checking accounts info...");
            checkAccountOnNull(account1);
            System.out.println("Account sender is OK.");
            checkAccountOnNull(account2);
            System.out.println("Account receiver is OK.");
            int account1Balance = account1.getBalance();
            if (account1Balance > amount) {
                account1.setBalance(amount * -1);
                System.out.println("Decreased balance of sender");
            } else {
                throw new RuntimeException("Insufficient money on account-sender!");
            }
            account2.setBalance(amount);
            System.out.println("Increased balance of sender");
        } else {
            throw new RuntimeException("Impossible to transfer 0 and less!");
        }



    }

    public int getTotalBalance() {
        AtomicInteger totalBalance = new AtomicInteger(0);
        accountList.forEach(acc -> totalBalance.addAndGet(acc.getBalance()));
        return totalBalance.get();
    }

    private void checkAccountOnNull(BankAccount account) {
        if (account == null) {
            throw new NullPointerException("Impossible to transfer. Account is NULL. Check given account");
        }
    }
}

class BankAccount {
    private AtomicInteger balance;

    public BankAccount(int balance) {
        this.balance = new AtomicInteger(balance);
    }

    public synchronized void setBalance(int amount) {
        this.balance.addAndGet(amount);
    }

    public int getBalance() {
        return balance.get();
    }
}
