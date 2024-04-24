/* ***************************************************************
* Autor............: Antonio Vinicius Silva Dutra
* Matricula........: 202110810
* Inicio...........: 31/03/2024
* Ultima alteracao.: 07/04/2024
* Nome.............: CamadaAplicacaoTransmissora.java
* Funcao...........: converte a mensagem digitada em ASCII e depois para binario.
  Por fim atribui ao array quadro[], utilizando uma mascara, o valor dos binarios
*************************************************************** */
package model;
import controller.*;

public class CamadaAplicacaoTransmissora {
    CamadaFisicaTransmissora cF = new CamadaFisicaTransmissora();

    void camadaDeAplicacaoTransmissora (String mensagem, MainControl mainControl) {
        char[] chars = mensagem.toCharArray();
        int[] quadro;
        String[] binary = new String[chars.length];
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
          //ascii = valorASCII;
    
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
        }
    
        cF.camadaFisicaTransmissora(quadro, mainControl);
      }//fim do metodo CamadaDeAplicacaoTransmissora
}
