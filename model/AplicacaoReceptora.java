/* ***************************************************************
* Autor............: Antonio Vinicius Silva Dutra
* Matricula........: 202110810
* Inicio...........: 31/03/2024
* Ultima alteracao.: 07/04/2024
* Nome.............: AplicacaoReceptora.java
* Funcao...........: imprime a mensagem na interface
*************************************************************** */
package model;
import controller.MainControl;
import javafx.application.Platform;

public class AplicacaoReceptora {
/* ***************************************************************
  * Metodo: AplicacaoReceptora()
  * Funcao: imprime a mensagem na interface
  * Parametros: mensagem final convertida
  * Retorno: void
  *************************************************************** */
  void aplicacaoReceptora (String mensagem, MainControl mainControl) {
    Platform.runLater(() -> {
      mainControl.setOutputText(mensagem);
    });
  }//fim do metodo AplicacaoReceptora
}
