package model;

public class Manchester {
    public int[] CamadaFisicaTransmissoraCodificacaoManchester (int quadro[]){
        int[] codificacaoManchester = new int[quadro.length*2];
        for (int i = 0, j = 0; i < quadro.length; i++) {
          if(quadro[i] == 0){
            codificacaoManchester[j] = 0;
            codificacaoManchester[j+1] = 1;
    
          }else{
            codificacaoManchester[j] = 1;
            codificacaoManchester[j+1] = 0;
          }
          j+=2;
        }//Fim do for
        System.out.print("\nManchester: ");
        for (int z = 0; z<codificacaoManchester.length;z++){
          System.out.print(codificacaoManchester[z]);
        }
        return codificacaoManchester;
    }//fim do metodo CamadaFisicaTransmissoraCodificacaoManchester
 
    public int[] CamadaFisicaReceptoraDecodificacaoManchester(int quadro[]){
    int[] manchester = new int[quadro.length/2];
    for (int i = 0, j = 0; i < quadro.length; i+=2) {
      if(quadro[i] == 0 && quadro[i+1] == 1){
      manchester[j] = 0;
      }
      if(quadro[i] == 1 && quadro[i+1] == 0){
        manchester[j] = 1;
      }
    j++;
    }
    return manchester;
  }
}
