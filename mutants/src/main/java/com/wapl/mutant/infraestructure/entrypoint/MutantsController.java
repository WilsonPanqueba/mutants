package com.wapl.mutant.infraestructure.entrypoint;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MutantsController {
  @Bean
  public RouterFunction<ServerResponse> routes(IMutantService handler){
      return RouterFunctions.route(POST("mutant"), handler::isMutant);
  }

}
