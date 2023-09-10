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
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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

  @FXML private ImageView highLowOne;
  @FXML private ImageView highLowTwo;
  @FXML private ImageView highLowThree;
  @FXML private ImageView highLowFour;
  @FXML private ImageView highLowFive;
  @FXML private ImageView highLowSix;
  @FXML private ImageView highLowSeven;
  @FXML private ImageView highLowEight;

  @FXML private ImageView highOne;
  @FXML private ImageView highTwo;
  @FXML private ImageView highThree;
  @FXML private ImageView highFour;
  @FXML private ImageView highFive;
  @FXML private ImageView highSix;
  @FXML private ImageView highSeven;
  @FXML private ImageView highEight;

  @FXML private ImageView lowHighOne;
  @FXML private ImageView lowHighTwo;
  @FXML private ImageView lowHighThree;
  @FXML private ImageView lowHighFour;
  @FXML private ImageView lowHighFive;
  @FXML private ImageView lowHighSix;
  @FXML private ImageView lowHighSeven;
  @FXML private ImageView lowHighEight;

  @FXML private ImageView lowOne;
  @FXML private ImageView lowTwo;
  @FXML private ImageView lowThree;
  @FXML private ImageView lowFour;
  @FXML private ImageView lowFive;
  @FXML private ImageView lowSix;
  @FXML private ImageView lowSeven;
  @FXML private ImageView lowEight;

  @FXML private ImageView transitionOne;
  @FXML private ImageView transitionTwo;
  @FXML private ImageView transitionThree;
  @FXML private ImageView transitionFour;
  @FXML private ImageView transitionFive;
  @FXML private ImageView transitionSix;
  @FXML private ImageView transitionSeven;

  private ImageView lowImgs[];
  private ImageView highImgs[];
  private ImageView highLowImgs[];
  private ImageView lowHighImgs[];
  private ImageView transitionImgs[];

  //Instanciando a string que vai receber a opcao selecionada no choiceBox
  private String selectedMethod = "";

  //Instanciando o inteiro que vai ser utilizado no switch - case
  private int option;

  private int lastSignal = 0; // the lastSignal sent by physical layer, 0 = low, 1 = high

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

    ImageView low[] = { lowOne, lowTwo, lowThree, lowFour, lowFive, lowSix, lowSeven, lowEight};
    lowImgs = low;

    ImageView high[] = { highOne, highTwo, highThree, highFour, highFive, highSix, highSeven, highEight};
    highImgs = high;

    ImageView highLow[] = { highLowOne, highLowTwo, highLowThree, highLowFour, highLowFive, 
    highLowSix, highLowSeven, highLowEight};
    highLowImgs = highLow;

    ImageView lowHigh[] = { lowHighOne, lowHighTwo, lowHighThree, lowHighFour, lowHighFive, 
    lowHighSix, lowHighSeven, lowHighEight};
    lowHighImgs = lowHigh;

    ImageView transition[] = { transitionOne, transitionTwo, transitionThree, transitionFour,
    transitionFive, transitionSix, transitionSeven};
    transitionImgs = transition;

    for (int i = 0; i < 8; i++) {
      lowImgs[i].setVisible(false);
      highImgs[i].setVisible(false);
      highLowImgs[i].setVisible(false);
      lowHighImgs[i].setVisible(false);
    }
    for (int i = 0; i < 7; i++) {
      transitionImgs[i].setVisible(false);
    }
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
    int[] quadro;
    String[] binary = new String[chars.length];
    int ascii;

    if(chars.length % 4 == 0){
      quadro = new int[chars.length / 4];
    } else {
      quadro = new int[(chars.length / 4) +1];
    }

    int indexBit = 0;
    String binaryTotal = "";

    //forma de atualizar os valores de ascii e binario no textArea de forma gradual
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    // Convertendo caracteres de acordo com a tabela ASCII
    for (int i = 0; i < chars.length; i++) {
      int valorASCII = chars[i];
      ascii = valorASCII;

      String binaryBuilder = "";
      // Convertendo ASCII p binario
      for (int j = 0; j < 7; j++) {
        binaryBuilder = String.valueOf(valorASCII % 2) + binaryBuilder;
        valorASCII /= 2;
      }

      while (binaryBuilder.length() < 8) {
        binaryBuilder = "0" + binaryBuilder;
      }

      binary[i] = binaryBuilder.toString();
      binaryTotal += binaryBuilder;

      for (int j = 0; j < 8; j++){
        int positionBit = binaryBuilder.charAt(j) == '0' ? 0 : 1;
        quadro[indexBit] = (quadro[indexBit] << 1) | positionBit;
      }
  
      if (i % 4 == 3){
        indexBit++;
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
      }, i * 500, TimeUnit.MILLISECONDS);
    }
    System.out.println("Valor binário TOTAL: " + binaryTotal);
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

  void MeioDeComunicacao(int fluxoBrutoDeBits[]) {
    int[] fluxoBrutoDeBitsPontoA, fluxoBrutoDeBitsPontoB;
    fluxoBrutoDeBitsPontoA = fluxoBrutoDeBits;
    fluxoBrutoDeBitsPontoB = new int[fluxoBrutoDeBitsPontoA.length];

    new Thread(() -> {
      int indexDoFluxoDeBits = 0;

      for (int i = 0; i < fluxoBrutoDeBits.length; i++) {
        int size = fluxoBrutoDeBits.length;
        int valor = fluxoBrutoDeBits[i];
        for (int j = 0; j < 8; j++) {
          int bitAtual = ((valor >> j) & 1);
          System.out.println("bit atual: " + bitAtual);
          refresh();
          giveSignal(bitAtual, j);

          try {
            Thread.sleep(500);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
      while (indexDoFluxoDeBits < fluxoBrutoDeBitsPontoA.length) {
        fluxoBrutoDeBitsPontoB[indexDoFluxoDeBits] += fluxoBrutoDeBitsPontoA[indexDoFluxoDeBits];
        indexDoFluxoDeBits++;
      }
        CamadaFisicaReceptora(fluxoBrutoDeBitsPontoB);
    }).start();
}  

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

  void CamadaDeAplicacaoReceptora(int quadro[]) {
    StringBuilder mensagemBuilder = new StringBuilder();

    for (int i = 0; i < quadro.length; i++) {
      int intBits = quadro[i];
      String binaryString = intBinary(intBits);
        
      //dividindo o grupo binario em sub grupos de 8 bits e convertendo para ASCII
      for (int j = 0; j < binaryString.length(); j += 8) {
        String binaryByte = binaryString.substring(j, j + 8);
        int asciiValue = binaryDecimal(binaryByte);
        mensagemBuilder.append((char) asciiValue);
      }
    }

    String mensagem = mensagemBuilder.toString();
    AplicacaoReceptora(mensagem);
  }

  //Função para converter um inteiro em uma representação binária com 32 bits
  String intBinary(int value) {
    StringBuilder binaryBuilder = new StringBuilder();
    
    for (int i = 31; i >= 0; i--) {
      int bit = (value >> i) & 1;
      binaryBuilder.append(bit);
    }
    return binaryBuilder.toString();
  }

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
  public static int binaryDecimal(String number) {
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

  public void refresh() {
      Platform.runLater(() -> {
          for (int i = 7; i >= 1; i--) {
            lowImgs[i].setVisible(lowImgs[i-1].isVisible());
            highImgs[i].setVisible(highImgs[i-1].isVisible());
            //midImgs[i].setVisible(midImgs[i - 1].isVisible());
          }
      });
  }
  
  public void giveSignal(int bit, int i) {
    Platform.runLater(() -> {
      if (bit == 0){
        lowImgs[0].setVisible(true);
      }
      else{
        highImgs[0].setVisible(true);
      }
      
      if (bit != lastSignal) {
        transitionImgs[0].setVisible(true);
      }
      lastSignal = bit;
    });
  }

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