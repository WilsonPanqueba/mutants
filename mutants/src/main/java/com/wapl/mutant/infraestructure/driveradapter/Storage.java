package com.wapl.mutant.infraestructure.driveradapter;

import org.springframework.stereotype.Service;
import com.wapl.mutant.infraestructure.helper.AdnStats;
import com.wapl.mutant.infraestructure.helper.AdnTest;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class Storage implements IStorage {
  AdnTestRepository adnTestRepository;

  @Override
  public void saveAdnTest(AdnTest adnTest) {
    adnTestRepository.save(adnTest);

  }

  @Override
  public Mono<AdnStats> getStats() {
    return adnTestRepository.stat();
  }

}
