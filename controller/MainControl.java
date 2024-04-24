package controller;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

import model.*;

public class MainControl implements Initializable {
    @FXML private Button iniciar;
    @FXML private Button voltar;
    @FXML private Button sendButton;
    @FXML private ChoiceBox<String> optionsBox;

    @FXML private TextArea outputText;
    @FXML private TextArea inputText;

    @FXML private ImageView startScreenImage;
    @FXML private ImageView executionScreenImage;

    @FXML private ImageView highSignalEight;
    @FXML private ImageView highSignalSeven;
    @FXML private ImageView highSignalSix;
    @FXML private ImageView highSignalFive;
    @FXML private ImageView highSignalFour;
    @FXML private ImageView highSignalThree;
    @FXML private ImageView highSignalTwo;
    @FXML private ImageView highSignalOne;

    @FXML private ImageView lowSignalEight;
    @FXML private ImageView lowSignalSeven;
    @FXML private ImageView lowSignalSix;
    @FXML private ImageView lowSignalFive;
    @FXML private ImageView lowSignalFour;
    @FXML private ImageView lowSignalThree;
    @FXML private ImageView lowSignalTwo;
    @FXML private ImageView lowSignalOne;

    @FXML private ImageView transitionEight;
    @FXML private ImageView transitionSeven;
    @FXML private ImageView transitionSix;
    @FXML private ImageView transitionFive;
    @FXML private ImageView transitionFour;
    @FXML private ImageView transitionThree;
    @FXML private ImageView transitionTwo;
    @FXML private ImageView transitionOne;

    //Instanciando a string que vai receber a opcao selecionada no choiceBox
    private String selectedMethod = "";
    String comparationSignal = "1";
    int comparationBinary = 0;

    //array de imgs que vao guardar as formas de onda
    private ImageView lowImgs[];
    private ImageView highImgs[];
    private ImageView transitionImgs[];

    private int option;
    private int lastBit = 0; 
    int NumCaracteres = 0;

    @FXML
    void optionsList(ActionEvent event) {
      selectedMethod = optionsBox.getSelectionModel().getSelectedItem();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
      optionsBox.getItems().addAll("Cod. Binaria", "Cod. Manchester", "Cod. Manchester Diferencial");

      //adiciona listeners para as ChoiceBox
      optionsBox.setOnAction(event -> optionsList(event));

      executionScreenImage.setVisible(false);
      inputText.setVisible(false);
      inputText.setDisable(true);
      outputText.setVisible(false);
      outputText.setDisable(true);
      sendButton.setVisible(false);
      sendButton.setDisable(true);
      voltar.setVisible(false);
      voltar.setDisable(true);

      ImageView low[] = { lowSignalOne, lowSignalTwo, lowSignalThree, lowSignalFour, lowSignalFive, lowSignalSix, lowSignalSeven, 
        lowSignalEight};
      lowImgs = low;

      ImageView high[] = { highSignalOne, highSignalTwo, highSignalThree, highSignalFour, highSignalFive, highSignalSix, 
        highSignalSeven, highSignalEight};
      highImgs = high;

      ImageView transition[] = { transitionOne, transitionTwo, transitionThree, transitionFour,
        transitionFive, transitionSix, transitionSeven, transitionEight};
      transitionImgs = transition;

      //desativando as imageView 
      for (int i = 0; i < 8; i++) {
        lowImgs[i].setVisible(false);
        highImgs[i].setVisible(false);
        transitionImgs[i].setVisible(false);
      }
    }

    /********************************************************************
  * Metodo: continueButton()
  * Funcao: botao de avanco depois que o usuario escolher o tipo de codificacao.
  ativa e desativa imagens e a choiceBox apos escolha do usuario
  * Parametros: ActionEvent = clique do mouse no botao
  * Retorno: void
  ****************************************************************** */
  @FXML
  void startButton(ActionEvent event) {
    if (!selectedMethod.isEmpty()) { //verifica se o usuario escolheu alguma opcao
      optionsBox.setVisible(false);
      optionsBox.setDisable(true);
      iniciar.setVisible(false);
      iniciar.setDisable(true);
      
      startScreenImage.setVisible(false);

      executionScreenImage.setVisible(true);
      inputText.setVisible(true);
      inputText.setDisable(false);
      inputText.setEditable(true);
      outputText.setVisible(true);
      outputText.setDisable(false);
      sendButton.setVisible(true);
      sendButton.setDisable(false);
      voltar.setVisible(true);
      voltar.setDisable(false);

      //getCodificacao();

    } else {
      //Mostra uma mensagem de erro caso o usuario nao tenha selecionado um metodo de codificacao
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("ERRO!");
      alert.setHeaderText("Opção inválida!");
      alert.setContentText("Por favor, selecione um método de codificação.");
      alert.showAndWait();
    }
  }//fim do metodo continueButton()

  @FXML
  void sendMessage(ActionEvent event) {
    if (!inputText.getText().isEmpty()) { //verifica se o usuario escolheu alguma opcao
      sendButton.setDisable(true);
      sendButton.setVisible(false);

      inputText.setEditable(false);

      String mensagemDigitada = getMsg();

      AplicacaoTransmissora transmissor = new AplicacaoTransmissora();
      transmissor.aplicacaoTransmissora(this, mensagemDigitada);
    } else {
      //Mostra uma mensagem de erro caso o usuario nao tenha selecionado um metodo de codificacao
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("ERRO!");
      alert.setHeaderText("Campo de mensagem vazio!");
      alert.setContentText("Por favor, digite uma mensagem.");
      alert.showAndWait();
    }
  }

  @FXML
  void backButton(ActionEvent event) {
    executionScreenImage.setVisible(false);
    voltar.setVisible(false);
    voltar.setDisable(true);

    startScreenImage.setVisible(true);
    optionsBox.setVisible(true);
    optionsBox.setDisable(false);
    iniciar.setVisible(true);
    iniciar.setDisable(false);

    inputText.setText("");
    outputText.setText("");

    //desativando as imageView 
    for (int i = 0; i < 8; i++) {
      lowImgs[i].setVisible(false);
      highImgs[i].setVisible(false);
      transitionImgs[i].setVisible(false);
    }
  }

  public String getMsg(){
    String mensagemDigitada = inputText.getText();
    return mensagemDigitada;
  }

  public void setText(String msgFinal) {
		outputText.setText(msgFinal);
	}

  /********************************************************************
  * Metodo: getCodificacao()
  * Funcao: atribui valores inteiros para as opcoes de codificao escolhida pelo usuario 
  * Parametros: nenhum
  * Retorno: int option = opcao escolhida
  ****************************************************************** */
  public int getCodificacao(){
    if(selectedMethod == "Cod. Binaria"){
      option = 0;
    } else if(selectedMethod == "Cod. Manchester") {
      option = 1;
    } else if (selectedMethod == "Cod. Manchester Diferencial"){
      option = 2;
    } else {
      System.out.println("Deu ruim!");
    }
    return option;
  }//fim do metodo getPosition()
  
  public void setNumCaracteres(int numCaracteres) {
    NumCaracteres = numCaracteres;
  }

  public int getNumCaracteres() {
    return NumCaracteres;
  }

  public void setOutputText(String mensagem){
    outputText.setText(mensagem);
  }

  /* ***************************************************************
  * Metodo: moveWave()
  * Funcao: move os elementos visuais (onda) de um indice para o outro
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  public void moveWave() {
    Platform.runLater(() -> {
      //ativa as imagens da onda da esquerda para a direita
        for (int i = 7; i >= 1; i--) {
          lowImgs[i].setVisible(lowImgs[i-1].isVisible());
          highImgs[i].setVisible(highImgs[i-1].isVisible());      
          transitionImgs[i].setVisible(transitionImgs[i-1].isVisible());
        }
    });
  }//fim do metodo moveWave()

  /* ***************************************************************
  * Metodo: activateWave()
  * Funcao: ativa as imagens na primeira posicao de acordo com o bit recebido.
  * Parametros: int bit = bit extraido pela mascara no metodo MeioDeComunicacao()
  * Retorno: void
  *************************************************************** */
  public void activateWave(int bit) {
    Platform.runLater(() -> {
      lowImgs[0].setVisible(false);
      transitionImgs[0].setVisible(false);
      highImgs[0].setVisible(false);

      // Se não houver mudança de bit, ativa a imagem correspondente ao bit atual
      if (bit == 0) {
        lowImgs[0].setVisible(true);
      } else {
        highImgs[0].setVisible(true);
      }
      // Verifica se o bit é diferente do último bit recebido
      if (bit != lastBit) {
        transitionImgs[0].setVisible(true);
      } 
      // Atualiza o último bit recebido
      lastBit = bit;
    });
  }

  //define o tamanho do array, com base que cada array Binario cabe 4 caracteres e Manchester 2 carac
	public int setTamanhoArray(int tipoDeCodificacao) { 
		switch (tipoDeCodificacao) {
			case 0:
				if (NumCaracteres % 4 == 0)
					return (NumCaracteres / 4);
				else
					return ((NumCaracteres / 4) + 1);

			default:
				if (NumCaracteres % 2 == 0)
					return (NumCaracteres / 2);
				else
					return ((NumCaracteres / 2) + 1);
		}
	}
}