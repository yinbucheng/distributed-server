package cn.bucheng.distributedserver.net;

import cn.bucheng.distributedserver.common.constant.TransferConstant;
import cn.bucheng.distributedserver.registry.DefaultServiceInstance;
import cn.bucheng.distributedserver.registry.ServiceInstance;
import cn.bucheng.distributedserver.registry.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 分布式事务服务端网络通信
 *
 * @ClassName NetServer
 * @Author buchengyin
 * @Date 2018/12/19 18:44
 **/
@Component
public class NetServer {

    private Logger logger = LoggerFactory.getLogger(NetServer.class);
    @Autowired
    private ServiceRegistry registry;

    @Value("${spring.application.name}")
    private String serviceId;
    @Value("${mst.server.host}")
    private String host;


    public void start(int port) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast("serverLengthDecode", new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
                    ch.pipeline().addLast("serverStringDecode", new StringDecoder());
                    ch.pipeline().addFirst("serverLengthEncode", new LengthFieldPrepender(4));
                    ch.pipeline().addFirst("serverStringEncode", new StringEncoder());
                    ch.pipeline().addLast("serverHandler", new NetServerHandler());
                }
            });

            ChannelFuture future = bootstrap.bind(port).sync();
            future.addListener((param) -> {
                if (param.isSuccess()) {
                    logger.info("start server success in port " + port);
                } else {
                    logger.error("start server fail in port " + port);
                }
            });
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error(e.toString());
        } finally {
            cancelRegistry(port);
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }


//    public void startRegistry(int serverPort) {
//        ServiceInstance instance = new DefaultServiceInstance(serviceId, host, serverPort, TransferConstant.EPHEMERAL);
//        boolean flag = registry.register(instance);
//        if (flag) {
//            logger.info("become leader to manager transaction");
//        }
//    }

    public void cancelRegistry(int serverPort){
        ServiceInstance instance = new DefaultServiceInstance(serviceId, host, serverPort, TransferConstant.EPHEMERAL);
        registry.deregister(instance);
    }
}
