package edu.cmu.sphinx.demo.ScientificCalculatorT14T5;

import java.util.Formatter;


public enum RegExRules {
	
	DEFINE("^(define variable) ([a-z]+)$", "<html>def <font color=GREEN>%s</font></html>"),	//  defining a Variable
	STORE1("^(store) ([a-z]+) ((([0-9]+\\s*)+)|(([0-9]+)(\\.([0-9]+)?)))$", "<html>str <font color=GREEN>%s</font></html>"),	//  storing a Number in the given variable
	STORE2("^store last result$", "MS"),		//  storing temp value like MS
	RETRIEVE1("^(retrieve) ([a-z]+)$", "<html><font color=GREEN>%s</font></html>"),	//	retrieving value of the given Variable or Constant
	RETRIEVE2("^retrieve last result$", "MR"),	//	retrieving temp value like MR
	ROOT("(square|cubic) (root of) ((([0-9]+\\s*)+)|([a-z]+))", "%sR(%s)"),		// Root operation of Variable or Constant
	LOG("(log) ((([0-9]+\\s*)+)|([a-z]+))( square| cube)?", "log^%s %s"),	// LOG operation with square or cube option
	POWER("((([0-9]+\\s*)+)|([a-z]+)) (square|cube)", "%s^%s"),		// Power operation
	SIMPLE_NUMBER("^(([1-9])(\\s[0-9])*)$", "%s"),		// Simple number ex: one three seven
	FLOAT_NUMBER("^(-?([0-9]+)(\\.([0-9]+)?))$", "%s"),	// Number with decimal point ex: 23.5
	GENERAL_RULE("((-?([0-9]+\\s*)+)(\\.([0-9]+))?|(([a-z]+))) "
			+ "(plus|minus|multiply|divide|power|log|root) "
			+ "((-?([0-9]+\\s*)+)(\\.([0-9]+))?|(([a-z]+)))", "");	// General rule for matching and evaluation
	
	private String rule;	// The regex rule
	private String format;	// The format of it's output


	private RegExRules(String rule, String format) {
		this.rule = rule;
		this.format = format;
	}
	
	public String getFormat(String... args) {
		Formatter formatter = new Formatter();
		String f = formatter.format(format, args).toString();
		formatter.close();
		return f;
	}
	
	public String getRule() {
		return rule;
	}
}
