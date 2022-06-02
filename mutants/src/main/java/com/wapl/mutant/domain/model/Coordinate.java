package com.wapl.mutant.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Coordinate {
	private Integer row;
	private Integer column;
	
	public static final BiFunction<Integer, Integer, List<Coordinate>> getCoordinates = (Integer rowLimit, Integer columnLimit)->{
		List<Coordinate> coordinates = new ArrayList<>();
		for (Integer rowActual = 0; rowActual <= rowLimit; rowActual++) {
			for (Integer columnActual = 0; columnActual <= columnLimit; columnActual++) {
				coordinates.add(Coordinate.builder().row(rowActual).column(columnActual).build());
			}
		}
		return coordinates;
	};
}