package io.bal;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import org.bal.app.proto.internal.PersonManagementMockery;

import java.io.IOException;

public class MockServer {

    public static void main(String... args) throws IOException, InterruptedException {
        int port = 5500;
        Server server = ServerBuilder.forPort(port).
                addService(new PersonManagementMockery.PersonManagementMock("localhost", 5501)).
                addService(ProtoReflectionService.newInstance()).build();
        server.start();

        System.out.println("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may has been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                server.shutdownNow();
                System.err.println("*** server shut down");
            }
        });
        server.awaitTermination();
    }
}
