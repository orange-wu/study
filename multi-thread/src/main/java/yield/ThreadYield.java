package yield;

/**
 * @description: 当一个线程调用yield方法时，当前线程会让出CPU使用权，然后处于就绪状态，线程调度器会从线程就绪队列里面获取一个线程优先级最高的线程，
 * 当然也有可能会调度到刚刚让出CPU的那个线程来获取CPU执行权。
 * @author: wczy9
 * @createTime: 2023-06-01  17:52
 */
public class ThreadYield implements Runnable {

    public ThreadYield(String name) {
        Thread thread = new Thread(this, name);
        thread.start();
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + " normal run" + i);
            if (i % 5 == 0) {
                System.out.println(Thread.currentThread().getName() + " yield cpu");
                //当i=0时让出CPU执行权，放弃时间片，进行下一轮调度
                Thread.yield();
            }
        }
        System.out.println(Thread.currentThread().getName() + " is over");
    }

    public static void main(String[] args) {
        new ThreadYield("threadA");
        new ThreadYield("threadB");
        new ThreadYield("threadC");
    }
}