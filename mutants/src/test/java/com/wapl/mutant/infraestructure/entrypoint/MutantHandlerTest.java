package com.wapl.mutant.infraestructure.entrypoint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.wapl.mutant.infraestructure.helper.AdnStats;
import com.wapl.mutant.infraestructure.helper.DnaRequest;
import com.wapl.mutant.usecases.IMutants;
import com.wapl.mutant.usecases.IStorage;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class MutantHandlerTest {
  @InjectMocks
  MutantHandler mutantService;
  @Mock
  IMutants mutants;
  @Mock
  ServerRequest request;
  @Mock
  IStorage storage;

  @ParameterizedTest
  @ValueSource(strings = {"ATAAAG,CAGA,TTAAX"})
  void mutantsBadRequest(String values) {
    DnaRequest dnaRequest = new DnaRequest();
    dnaRequest.setDna(List.of(values.split(",")));
    when(request.bodyToMono(DnaRequest.class)).thenReturn(Mono.just(dnaRequest));
    when(mutants.validMutation(any())).thenThrow(RuntimeException.class);
    Mono<ServerResponse> result = mutantService.isMutant(request);
    result.subscribe(response -> {
      assertEquals(HttpStatus.BAD_REQUEST.toString().toUpperCase(),
          response.statusCode().toString().toUpperCase());
    });
    verify(storage, times(0)).saveAdnTest(any());
  }

  @ParameterizedTest
  @ValueSource(strings = {"ATGCGA,CAGTGC,TTATGT,AGAAGG,CCCCTA,TCACTG,CCCCTA"})
  void mutantsTrue(String values) {
    DnaRequest dnaRequest = new DnaRequest();
    dnaRequest.setDna(List.of(values.split(",")));
    when(request.bodyToMono(DnaRequest.class)).thenReturn(Mono.just(dnaRequest));
    when(mutants.validMutation(any())).thenReturn(true);
    doNothing().when(storage).saveAdnTest(any());
    Mono<ServerResponse> result = mutantService.isMutant(request);
    result.subscribe(response -> {
      assertEquals(HttpStatus.OK.toString().toUpperCase(),
          response.statusCode().toString().toUpperCase());
    });
    verify(storage, times(1)).saveAdnTest(any());
  }

  @ParameterizedTest
  @ValueSource(strings = {"CTAG,AGCT,GCTA,CTAG"})
  void mutantsFalse(String values) {
    DnaRequest dnaRequest = new DnaRequest();
    dnaRequest.setDna(List.of(values.split(",")));
    when(request.bodyToMono(DnaRequest.class)).thenReturn(Mono.just(dnaRequest));
    when(mutants.validMutation(any())).thenReturn(false);
    doNothing().when(storage).saveAdnTest(any());
    Mono<ServerResponse> result = mutantService.isMutant(request);
    result.subscribe(response -> {
      assertEquals(HttpStatus.FORBIDDEN.toString().toUpperCase(),
          response.statusCode().toString().toUpperCase());
    });
    verify(storage, times(1)).saveAdnTest(any());
  }

  @Test
  void mutantshealth() {
    when(storage.getHealth()).thenReturn(Mono.just(Boolean.TRUE));
    Mono<ServerResponse> result = mutantService.health(request);
    result.subscribe(response -> {
      assertEquals(HttpStatus.OK.toString().toUpperCase(),
          response.statusCode().toString().toUpperCase());
    });
  }

  @Test
  void statsOK() {
    AdnStats adnStats = new AdnStats();
    adnStats.setCountHumanDna(1);
    adnStats.setCountMutantDna(1);
    when(storage.getStats()).thenReturn(Mono.just(adnStats));
    Mono<ServerResponse> result = mutantService.stats(request);
    result.subscribe(response -> {
      assertEquals(HttpStatus.OK, response.statusCode());
      assertTrue(response.headers().containsKey(HttpHeaders.CONTENT_TYPE));
      assertEquals(MediaType.APPLICATION_JSON_VALUE.toString().toUpperCase(),
          response.headers().getContentType().toString().toUpperCase());
    });
  }

}
