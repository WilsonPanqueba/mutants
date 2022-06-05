package com.wapl.mutant.infraestructure.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AdnStats {
  private Integer countMutantDna;
  private Integer countHumanDna;
  public Integer getRatio() {
    return countMutantDna!=0?countHumanDna/countMutantDna:countMutantDna;
  }
}
