package com.wapl.mutant.usecases;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class MutantsTest {
	
	@ParameterizedTest
	@NullAndEmptySource
	void mutantsExceptionsWithNull(List<String> values){
		assertThrows(RuntimeException.class, () -> {
			Mutants.isMutant.test(values);
	    });
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"ATAAAG,CAGA,TTAAA", "ATG,CAG,TTA","ATGG,CAGG,TTAA","ATG,CAG,TTA,TTA"})
	void mutantsExceptionsDNAIncomplete(String values){
		assertThrows(RuntimeException.class, () -> {
			Mutants.isMutant.test(List.of(values.split(",")));
	    });
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"ATGCGA,CAGTGC,TTATGT,AGAAGG,CCCCTA,TCACTG,CCCCTA","AAAA,AGCT,GCTA,CTAG","AGCT,AAAA,GCTA,CTAG","AGCT,GCTA,AAAA,CTAG","AGCT,GCTA,CTAG,AAAA","AGCT,ACTG,ATGC,AGCT","GACT,CATG,TAGC,GACT","GCAT,CTAG,TGAC,GCAT","GCTA,CTGA,TGCA,GCTA","GCTA,CTAG,TAGC,AGCT","AGCT,CATG,TGAC,GCTA", "ATGCAT,GCATGC,ATGCAT,GCATGC,ATGCAC,GCATCC,ATGCAT,GCCTGC"})
	void mutantsTrue(String adnString){
		assertTrue(Mutants.isMutant.test(List.of(adnString.split(","))));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"CTAG,AGCT,GCTA,CTAG", "ATGCAT,GCATGC,ATGCAT,GCATGC,ATGCAC,GCATAC,ATGCAT,GCCTGC"})
	void mutantsFalse(String adnString){
		assertFalse(Mutants.isMutant.test(List.of(adnString.split(","))));
	}
}
