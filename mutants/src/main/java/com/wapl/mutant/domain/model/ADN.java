package com.wapl.mutant.domain.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ADN {
  public static final int LENGTHADN = 4;
  private List<String> adnChar;
  private Integer columnLimit;
  private Integer rowLimit;
  private static Predicate<List<String>> validADN = adn ->{
    Comparator<String> comparator = Comparator.comparing(String::length);
    BinaryOperator<String> maxLenght = BinaryOperator.maxBy(comparator);
    BinaryOperator<String> minLenght = BinaryOperator.minBy(comparator);
    BiPredicate<String, String> equalsLength = (min, max) -> min.length() == max.length();
    
    Optional<String> maxLength = adn.stream().reduce(maxLenght);
    Optional<String> minLength = adn.stream().reduce(minLenght);
    return maxLength.get().length() - LENGTHADN >= 0
        && adn.size() - LENGTHADN >= 0 && equalsLength.test(minLength.get(), maxLength.get());
  };

  public static final Function<List<String>, ADN> loadADN = (List<String> adn) -> {
    if (!validADN.test(adn)) throw new RuntimeException();
    return ADN.builder().adnChar(adn).columnLimit(adn.get(0).length() - LENGTHADN)
        .rowLimit(adn.size() - LENGTHADN).build();
  };

  public static final BiFunction<Coordinate, List<String>, List<String>> getSubADN =
      (Coordinate coordinate, List<String> adn) -> {
        List<String> subADN = new ArrayList<>();
        for (Integer rowActualSubADN = 0; rowActualSubADN < LENGTHADN; rowActualSubADN++) {
          subADN.add(rowActualSubADN, adn.get(coordinate.getRow() + rowActualSubADN)
              .substring(coordinate.getColumn(), coordinate.getColumn() + LENGTHADN));
        }
        return subADN;
      };

}
