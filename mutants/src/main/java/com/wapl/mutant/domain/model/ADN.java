package com.wapl.mutant.domain.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

public class ADN{

  private List<String> structure;

  /**
   * Crea un objeto ADN a partir de una estructura dada
   * 
   * @param structureADN estructura completa de ADN
   */
  protected ADN(List<String> structureADN) {
    setStructure(structureADN);
  }

  /**
   * Valida la estructura de ADN y si es valida la asigna al objeto ADN
   * 
   * @param structureADN estructura completa de ADN
   * @throws RuntimeException si la estructura de ADN no es valida
   */
  private void setStructure(List<String> structureADN) {
    Predicate<List<String>> validStructureADN = structureAdn -> {
      Integer maxLength = structureAdn.stream()
          .reduce(BinaryOperator.maxBy(Comparator.comparing(String::length))).get().length();
      Integer minLength = structureAdn.stream()
          .reduce(BinaryOperator.minBy(Comparator.comparing(String::length))).get().length();
      Predicate<String> validaTuple = (String adnList) -> adnList.matches("^[ATCG]*$");
      return minLength.equals(maxLength) && structureAdn.stream().allMatch(validaTuple);
    };

    if (!validStructureADN.test(structureADN))
      throw new RuntimeException();
    this.structure = structureADN;
  }

  /**
   * Generacion de coordenadas para generar secciones de ADN
   * 
   * @param lengthSubADN tamaño de la matriz a generar
   * @return Listado de coordenadas desde las que se generaran las secciones de ADN
   * @throws RuntimeException si la estructura de ADN no es valida
   * @see Coordinate#getCoordinates
   */
  protected List<Coordinate> getCoordinates(Integer lengthSubADN) {
    if (!validCoordinate.test(lengthSubADN))
      throw new RuntimeException();
    return getCoordinates.apply(structure.size() - lengthSubADN,
        structure.get(0).length() - lengthSubADN);
  }
  
  /**
   * Generacion de coordenadas para generar secciones de ADN
   * 
   * @param lengthSubADN tamaño de la matriz a generar
   * @return Listado de coordenadas desde las que se generaran las secciones de ADN
   */
  private final BiFunction<Integer, Integer, List<Coordinate>> getCoordinates = (Integer rowLimit, Integer columnLimit)->{
    List<Coordinate> coordinates = new ArrayList<>();
    for (Integer rowActual = 0; rowActual <= rowLimit; rowActual++) {
        for (Integer columnActual = 0; columnActual <= columnLimit; columnActual++) {
            coordinates.add(Coordinate.builder().row(rowActual).column(columnActual).build());
        }
    }
    return coordinates;
};
  
  /**
   * Valida la posibilidad de generar secciones de ADN
   * 
   * @param lengthSubADN tamaño de las secciones de ADN
   * @return Si es posible generar las coordenadas retorna TRUE si no retorna FALSE
   */
  protected final IntPredicate validCoordinate = lengthSubADN -> {
    Integer maxLength = structure.stream()
        .reduce(BinaryOperator.maxBy(Comparator.comparing(String::length))).get().length();
    return maxLength - lengthSubADN >= 0 && structure.size() - lengthSubADN >= 0;
  };

  /**
   * Genera una secciones de ADN segun las coordenadas dadas
   * 
   * @param coordinate coordenada Superior Izquierda desde donde se generara la seccion de ADN
   * @param lengthSubADN tamaño de las secciones de ADN
   * @return Seccion de ADN
   */
  protected final BiFunction<Coordinate, Integer, List<String>> getSubADN =
      (coordinate, lengthSubADN) -> {
        List<String> subADN = new ArrayList<>();
        for (Integer rowActualSubADN = 0; rowActualSubADN < lengthSubADN; rowActualSubADN++) {
          subADN.add(rowActualSubADN, structure.get(coordinate.getRow() + rowActualSubADN)
              .substring(coordinate.getColumn(), coordinate.getColumn() + lengthSubADN));
        }
        return subADN;
      };



}
