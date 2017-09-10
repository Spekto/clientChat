package pl.andrzejpepiak.controllers;


        import com.google.gson.reflect.TypeToken;
        import javafx.application.Platform;
        import javafx.fxml.FXML;
        import javafx.fxml.Initializable;
        import javafx.scene.control.TextArea;
        import javafx.scene.control.TextField;
        import javafx.scene.input.KeyCode;
        import pl.andrzejpepiak.DialogUtils;
        import pl.andrzejpepiak.models.ChatSocket;
        import pl.andrzejpepiak.models.IMessageObserver;
        import pl.andrzejpepiak.models.MessageFactory;

        import java.lang.reflect.Type;
        import java.net.URL;
        import java.util.ResourceBundle;

public class Controller implements Initializable, IMessageObserver {


    private ChatSocket socket;

    @FXML
    TextArea area;

    @FXML
    TextField field;



    public Controller(){
        socket = ChatSocket.getSocket();
    }

    public void initialize(URL location, ResourceBundle resources) {
        socket.connect();
        sendNickPacket(DialogUtils.createNickDialog(null));
        field.requestFocus();
        area.setWrapText(true);

        socket.setObserver(this);


        field.setOnKeyPressed(s -> {
            if(s.getCode() == KeyCode.ENTER){
                sendMessagePacket(field.getText());
                field.clear();
            }
        });
    }

    @Override
    public void handleMessage(String s) {
        Type token = new TypeToken<MessageFactory>() {}.getType();
        MessageFactory factory = MessageFactory.GSON.fromJson(s, token);

        switch (factory.getMessageType()){
            case SEND_MESSAGE:;{
                area.appendText("\n"+factory.getMessage());
                break;
            }
            case NICK_NOT_FREE:{
                Platform.runLater(() -> sendNickPacket(DialogUtils.createNickDialog(factory.getMessage())));
                break;
            }
            case USER_JOIN:{
                //ten case sie przyda jak bedziemy miec liste obok
                area.appendText("-->"+ factory.getMessage() + "<--");
                break;
            }
            case USER_LEFT:{
                //ten case sie przyda jak bedziemy miec liste obok
                area.appendText("<--"+ factory.getMessage() + "-->");
                break;
            }
        }
    }

    private void sendNickPacket (String nick){
        MessageFactory factory = new MessageFactory();
        factory.setMessageType(MessageFactory.MessageType.SET_NICK);
        factory.setMessage(nick);
        sendMessage(factory);
    }
    private void sendMessagePacket (String message){
        MessageFactory factory = new MessageFactory();
        factory.setMessageType(MessageFactory.MessageType.SEND_MESSAGE);
        factory.setMessage(message);
        sendMessage(factory);
    }

    private void sendMessage(MessageFactory factory){
        socket.sendMessage(MessageFactory.GSON.toJson(factory));
    }
}
