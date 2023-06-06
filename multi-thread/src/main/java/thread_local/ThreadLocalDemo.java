package thread_local;

/**
 * @description: ThreadLocal演示
 * @author: wczy9
 * @createTime: 2023-06-06  20:32
 */
public class ThreadLocalDemo {

    static ThreadLocal<String> localVariable = new ThreadLocal<>();

    static void print(String str) {
        //打印当前线程本地内存中的localVariable变量的值
        System.out.println(str + " : " + localVariable.get());
        //清除当前线程本地内存中的localVariable变量的值
        localVariable.remove();
    }

    public static void main(String[] args) {

        Thread threadA = new Thread(() -> {
            localVariable.set("threadA local variable");
            print("threadA");
            System.out.println("threadA remove after : " + localVariable.get());
        }, "threadA");

        Thread threadB = new Thread(() -> {
            localVariable.set("threadB local variable");
            print("threadB");
            System.out.println("threadB remove after : " + localVariable.get());
        }, "threadB");

        threadA.start();
        threadB.start();

        /*
        print:
            threadB : threadB local variable
            threadB remove after : null
            threadA : threadA local variable
            threadA remove after : null
         */
    }

}