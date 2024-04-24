package model;
import controller.*;

public class CamadaFisicaTransmissora {
  StringBuilder signalManchester; //Sinais da codificacao bit a bit Manchester Diferencial
  MeioComunicacao mC = new MeioComunicacao();

  void camadaFisicaTransmissora (int quadro[], MainControl mainControl) {
    int tipoDeCodificacao = mainControl.getCodificacao(); //alterar de acordo o teste
    int fluxoBrutoDeBits[] = quadro; //ATENÇÃO: trabalhar com BITS!!!
    
    switch (tipoDeCodificacao) {
    case 0 : //codificao binaria
      fluxoBrutoDeBits = CamadaFisicaTransmissoraCodificacaoBinaria(quadro);
    break;
    case 1 : //codificacao manchester
      fluxoBrutoDeBits = CamadaFisicaTransmissoraCodificacaoManchester(quadro);
    break;
    case 2 : //codificacao manchester diferencial
      fluxoBrutoDeBits = CamadaFisicaTransmissoraCodificacaoManchesterDiferencial(quadro);
    break;
    }//fim do switch/case
    mC.MeioDeComunicacao(fluxoBrutoDeBits, mainControl);
    }//fim do metodo CamadaFisicaTransmissora
        
    public int[] CamadaFisicaTransmissoraCodificacaoBinaria (int quadro []) {
      return quadro;
    }//fim do metodo CamadaFisicaTransmissoraCodificacaoBinaria 

    /********************************************************************
  * Metodo: CamadaFisicaTransmissoraCodificacaoManchester()
  * Funcao: codifica os bits do quadro em codificacao Manchester
  * Parametros: quadro de bits
  * Retorno: array de inteiro com os bits manchester
  ****************************************************************** */
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
  * Metodo: CamadaFisicaTransmissoraCodificacaoManchesterDiferencial()
  * Funcao: codifica os bits do quadro em codificacao Manchester Diferencial
  * Parametros: quadro de bits
  * Retorno: array de inteiro com os bits manchester diferencial
  ****************************************************************** */
  public int[] CamadaFisicaTransmissoraCodificacaoManchesterDiferencial(int[] quadro) {
    signalManchester = new StringBuilder(); //Estrutura do sinal
    int[] differentialManchester = new int[quadro.length * 2];//Cria array com o dobro do tamanho, ja que armazenara o bit e sua codificacao corespondente
    int index = 0; //indice para manchesterEncode
    int desloc = 0; //Varivael que ira auxiliar no deslocamento e insercao de bits
    int encodedBit = 0;//Armazena o valor do bit anterior, para auxiliar na codificacao Manchester Diferencial, inicia Alto Baixo
    
    //Percorre cada indice de array do quadro
    for (int i = 0; i < quadro.length; i++) {
      int currentBit;//Variavel para armazenar o bit atual
      int value = quadro[i]; // armazena o conteudo do quadro no indice quadroIndex
      //Cada iteracao, codifica 1 index do quadro preenchendo 2 index do manchester, pois codificado cada indice so pode armazenar 2 letras
      for (int k = 0; k < 32; k++) {
        /*Quando o deslocamento for 32 o indice da codificacao esta completamente preenchido 
        entao passa para o proximo indice e retorna o auxiliar de deslocamento para 0*/
        if (desloc == 32) {
          index++;
          desloc = 0;
        }
        currentBit = (value >> k) & 1; //Realiza o deslocamento a direita em k posicoes e captura o bit, por meio do operador AND
        if (currentBit == 1 || currentBit == -1){//Em bit = 1 ocorre a alteracao no bit de codificacao
          if(currentBit == encodedBit){
            differentialManchester[index] = encode(differentialManchester[index], desloc, 0, 1);//Codifica 01
            signalManchester.append(01);
            encodedBit = 0;//Atualiza o bit de codificacao
          } else {
            differentialManchester[index] = encode(differentialManchester[index], desloc, 1, 0);//Codifica 10
            signalManchester.append(10);//Adiciona a codificacao ao sinal
            encodedBit = 1;//Atualiza o bit de codificacao
          }
        }
        else{
          differentialManchester[index] |= (encodedBit == 1) ? encode(differentialManchester[index], desloc, 1, 0) : encode(differentialManchester[index], desloc, 0, 1);
          signalManchester = (currentBit==1) ? signalManchester.append(10) : signalManchester.append(01); //Adiciona a codificacao ao sinal
        }
        desloc += 2; //Incrementa o deslocamento em 2, ja que 2 posicoes ja foram inseridas com a codificacao
      }
    }
    signalManchester = signalManchester.reverse();

    return differentialManchester;
  }

  public int encode(int index, int desloc, int bit, int encode){
    //Realiza o deslocamento a esquerda em deslocation posicoes
    index |= (bit << desloc); 
    //Realiza o deslocamento a esquerda em deslocation + 1 posisoes
    index |= (encode << desloc + 1); 
    return index;
  }
}
