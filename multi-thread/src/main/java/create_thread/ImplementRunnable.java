package create_thread;

/**
 * @description: 实现Runnable接口重写run方法来创建线程
 * 优点：实现Runnable接口，还可以继承其他类；
 *      多个线程共享一个目标对象，适合多个线程来处理同一份资源的情况。
 * 缺点：任务没有返回值。
 * @author: wczy9
 * @createTime: 2023-03-13  16:03
 */
public class ImplementRunnable implements Runnable{
    @Override
    public void run() {
        System.out.println("i am a child thread: " + Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        System.out.println("i am a main thread: " + Thread.currentThread().getName());
        //创建Runnable实例
        Runnable implementRunnable = new ImplementRunnable();
        //同一个目标对象创建多个线程并启动
        new Thread(implementRunnable).start();
        new Thread(implementRunnable).start();
    }
}