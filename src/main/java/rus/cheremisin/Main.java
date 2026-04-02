package rus.cheremisin;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ComplexTaskExecutor taskExecutor = new ComplexTaskExecutor(5);

        Runnable testRunnable = () -> {
            System.out.println(Thread.currentThread().getName() + " started the test.");

            // Выполнение задач
            taskExecutor.executeTasks();

            System.out.println(Thread.currentThread().getName() + " completed the test.");
        };

        Thread thread1 = new Thread(testRunnable, "TestThread-1");
        Thread thread2 = new Thread(testRunnable, "TestThread-2");

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }
}

class ComplexTask implements Runnable {
    private CyclicBarrier cyclicBarrier;

    public ComplexTask(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        try {
            execute();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void execute() throws BrokenBarrierException, InterruptedException {
        System.out.println(Thread.currentThread().getName() + " is doing its part of GREATER TASK");
        Thread.sleep(2000);
        System.out.println(Thread.currentThread().getName() + " is waiting at the barrier");
        cyclicBarrier.await();
    }
}

class ComplexTaskExecutor {
    private ExecutorService executorService;
    private CyclicBarrier cyclicBarrier;
    private int numberOfTasks;

    public ComplexTaskExecutor(int numberOfTasks) {
        this.numberOfTasks = numberOfTasks;
        cyclicBarrier = new CyclicBarrier(numberOfTasks, () -> {
            System.out.println("All tasks passed barrier");
            executorService.shutdown();
        });
        executorService = Executors.newFixedThreadPool(numberOfTasks);
    }

    public void executeTasks() {
        for (int i = 0; i < numberOfTasks; i++) {
            executorService.submit(new ComplexTask(cyclicBarrier));
        }
    }

}