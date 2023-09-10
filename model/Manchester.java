package model;

public class Manchester {
  public int[] CamadaFisicaTransmissoraCodificacaoManchester(int[] quadro) {
    int[] manchesterSignal = new int[quadro.length * 2];
    int deslocIndex = 0;  
    int bitDesloc = 0;

    for (int valor : quadro) {
      for (int k = 0; k < 32; k++) {
        if (bitDesloc == 32) {
          deslocIndex++;
          bitDesloc = 0;
        }

        manchesterSignal[deslocIndex] |= (((((valor >> k) & 1) == 0) ? 0 : 1) << bitDesloc);
        manchesterSignal[deslocIndex] |= (((((valor >> k) & 1) == 0) ? 1 : 0) << (bitDesloc + 1));

        bitDesloc += 2;
      }
    }
    return manchesterSignal;
  }

  public int[] CamadaFisicaReceptoraDecodificacaoManchester(int[] quadroManchester) {
    int[] finalDecod = new int[quadroManchester.length / 2];
    int decodPosition = 0; // indice para quadroDecodificado
    int deslocBit = 0; // inicialize o bit anterior como 0

    for (int quadroIndex = 0; quadroIndex < finalDecod.length; quadroIndex++) {
      for (int k = 0; k < 64; k += 2) { 
        int valor = quadroManchester[decodPosition];

        if (k>31){
          valor = quadroManchester[decodPosition + 1];
        }
        if(deslocBit == 32){
          deslocBit = 0;
        }

        finalDecod[quadroIndex] |= (((valor >> k) & 1) << deslocBit);
        deslocBit++;
      }

      decodPosition += 2;
    }
    return finalDecod;
  }
}
