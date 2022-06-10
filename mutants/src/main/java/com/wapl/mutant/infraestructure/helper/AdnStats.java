package com.wapl.mutant.infraestructure.helper;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import lombok.Data;

@Data
public class AdnStats  {
  @Id
  private float countMutantDna;
  private float countHumanDna;
  @Transient
  public float getRatio() {
    float respuesta = countHumanDna==0?1:countMutantDna/countHumanDna;
    respuesta = countMutantDna==0?0:respuesta;
    return respuesta;
  }
}
