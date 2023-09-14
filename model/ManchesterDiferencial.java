/* ***************************************************************
* Autor............: Antonio Vinicius Silva Dutra
* Matricula........: 202110810
* Inicio...........: 04/09/2023
* Ultima alteracao.: 12/09/2023
* Nome.............: ManchesterDiferencial.java
* Funcao...........: codifica e decodifica o quadro[] binario em Manchester Diferencial
*************************************************************** */
package model;

public class ManchesterDiferencial {
  /********************************************************************
  * Metodo: CamadaFisicaTransmissoraCodificacaoManchesterDiferencial()
  * Funcao: codifica os bits do quadro em codificacao Manchester Diferencial
  * Parametros: quadro de bits
  * Retorno: array de inteiro com os bits manchester diferencial
  ****************************************************************** */
  public int[] CamadaFisicaTransmissoraCodificacaoManchesterDiferencial(int[] quadro) {
    int[] manchesterDiferentialSignal = new int[quadro.length * 2]; //dobro de tamanho do array original
    int deslocIndex = 0;  
    int bitDesloc = 0;
    int lastTransition = 0;  //armazena o ultimo valor da transicao (0 ou 1).
    
    for (int valor : quadro) {
      for (int k = 0; k < 32; k++) { //cada valor no quadro tem 32 bits
        if (bitDesloc == 32) {
          deslocIndex++;
          bitDesloc = 0;
        }
        //realiza a codificacao
        //o bit codificado depende da mudancao ou nao em relacao ao bit anterior (lastTransition)
        //operacoes de deslocamento bom bitWise abaix (semelhantes ao metodo Manchester)
        manchesterDiferentialSignal[deslocIndex] |= (((((valor >> k) & 1) == 0) ? 0 : 1) ^ lastTransition) << bitDesloc;
        lastTransition = (((valor >> k) & 1) == 0) ? 0 : 1;  //atualiza o ultimo valor da transicao.

        bitDesloc += 2;
      }
    }

    return manchesterDiferentialSignal;
  }// fim do metodo CamadaFisicaTransmissoraCodificacaoManchesterDiferencial()
    
  /********************************************************************
  * Metodo: CamadaFisicaTransmissoraDecodificacaoManchesterDiferencial()
  * Funcao: decodifica os bits do quadro manchester diferencial em binario novamente
  * Parametros: quadro de bits manchester diferencial
  * Retorno: array de inteiro com os bits decodificados
  ****************************************************************** */
  public int[] CamadaFisicaReceptoraDecodificacaoManchesterDiferencial(int[] quadroManchesterDiferential) {
    int[] finalDecod = new int[quadroManchesterDiferential.length / 2];
    int decodPosition = 0; // indice para quadro decodificado (finalDecod)
    int deslocBit = 0; // inicialize o bit anterior como 0
    int lastTransition = 0;  //armazena o ultimo valor da transicao (0 ou 1)
    
    for (int bitsIndex = 0; bitsIndex < finalDecod.length; bitsIndex++) {
      for (int k = 0; k < 64; k += 2) {
        int valor = quadroManchesterDiferential[decodPosition];

        //valor eh atualizado para o proximo valor de quadroManchesterDiferential, representando os proximos dois bits codificados
        if (k>31){
          valor = quadroManchesterDiferential[decodPosition + 1];
        }
        if(deslocBit == 32){
          deslocBit = 0;
        }
        //atualiza o valor atual em finalDecod no indice bitsIndex e na posicao deslocBit usando operacoes bitwise
        finalDecod[bitsIndex] |= ((((valor >> k) & 1) ^ lastTransition) << deslocBit);
        lastTransition = ((valor >> k) & 1) ^ lastTransition; // depois de decodificar um bit do par de bits, 
        //lastTransition eh atualizado com o valor do bit atual
        deslocBit++;
      }
        decodPosition += 2;
    }
    return finalDecod;
  }// fim do metodo CamadaFisicaTransmissoraCodificacaoManchesterDiferencial()
}