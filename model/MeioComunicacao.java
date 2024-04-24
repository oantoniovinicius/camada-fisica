package model;

import controller.*;

public class MeioComunicacao {
  private int currentBit = 0;
  CamadaFisicaReceptora cP = new CamadaFisicaReceptora();
  
  /* ***************************************************************
  * Metodo: MeioDeComunicacao
  * Funcao: transmite os bits da mensagem de um ponto A (transmissor) para um ponto B (receptor).
  Alem disso, cuida da animacao do sinal na interface
  * Parametros: int fluxoBrutoDeBits[] = fluxo de bits ja codificados
  * Retorno: void
  *************************************************************** */
  void MeioDeComunicacao(int fluxoBrutoDeBits[], MainControl mainControl) {
    int[] fluxoBrutoDeBitsPontoA, fluxoBrutoDeBitsPontoB;
    fluxoBrutoDeBitsPontoA = fluxoBrutoDeBits;
    fluxoBrutoDeBitsPontoB = new int[fluxoBrutoDeBitsPontoA.length];

    String mensagemDigitada = mainControl.getMsg(); 
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
            mainControl.moveWave();
            mainControl.activateWave(currentBit);

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
      cP.camadaFisicaReceptora(fluxoBrutoDeBitsPontoB, mainControl);
    }).start();
  }//fim do metodo MeioDeComunicacao()

}
