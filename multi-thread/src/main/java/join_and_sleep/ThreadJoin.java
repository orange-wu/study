package join_and_sleep;

/**
 * @description: join方法，等待线程全部加载完毕。
 * @author: wczy9
 * @createTime: 2023-06-01  16:12
 */
public class ThreadJoin {

    public static void main(String[] args) throws InterruptedException {
        Thread threadA = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("ThreadA over!");
        }, "threadA");

        Thread threadB = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("threadB over!");
        }, "threadB");

        //启动线程
        threadA.start();
        threadB.start();
        System.out.println("wait threadA and threadB over");
        //等待线程结束
        threadA.join();
        threadB.join();
        System.out.println("threadA and threadB all over");
    }

}