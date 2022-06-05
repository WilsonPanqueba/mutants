package com.wapl.mutant.infraestructure.driveradapter;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import com.wapl.mutant.infraestructure.helper.AdnStats;
import com.wapl.mutant.infraestructure.helper.AdnTest;
import reactor.core.publisher.Mono;
 
@Repository
public interface AdnTestRepository extends ReactiveCrudRepository<AdnTest, String> {
  @Query("select count_mutant_dna, count_human_dna from "
      + "(select count(adn_md5) as count_mutant_dna from adn_test where is_mutant=true) t1, "
      + "(select count(adn_md5) as count_human_dna from adn_test where is_mutant=false) t2;")
  Mono<AdnStats> stat();
  @Query("select true;")
  Mono<Boolean> health();
}