
package com.wapl.mutant.usecases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import com.wapl.mutant.entities.ADN;
import com.wapl.mutant.entities.Coordinates;

import reactor.core.publisher.Mono;

public class Mutants {

	private static final List<String> MUTACIONES = Arrays.asList("AAAA", "TTTT", "CCCC", "GGGG");

	private Mutants() {
		throw new IllegalStateException("Utility class");
	}

	private static final Function<List<String>, ADN> loadADN = (List<String> adn) -> {
		Integer columnas = adn.get(0).length();
		Integer filas = adn.size();
		return new ADN(adn, columnas, filas);
	};

	private static final BiFunction<List<String>, Coordinates, List<String>> loadMatriz4X4 = (List<String> adnChar,
			Coordinates cordenada) -> {
		List<String> matrizMinima = new ArrayList<>();
		for (Integer filIni4x4 = 0; filIni4x4 < 4; filIni4x4++) {
			matrizMinima.add(filIni4x4, adnChar.get(cordenada.getFil() + filIni4x4).substring(cordenada.getCol(),
					cordenada.getCol() + 4));
		}
		return matrizMinima;
	};

	private static final BiPredicate<String, String> compartorNotMutant = (base, actual) -> {
		return !base.equals(actual);
	};

	private static final Predicate<List<String>> isMutantDiagonalID = (List<String> matrizMinima) -> {
		for (Integer filIni = 1; filIni < 4; filIni++) {
			if(compartorNotMutant.test(String.valueOf(matrizMinima.get(0).charAt(0)),
					String.valueOf(matrizMinima.get(filIni).charAt(filIni)))) 
				return false;
		}
		return true;
	};

	private static final Predicate<List<String>> isMutantDiagonalDI = (List<String> matrizMinima) -> {

		for (Integer filIni = 2; filIni >= 0; filIni--) {
			if( compartorNotMutant.test(String.valueOf(matrizMinima.get(3).charAt(0)),
					String.valueOf(matrizMinima.get(filIni).charAt(3-filIni)))) 
				return false;
			
		}
		return true;
	};

	private static final Predicate<List<String>> isMutantFila = (List<String> matrizMinima) -> {
		Integer colIni = 0;
		do {
			StringBuilder bld = new StringBuilder();
			for (Integer filIni = 0; filIni < 4; filIni++) {
				bld.append(matrizMinima.get(filIni).charAt(colIni));
			}
			if (MUTACIONES.contains(bld.toString()))
				return Boolean.TRUE;
			colIni++;
		} while (colIni < 4);
		return Boolean.FALSE;
	};

	private static final Predicate<List<String>> isMutantColumna = (List<String> matrizMinima) -> {		
		int filIni = 0;
		do {
			Boolean isMutante=null;
			for (Integer colIni = 1; colIni < 4 && isMutante==null; colIni++) {
				if(compartorNotMutant.test(String.valueOf(matrizMinima.get(filIni).charAt(0)),
						String.valueOf(matrizMinima.get(filIni).charAt(colIni)))) {
					isMutante=false;
				}
			}
			if (isMutante==null)
				return true;
			filIni++;
			
		} while (filIni < 4);
		return false;
	};

	public static final Predicate<List<String>> isMutant = (List<String> adnString) -> {
		ADN adn = loadADN.apply(adnString);
		Integer colFin = adn.getColumnas() - 4;
		Integer filFin = adn.getFilas() - 4;
		if(colFin<0||filFin<0)
			throw new RuntimeException();
		for (Integer filIni = 0; filIni <= filFin; filIni++) {
			for (Integer colIni = 0; colIni <= colFin; colIni++) {
				List<String> matrizMinima = loadMatriz4X4.apply(adn.getAdnChar(), new Coordinates(filIni, colIni));
				if (isMutantColumna.test(matrizMinima))
					return true;
				if (isMutantFila.test(matrizMinima))
					return true;
				if (isMutantDiagonalID.test(matrizMinima))
					return true;
				if (isMutantDiagonalDI.test(matrizMinima))
					return true;
			}
		}
		return false;
	};

}
