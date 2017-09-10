package pl.andrzejpepiak;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class DialogUtils {

    public static String createNickDialog(String message){
        TextInputDialog dialog = new TextInputDialog("Twój nick");
        dialog.setTitle("Nick");

        if(message == null){
            dialog.setHeaderText("Ustawienie nick");
        }else {
            dialog.setHeaderText(message);
        }
        dialog.setContentText("Twój nick : ");

        Optional<String> result = dialog.showAndWait();
        if( result.isPresent()){
            return result.get();
        }
        return null;
    }

    public static void showDialog(String title, String header, String msg){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(msg);

        alert.show();
    }
}
