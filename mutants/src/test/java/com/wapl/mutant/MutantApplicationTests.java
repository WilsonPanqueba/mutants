package com.wapl.mutant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.wapl.mutant.infraestructure.entrypoint.MutantHandler;
import com.wapl.mutant.infraestructure.helper.DnaRequest;
import com.wapl.mutant.usecases.Mutants;
import com.wapl.mutant.usecases.Storage;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MutantApplicationTests {
  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;
  @Autowired
  private MutantHandler mutantHandler;
  @Autowired
  private Mutants mutants;
  @Autowired
  private Storage storage;
  @Autowired
  RouterFunction<ServerResponse> mutantRoute;

	@Test
	void contextLoads() {
      assertThat(mutantHandler).isNotNull();
      assertThat(mutantRoute).isNotNull();
      assertThat(mutants).isNotNull();
      assertThat(storage).isNotNull();
	}
	
	@ParameterizedTest
	@NullSource
    void requestBadRequestNull(List<String> values) throws Exception {
      final String baseUrl = "http://localhost:"+port+"/mutant";
      URI uri = new URI(baseUrl);
      HttpHeaders headers = new HttpHeaders();
      headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
      DnaRequest dnaRequest= new DnaRequest();
      dnaRequest.setDna(values);
      HttpEntity<DnaRequest> request = new HttpEntity<>(dnaRequest, headers);
      ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);
      assertEquals(HttpStatus.BAD_REQUEST,result.getStatusCode());
    }
	
	@ParameterizedTest
    @ValueSource(strings = {"", "ATAAAG,CAGA,TTAAX","ATAAAG,CAGA,TTAAA", "ATG,CAG,TTA","ATGG,CAGG,TTAA","ATG,CAG,TTA,TTA"})
    void requestBadRequest(String values) throws Exception {
	  final String baseUrl = "http://localhost:"+port+"/mutant";
      URI uri = new URI(baseUrl);
      HttpHeaders headers = new HttpHeaders();
      headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
      DnaRequest dnaRequest= new DnaRequest();
      dnaRequest.setDna(List.of(values.split(",")));
      HttpEntity<DnaRequest> request = new HttpEntity<>(dnaRequest, headers);
      ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);
      assertEquals(HttpStatus.BAD_REQUEST,result.getStatusCode());
    }
	
	@ParameterizedTest
    @ValueSource(strings = {"ATGCGA,CAGTGC,TTATGT,AGAAGG,CCCCTA,TCACTG,CCCCTA","AAAA,AGCT,GCTA,CTAG","AGCT,AAAA,GCTA,CTAG","AGCT,GCTA,AAAA,CTAG","AGCT,GCTA,CTAG,AAAA","AGCT,ACTG,ATGC,AGCT","GACT,CATG,TAGC,GACT","GCAT,CTAG,TGAC,GCAT","GCTA,CTGA,TGCA,GCTA","GCTA,CTAG,TAGC,AGCT","AGCT,CATG,TGAC,GCTA", "ATGCAT,GCATGC,ATGCAT,GCATGC,ATGCAC,GCATCC,ATGCAT,GCCTGC"}
    )
    void requestOK(String values) throws Exception {
      final String baseUrl = "http://localhost:"+port+"/mutant";
      URI uri = new URI(baseUrl);
      HttpHeaders headers = new HttpHeaders();
      headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
      DnaRequest dnaRequest= new DnaRequest();
      dnaRequest.setDna(List.of(values.split(",")));
      HttpEntity<DnaRequest> request = new HttpEntity<>(dnaRequest, headers);
      ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);
      assertEquals(HttpStatus.OK,result.getStatusCode());
    }
	
	@ParameterizedTest
    @ValueSource(strings = {"CTAG,AGCT,GCTA,CTAG", "ATGCAT,GCATGC,ATGCAT,GCATGC,ATGCAC,GCATAC,ATGCAT,GCCTGC"}
    )
    void requestFORBIDDEN(String values) throws Exception {
      final String baseUrl = "http://localhost:"+port+"/mutant";
      URI uri = new URI(baseUrl);
      HttpHeaders headers = new HttpHeaders();
      headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
      DnaRequest dnaRequest= new DnaRequest();
      dnaRequest.setDna(List.of(values.split(",")));
      HttpEntity<DnaRequest> request = new HttpEntity<>(dnaRequest, headers);
      ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);
      assertEquals(HttpStatus.FORBIDDEN,result.getStatusCode());
    }
	
	@Test
    void requesthealthOK() throws Exception {
      final String baseUrl = "http://localhost:"+port+"/health";
      URI uri = new URI(baseUrl);
      ResponseEntity<String> result = this.restTemplate.getForEntity(uri, String.class);
      assertEquals(HttpStatus.OK,result.getStatusCode());
    }
	
	@Test
    void requeststatsOK() throws Exception {
      final String baseUrl = "http://localhost:"+port+"/stats";
      URI uri = new URI(baseUrl);
      ResponseEntity<String> result = this.restTemplate.getForEntity(uri, String.class);
      assertEquals(HttpStatus.OK,result.getStatusCode());
      assertTrue(result.getBody().contains("count_mutant_dna"));
      assertTrue(result.getBody().contains("count_human_dna"));
      assertTrue(result.getBody().contains("ratio"));
    }

}
