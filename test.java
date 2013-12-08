package edu.cmu.sphinx.demo.ScientificCalculatorT14T5;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

	public test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String command = "8 divide 4 plus 9 minus 2 power 2";
		String command = "8 0 divide 15 plus 5 minus 1 100 5";
		System.out.println(evaluate(command));
	}
	
	public static double evaluate(String line) {
		if (isNumber(line))
			return Double.parseDouble(line);
		Matcher matcher = Pattern.compile(RegExRules.GENERAL_RULE.getRule()).matcher(line);
		if (matcher.find()) {
			System.out.println("line: " + line);
			return evaluate(line.replace(matcher.group(0), calculateOperation(""+getNumber(matcher.group(1)), ""+getNumber(matcher.group(9)), matcher.group(8)) + " "));
		}
		System.out.println("fail");
		return 0;
	}
	
	public static double calculateOperation(String va1, String va2, String operand) {
		System.out.println(va1 + " " + operand + " " + va2);
		double v1 = Double.parseDouble(va1);
		double v2 = Double.parseDouble(va2);
		if (operand.equalsIgnoreCase("plus"))
			return v1 + v2;
		if (operand.equalsIgnoreCase("minus"))
			return v1 - v2;
		if (operand.equalsIgnoreCase("multiply"))
			return v1 * v2;
		if (operand.equalsIgnoreCase("divide"))
			return v1 / v2;
		if (operand.equalsIgnoreCase("power"))
			return Math.pow(v1,v2);
		if (operand.equalsIgnoreCase("square"))
			return Math.sqrt(v1);
		if (operand.equalsIgnoreCase("cubic"))
			return Math.cbrt(v1);
		if (operand.equalsIgnoreCase("log"))
			return Math.log10(v1);
		return 0.0;
	}
	
	public static double getNumber(String line) {
		System.out.println("getNumber " + line);
		if (isNumber(line.replace(" ", ""))) {
			line = line.trim();
			Matcher matcher1 = Pattern.compile(RegExRules.SIMPLE_NUMBER.getRule()).matcher(line);
			Matcher matcher2 = Pattern.compile(RegExRules.FLOAT_NUMBER.getRule()).matcher(line);
//			Matcher matcher3 = Pattern.compile(RegExRules.WORD_NUMBER.getRule()).matcher(line);
			if (matcher1.find()){
				System.out.println("simple number");
				return Double.parseDouble(line.replace(" ", ""));
			}
			if (matcher2.find()) {
				System.out.println("float number");
				return Double.parseDouble(line.replace(" ", ""));
			}
			String temp[] = line.split(" ");
			double[] num = new double[temp.length];
			double result = 0;
			for (int i=0; i<num.length; i++) {
				num[i] = Double.parseDouble(temp[i]);
				if (num[i] == 1000) {
					num[i] = 1000 * num[i-1];
					num[i-1] = 0;
				} else if (num[i] == 100) {
					num[i] = 100 * num[i-1];
					num[i-1] = 0;
				}
			}
			for (int i=0; i<num.length; i++)
				result += num[i];
			return result;
		}
		return -111;
	}
	
	public static boolean isNumber(String line) {
		try {
			Double.parseDouble(line);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
