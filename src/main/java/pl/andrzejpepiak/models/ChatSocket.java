package pl.andrzejpepiak.models;

import com.sun.org.apache.xpath.internal.SourceTree;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@ClientEndpoint
public class ChatSocket {
    private static ChatSocket socket = new ChatSocket();

    public static ChatSocket getSocket(){
        return socket;
    }

    private WebSocketContainer webSocketContainer;
    private Session session;
    private ChatSocket(){
        webSocketContainer = ContainerProvider.getWebSocketContainer();
    }

    private IMessageObserver obsever;


    public IMessageObserver getObsever() {
        return obsever;
    }

    public void setObserver(IMessageObserver obsever) {
        this.obsever = obsever;
    }

    @OnOpen
    public void open(Session session){
        this.session = session;
        System.out.println("Nawiązano połączenie z serwerem");
    }

    @OnMessage
    public void message(Session session, String message){
        obsever.handleMessage(message);
    }

    public void sendMessage(String message){
        try {
            session.getBasicRemote().sendText(message);// get basic remote strumien wysylajacy wiad miedzy dwoma punktami send text - do niej wysylamy wiaodmosc
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect(){
        try {
            webSocketContainer.connectToServer(this,new URI("ws://localhost:8080/chat"));
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
