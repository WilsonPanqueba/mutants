package com.wapl.mutant.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ADN {
  public static final int LENGTHADN = 4;
  private List<String> adnChar;
  private Integer columnLimit;
  private Integer rowLimit;

  public static final Function<List<String>, ADN> loadADN = (List<String> adn) -> {
    if (adn.get(0).length() - LENGTHADN < 0 || adn.size() - LENGTHADN < 0)
      throw new RuntimeException();
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
