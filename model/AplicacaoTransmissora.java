/* ***************************************************************
* Autor............: Antonio Vinicius Silva Dutra
* Matricula........: 202110810
* Inicio...........: 31/03/2024
* Ultima alteracao.: 07/04/2024
* Nome.............: AplicacaoTransmissora.java
* Funcao...........: armazenar a mensagem digitada na interface e chamar a proxima funcao de transmissao
*************************************************************** */
package model;

import controller.MainControl;

public class AplicacaoTransmissora {
  CamadaAplicacaoTransmissora cT = new CamadaAplicacaoTransmissora();
    
  public void aplicacaoTransmissora(MainControl mainController, String mensagem) {  
    cT.camadaDeAplicacaoTransmissora (mensagem, mainController);
  }//Fim do metodo AplicacaoTransmissora
}
