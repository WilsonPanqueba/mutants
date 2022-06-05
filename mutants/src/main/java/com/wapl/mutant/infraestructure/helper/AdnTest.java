package com.wapl.mutant.infraestructure.helper;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class AdnTest implements Persistable<String> {

  @Id
  private String adnMD5;
  private boolean isMutant;
  @Transient
  private boolean exist;

  public static final AdnTest builder() {
    return new AdnTest();
  }
  
  public void setExist(boolean exist) {
    this.exist=exist;
  }

  private AdnTest() {}
  
  public AdnTest adn(@NonNull List<String> adnStructure) {
    String cadena="";
    cadena = adnStructure.parallelStream().reduce(cadena,
        (subcadena1, subcadena2) -> subcadena1.concat(subcadena2));
    MessageDigest messageDigest;
    try {
      messageDigest = MessageDigest.getInstance("MD5");
      messageDigest.update(cadena.getBytes(), 0, cadena.length());
      this.adnMD5 = new BigInteger(1, messageDigest.digest()).toString(16);
    } catch (NoSuchAlgorithmException e) {
      this.adnMD5=cadena;
    }
    return this;
  }
  public AdnTest mutant(@NonNull Boolean isMutant) {
    this.isMutant = isMutant;
    return this;
  }

  public AdnTest build() {
    return this;
  }

  @Override
  public String getId() {
    return adnMD5;
  }

  @Override
  public boolean isNew() {
    return !exist;
  }

}
