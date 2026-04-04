package rus.cheremisin;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        ConcurrentBank bank = new ConcurrentBank();

        // Создание счетов
        BankAccount account1 = bank.createAccount(1000);
        BankAccount account2 = bank.createAccount(500);

        // Перевод между счетами
        Thread transferThread1 = new Thread(() -> bank.transfer(account1, account2, 200));
        Thread transferThread2 = new Thread(() -> bank.transfer(account2, account1, 100));
        Thread transferThread3 = new Thread(() -> bank.transfer(account1, account2, 100));
        Thread transferThread4 = new Thread(() -> bank.transfer(account2, account1, 100));

        transferThread1.start();
        transferThread2.start();
        transferThread3.start();
        transferThread4.start();

        try {
            transferThread1.join();
            transferThread2.join();
            transferThread3.join();
            transferThread4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Total balance: " + bank.getTotalBalance());
    }
}

class ConcurrentBank {

    private Queue<BankAccount> accounts = new LinkedBlockingQueue<>();

    public BankAccount createAccount(int initialAmount) {
        BankAccount newAccount = new BankAccount(initialAmount);
        accounts.add(newAccount);
        return newAccount;
    }

    public void transfer(BankAccount account1, BankAccount account2, int amount) {
        BankAccount first = account1.hashCode() < account2.hashCode() ? account1 : account2;
        BankAccount second = first == account2 ? account1 : account2;

        synchronized (first) {
            synchronized (second) {
                if (amount > 0) {
                    System.out.println("Checking accounts info...");
                    checkAccountOnNull(account1);
                    System.out.println("Account sender is OK. Balance is " + account1.getBalance());
                    int account1Balance = account1.getBalance();
                    if (account1Balance > amount) {
                        account1.setBalance(amount * -1);
                        System.out.println("Decreased balance of sender. Account ID #" + account1.getId() + "(-" + amount + ").  Balance is " + account1.getBalance());
                    } else {
                        throw new RuntimeException("Insufficient money on account-sender!");
                    }
                } else {
                    throw new RuntimeException("Impossible to transfer 0 and less!");
                }

                checkAccountOnNull(account2);
                System.out.println("Account receiver is OK. Balance is " + account2.getBalance());
                account2.setBalance(amount);
                System.out.println("Increased balance of receiver. Account ID #" + account1.getId() + "(+" + amount + "). Balance is " + account2.getBalance());
            }
        }

    }

    public int getTotalBalance() {
        AtomicInteger totalBalance = new AtomicInteger(0);
        accounts.forEach(acc -> totalBalance.addAndGet(acc.getBalance()));
        return totalBalance.get();
    }

    private void checkAccountOnNull(BankAccount account) {
        if (account == null) {
            throw new NullPointerException("Impossible to transfer. Account is NULL. Check given account");
        }
    }
}

class BankAccount {
    private static long idCounter;
    private long id;
    private AtomicInteger balance;

//    private final ReentrantLock lock = new ReentrantLock();

    public BankAccount(int balance) {
        this.id = idCounter++;
        this.balance = new AtomicInteger(balance);
    }

    public void setBalance(int amount) {
        this.balance.addAndGet(amount);
    }

    public int getBalance() {
        return balance.get();
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        BankAccount that = (BankAccount) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hashCode(id);
    }
}
