package com.wapl.mutant.infraestructure.entrypoint;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


public interface IMutantHandler {

  Mono<ServerResponse> isMutant(ServerRequest request);
  Mono<ServerResponse> health(ServerRequest request);

}
