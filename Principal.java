/* ***************************************************************
* Autor............: Antonio Vinicius Silva Dutra
* Matricula........: 202110810
* Inicio...........: /09/2023
* Ultima alteracao.: 31/03/2024
* Nome.............: Principal.java
* Funcao...........: Classe principal da aplicacao
*************************************************************** */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import controller.*;

public class Principal extends Application {
  private static Scene telaEntrada; //cena inicial
  MainControl mainController = new MainControl();  

  @Override
  public void start(Stage stagePrimary) throws Exception {
    stagePrimary.getIcons().add(new Image("./img/icon.png"));//Definindo icone do programa

    stagePrimary.setTitle("Camada Fisica"); // Setando Nome na barra de pesquisa
    Parent fxmlStart = FXMLLoader.load(getClass().getResource("view/ScreenFXML.fxml")); // Carregamento do FXML Tela
    telaEntrada = new Scene(fxmlStart); // Definicao cena nova com o fxml carregados

    stagePrimary.setScene(telaEntrada); //setando a Cena no stage
    stagePrimary.setResizable(false); //Impossibilitando mudar tamanho da janela
    stagePrimary.show(); //mostrando o stage
  }

  public static void main(String[] args) {
    launch(args);
  }
}
