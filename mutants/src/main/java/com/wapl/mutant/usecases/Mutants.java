
package com.wapl.mutant.usecases;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import com.wapl.mutant.domain.model.ADN;
import com.wapl.mutant.domain.model.Coordinate;

public class Mutants {
  public static final int LENGTHMUTATION = ADN.LENGTHADN;
  private static final List<String> MUTATIONES = Arrays.asList("AAAA", "TTTT", "CCCC", "GGGG");

  private Mutants() {
    throw new IllegalStateException("Utility class");
  }
  
  private static final BiPredicate<String, String> notMutant =
      (base, actual) -> !base.equals(actual);

  private static final Predicate<List<String>> isMutantDiagonalID = (List<String> subADN) -> {
    for (Integer filaSubADN = 1; filaSubADN < LENGTHMUTATION; filaSubADN++) {
      if (notMutant.test(String.valueOf(subADN.get(0).charAt(0)),
          String.valueOf(subADN.get(filaSubADN).charAt(filaSubADN))))
        return false;
    }
    return true;
  };

  private static final Predicate<List<String>> isMutantDiagonalDI = (List<String> subADN) -> {

    for (Integer rowActual = 2; rowActual >= 0; rowActual--) {
      if (notMutant.test(String.valueOf(subADN.get(3).charAt(0)),
          String.valueOf(subADN.get(rowActual).charAt(3 - rowActual))))
        return false;

    }
    return true;
  };

  private static final Predicate<List<String>> isMutantFila = (List<String> subADN) -> {
    Integer columnActual = 0;
    do {
      StringBuilder bld = new StringBuilder();
      for (Integer rowActual = 0; rowActual < LENGTHMUTATION; rowActual++) {
        bld.append(subADN.get(rowActual).charAt(columnActual));
      }
      if (MUTATIONES.contains(bld.toString()))
        return Boolean.TRUE;
      columnActual++;
    } while (columnActual < LENGTHMUTATION);
    return Boolean.FALSE;
  };

  private static final Predicate<List<String>> isMutantColumna = (List<String> subADN) -> {
    int rowActual = 0;
    do {
      Boolean testMutant = null;
      for (Integer columnActual = 1; columnActual < LENGTHMUTATION
          && testMutant == null; columnActual++) {
        if (notMutant.test(String.valueOf(subADN.get(rowActual).charAt(0)),
            String.valueOf(subADN.get(rowActual).charAt(columnActual)))) {
          testMutant = false;
        }
      }
      if (testMutant == null)
        return true;
      rowActual++;

    } while (rowActual < LENGTHMUTATION);
    return false;
  };

  private static final Predicate<List<String>> validADNMutant =
      (List<String> subADN) -> (isMutantColumna.test(subADN) || isMutantFila.test(subADN)
          || isMutantDiagonalID.test(subADN) || isMutantDiagonalDI.test(subADN));

  public static final Predicate<List<String>> isMutant = (List<String> adnString) -> {
    ADN adn = ADN.loadADN.apply(adnString);
    return Coordinate.getCoordinates
        .apply(adn.getRowLimit(), adn.getColumnLimit())
        .stream()
        .map(coordenate -> ADN.getSubADN.apply(coordenate, adn.getAdnChar()))
        .anyMatch(validADNMutant);
  };
}
