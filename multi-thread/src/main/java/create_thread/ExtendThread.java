package create_thread;

/**
 * @description: 继承Thread类重写run方法来创建线程
 * 优点：在run方法内获取当前线程可以直接使用this，不需要使用Thread.currentThread()方法。
 * 缺点：如果继承了Thread类就不能再继承其他类；
 *      当多个线程执行一样的任务时需要多份任务代码，任务和代码没有分离；
 *      任务没有返回值。
 * @author: wczy9
 * @createTime: 2023-03-13  15:43
 */
public class ExtendThread extends Thread {
    @Override
    public void run() {
        System.out.println("i am a child thread: " + this.getName());
    }

    public static void main(String[] args) {
        System.out.println("i am a main thread: " + Thread.currentThread().getName());
        //创建线程
        ExtendThread extendThread = new ExtendThread();
        //启动线程
        extendThread.start();
    }
}