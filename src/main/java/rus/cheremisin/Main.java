package rus.cheremisin;


import java.math.BigInteger;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Main {
    public static void main(String[] args) {
        int n = 10; // Вычисление факториала для числа 10

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        FactorialTask factorialTask = new FactorialTask(n);

        BigInteger result = forkJoinPool.invoke(factorialTask);

        System.out.println("Факториал " + n + "! = " + result);
    }
}

class FactorialTask extends RecursiveTask<BigInteger> {
    private int n;

    public FactorialTask(int n) {
        this.n = n;
    }

    @Override
    public BigInteger compute() {
        if (n == 0) {
            return BigInteger.ONE;
        } else if (n < 0) {
            throw new RuntimeException("Нельзя вычислить факториал отрицательного числа!");
        }
        FactorialTask innerTask = new FactorialTask(n - 1);
        innerTask.fork();

        BigInteger subResult = innerTask.join();
        return BigInteger.valueOf(n).multiply(subResult);
    }
}





