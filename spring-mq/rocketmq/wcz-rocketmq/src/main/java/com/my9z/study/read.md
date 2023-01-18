* app    ---使用事例
* common ---公共包
* core   ---封装核心

`common和core包可以单独打包，作为纯粹的RocketMQ封装，并且后续学习下spring boot starter的编写可以上传到私服上。`

1. producer:
    * 非事务消息生产者，直接调用WczMQProducer中的api发送相关消息；
    * 事务消费生产者:
        1. 继承AbstractMQTransactionListener事务消息监听抽象类，重写执行本地事务和回查本地事务的方法；
        2. 添加MQTransactionListener注解，声明事务消息生产者的消费者组。

2. consumer:
    1. 实现ConsumerListener消费者接口，重写消费模式和消费逻辑的方法；
    2. 添加MQConsumer注解，声明唯一key，生产者组，topic和tag等信息。