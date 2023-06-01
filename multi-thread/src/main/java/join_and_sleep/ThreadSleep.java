package join_and_sleep;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: 当一个执行中的线程调用了Thread的sleep方法后，调用线程会暂时让出指定时间的执行权，也就是这期间不参与CPU的调度，
 * 但是该线程所拥有的资源器资源，比如锁保持持有不让出。指定的睡眠时间到了后，该函数正常返回，线程就会处于就绪状态，然后参与CPU调度。
 * @author: wczy9
 * @createTime: 2023-06-01  16:21
 */
public class ThreadSleep {

    //创建一个独占锁
    private static final Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        Thread threadA = new Thread(() -> {
            //获取锁
            lock.lock();
            try {
                System.out.println("threadA is in sleep");
                Thread.sleep(10000);
                System.out.println("threadA is in awake");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                //释放锁
                lock.unlock();
            }
        }, "threadA");

        Thread threadB = new Thread(() -> {
            //获取锁
            lock.lock();
            try {
                System.out.println("threadB is in sleep");
                Thread.sleep(10000);
                System.out.println("threadB is in awake");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                //释放锁
                lock.unlock();
            }
        }, "threadB");

        //启动线程
        threadA.start();
        threadB.start();
    }

}