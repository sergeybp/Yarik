package InitServer;

import AbstractServer.AbstractServer;
import InitServer.Utils.FastScanner;
import Network.Messages.InitMessages.MessageAskForServer;
import Network.Messages.InitMessages.MessageAskServerLoad;
import Network.Messages.MessageUnknown;
import Network.Messages.YarikMessage;
import Network.StandardQuad;
import org.json.simple.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sergeybp on 27.10.16.
 */
public class InitServer extends AbstractServer {

    private ArrayList<StandardQuad> users = new ArrayList<>();
    private ArrayList<StandardQuad> servers = new ArrayList<>();

    public InitServer(StandardQuad quad) {
        super(quad);
        getConfigs();
    }

    @Override
    public void handleReceive(JSONObject object) {
        YarikMessage gotMessage = new MessageUnknown();
        try {
            gotMessage = gotMessage.decode(object);
        } catch (Exception e) {
            throw new InitServerException("Json is not full");
        }

        switch (gotMessage.getMessageType()) {
            case ASKFORSERVER: askForServer(gotMessage);
            case ASKESERVERLOAD: askServerLoad(gotMessage);
        }
    }

    private void askForServer(YarikMessage gotMessage) {
        StandardQuad user = new StandardQuad(gotMessage.getMessageContent().get(0).getValue());
        users.add(user);

        YarikMessage message = new MessageAskServerLoad();

        ArrayList<String> content = new ArrayList<>();
        content.add(myQuad.toString());
        content.add("PLEASE GIVE");

        try {
            message.setFieldsContent(content);
        } catch (Exception e) {
            throw new InitServerException("Content is not full");
        }

        final JSONObject encodedMessage = message.encode();
        servers.forEach(server -> sendJSON(server.ip, server.port, encodedMessage));
    }

    private void askServerLoad(YarikMessage gotMessage) {
        StandardQuad serverToRet = new StandardQuad(gotMessage.getMessageContent().get(0).getValue());
        String available = gotMessage.getMessageContent().get(1).getValue();

        if (available.equals("AVAILABLE")) {
            YarikMessage message = new MessageAskForServer();

            ArrayList<String> content = new ArrayList<>();
            content.add(serverToRet.toString());
            content.add("Have a nice day");

            try {
                message.setFieldsContent(content);
            } catch (Exception e) {
                throw new InitServerException("Content is not full");
            }

            final JSONObject encodedMessage = message.encode();

            if (!users.isEmpty()) {
                StandardQuad userNow = users.get(0);
                users.remove(0);
                sendJSON(userNow.ip, userNow.port, encodedMessage);
            }
        }
    }

    public void getConfigs() {
        InputStream is;
        try {
            URL url = getClass().getClassLoader().getResource("init_config");
            assert url != null;

            is = new FileInputStream(url.getFile());
        } catch (FileNotFoundException e) {
            throw new InitServerException("Init config file is not found");
        }
        FastScanner in = new FastScanner(is);
        while (in.hasNext()) {
            String s = in.nextString();
            servers.add(new StandardQuad(s));
        }

    }
}
