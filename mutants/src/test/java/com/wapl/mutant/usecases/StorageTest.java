package com.wapl.mutant.usecases;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.wapl.mutant.infraestructure.driveradapter.AdnTestRepository;
import com.wapl.mutant.infraestructure.helper.AdnStats;
import com.wapl.mutant.infraestructure.helper.AdnTest;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class StorageTest {
  @InjectMocks
  Storage storage;
  @Mock
  AdnTestRepository adnTestRepository;
  AdnTest adnTest;
  
  @ParameterizedTest
  @NullAndEmptySource
  void storageSaveAdnTestException(List<String> adnStructure) {
    assertThrows(RuntimeException.class, () -> {
      storage.saveAdnTest(null);
    });
  }
  
  @ParameterizedTest
  @ValueSource(strings = {"ATGCGA,CAGTGC,TTATGT,AGAAGG,CCCCTA,TCACTG,CCCCTA"})
  void storageSaveAdn(String adnString) {
    List<String> adnStructure=List.of(adnString.split(","));
    adnTest=AdnTest.builder().adn(adnStructure).mutant(true);
    when(adnTestRepository.existsById(adnTest.getAdnMD5())).thenReturn(Mono.just(Boolean.FALSE));
    when(adnTestRepository.save(adnTest)).thenReturn(Mono.just(adnTest));
    storage.saveAdnTest(adnTest);
    verify(adnTestRepository, times(1)).save(adnTest);
  }
  
  @ParameterizedTest
  @ValueSource(strings = {"ATGCGA,CAGTGC,TTATGT,AGAAGG,CCCCTA,TCACTG,CCCCTA"})
  void storageNotSaveAdn(String adnString) {
    List<String> adnStructure=List.of(adnString.split(","));
    adnTest=AdnTest.builder().adn(adnStructure).mutant(true);
    when(adnTestRepository.existsById(adnTest.getAdnMD5())).thenReturn(Mono.just(Boolean.TRUE));
    storage.saveAdnTest(adnTest);
    verify(adnTestRepository, times(0)).save(adnTest);
  }
  @Test
  void getStat() {
    when(adnTestRepository.stat()).thenReturn(Mono.just(new AdnStats()));
    storage.getStats();
    verify(adnTestRepository, times(1)).stat();
  }
}
