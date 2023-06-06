package thread_local;

/**
 * @description: InheritableThreadLocal类继承自ThreadLocal，它提供了一个特性，让子线程可以访问在父线程中设置的本地变量。
 * @author: wczy9
 * @createTime: 2023-06-06  21:41
 */
public class InheritableThreadLocalExtend {

    public static InheritableThreadLocal<String> threadLocal = new InheritableThreadLocal<>();

    public static void main(String[] args) {

        //主线程设置线程变量
        threadLocal.set("hello world");
        //子线程获取不到
        Thread threadA = new Thread(() -> System.out.println("threadA : " + threadLocal.get()), "threadA");
        threadA.start();
        System.out.println("main : " + threadLocal.get());
        /*
        print:
            threadA : hello world
            main : hello world
         */
    }

}