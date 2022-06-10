package com.wapl.mutant.infraestructure.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class AdnStatsTest {
  
  @ParameterizedTest
  @CsvSource({"0,0"})
  void getRatioNoTest(float human, float mutant) {
    AdnStats adnStats = new AdnStats();
    adnStats.setCountHumanDna(human);
    adnStats.setCountMutantDna(mutant);
    assertEquals(0, adnStats.getRatio());
  }
  
  @ParameterizedTest
  @CsvSource({"100,0"})
  void getRatioNoMutants(float human, float mutant) {
    AdnStats adnStats = new AdnStats();
    adnStats.setCountHumanDna(human);
    adnStats.setCountMutantDna(mutant);
    assertEquals(0, adnStats.getRatio());
  }
  
  @ParameterizedTest
  @CsvSource({"0,100"})
  void getRatioNoHumans(float human, float mutant) {
    AdnStats adnStats = new AdnStats();
    adnStats.setCountHumanDna(human);
    adnStats.setCountMutantDna(mutant);
    assertEquals(1, adnStats.getRatio());
  }
  
  @ParameterizedTest
  @CsvSource({"40,100","100,200","200,100", "3,5"})
  void getRatioMutantsAndHumans(float human, float mutant) {
    AdnStats adnStats = new AdnStats();
    adnStats.setCountHumanDna(human);
    adnStats.setCountMutantDna(mutant);
    assertEquals(mutant/human, adnStats.getRatio());
  }

}
