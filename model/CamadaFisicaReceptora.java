package model;
import controller.*;

public class CamadaFisicaReceptora {
  CamadaAplicacaoReceptora cP = new CamadaAplicacaoReceptora();

  /* ***************************************************************
  * Metodo: CamadaFisicaReceptora
  * Funcao: chama os metodos de decodificacao de acordo com a opcao escolhida pelo usuario
  * Parametros: int quadro[] = quadro com os bits
  * Retorno: void
  *************************************************************** */
  void camadaFisicaReceptora (int quadro[], MainControl mainControl) {
    int tipoDeDecodificacao = mainControl.getCodificacao();  
    int fluxoBrutoDeBits [] = quadro;
    switch (tipoDeDecodificacao) {
    case 0 : //decodificao binaria
      fluxoBrutoDeBits = CamadaFisicaReceptoraCodificacaoBinaria(quadro);
    break;
    case 1 : //decodificacao manchester
      fluxoBrutoDeBits = CamadaFisicaReceptoraDecodificacaoManchester(quadro);
    break;
    case 2 : //decodificacao manchester diferencial
      fluxoBrutoDeBits = CamadaFisicaReceptoraDecodificacaoManchesterDiferencial(quadro);
    break;
    }//fim do switch/case

    cP.camadaDeAplicacaoReceptora(fluxoBrutoDeBits, mainControl);
  }//fim do metodo CamadaFisicaTransmissora()   

  public int[] CamadaFisicaReceptoraCodificacaoBinaria (int quadro []) {
    return quadro;
  }//fim do metodo CamadaFisicaReceptoraDecodificacaoBinaria 

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

  /********************************************************************
  * Metodo: CamadaFisicaTransmissoraDecodificacaoManchesterDiferencial()
  * Funcao: decodifica os bits do quadro manchester diferencial em binario novamente
  * Parametros: quadro de bits manchester diferencial
  * Retorno: array de inteiro com os bits decodificados
  ****************************************************************** */
  public int[] CamadaFisicaReceptoraDecodificacaoManchesterDiferencial(int[] quadroDiferencial) {
    int[] descodificacao = new int[quadroDiferencial.length / 2];
    int indexContent; 
    int encodedIndex = 0; //indice para quadroDecodificado
    int desloc = 0; 
    String aux = "10";
    boolean flag = true; //Auxiliar para controlar a troca
    for (int index = 0; index < descodificacao.length; index++) {//Percorre o array decodificado
      for (int k = 0; k < 64 ; k += 2) { 
        indexContent = (k > 31) ?  quadroDiferencial[encodedIndex+1] : quadroDiferencial[encodedIndex];
        if (desloc == 32) {desloc = 0;}

        int currentBit = ((indexContent >> (k)) & 1);
        int bit2 = ((indexContent >> (k+1)) & 1);
        StringBuilder verif = new StringBuilder();
        verif.append(currentBit);
        verif.append(bit2);

        if ((verif.toString().equals(aux))){
          if(flag){
			descodificacao[index] |= decode(descodificacao[index], desloc, 1);// insere o bit 1
			aux = verif.toString();	
            flag = false;
		  }
        }
        else{
		  if(!flag){
			descodificacao[index] |= decode(descodificacao[index], desloc, 1); //insere o bit 1
		    aux = verif.toString();
		  }
        }
        desloc++;
      }
      //Incrementa o indice do array codificado de 2 em 2, ja que no for mais interno, trabalha-se 2 index deste array
      encodedIndex+=2;
    }
    //retorna o array decodificado
    return descodificacao;
  }

  public int decode(int index, int desloc, int bit){
    //Realiza o deslocamento a esquerda em deslocation posisoes, insere o bit e adiciona ao indice atraves do operador | 
    return index |= (bit << desloc);
  }
}
