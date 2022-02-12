package com.ufcg.psoft.mercadofacil.validation;

/**
 * Classe que agrupa algums métodos estáticos para validação de campos.
 */
public abstract class FieldValidator {

	public static <T extends Number> void notNegative(T num, String message) {
		if (num.doubleValue() < 0) throw new IllegalArgumentException(message);
	}
	
	public static <T extends Number> void negative(T num, String message) {
		if (num.doubleValue() >= 0) throw new IllegalArgumentException(message);
	}

	public static <T extends Number> void notPositive(T num, String message) {
		if (num.doubleValue() > 0) throw new IllegalArgumentException(message);
	}

	public static <T extends Number> void positive(T num, String message) {
		if (num.doubleValue() <= 0) throw new IllegalArgumentException(message);
	}

}
