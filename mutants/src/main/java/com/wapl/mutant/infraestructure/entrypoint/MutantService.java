package com.wapl.mutant.infraestructure.entrypoint;

import java.util.function.Function;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.wapl.mutant.infraestructure.helper.DnaRequest;
import com.wapl.mutant.usecases.Mutants;
import reactor.core.publisher.Mono;

@Service
public class MutantService implements IMutantService {
  private static final Function<Boolean, Mono<ServerResponse>> returnServerResponse =
      (Boolean validation) -> Boolean.TRUE.equals(validation) ? ServerResponse.ok().build()
          : ServerResponse.status(HttpStatus.FORBIDDEN).build();

      private static final Function<Throwable, Mono<ServerResponse>> returnServerResponseError = 
          (Throwable error) -> ServerResponse.badRequest().build();
          
  @Override
  public Mono<ServerResponse> isMutant(ServerRequest request) {
    Mono<DnaRequest> dnaRequestMono = request.bodyToMono(DnaRequest.class);
    return dnaRequestMono.map(dna -> Mutants.isMutant.test(dna.getDna()))
        .flatMap(returnServerResponse)
        .onErrorResume(returnServerResponseError::apply);
        
  }
}
