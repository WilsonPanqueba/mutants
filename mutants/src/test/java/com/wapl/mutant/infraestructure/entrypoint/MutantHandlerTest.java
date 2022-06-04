package com.wapl.mutant.infraestructure.entrypoint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.wapl.mutant.infraestructure.helper.DnaRequest;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class MutantHandlerTest {
  @InjectMocks
  MutantHandler mutantService;
  @Mock
  ServerRequest request;
  
  @ParameterizedTest
  @ValueSource(strings = {"ATAAAG,CAGA,TTAAX","ATAAAG,CAGA,TTAAA", "ATG,CAG,TTA","ATGG,CAGG,TTAA","ATG,CAG,TTA,TTA"})
  void mutantsBadRequest(String values){
    DnaRequest dnaRequest= new DnaRequest();
    dnaRequest.setDna(List.of(values.split(",")));
        when(request.bodyToMono(DnaRequest.class)).thenReturn(Mono.just(dnaRequest));
        Mono<ServerResponse> result = mutantService.isMutant(request);
        result.subscribe(response -> {
          assertEquals(HttpStatus.BAD_REQUEST, response.statusCode());
        });
  }
  
  @ParameterizedTest
  @ValueSource(strings = {"ATGCGA,CAGTGC,TTATGT,AGAAGG,CCCCTA,TCACTG,CCCCTA","AAAA,AGCT,GCTA,CTAG","AGCT,AAAA,GCTA,CTAG","AGCT,GCTA,AAAA,CTAG","AGCT,GCTA,CTAG,AAAA","AGCT,ACTG,ATGC,AGCT","GACT,CATG,TAGC,GACT","GCAT,CTAG,TGAC,GCAT","GCTA,CTGA,TGCA,GCTA","GCTA,CTAG,TAGC,AGCT","AGCT,CATG,TGAC,GCTA", "ATGCAT,GCATGC,ATGCAT,GCATGC,ATGCAC,GCATCC,ATGCAT,GCCTGC"})
  void mutantsTrue(String values){
    DnaRequest dnaRequest= new DnaRequest();
    dnaRequest.setDna(List.of(values.split(",")));
        when(request.bodyToMono(DnaRequest.class)).thenReturn(Mono.just(dnaRequest));
        Mono<ServerResponse> result = mutantService.isMutant(request);
        result.subscribe(response -> {
          assertEquals(HttpStatus.OK, response.statusCode());
        });
  }
  
  @ParameterizedTest
  @ValueSource(strings = {"CTAG,AGCT,GCTA,CTAG", "ATGCAT,GCATGC,ATGCAT,GCATGC,ATGCAC,GCATAC,ATGCAT,GCCTGC"})
  void mutantsFalse(String values){
    DnaRequest dnaRequest= new DnaRequest();
    dnaRequest.setDna(List.of(values.split(",")));
        when(request.bodyToMono(DnaRequest.class)).thenReturn(Mono.just(dnaRequest));
        Mono<ServerResponse> result = mutantService.isMutant(request);
        result.subscribe(response -> {
          assertEquals(HttpStatus.FORBIDDEN, response.statusCode());
        });
  }
  
  @Test
  void mutantshealth(){
        Mono<ServerResponse> result = mutantService.health(request);
        result.subscribe(response -> {
          assertEquals(HttpStatus.OK, response.statusCode());
        });
  }
  
}
