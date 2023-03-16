package wait_and_notify;

/**
 * @description: 当前线程调用共享变量的wait()方法只会释放当前共享变量上的锁，如果当前线程还持有其他共享变量的锁，则这些锁不会释放。
 * @author: wczy9
 * @createTime: 2023-03-15  09:41
 */
public class WaitReleaseOneLock {

    //资源A
    private static final Object resourceA = new Object();
    //资源B
    private static final Object resourceB = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread threadA = new Thread(() -> {
            //获取resourceA共享资源的监视锁
            synchronized (resourceA) {
                System.out.println("threadA get resourceA lock");
                //获取resourceB共享资源的监视锁
                synchronized (resourceB) {
                    System.out.println("threadA get resourceB lock");
                    //threadA阻塞并释放已经获取到的threadA
                    System.out.println("threadA release resourceA lock");
                    try {
                        resourceA.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "threadA");

        Thread threadB = new Thread(() -> {
            try {
                //休眠1s
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //获取resourceA共享资源的监视锁
            synchronized (resourceA) {
                System.out.println("threadB get resourceA lock");
                System.out.println("threadB try get resourceB lock...");
                //获取resourceB共享资源的监视锁
                synchronized (resourceB) {
                    System.out.println("threadB get resourceB lock");
                    //threadB阻塞并释放已经获取到的threadA
                    System.out.println("threadB release resourceA lock");
                    try {
                        resourceA.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "threadB");
        //启动线程
        threadA.start();
        threadB.start();
        //等待线程结束
        threadA.join();
        threadB.join();

        System.out.println("main thread end");
    }

}