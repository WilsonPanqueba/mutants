package com.wapl.mutant.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Coordinate {
	private final Integer row;
	private final Integer column;
	
	
}