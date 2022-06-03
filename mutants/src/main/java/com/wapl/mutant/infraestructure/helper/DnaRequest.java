package com.wapl.mutant.infraestructure.helper;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DnaRequest  {
  private List<String> dna = new ArrayList<>();
}
