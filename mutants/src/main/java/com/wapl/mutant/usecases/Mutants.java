
package com.wapl.mutant.usecases;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import org.springframework.stereotype.Service;
import com.wapl.mutant.usecases.model.ADN;

@Service
public class Mutants implements IMutants {
  private static final Integer LENGTHSUBADN = 4;

  private static final List<String> MUTATIONES = Arrays.asList("AAAA", "TTTT", "CCCC", "GGGG");
  /**
   * Compara dos moleculas de ADN
   * 
   * @param MoleculeBase
   * @param MoleculeActual
   * @return Si las moleculas no son iguales retorna TRUE y no es mutante, Si son iguales retorna
   *         FALSE puede ser mutante
   */
  private BiPredicate<String, String> notMutant =
      (MoleculeBase, MoleculeActual) -> !MoleculeBase.equals(MoleculeActual);

  /**
   * Valida la diagonal desde Izquierda Superior a Derecha Inferior en una seccion de ADN de 4x4
   * 
   * @param subADN
   * @return Si la seccion de ADN tiene una secuencia en donde todas las moleculas son iguales en la
   *         diagonal desde Izquierda Superior a Derecha Inferior retorna TRUE indicando que es
   *         mutante Si alguna es diferente retorna FALSE indicando que no es mutante
   */
  private Predicate<List<String>> isMutantDiagonalLR = (List<String> subADN) -> {
    for (Integer filaSubADN = 1; filaSubADN < LENGTHSUBADN; filaSubADN++) {
      if (notMutant.test(String.valueOf(subADN.get(0).charAt(0)),
          String.valueOf(subADN.get(filaSubADN).charAt(filaSubADN))))
        return false;
    }
    return true;
  };

  /**
   * Valida la diagonal desde Derecha Superior a Izquierda Inferior en una seccion de ADN de 4x4
   * 
   * @param subADN
   * @return Si la seccion de ADN tiene una secuencia en donde todas las moleculas son iguales en la
   *         diagonal desde Izquierda Superior a Derecha Inferior retorna TRUE indicando que es
   *         mutante Si alguna es diferente retorna FALSE indicando que no es mutante
   */
  private Predicate<List<String>> isMutantDiagonalRL = (List<String> subADN) -> {

    for (Integer rowActual = 2; rowActual >= 0; rowActual--) {
      if (notMutant.test(String.valueOf(subADN.get(3).charAt(0)),
          String.valueOf(subADN.get(rowActual).charAt(3 - rowActual))))
        return false;

    }
    return true;
  };

  /**
   * Valida las columnas en una seccion de ADN de 4x4
   * 
   * @param subADN
   * @return Si la seccion de ADN tiene una secuencia en donde todas las moleculas son iguales en
   *         una de las columnas retorna TRUE indicando que es mutante Si ninguna de las columnas es
   *         mutante se retorna FALSE indicando que no es mutante
   */
  private Predicate<List<String>> isMutantColumn = (List<String> subADN) -> {
    Integer columnActual = 0;
    do {
      StringBuilder bld = new StringBuilder();
      for (Integer rowActual = 0; rowActual < LENGTHSUBADN; rowActual++) {
        bld.append(subADN.get(rowActual).charAt(columnActual));
      }
      if (MUTATIONES.contains(bld.toString()))
        return Boolean.TRUE;
      columnActual++;
    } while (columnActual < LENGTHSUBADN);
    return Boolean.FALSE;
  };

  /**
   * Valida las filas en una seccion de ADN de 4x4
   * 
   * @param subADN
   * @return Si la seccion de ADN tiene una secuencia en donde todas las moleculas son iguales en
   *         una de las filas retorna TRUE indicando que es mutante Si ninguna de las filas es
   *         mutante se retorna FALSE indicando que no es mutante
   */
  private Predicate<List<String>> isMutantRow = (List<String> subADN) -> {
    int rowActual = 0;
    do {
      Boolean testMutant = null;
      for (Integer columnActual = 1; columnActual < LENGTHSUBADN
          && testMutant == null; columnActual++) {
        if (notMutant.test(String.valueOf(subADN.get(rowActual).charAt(0)),
            String.valueOf(subADN.get(rowActual).charAt(columnActual)))) {
          testMutant = false;
        }
      }
      if (testMutant == null)
        return true;
      rowActual++;
    } while (rowActual < LENGTHSUBADN);
    return false;
  };

  /**
   * Valida una seccion de ADN de 4x4
   * 
   * @param subADN
   * @return Valida una seccion de ADN por columnas, diagonales y fila, si alguna es TRUE, es
   *         mutante si todas son FALSE no es mutante
   * @see #isMutantRow
   * @see #isMutantDiagonalRL
   * @see #isMutantDiagonalLR
   * @see #isMutantColumn
   */
  private Predicate<List<String>> validADNMutant =
      (List<String> subADN) -> isMutantRow.test(subADN) || isMutantDiagonalLR.test(subADN)
          || isMutantDiagonalRL.test(subADN) || (isMutantColumn.test(subADN));

  /**
   * Valida estructura de ADN si es mutante
   * 
   * @param structureADN estructura complleta de ADN
   * @return Valida una seccion de ADN por columna, fila y diagonales, si alguna es TRUE, es mutante
   *         si todas son FALSE no es mutante
   * @see Mutants#getCoordinates(Integer)
   */
  @Override
  public final Boolean validMutation(List<String> structureADN) {
    ADN adn = new ADN(structureADN);
    return adn.subADNs.apply(LENGTHSUBADN).anyMatch(validADNMutant);
  }
}
