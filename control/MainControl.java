package control;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import model.*;

public class MainControl implements Initializable{

  @FXML private Button iniciar;
  @FXML private Button continueButton;
  @FXML private Button sendButton;

  @FXML private ChoiceBox<String> optionsBox;

  @FXML private TextArea textArea;
  @FXML private TextArea asciiText;
  @FXML private TextArea bitsText;
  @FXML private TextArea output;

  @FXML private ImageView textLogo;
  @FXML private ImageView transmissor;
  @FXML private ImageView receptor;
  @FXML private ImageView dialogoDireito;
  @FXML private ImageView dialogoEsquerdo;
  @FXML private ImageView signalRadio;

  //Instanciando a string que vai receber a opcao selecionada no choiceBox
  private String selectedMethod = "";

  //Instanciando o inteiro que vai ser utilizado no switch - case
  private int option;

  Binaria binarioControl = new Binaria();
  Manchester manchesterControl = new Manchester();
  ManchesterDiferencial diferencialControl = new ManchesterDiferencial();

  @FXML
  void startButton(ActionEvent event) {
    iniciar.setDisable(true);
    iniciar.setVisible(false);

    optionsBox.setVisible(true);
    optionsBox.setDisable(false);

    continueButton.setVisible(true);
    continueButton.setDisable(false);
    //textLogo.setVisible(false);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    optionsBox.setVisible(false);
    optionsBox.setDisable(true);
    continueButton.setVisible(false);
    continueButton.setDisable(true);
    sendButton.setVisible(false);
    sendButton.setDisable(true);

    receptor.setVisible(false);
    transmissor.setVisible(false);
    dialogoDireito.setVisible(false);
    dialogoEsquerdo.setVisible(false);

    signalRadio.setVisible(false);

    textArea.setDisable(true);
    textArea.setVisible(false);

    output.setEditable(false);
    output.setVisible(false);
    bitsText.setEditable(false);
    bitsText.setVisible(false);
    asciiText.setEditable(false);
    asciiText.setVisible(false);

    asciiText.setText("ASCII:\n\n");
    bitsText.setText("Bits:\n\n");

    //Define as solucoes disponiveis na ChoiceBox de solucoes
    ObservableList<String> methodOptions = FXCollections.observableArrayList("", "CODIFICACAO BINARIA", 
    "CODIFICACAO MANCHESTER", "CODIFICACAO MANCHESTER DIFERENCIAL");
    optionsBox.setItems(methodOptions);
    optionsBox.getSelectionModel().selectFirst();

    //Adiciona listeners para as ChoiceBox
    optionsBox.setOnAction(event -> optionsList(event));

  }

  /* ***************************************************************
* Metodo: AplicacaoTransmissora
* Funcao: armazenar a mensagem digitada na interface e chamar a proxima funcao de transmissao
* Parametros: MouseEvent event = botao enviarMensagem
* Retorno: void
*************************************************************** */
  @FXML
  void AplicacaoTransmissora(MouseEvent event) {  
    String mensagemDigitada = textArea.getText();
    CamadaDeAplicacaoTransmissora (mensagemDigitada);
  }//Fim do metodo AplicacaoTransmissora

  void CamadaDeAplicacaoTransmissora(String mensagemDigitada) {
    char[] chars = mensagemDigitada.toCharArray();
    int[] quadro = new int[chars.length * 8];
    String[] binary = new String[chars.length];
    int ascii;

    //forma de atualizar os valores de ascii e binario no textArea de forma gradual
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    // Convertendo caracteres de acordo com a tabela ASCII
    for (int i = 0; i < chars.length; i++) {
      int valorASCII = chars[i];
      ascii = valorASCII;

      String binaryBuilder = "";
      // Convertendo ASCII p binário
      for (int j = 0; j < 7; j++) {
        binaryBuilder = String.valueOf(valorASCII % 2) + binaryBuilder;
        valorASCII /= 2;
      }

      while (binaryBuilder.length() < 8) {
        binaryBuilder = "0" + binaryBuilder;
      }

      binary[i] = binaryBuilder.toString();

      //preenchendo o quadro com os bits do binario
      //aqui ocorre a conversao da mensagem enviada para uma sequencia de bits(quadro)
      for (int k = 0; k < 8; k++) {//percorre cada um dos 8 bits
        quadro[i * 8 + k] = Integer.parseInt(binary[i].substring(k, k + 1)); //atribui cada digito binario ao array quadro
      }

      String asciiValue = String.valueOf(ascii);
      final int currentIndex = i;
      //abaixo um executor apenas para que a impressao dos valores de ascii e dos bits sejam feita de forma gradual no textArea
      executor.schedule(() -> {
        String showASCII = String.valueOf(chars[currentIndex]);
        String showBits = String.valueOf(binary[currentIndex]);
            
        Platform.runLater(() -> {
          asciiText.appendText("[" + showASCII + "] = " + asciiValue + "\n");
          bitsText.appendText("[" + showASCII + "] = " + showBits + "\n");
        });
      }, i * 1000, TimeUnit.MILLISECONDS);

      System.out.println("Caractere: " + chars[i]);
      System.out.println("Valor ASCII: " + ascii);
      System.out.println("Valor binário: " + binary[i]);
    }

    //fechando o executor apos processamento
    executor.schedule(() -> {
        executor.shutdown();
        CamadaFisicaTransmissora(quadro);
    }, chars.length * 50, TimeUnit.MILLISECONDS);

    CamadaFisicaTransmissora(quadro);
  }

  void CamadaFisicaTransmissora (int quadro[]) {
    int tipoDeCodificacao = getCodificacao(); 
    int fluxoBrutoDeBits [] = quadro; 
    switch (tipoDeCodificacao) {
      case 0 : //codificao binaria
        fluxoBrutoDeBits = binarioControl.CamadaFisicaTransmissoraCodificacaoBinaria(quadro);
      break;
    case 1 : //codificacao manchester
      fluxoBrutoDeBits = manchesterControl.CamadaFisicaTransmissoraCodificacaoManchester(quadro);
    break;
    case 2 : //codificacao manchester diferencial
      fluxoBrutoDeBits = diferencialControl.CamadaFisicaTransmissoraCodificacaoManchesterDiferencial(quadro);
    break;
    }//fim do switch/case
    MeioDeComunicacao(fluxoBrutoDeBits);
  }//fim do metodo CamadaFisicaTransmissora

  void MeioDeComunicacao (int fluxoBrutoDeBits []) {
    int[] fluxoBrutoDeBitsPontoA, fluxoBrutoDeBitsPontoB;
    fluxoBrutoDeBitsPontoA = fluxoBrutoDeBits;
    fluxoBrutoDeBitsPontoB = new int [fluxoBrutoDeBitsPontoA.length];
    int indexDoFluxoDeBits = 0;
    while (indexDoFluxoDeBits < fluxoBrutoDeBitsPontoA.length){
      fluxoBrutoDeBitsPontoB[indexDoFluxoDeBits] += fluxoBrutoDeBitsPontoA[indexDoFluxoDeBits];
      indexDoFluxoDeBits++;
    }

    CamadaFisicaReceptora(fluxoBrutoDeBitsPontoB);
  }//fim do metodo MeioDeTransmissao

  void CamadaFisicaReceptora (int quadro[]) {
    int tipoDeDecodificacao = getCodificacao();  
    int fluxoBrutoDeBits [] = quadro;
    switch (tipoDeDecodificacao) {
    case 0 : //codificao binaria
      fluxoBrutoDeBits = binarioControl.CamadaFisicaReceptoraCodificacaoBinaria(quadro);
    break;
    case 1 : //codificacao manchester
      fluxoBrutoDeBits = manchesterControl.CamadaFisicaReceptoraDecodificacaoManchester(quadro);
    break;
    case 2 : //codificacao manchester diferencial
      fluxoBrutoDeBits = diferencialControl.CamadaFisicaReceptoraDecodificacaoManchesterDiferencial(quadro);
    break;
    }//fim do switch/case

    CamadaDeAplicacaoReceptora(fluxoBrutoDeBits);
  }//fim do metodo CamadaFisicaTransmissora

  void CamadaDeAplicacaoReceptora (int quadro []) {
    String mensagem = "";
    String letra = "";
    int contador = 0;
    
    for (int i = 0; i < quadro.length; i++){
      letra += quadro[i];
      if (contador == 7) {
        mensagem += (char) binaryToDecimal(letra);
        letra = "";
        contador = -1;
      }
      contador ++;
      AplicacaoReceptora(mensagem);
    }
  }//fim do metodo CamadaDeAplicacaoReceptora

  void AplicacaoReceptora (String mensagem) {
    Platform.runLater(() -> {
      output.setText(mensagem);
    });
  }//fim do metodo AplicacaoReceptora

  /* ***************************************************************
  * Metodo: binaryToDecimal()
  * Funcao: Pega um numero binario e o transforma para decimal iterando atraves dos digitos
  da direita p esquerda, multiplicando o digito por 2 (valor da base) e somando os resultados
  para calcular o decimal correspondente
  * Parametros: String numero (fluxoDeBits)
  * Retorno: int valorDecimal (valor convertido de binario para decimal)
  *************************************************************** */
  public static int binaryToDecimal(String number) {
    int valorDecimal = 0;
    int base = 1;
    
    for (int i = number.length() - 1; i >= 0; i--) {
      char bit = number.charAt(i);
      if (bit == '1' || bit == '0') {
        valorDecimal += (bit - '0') * base;
        base *= 2;
      } else {
        throw new IllegalArgumentException("Erro!");
      }
    }
    return valorDecimal;
  }//fim do metodo binaryToDecimal

  @FXML 
  void enviarMensagem(ActionEvent event) {
    if(!textArea.getText().isEmpty()){
      signalRadio.setVisible(true);
      sendButton.setDisable(true);
      sendButton.setVisible(false);

      textArea.setEditable(false);

      dialogoDireito.setVisible(true);

      output.setVisible(true);
      bitsText.setVisible(true);
      asciiText.setVisible(true);
  
    } else {
      //Mostra uma mensagem de erro caso o usuario nao tenha selecionado um metodo de codificacao
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("ERRO!");
      alert.setHeaderText("Texto vazio!");
      alert.setContentText("Por favor, envie uma mensagem.");
      alert.showAndWait();
    }
  }

  @FXML
  void continueButton(ActionEvent event) {
    if (!selectedMethod.isEmpty()) {
      optionsBox.setVisible(false);
      optionsBox.setDisable(true);
      continueButton.setVisible(false);
      continueButton.setDisable(true);

      textLogo.setVisible(false);

      receptor.setVisible(true);
      transmissor.setVisible(true);

      dialogoEsquerdo.setVisible(true);

      textArea.setDisable(false);
      textArea.setVisible(true);
      textArea.setPromptText("FACA CONTATO COM A CAPSULA:");

      sendButton.setVisible(true);
      sendButton.setDisable(false);
    } else {
      //Mostra uma mensagem de erro caso o usuario nao tenha selecionado um metodo de codificacao
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("ERRO!");
      alert.setHeaderText("Opção inválida!");
      alert.setContentText("Por favor, selecione um método de codificação.");
      alert.showAndWait();
    }
  }

    /********************************************************************
  * Metodo: optionsList()
  * Funcao: atribui o valor da opcao selecionada no choiceBox para a variavel selectedMethod
  * Parametros: ActionEvent event
  * Retorno: void
  ****************************************************************** */
  @FXML
  void optionsList(ActionEvent event){
    selectedMethod = optionsBox.getSelectionModel().getSelectedItem();
  }//fim do metodo positionList()

    /********************************************************************
  * Metodo: getPosition()
  * Funcao: atribui valores inteiros para as opcoes de codificao escolhida pelo usuario 
  * Parametros: nenhum
  * Retorno: int option = opcao escolhida
  ****************************************************************** */
  public int getCodificacao(){
    if(selectedMethod == "CODIFICACAO BINARIA"){
      option = 0;
    } else if(selectedMethod == "CODIFICACAO MANCHESTER") {
      option = 1;
    } else if (selectedMethod == "CODIFICACAO MANCHESTER DIFERENCIAL"){
      option = 2;
    } else {
      System.out.println("Deu ruim!");
    }
    return option;
  }//fim do metodo getPosition()
}