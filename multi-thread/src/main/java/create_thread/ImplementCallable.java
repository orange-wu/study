package create_thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @description: 实现Callable接口重写call方法来创建线程
 * 优点：实现Runnable接口，还可以继承其他类；
 *      多个线程共享一个目标对象，适合多个线程来处理同一份资源的情况；
 *      有返回值，并且可以抛出异常。
 * @author: wczy9
 * @createTime: 2023-03-13  16:54
 */
public class ImplementCallable implements Callable<String> {

    private int i;

    public ImplementCallable(int i) {
        this.i = i;
    }

    public ImplementCallable() {
    }

    @Override
    public String call() throws Exception {
        i++;
        System.out.println(i);
        if (i % 2 == 0) {
            throw new Exception("Call Exception");
        }
        return "i am a child thread: " + Thread.currentThread().getName();
    }

    public static void main(String[] args) {
        System.out.println("i am a main thread: " + Thread.currentThread().getName());
        //创建异步任务
        FutureTask<String> futureTask = new FutureTask<>(new ImplementCallable(0));
        FutureTask<String> futureTask1 = new FutureTask<>(new ImplementCallable(1));
        //启动线程
        new Thread(futureTask).start();
        new Thread(futureTask1).start();
        try {
            //等待任务执行完毕，并返回结果
            String result = futureTask.get();
            System.out.println(result);
            String exception = futureTask1.get();
            System.out.println(exception);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}