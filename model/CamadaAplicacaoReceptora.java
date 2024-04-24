package model;
import controller.*;

public class CamadaAplicacaoReceptora {
    AplicacaoReceptora aP = new AplicacaoReceptora();
  /* ***************************************************************
  * Metodo: CamadaDeAplicacaoReceptora
  * Funcao: chama metodos para converter o quadro com os bits de volta para a mensagem de texto
  * Parametros: int quadro[] = quadro com os bits
  * Retorno: void
  *************************************************************** */
  void camadaDeAplicacaoReceptora(int quadro[], MainControl mainControl) {
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
    aP.aplicacaoReceptora(mensagem, mainControl);
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

}

