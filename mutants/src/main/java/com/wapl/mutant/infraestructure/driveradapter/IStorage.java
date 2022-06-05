package com.wapl.mutant.infraestructure.driveradapter;

import com.wapl.mutant.infraestructure.helper.AdnStats;
import com.wapl.mutant.infraestructure.helper.AdnTest;
import reactor.core.publisher.Mono;

public interface IStorage {
  void saveAdnTest(AdnTest adnTest);
  Mono<AdnStats> getStats();
}
