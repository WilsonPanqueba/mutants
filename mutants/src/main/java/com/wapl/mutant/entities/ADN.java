package com.wapl.mutant.entities;

import java.util.List;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Validated
@Getter
public class ADN {
	private List<String> adnChar;
	private Integer columnas;
	private Integer filas;
}