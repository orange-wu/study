package thread_local;

/**
 * @description: ThreadLocal不支持继承性
 * @author: wczy9
 * @createTime: 2023-06-06  21:14
 */
public class ThreadLocalNotExtend {

    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {

        //主线程设置线程变量
        threadLocal.set("hello world");
        //子线程获取不到
        Thread threadA = new Thread(() -> System.out.println("threadA : " + threadLocal.get()), "threadA");
        threadA.start();
        System.out.println("main : " + threadLocal.get());
        /*
        print:
            threadA : null
            main : hello world
         */
    }
}