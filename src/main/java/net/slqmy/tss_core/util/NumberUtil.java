package net.slqmy.tss_core.util;

import org.jetbrains.annotations.NotNull;

public class NumberUtil {

  public static @NotNull String toRomanNumeral(int number) {
	StringBuilder romanNumeral = new StringBuilder();

	int subTen = number % 10;
	int subHundred = (number % 100 - subTen);
	int subThousand = (number % 1000 - subHundred);

	if (number != subThousand) {
	  romanNumeral.append("M".repeat((int) Math.floor(number / 1000.0D)));
	}

	if (subThousand != subHundred) {
	  subThousand /= 100;
	  String subThousandAppend;
	  switch (subThousand) {
		case 4 -> subThousandAppend = "CD";
		case 9 -> subThousandAppend = "CM";
		default -> {
		  if (subThousand < 4) {
			subThousandAppend = "C".repeat(subThousand);
		  } else {
			subThousandAppend = "D" + "C".repeat(subThousand - 5);
		  }
		}
	  }
	  romanNumeral.append(subThousandAppend);
	}

	if (subTen != subHundred) {
	  subHundred /= 10;
	  String subHundredAppend;
	  switch (subHundred) {
		case 4 -> subHundredAppend = "IL";
		case 9 -> subHundredAppend = "IC";
		default -> {
		  if (subHundred < 4) {
			subHundredAppend = "X".repeat(subHundred);
		  } else {
			subHundredAppend = "L" + "X".repeat(subHundred - 5);
		  }
		}
	  }
	  romanNumeral.append(subHundredAppend);
	}

	String subTenAppend;
	switch (subTen) {
	  case 4 -> subTenAppend = "IV";
	  case 9 -> subTenAppend = "IX";
	  default -> {
		if (subTen < 4) {
		  subTenAppend = "I".repeat(subTen);
		} else {
		  subTenAppend = "V" + "I".repeat(subTen - 5);
		}
	  }
	}
	romanNumeral.append(subTenAppend);

	return romanNumeral.toString();
  }
}
