package interrupte;

/**
 * @description: interrupted()方法是获取当前线程的中断状态，并清除中断标志 isInterrupted()方法是获取调用线程的中断状态
 * @author: wczy9
 * @createTime: 2023-06-02  21:03
 */
public class InterruptedAndIsInterrupted {

    public static void main(String[] args) throws InterruptedException {
        Thread threadA = new Thread(() -> {
            for (; ; ) {

            }
        }, "threadA");

        threadA.start();
        threadA.interrupt();
        System.out.println(threadA.isInterrupted());//true
        System.out.println(threadA.interrupted());//false
        System.out.println(Thread.interrupted());//false
        System.out.println(threadA.isInterrupted());//true
        threadA.join();
        System.out.println("main thread is over");
    }

}