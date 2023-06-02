package interrupte;

/**
 * @description: 当线程调用sleep函数、wait系列函数或者join函数来阻塞挂起的时候，调用该线程的interrupt方法，
 * 强制sleep方法抛出InterruptedException异常而返回，线程恢复到激活状态
 * @author: wczy9
 * @createTime: 2023-06-02  16:09
 */
public class InterruptTheadException {

    public static void main(String[] args) throws InterruptedException {
        Thread threadA = new Thread(() -> {
            try {
                System.out.println("threadA begin sleep for 2000 seconds");
                Thread.sleep(20000);
                System.out.println("threadA awaking");
            } catch (InterruptedException e) {
                System.out.println("threadA is interrupted while sleeping");
                return;
            }
            System.out.println("thread leaving normally");
        }, "threadA");

        threadA.start();
        //确保子线程进入休眠状态
        Thread.sleep(1000);
        //打断子线程休眠，让子线程从sleep函数返回
        threadA.interrupt();
        //等待子线程执行完毕
        threadA.join();

        System.out.println("main thread is over");
    }

}