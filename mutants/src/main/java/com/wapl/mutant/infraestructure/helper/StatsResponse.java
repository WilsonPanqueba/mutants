package com.wapl.mutant.infraestructure.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StatsResponse  {
  @JsonProperty("count_mutant_dna")
  private String countMutantDna;
  @JsonProperty("count_human_dna")
  private String countHumanDna;
  @JsonProperty("ratio")
  private String ratio;
}
