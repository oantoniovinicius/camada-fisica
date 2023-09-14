/* ***************************************************************
* Autor............: Antonio Vinicius Silva Dutra
* Matricula........: 202110810
* Inicio...........: 04/09/2023
* Ultima alteracao.: 12/09/2023
* Nome.............: MainControl.java
* Funcao...........: gerencia e controla a GUI do fxml, gerenciando os metodos principais da aplicacao
*************************************************************** */
package control;
import java.net.URL;
import java.util.ResourceBundle;
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
  //botoes da aplicacao
  @FXML private Button iniciar;
  @FXML private Button continueButton;
  @FXML private Button sendButton;
  @FXML private Button backButton;

  //choiceBox com as opcoes de codificacao
  @FXML private ChoiceBox<String> optionsBox;

  //textAreas que vao aparecer na aplicacao
  @FXML private TextArea textArea;
  @FXML private TextArea asciiText;
  @FXML private TextArea bitsText;
  @FXML private TextArea output;

  //alguns imageView da aplicacao (ovni, capsula, etc)
  @FXML private ImageView textLogo;
  @FXML private ImageView transmissor;
  @FXML private ImageView receptor;
  @FXML private ImageView dialogoDireito;
  @FXML private ImageView dialogoEsquerdo;
  @FXML private ImageView signalRadio;

  //ondas de bit = 1
  @FXML private ImageView highOne;
  @FXML private ImageView highTwo;
  @FXML private ImageView highThree;
  @FXML private ImageView highFour;
  @FXML private ImageView highFive;
  @FXML private ImageView highSix;
  @FXML private ImageView highSeven;
  @FXML private ImageView highEight;

  //ondas de bit = 0
  @FXML private ImageView lowOne;
  @FXML private ImageView lowTwo;
  @FXML private ImageView lowThree;
  @FXML private ImageView lowFour;
  @FXML private ImageView lowFive;
  @FXML private ImageView lowSix;
  @FXML private ImageView lowSeven;
  @FXML private ImageView lowEight;

  //ondas para a transicao de bit para 0 ou para 1
  @FXML private ImageView transitionOne;
  @FXML private ImageView transitionTwo;
  @FXML private ImageView transitionThree;
  @FXML private ImageView transitionFour;
  @FXML private ImageView transitionFive;
  @FXML private ImageView transitionSix;
  @FXML private ImageView transitionSeven;
  @FXML private ImageView transitionEight;

  //array de imgs que vao guardar as formas de onda
  private ImageView lowImgs[];
  private ImageView highImgs[];
  private ImageView transitionImgs[];

  //Instanciando a string que vai receber a opcao selecionada no choiceBox
  private String selectedMethod = "";

  //Instanciando o inteiro que vai ser utilizado no switch - case
  private int option;
  private int lastBit = 0; 
  private int currentBit = 0;

  //instanciando os controles das classes de codificacao e decodificacao
  Binaria binarioControl = new Binaria();
  Manchester manchesterControl = new Manchester();
  ManchesterDiferencial diferencialControl = new ManchesterDiferencial();

  /********************************************************************
  * Metodo: startButton()
  * Funcao: inicio do programa em si. Apos clicar nesse botao, habilita as opcoes de codificacao
  * Parametros: ActionEvent = event (clique do mouse)
  * Retorno: void
  ****************************************************************** */
  @FXML
  void startButton(ActionEvent event) {
    iniciar.setDisable(true);
    iniciar.setVisible(false);

    optionsBox.setVisible(true);
    optionsBox.setDisable(false);

    continueButton.setVisible(true);
    continueButton.setDisable(false);
    //textLogo.setVisible(false);
  }//fim do metodo startButton()

  /********************************************************************
  * Metodo: initialize()
  * Funcao: desativa alguns elementos graficos que serao utilizados de acordo com o clique
  nos botoes
  * Parametros: URL location, resources
  * Retorno: void
  ****************************************************************** */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    optionsBox.setVisible(false);
    optionsBox.setDisable(true);
    continueButton.setVisible(false);
    continueButton.setDisable(true);
    sendButton.setVisible(false);
    sendButton.setDisable(true);
    backButton.setVisible(false);
    backButton.setDisable(true);

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
    bitsText.setText("Bits (binario):\n\n");

    //define as codificacoes disponiveis na ChoiceBox de solucoes
    ObservableList<String> methodOptions = FXCollections.observableArrayList("", "CODIFICACAO BINARIA", 
    "CODIFICACAO MANCHESTER", "CODIFICACAO MANCHESTER DIFERENCIAL");
    optionsBox.setItems(methodOptions);
    optionsBox.getSelectionModel().selectFirst();

    //adiciona listeners para as ChoiceBox
    optionsBox.setOnAction(event -> optionsList(event));

    ImageView low[] = { lowOne, lowTwo, lowThree, lowFour, lowFive, lowSix, lowSeven, lowEight};
    lowImgs = low;

    ImageView high[] = { highOne, highTwo, highThree, highFour, highFive, highSix, highSeven, highEight};
    highImgs = high;

    ImageView transition[] = { transitionOne, transitionTwo, transitionThree, transitionFour,
    transitionFive, transitionSix, transitionSeven, transitionEight};
    transitionImgs = transition;

    //desativando as imageView 
    for (int i = 0; i < 8; i++) {
      lowImgs[i].setVisible(false);
      highImgs[i].setVisible(false);
    }
    for (int i = 0; i < 7; i++) {
      transitionImgs[i].setVisible(false);
    }
  } //fim do metodo initialize()

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

  /* ***************************************************************
  * Metodo: CamadaDeAplicacaoTransmissora
  * Funcao: converte a mensagem digitada em ASCII e depois para binario.
  Por fim atribui ao array quadro[], utilizando uma mascara, o valor dos binarios
  * Parametros: String mensagemDigitada = mensagem digitada pelo usuario
  * Retorno: void
  *************************************************************** */
  void CamadaDeAplicacaoTransmissora(String mensagemDigitada) {
    char[] chars = mensagemDigitada.toCharArray();
    int[] quadro;
    String[] binary = new String[chars.length];
    int ascii;
    int indexBit = 0;

    //confere se eh multiplo de 4, pois sao 4 caracteres por posicao no array
    //basicamente, abaixo eh calculado o tamanho que o array quadro tem que ter
    //para alocar todos os caracteres e bits corretamente
    if(chars.length % 4 == 0){ 
      quadro = new int[chars.length / 4]; 
    } else {
      quadro = new int[(chars.length / 4) +1];
    }
    //Convertendo caracteres de acordo com a tabela ASCII
    for (int i = 0; i < chars.length; i++) {
      int valorASCII = chars[i];
      ascii = valorASCII;

      String binaryBuilder = "";
      // onvertendo ASCII p binario
      for (int j = 0; j < 7; j++) { //executa 7 vezes representando cada um dos sete bits significativos do valor ASCII 
        binaryBuilder = String.valueOf(valorASCII % 2) + binaryBuilder;
        valorASCII /= 2;
      }

      while (binaryBuilder.length() < 8) {
        binaryBuilder = "0" + binaryBuilder; //garante que a representacao binaria tenha 8 bits no total
      }

      binary[i] = binaryBuilder.toString();

      for (int j = 0; j < 8; j++){ //itera pelos 8 bits
        int positionBit = binaryBuilder.charAt(j) == '0' ? 0 : 1;//verifica se o bit atual eh 0 ou 1 e armazena na variavel
        //usando uma mascara de deslocamento para a esquerda, atribuimos os bits ao quadro
        quadro[indexBit] = (quadro[indexBit] << 1) | positionBit; 
      }
  
      if (i % 4 == 3){ //verifica se estamos no quarto caractere para mover para a proxima posicao do quadro
        indexBit++;
      }

      String asciiValue = String.valueOf(ascii);

      //imprimindo os valores em seus respectivos textArea
      final int currentIndex = i;
      String showASCII = String.valueOf(chars[currentIndex]); 
      String showBits = String.valueOf(binary[currentIndex]);
            
      asciiText.appendText("[" + showASCII + "] = " + asciiValue + "\n");
      bitsText.appendText("[" + showASCII + "] = " + showBits + "\n");
    }
    CamadaFisicaTransmissora(quadro);
  }//fim do metodo CamadaDeAplicacaoTransmissora()

    /* ***************************************************************
  * Metodo: CamadaFisicaTransmissora
  * Funcao: passa o quadro para uma variavel fluxoBrutoDeBits que vai ser codificada de acordo
  com o metodo de codificacao escolhido pelo usuario na interface
  * Parametros: int quadro[] = quadro com os bits
  * Retorno: void
  *************************************************************** */
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
  }//fim do metodo CamadaFisicaTransmissora()

  /* ***************************************************************
  * Metodo: MeioDeComunicacao
  * Funcao: transmite os bits da mensagem de um ponto A (transmissor) para um ponto B (receptor).
  Alem disso, cuida da animacao do sinal na interface
  * Parametros: int fluxoBrutoDeBits[] = fluxo de bits ja codificados
  * Retorno: void
  *************************************************************** */
  void MeioDeComunicacao(int fluxoBrutoDeBits[]) {
    int[] fluxoBrutoDeBitsPontoA, fluxoBrutoDeBitsPontoB;
    fluxoBrutoDeBitsPontoA = fluxoBrutoDeBits;
    fluxoBrutoDeBitsPontoB = new int[fluxoBrutoDeBitsPontoA.length];

    String mensagemDigitada = textArea.getText(); 
    char[] chars = mensagemDigitada.toCharArray();
    int sizeMessage = chars.length; //pegando o tamanho da mensagem para ser usado no for
    new Thread(() -> {
      int indexDoFluxoDeBits = 0;
      int tradeBits = 0;
      int indexArray = 0;
      for (int i = 0; i < sizeMessage; i++) { //itera atraves dos caracteres da mensagem
        //abaixo verifica se ja foram processados 4 caracteres
        if(i % 4 == 0 && i != 0){
          indexArray++; 
        }
        for (int j = 0; j < 8; j++) { //8 bits por caractere
          int mask = 1 << tradeBits; //pega 1 bit
          int value = fluxoBrutoDeBits[indexArray] & mask; //extraindo os bits indicados pela mascara
          //abaixo, desloca os bits para que o bit de interesse (aquele definido pela máscara) 
          //seja movido para a posição mais baixa (bit menos significativo) 
          currentBit = value >> tradeBits;

          //metodos para ativar as imagens na interface, fazendo a passagem da onda em si
          moveWave();
          activateWave(currentBit);

          try {
            Thread.sleep(100);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          tradeBits++;
        }
      }
      while (indexDoFluxoDeBits < fluxoBrutoDeBitsPontoA.length) {
        fluxoBrutoDeBitsPontoB[indexDoFluxoDeBits] += fluxoBrutoDeBitsPontoA[indexDoFluxoDeBits];
        indexDoFluxoDeBits++;
      }
      CamadaFisicaReceptora(fluxoBrutoDeBitsPontoB);
    }).start();
  }//fim do metodo MeioDeComunicacao()

  /* ***************************************************************
  * Metodo: CamadaFisicaReceptora
  * Funcao: chama os metodos de decodificacao de acordo com a opcao escolhida pelo usuario
  * Parametros: int quadro[] = quadro com os bits
  * Retorno: void
  *************************************************************** */
  void CamadaFisicaReceptora (int quadro[]) {
    int tipoDeDecodificacao = getCodificacao();  
    int fluxoBrutoDeBits [] = quadro;
    switch (tipoDeDecodificacao) {
    case 0 : //decodificao binaria
      fluxoBrutoDeBits = binarioControl.CamadaFisicaReceptoraCodificacaoBinaria(quadro);
    break;
    case 1 : //decodificacao manchester
      fluxoBrutoDeBits = manchesterControl.CamadaFisicaReceptoraDecodificacaoManchester(quadro);
    break;
    case 2 : //decodificacao manchester diferencial
      fluxoBrutoDeBits = diferencialControl.CamadaFisicaReceptoraDecodificacaoManchesterDiferencial(quadro);
    break;
    }//fim do switch/case

    CamadaDeAplicacaoReceptora(fluxoBrutoDeBits);
  }//fim do metodo CamadaFisicaTransmissora()

  /* ***************************************************************
  * Metodo: CamadaDeAplicacaoReceptora
  * Funcao: chama metodos para converter o quadro com os bits de volta para a mensagem de texto
  * Parametros: int quadro[] = quadro com os bits
  * Retorno: void
  *************************************************************** */
  void CamadaDeAplicacaoReceptora(int quadro[]) {
    StringBuilder mensagemBuilder = new StringBuilder();

    for (int i = 0; i < quadro.length; i++) {
      int intBits = quadro[i]; //para cada elemento do array, extrai um valor inteiro
      String binaryString = intBinary(intBits);//converte o inteiro em uma representacao de 32 bits e armazena numa string
        
      //dividindo o grupo binario em sub grupos de 8 bits e convertendo para ASCII
      for (int j = 0; j < binaryString.length(); j += 8) {
        String binaryByte = binaryString.substring(j, j + 8); //extrai 8 bits da representacao binaria
        int asciiValue = binaryToDecimal(binaryByte);//converte o subgrupo em um valor ASCII
        mensagemBuilder.append((char) asciiValue);//acrescentado ao stringbuilder como um caractere
      }
    }

    String mensagem = mensagemBuilder.toString();
    AplicacaoReceptora(mensagem);
  }//fim do metodo CamadaDeAplicacaoReceptora()

  /* ***************************************************************
  * Metodo: intBinary
  * Funcao: converte um inteiro em uma representação binária com 32 bits
  * Parametros: int value = inteiro de bits
  * Retorno: void
  *************************************************************** */
  String intBinary(int value) {
    StringBuilder binaryBuilder = new StringBuilder();
    
    for (int i = 31; i >= 0; i--) {//comeca no bit mais significativo (31)
      int bit = (value >> i) & 1;//desloca o bit de interesse para a posicao mais baixa(bit menos significativo) e extrai o bit
      binaryBuilder.append(bit);//construcao da representacao binaria
    }
    return binaryBuilder.toString();
  }//fim do metodo intBinary()

  /* ***************************************************************
  * Metodo: AplicacaoReceptora()
  * Funcao: imprime a mensagem na interface
  * Parametros: mensagem final convertida
  * Retorno: void
  *************************************************************** */
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

      //se o bit for 0, ativa a imagem correspondente
      if (bit == 0){
        lowImgs[0].setVisible(true);
      }
      else{
        highImgs[0].setVisible(true);
      }
      //se o bit for diferente do ultimo bit recebido, entao vai ter uma transicao entre eles
      //seja de 0 para 1 ou de 1 para 0
      if (bit != lastBit) {
        transitionImgs[0].setVisible(true);
      }
      lastBit = currentBit;
    });
  }

  /********************************************************************
  * Metodo: enviarMensagem()
  * Funcao: botao para o usuario iniciar todo o processo de envio da mensagem.
  no SceneBuilder, foi definido que apos o clique, o botao chama o metodo inicial da camada fisica
  * Parametros: ActionEvent = clique do mouse no botao
  * Retorno: void
  ****************************************************************** */
  @FXML 
  void enviarMensagem(ActionEvent event) {
    if(!textArea.getText().isEmpty()){ //verifica se o usuario digitou alguma mensagem
      signalRadio.setVisible(true);
      sendButton.setDisable(true);
      sendButton.setVisible(false);

      textArea.setEditable(false);

      dialogoDireito.setVisible(true);

      output.setVisible(true);
      bitsText.setVisible(true);
      asciiText.setVisible(true);

      backButton.setVisible(true);
      backButton.setDisable(false);
  
    } else {
      //Mostra uma mensagem de erro caso o usuario nao tenha digitado nenhuma mensagem
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("ERRO!");
      alert.setHeaderText("Texto vazio!");
      alert.setContentText("Por favor, envie uma mensagem.");
      alert.showAndWait();
    }
  }//fim do metodo enviarMensagem()

  /********************************************************************
  * Metodo: voltar()
  * Funcao: botao que permite ao usuario retornar para o menu de selecao de codificacao
  reativando os botoes, imagens e choiceBox na interface
  * Parametros: ActionEvent = clique do mouse no botao
  * Retorno: void
  ****************************************************************** */
  @FXML
  void voltar(ActionEvent event) {
    asciiText.setVisible(false);
    bitsText.setVisible(false);
    asciiText.setText("ASCII:\n\n");
    bitsText.setText("Bits(binario):\n\n");

    optionsBox.setVisible(true);
    optionsBox.setDisable(false);

    continueButton.setVisible(true);
    continueButton.setDisable(false);

    backButton.setVisible(false);
    backButton.setDisable(true);

    signalRadio.setVisible(false);

    textLogo.setVisible(true);
    receptor.setVisible(false);
    transmissor.setVisible(false);
    dialogoDireito.setVisible(false);
    dialogoEsquerdo.setVisible(false);
    output.setVisible(false);
    output.setText("");
    textArea.setVisible(false);
    textArea.setText("");

    for (int i = 0; i < 8; i++) {
      lowImgs[i].setVisible(false);
      highImgs[i].setVisible(false);
    }
    for (int i = 0; i < 7; i++) {
      transitionImgs[i].setVisible(false);
    }
  }//fim do metodo voltar()

  /********************************************************************
  * Metodo: continueButton()
  * Funcao: botao de avanco depois que o usuario escolher o tipo de codificacao.
  ativa e desativa imagens e a choiceBox apos escolha do usuario
  * Parametros: ActionEvent = clique do mouse no botao
  * Retorno: void
  ****************************************************************** */
  @FXML
  void continueButton(ActionEvent event) {
    if (!selectedMethod.isEmpty()) { //verifica se o usuario escolheu alguma opcao
      optionsBox.setVisible(false);
      optionsBox.setDisable(true);
      continueButton.setVisible(false);
      continueButton.setDisable(true);

      textLogo.setVisible(false);

      receptor.setVisible(true);
      transmissor.setVisible(true);

      dialogoEsquerdo.setVisible(true);

      textArea.setEditable(true);
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
  }//fim do metodo continueButton()

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
  * Metodo: getCodificacao()
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