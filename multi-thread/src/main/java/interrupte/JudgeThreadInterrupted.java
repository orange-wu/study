package interrupte;

/**
 * @description: 根据中断标志判断线程是否终止
 * @author: wczy9
 * @createTime: 2023-06-02  11:30
 */
public class JudgeThreadInterrupted {

    public static void main(String[] args) throws InterruptedException {
        Thread threadA = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()){
                System.out.println(Thread.currentThread().getName() + " hello");
            }
            System.out.println(Thread.currentThread().getName() + " isInterrupted");
        }, "threadA");

        threadA.start();

        //主线程睡眠 让threadA充分输出
        Thread.sleep(10);

        //中断子线程
        threadA.interrupt();

        //等待子线程执行完毕
        threadA.join();

        System.out.println("main thread end");
    }

}