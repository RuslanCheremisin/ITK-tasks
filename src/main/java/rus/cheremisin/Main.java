package rus.cheremisin;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue blockingQueue = new BlockingQueue(10);
        blockingQueue.doTheTask();

    }
}

class BlockingQueue {
    private final int sizeLimit;
    private final Queue<Integer> queue = new LinkedList<>();

    public BlockingQueue(int sizeLimit) {
        this.sizeLimit = sizeLimit;
    }

    public void enqueue() {
        Random random = new Random();
        while (true) {
            synchronized (this) {
                while (size() == sizeLimit) {
                    System.out.println(Thread.currentThread().getName() + " queue is full! waiting... Size = " + size());
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                int randomInt = random.nextInt(100);
                queue.add(randomInt);
                System.out.println(Thread.currentThread().getName() + " added element " + randomInt + ". Size = " + size());
                notifyAll();
            }
        }
    }

    public void dequeue() {
        while (true) {
            synchronized (this) {
                while (size() == 0) {
                    System.out.println(Thread.currentThread().getName() + " queue is empty! waiting...! Size = " + size());
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                int removedInt = queue.remove();
                System.out.println(Thread.currentThread().getName() + " removed element " + removedInt + ". Size = " + size());
                notifyAll();

            }
        }
    }

    public synchronized int size() {
        return queue.size();
    }

    public void doTheTask() throws InterruptedException {
        Thread thread1 = new Thread(() -> enqueue());
        Thread thread2 = new Thread(() -> dequeue());
        Thread thread3 = new Thread(() -> dequeue());
        Thread thread4 = new Thread(() -> enqueue());

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
    }


}