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

    public void enqueue() throws InterruptedException {
        synchronized (this) {
            Random random = new Random();
            while (true) {
                if (size() == sizeLimit) {
                    System.out.println("queue is full!");
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                Thread.sleep(500);
                int randomInt = random.nextInt(100);
                queue.add(randomInt);
                System.out.println("added element " + randomInt);
                notify();
            }
        }
    }

    public void dequeue() throws InterruptedException {
        synchronized (this) {
            while (true) {
                if (size() == 0) {
                    System.out.println("queue is empty!");
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                Thread.sleep(500);
                int removedInt = queue.remove();
                System.out.println("removed element " + removedInt);
                notify();

            }
        }
    }

    public int size() {
        return queue.size();
    }

    public void doTheTask() throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            try {
                enqueue();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                dequeue();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread1.join();
    }


}