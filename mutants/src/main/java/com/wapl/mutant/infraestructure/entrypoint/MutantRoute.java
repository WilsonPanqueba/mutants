package com.wapl.mutant.infraestructure.entrypoint;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MutantRoute {
  /**
   * Valida si una cadena de ADN es mutante
   * 
   * @param request Solicitud cuyo body es una estructura DnaRequest
   * @return Si es mutante retorna status OK, si no es mutante retorna FORBIDDEN y si hay error en los datos retorna  BADREQUEST
   * @see IMutantHandler
   */
  @Bean
  public RouterFunction<ServerResponse> routes(IMutantHandler handler){
      return RouterFunctions
          .route(POST("mutant"), handler::isMutant)
          .andRoute(GET("health"), handler::health)
          .andRoute(GET("stats"), handler::stats);
  }

}
