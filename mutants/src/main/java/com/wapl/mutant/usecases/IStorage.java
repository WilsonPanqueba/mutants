package com.wapl.mutant.usecases;

import com.wapl.mutant.infraestructure.helper.AdnStats;
import com.wapl.mutant.infraestructure.helper.AdnTest;
import reactor.core.publisher.Mono;

public interface IStorage {
  void saveAdnTest(AdnTest adnTest);
  Mono<AdnStats> getStats();
  Mono<Boolean> getHealth();
}
