package com.wapl.mutant.infraestructure.helper;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;

@Document
@Getter
public class AdnTest {

  @Id
  private String adnMD5;
  private boolean isMutant;

  public static final AdnTest builder() {
    return new AdnTest();
  }

  private AdnTest() {}

  public AdnTest adn(List<String> adnStructure) {

    String cadena = adnStructure.parallelStream().reduce(new String(),
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

  public AdnTest mutant(Boolean isMutant) {
    this.isMutant = isMutant;
    return this;
  }

  public AdnTest build() {
    return this;
  }

}
