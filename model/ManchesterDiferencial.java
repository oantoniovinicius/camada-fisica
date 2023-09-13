/* ***************************************************************
* Autor............: Antonio Vinicius Silva Dutra
* Matricula........: 202110810
* Inicio...........: 04/09/2023
* Ultima alteracao.: 12/09/2023
* Nome.............: Principal.java
* Funcao...........: Inicia a GUI do programa pelo metodo start()
*************************************************************** */
package model;

public class ManchesterDiferencial {
  public int[] CamadaFisicaTransmissoraCodificacaoManchesterDiferencial(int[] quadro) {
    int[] manchesterDiferentialSignal = new int[quadro.length * 2];
    int deslocIndex = 0;  
    int bitDesloc = 0;
    int lastTransition = 0;  //armazena o ultimo valor da transicao (0 ou 1).
    
    for (int valor : quadro) {
      for (int k = 0; k < 32; k++) {
        if (bitDesloc == 32) {
          deslocIndex++;
          bitDesloc = 0;
        }
        //realiza a codificacao
        manchesterDiferentialSignal[deslocIndex] |= (((((valor >> k) & 1) == 0) ? 0 : 1) ^ lastTransition) << bitDesloc;
    
        lastTransition = (((valor >> k) & 1) == 0) ? 0 : 1;  //atualiza o ultimo valor da transicao.
        bitDesloc += 2;
      }
    }

    return manchesterDiferentialSignal;
  }
    
  public int[] CamadaFisicaReceptoraDecodificacaoManchesterDiferencial(int[] quadroManchesterDiferential) {
    int[] finalDecod = new int[quadroManchesterDiferential.length / 2];
    int decodPosition = 0; // indice para quadroDecodificado
    int deslocBit = 0; // inicialize o bit anterior como 0
    int lastTransition = 0;  //armazena o ultimo valor da transicao (0 ou 1).
    
    for (int bitsIndex = 0; bitsIndex < finalDecod.length; bitsIndex++) {
      for (int k = 0; k < 64; k += 2) {
        int valor = quadroManchesterDiferential[decodPosition];

        if (k>31){
          valor = quadroManchesterDiferential[decodPosition + 1];
        }
        if(deslocBit == 32){
          deslocBit = 0;
        }

        finalDecod[bitsIndex] |= ((((valor >> k) & 1) ^ lastTransition) << deslocBit);
        lastTransition = ((valor >> k) & 1) ^ lastTransition;  
        deslocBit++;
      }
        decodPosition += 2;
    }
    return finalDecod;
  }
}