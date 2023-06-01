package wait_and_notify;

/**
 * @description:
 * 一个线程调用共享对象的 notify()方法后，会唤醒一个在该共享变量上调用wait系列方法后被挂起的线程。一个共享变量上可能会有多个线程在等待，具体唤醒哪个等待的线程是随机的。
 * 一个线程调用共享对象的 notifyAll()方法后，会唤醒所有在该共享变量上调用wait系列方法后被挂起的线程。
 * @author: wczy9
 * @createTime: 2023-06-01  10:54
 */
public class NotifyAwakeThread {

    private static final Object resourceA = new Object();

    public static void main(String[] args) throws InterruptedException {

        Thread threadA = new Thread(() -> {
            //获取resourceA共享资源的监视锁
            synchronized (resourceA) {
                System.out.println("threadA get resourceA lock");
                try {
                    System.out.println("threadA begin wait");
                    resourceA.wait();
                    System.out.println("threadA end wait");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "threadA");

        Thread threadB = new Thread(() -> {
            //获取resourceA共享资源的监视锁
            synchronized (resourceA) {
                System.out.println("threadB get resourceA lock");
                try {
                    System.out.println("threadB begin wait");
                    resourceA.wait();
                    System.out.println("threadB end wait");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "threadB");

        Thread threadC = new Thread(() -> {
            synchronized (resourceA) {
                System.out.println("threadC begin notify");
//                resourceA.notify();
                resourceA.notifyAll();
            }
        }, "threadC");

        //启动线程
        threadA.start();
        threadB.start();
        //让主线程睡眠1s，确保线程C能在线程A B调用wait后再执行notify
        Thread.sleep(1000);
        threadC.start();

        //等待线程结束
        threadA.join();
        threadB.join();
        threadC.join();

        System.out.println("main thread end");
    }
    //notify输出结果
    //threadA get resourceA lock
    //threadA begin wait
    //threadB get resourceA lock
    //threadB begin wait
    //threadC begin notify
    //threadA end wait
    //从结果可以看到，线程C在共享资源调用resourceA的notify方法，这会激活resourceA的阻塞集合里面随机的一个线程，这里激活了线程A，所以线程A的
    //wait方法返回了，线程B则还处于阻塞状态

    //notifyAll
    //threadA get resourceA lock
    //threadA begin wait
    //threadB get resourceA lock
    //threadB begin wait
    //threadC begin notify
    //threadB end wait
    //threadA end wait
    //main thread end
    //线程C调用notifyAll方法会唤醒resourceA的阻塞集合里面所有的线程，这里线程A和线程B都会被唤醒，只是线程B获得到resourceA上的锁
}