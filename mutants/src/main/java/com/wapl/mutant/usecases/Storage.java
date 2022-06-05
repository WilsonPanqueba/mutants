package com.wapl.mutant.usecases;

import org.springframework.stereotype.Service;
import com.wapl.mutant.infraestructure.driveradapter.AdnTestRepository;
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
    adnTestRepository.existsById(adnTest.getAdnMD5()).doOnError(Throwable::printStackTrace).subscribe(exist->{
      adnTest.setExist(exist);
      if (!adnTest.isExist())
        adnTestRepository.save(adnTest).doOnError(Throwable::printStackTrace).subscribe();
    });
      

  }

  @Override
  public Mono<AdnStats> getStats() {
    return adnTestRepository.stat();
  }

  @Override
  public Mono<Boolean> getHealth() {
    return adnTestRepository.health();
  }

}
