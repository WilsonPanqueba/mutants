package com.wapl.mutant.infraestructure.entrypoint;

import java.util.function.Function;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.wapl.mutant.infraestructure.driveradapter.IStorage;
import com.wapl.mutant.infraestructure.helper.AdnTest;
import com.wapl.mutant.infraestructure.helper.DnaRequest;
import com.wapl.mutant.infraestructure.helper.StatsResponse;
import com.wapl.mutant.usecases.IMutants;
import com.wapl.mutant.usecases.Mutants;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class MutantHandler implements IMutantHandler {
  private IMutants mutants;
  private IStorage storage;
  /**
   * Genera respuesta en caso de exception
   * 
   * @param validation Si es mutante obtiene TRUE y responde status OK, si no es mutante obtiene
   *        FALSE y retorna status FORBIDDEN
   * @return responde status OK o FORBIDDEN segun el valor de entrada
   */
  private static final Function<Boolean, Mono<ServerResponse>> returnServerResponse =
      (Boolean validation) -> Boolean.TRUE.equals(validation) ? ServerResponse.ok().build()
          : ServerResponse.status(HttpStatus.FORBIDDEN).build();
  /**
   * Genera respuesta en caso de exception
   * 
   * @param error
   * @return Respuesta de BADREQUEST al cliente
   */
  private static final Function<Throwable, Mono<ServerResponse>> returnServerResponseError =
      (Throwable error) -> {
        error.printStackTrace();
        return ServerResponse.badRequest().build();
      };

  /**
   * Valida si una cadena de ADN es mutante
   * 
   * @param request Soicitud cuyo body es una estructura DnaRequest
   * @return Si es mutante retorna status OK, si no es mutante retorna FORBIDDEN y si hay error en
   *         los datos retorna BADREQUEST
   * @see Mutants
   */
  @Override
  public Mono<ServerResponse> isMutant(ServerRequest request) {
    Mono<DnaRequest> dnaRequestMono = request.bodyToMono(DnaRequest.class);
    return dnaRequestMono
        .map(dna -> AdnTest.builder().adn(dna.getDna()).mutant(mutants.validMutation(dna.getDna())))
        .map(dnatest -> {
          storage.saveAdnTest(dnatest);
          return dnatest.isMutant();
        }).flatMap(returnServerResponse).onErrorResume(returnServerResponseError::apply);
  }

  /**
   * Valida el estado del servicio
   * 
   * @param request Solicitud de health
   * @return Si esta habiltado status OK, si no FORBIDDEN
   * @see Mutants
   */
  @Override
  public Mono<ServerResponse> health(ServerRequest request) {
    return ServerResponse.ok().build();
  }

  @Override
  public Mono<ServerResponse> stats(ServerRequest request) {
    storage.getStats()
        .map(adnStats -> StatsResponse.builder().countHumanDna(adnStats.getCountHumanDna())
            .countMutantDna(adnStats.getCountMutantDna()).ratio(adnStats.getRatio()).build());

    return ServerResponse.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .body(BodyInserters.fromValue(storage.getStats()
            .map(adnStats -> StatsResponse.builder().countHumanDna(adnStats.getCountHumanDna())
                .countMutantDna(adnStats.getCountMutantDna()).ratio(adnStats.getRatio()).build())));
  }
}
