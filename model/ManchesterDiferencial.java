package model;

public class ManchesterDiferencial {
  /* ***************************************************************
* Metodo: CamadaFisicaTransmissoraCodificacaoManchesterDiferencial
* Funcao: Codificar o fluxo de bits em manchester diferencial
* Parametros: int[] quadro = fluxoDeBits
* Retorno: int = fluxoDeBits em manchester diferencial
*************************************************************** */
public int[] CamadaFisicaTransmissoraCodificacaoManchesterDiferencial (int quadro[]){
    //graphicHBox.
    int[] codificacaoManchesterDiferencial = new int[quadro.length * 2];
    for (int i = 0, j = 0; i < quadro.length; i++) {
      // Caso inicial, baixo-alto
      if (i == 0) {
        if (quadro[i] == 0) {
          codificacaoManchesterDiferencial[j] = 0;
          codificacaoManchesterDiferencial[j + 1] = 1;
          //show(codificacaoManchesterDiferencial, graphicHBox);
        } else {
          codificacaoManchesterDiferencial[j] = 1;
          codificacaoManchesterDiferencial[j + 1] = 0;
          //show(codificacaoManchesterDiferencial, graphicHBox);
        }
      } else {
        if (quadro[i] == quadro[i - 1]) {
          codificacaoManchesterDiferencial[j] = codificacaoManchesterDiferencial[j - 1];
          codificacaoManchesterDiferencial[j + 1] = codificacaoManchesterDiferencial[j - 2];
          //show(codificacaoManchesterDiferencial, graphicHBox);
        } else {
          codificacaoManchesterDiferencial[j] = codificacaoManchesterDiferencial[j - 2];
          codificacaoManchesterDiferencial[j + 1] = codificacaoManchesterDiferencial[j - 1];
        } 
      }
      j += 2;
    }//Fim do for
    System.out.print("\nManchesterDiferencial: ");
    for (int z = 0; z<codificacaoManchesterDiferencial.length;z++){
      System.out.print(codificacaoManchesterDiferencial[z]);
    }
    return codificacaoManchesterDiferencial;
  }//fim do CamadaFisicaTransmissoraCodificacaoManchesterDiferencial

  /* ***************************************************************
* Metodo: CamadaFisicaReceptoraDecodificacaoManchesterDiferencial
* Funcao: Decodificar o fluxoDeBits que se encontra em manchester diferencial
* Parametros: int[] quadro = fluxoDeManchesterDiferencial
* Retorno: int = fluxoDeBits em bits
*************************************************************** */
public int[] CamadaFisicaReceptoraDecodificacaoManchesterDiferencial(int quadro[]){
    int[] decodificacaoManchesterDiferencial = new int[quadro.length / 2];
    for (int i = 0, j = 0; i < quadro.length; i += 2) {
      if (i == 0) {
        if (quadro[i] == 0 && quadro[i + 1] == 1) {
          decodificacaoManchesterDiferencial[0] = 0;
        }
        if (quadro[i] == 1 && quadro[i + 1] == 0) {
          decodificacaoManchesterDiferencial[0] = 1;
        }
      } else {
        if (quadro[i] == quadro[i - 1]) {
          decodificacaoManchesterDiferencial[j] = decodificacaoManchesterDiferencial[j - 1];
        } else {
          if (decodificacaoManchesterDiferencial[j - 1] == 1) {
            decodificacaoManchesterDiferencial[j] = 0;
          } else {
            decodificacaoManchesterDiferencial[j] = 1;
          }
        }
      }
      j++;
    }
  return decodificacaoManchesterDiferencial;
  }//fim do CamadaFisicaReceptoraDecodificacaoManchesterDiferencial
}
