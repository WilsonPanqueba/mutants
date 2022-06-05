package com.wapl.mutant.infraestructure.driveradapter;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.wapl.mutant.infraestructure.helper.AdnStats;
import com.wapl.mutant.infraestructure.helper.AdnTest;
import reactor.core.publisher.Mono;
 
public interface AdnTestRepository extends ReactiveMongoRepository<AdnTest, String> {
  @Query("select(select count(adnMD5) from Adntest where isMutant=true as countMutantDna,"
      + "        select count(adnMD5) from Adntest where isMutant=false as countHumanDna);")
  Mono<AdnStats> stat();
}