package ifmo.server.main;

import com.xebialabs.restito.server.StubServer;
import ifmo.database.DatabaseServer;
import ifmo.network.Messages.InitMessages.MessageAskServerLoad;
import ifmo.network.Messages.YarikMessage;
import ifmo.network.Network;
import ifmo.network.StandardQuad;
import org.junit.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.ArrayList;
import java.util.function.Consumer;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.stringContent;

/**
 * Created by nikita on 15.12.16.
 */
@Features(value = "Main server testing")
public class MainServerTest {

    private DatabaseServer databaseServer;

    @Test
    public void testHandleReceive() throws Exception {
        int port = 32453;
        withStubServer(port, s -> {
            StandardQuad standardQuad = new StandardQuad("DB","localhost",s.getPort(), Network.DATABASE.name());
            databaseServer = new DatabaseServer(standardQuad);
            databaseServer.start();
            whenHttp(s)
                    .match()
                    .then(stringContent("test"));
            YarikMessage message = new MessageAskServerLoad();
            ArrayList<String> content = new ArrayList<>();
            content.add(standardQuad.toString());
            content.add("Hello");
            message.setFieldsContent(content);
            databaseServer.handleReceive(message.encode());
        });

    }

    @Step(value = "Running with stub server")
    private void withStubServer(int port, Consumer<StubServer> callback) {
        StubServer stubServer = null;
        try {
            stubServer = new StubServer(port).run();
            callback.accept(stubServer);
        } finally {
            if (stubServer != null) {
                stubServer.stop();
            }
        }
    }
}