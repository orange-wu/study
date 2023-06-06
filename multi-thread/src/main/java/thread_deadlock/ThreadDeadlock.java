package thread_deadlock;

/**
 * @description: 线程死锁演示
 * @author: wczy9
 * @createTime: 2023-06-05  21:40
 */
public class ThreadDeadlock {

    private static final Object resourceA = new Object();
    private static final Object resourceB = new Object();

    public static void main(String[] args) {
        Thread threadA = new Thread(() -> {
            synchronized (resourceA) {
                System.out.println("threadA get resourceA");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("threadA wait resourceB");
                synchronized (resourceB) {
                    System.out.println("threadA get resourceB");
                }
            }
        }, "threadA");

        Thread threadB = new Thread(() -> {
            synchronized (resourceB) {
                System.out.println("threadB get resourceB");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("threadB wait resourceA");
                synchronized (resourceA) {
                    System.out.println("threadB get resourceA");
                }
            }
        }, "threadB");

        threadA.start();
        threadB.start();
        /*
          print:
          threadA get resourceA
          threadB get resourceB
          threadA wait resourceB
          threadB wait resourceA
         */
    }
}