/* ***************************************************************
* Autor............: Antonio Vinicius Silva Dutra
* Matricula........: 202110810
* Inicio...........: 04/09/2023
* Ultima alteracao.: 12/09/2023
* Nome.............: Manchester.java
* Funcao...........: codifica e decodifica o quadro[] binario em Manchester
*************************************************************** */
package model;

public class Manchester {
  /********************************************************************
  * Metodo: CamadaFisicaTransmissoraCodificacaoManchester()
  * Funcao: codifica os bits do quadro em codificacao Manchester
  * Parametros: quadro de bits
  * Retorno: array de inteiro com os bits manchester
  ****************************************************************** */
  public int[] CamadaFisicaTransmissoraCodificacaoManchester(int[] quadro) {
    int[] manchesterSignal = new int[quadro.length * 2]; //array com dobro de tamanho do original
    int deslocIndex = 0;  
    int bitDesloc = 0;

    for (int valor : quadro) { //percorre cada valor no array de entrada
      for (int k = 0; k < 32; k++) { //cada valor no quadro tem 32 bits
        if (bitDesloc == 32) {
          deslocIndex++;
          bitDesloc = 0; //move para o proximo elemento em manchesterSignal
        }

        //utilizando mascara, atualiza o valor em manchesterSignal movendo o bit na posicao K do 'valor' atual
        //para a posicao mais baixa (bit menos significativo) e obtem o valor desse bit.
        //Converte os valores dos bits de acordo com os valores deles '0' ou '1' e move o valor obtido
        //para a posicao correta dentro do elemento atual do manchesterSignal, de acordo com o bitDesloc
        manchesterSignal[deslocIndex] |= (((((valor >> k) & 1) == 0) ? 0 : 1) << bitDesloc);
        manchesterSignal[deslocIndex] |= (((((valor >> k) & 1) == 0) ? 1 : 0) << (bitDesloc + 1));

        bitDesloc += 2; //incrementa 2 para avancar para a proxima posicao
      }
    }
    return manchesterSignal;
  }// fim do metodo CamadaFisicaTransmissoraCodificacaoManchester()

  /********************************************************************
  * Metodo: CamadaFisicaTransmissoraDecodificacaoManchester()
  * Funcao: decodifica os bits do quadro manchester em binario novamente
  * Parametros: quadro de bits manchester
  * Retorno: array de inteiro com os bits decodificados
  ****************************************************************** */
  public int[] CamadaFisicaReceptoraDecodificacaoManchester(int[] quadroManchester) {
    int[] finalDecod = new int[quadroManchester.length / 2]; //reduz a taxa bits para a metade (tamanho original)
    int decodPosition = 0; // indice para quadroManchester
    int deslocBit = 0; 

    for (int quadroIndex = 0; quadroIndex < finalDecod.length; quadroIndex++) {
      for (int k = 0; k < 64; k += 2) { 
        int valor = quadroManchester[decodPosition]; //o valor atual no quadro eh lido aqui

        if (k>31){ //se for maior que 31, significa que estamos no segundo valor de 32 bits no quadro
          //valor eh atualizado para o proximo valor de quadroManchester, representando os proximos dois bits codificado
          valor = quadroManchester[decodPosition + 1];
        }
        if(deslocBit == 32){ //verifica se atingiu 32, reiniciando para a posicao 0 mais proxima
          deslocBit = 0;
        }

        //atualiza o valor do quadro usando operacoes bitwise
        finalDecod[quadroIndex] |= (((valor >> k) & 1) << deslocBit);
        deslocBit++;
      }
      decodPosition += 2;
    }
    return finalDecod;
  }// fim do metodo CamadaFisicaTransmissoraDecodificacaoManchester()
}
