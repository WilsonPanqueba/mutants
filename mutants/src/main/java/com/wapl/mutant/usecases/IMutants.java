package com.wapl.mutant.usecases;

import java.util.List;

@FunctionalInterface
public interface IMutants {
  public Boolean validMutation  (List<String> structureADN);

}
