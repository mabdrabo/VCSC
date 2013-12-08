package edu.cmu.sphinx.demo.ScientificCalculatorT14T5;

/*
 * Copyright 1999-2004 Carnegie Mellon University.
 * Portions Copyright 2004 Sun Microsystems, Inc.
 * Portions Copyright 2004 Mitsubishi Electric Research Laboratories.
 * All Rights Reserved.  Use is subject to license terms.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 *
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;


/**
 * A simple ScientificCalculator, a speech application built using Sphinx-4. This application uses the Sphinx-4
 * endpointer, which automatically segments incoming audio into utterances and silences.
 */
public class ScientificCalculator {
	HashMap<String, Double> memory;
	Recognizer recognizer;
	Microphone mic;
	ConfigurationManager cm;
	static boolean resultFlag = true;
	
	public ScientificCalculator() {
		this.memory = new HashMap<String, Double>();
		this.memory.put("pi", 22.0/7);	// PI constant
		this.memory.put("eu", 2.71828);	// Euler's constant
		this.memory.put("an", 0.0);		// stores last answer
		this.memory.put("tm", 0.0);		// Temp. Memory saved by user
		this.memory.put("a", 0.0);
		this.memory.put("b", 0.0);
		this.memory.put("c", 0.0);
		this.memory.put("x", 0.0);
		this.memory.put("y", 0.0);
		this.memory.put("z", 0.0);

		cm = new ConfigurationManager(ScientificCalculator.class.getResource("ScientificCalculator.config.xml"));
        recognizer = (Recognizer) cm.lookup("recognizer");
        recognizer.allocate();
        
	}

	public HashMap<String, String> parse(String input) {
		System.out.println("parsing");
		for (RegExRules regexEnum : RegExRules.values()) {
			String tempCommand;
			Matcher matcher = Pattern.compile(regexEnum.getRule()).matcher(input);
			if (matcher.find()) {
				HashMap<String, String> returnMap = new HashMap<String, String>();
				returnMap.put("regexEnum", regexEnum.name());
				switch (regexEnum) {
				case DEFINE:
					if (!matcher.group(2).equals("pi") && !matcher.group(2).equals("eu")) {
						memory.put(matcher.group(2), 0.0);
						returnMap.put("command", regexEnum.getFormat(""+matcher.group(2)));
						returnMap.put("result", "0");
					}
					break;
				case STORE1:
					if (memory.containsKey(matcher.group(2)) && !matcher.group(2).equals("pi") && !matcher.group(2).equals("eu") ) {
						memory.put(matcher.group(2), getNumber(matcher.group(3)));
						returnMap.put("command", regexEnum.getFormat(""+matcher.group(2)));
						returnMap.put("result", matcher.group(3));
					}
					break;
				case STORE2:
					memory.put("tm", memory.get("an"));
					returnMap.put("command", regexEnum.getFormat());
					returnMap.put("result", ""+memory.get("tm"));
					break;
				case RETRIEVE1:
					if (memory.containsKey(matcher.group(2))) {
						returnMap.put("command", regexEnum.getFormat(""+matcher.group(2)));		//  the variable or constant
						returnMap.put("result", ""+memory.get(matcher.group(2)));
					}
					break;
				case RETRIEVE2:
					returnMap.put("command", regexEnum.getFormat());
					returnMap.put("result", ""+memory.get("tm"));
					break;
				case ROOT:
					System.out.println("ROOT case: " + input);
					if (matcher.group(0).contains("square"))
						tempCommand = "2 " + matcher.group(0).replace("square root of", "root");
					else
					if (matcher.group(0).contains("cubic"))
						tempCommand = "3 " + matcher.group(0).replace("cubic root of", "root");
					else
						tempCommand = "";
					input = input.replace(matcher.group(0), evaluate(tempCommand, new ArrayList<String>()).get("result"));
					continue;
				case POWER:
					System.out.println("POWER case");
					if (matcher.group(0).contains("square"))
						input = input.replace(matcher.group(0), matcher.group(0).replace("square", "") + "power 2");
					else
					if (matcher.group(0).contains("cube"))
						input = input.replace(matcher.group(0), matcher.group(0).replace("cube", "") + "power 3");
					continue;
				case LOG:
					System.out.println("LOG case");
					if (matcher.group(0).contains("square"))
						tempCommand = "2 " + matcher.group(0).replace(" square", "");
					else
					if (matcher.group(0).contains("cube"))
						tempCommand = "3 " + matcher.group(0).replace(" cube", "");
					else
						tempCommand = "1 " + matcher.group(0);
					input = input.replace(matcher.group(0), evaluate(tempCommand, new ArrayList<String>()).get("result"));
					continue;
				case GENERAL_RULE:
					System.out.println("GENERAL case: " + input);
					returnMap = evaluate(input, new ArrayList<String>());
					memory.put("an", Double.parseDouble(returnMap.get("result")));
					System.out.println(returnMap.get("command"));
					return returnMap;
				default:
					break;
				}
				if (returnMap.size() > 1)
					return returnMap;
			}
		}
		System.out.println(memory.toString());
		return null;
	}
	
	public HashMap<String, String> evaluate(String line, ArrayList<String> formattedCommand) {
		if (isNumber(line)) {
			String command = "";
			for (String s : formattedCommand){
				command += s;
			}
			HashMap<String, String> returnMap = new HashMap<String, String>();
			returnMap.put("command", command);
			returnMap.put("result", line);
			resultFlag = true;
			return returnMap;
		}
		Matcher matcher = Pattern.compile(RegExRules.GENERAL_RULE.getRule()).matcher(line);
		if (matcher.find()) {
			System.out.println("line: " + line);
			String v1 = ""+getNumber(matcher.group(1));
			String v2 = ""+getNumber(matcher.group(9));
			String operation = matcher.group(8);
			if(resultFlag)
				formattedCommand.add(v1+" ");
			
			if(operation.equals("plus")){
				formattedCommand.add("+ ");
			}
			else{
				if(operation.equals("minus")){
					formattedCommand.add("- ");
				}
				else{
					if(operation.equals("multiply")){
						formattedCommand.add("* ");
					}
					else{
						if(operation.equals("divide")){
							formattedCommand.add("/ ");
						}
						else{
							if(operation.equals("power")){
								formattedCommand.add("^ ");
							}
							else{
								if(operation.equals("log")){
									formattedCommand.add("log ");
								}
								else{
									if(operation.equals("root")){
										formattedCommand.add("sqrt ");
									}
								}
							}
						}
					}
				}
			}
			formattedCommand.add(v2+" ");
			resultFlag = false;
			return evaluate(line.replace(matcher.group(0), calculateOperation(v1, v2, operation)+" "), formattedCommand);
		}
		System.out.println("fail");
		return null;
	}
	
	public boolean isNumber(String line) {
		try {
			System.out.println("check if num: " + line);
			Double.parseDouble(line);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public double getNumber(String line) {
		if (isNumber(line.replace(" ", ""))) {
			line = line.trim();
			Matcher matcher1 = Pattern.compile(RegExRules.SIMPLE_NUMBER.getRule()).matcher(line);
			Matcher matcher2 = Pattern.compile(RegExRules.FLOAT_NUMBER.getRule()).matcher(line);
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
				if (num[i] == 1000 && i>0) {
					num[i] = 1000 * num[i-1];
					num[i-1] = 0;
				}
				if (num[i] == 100 && i>0) {
					num[i] = 100 * num[i-1];
					num[i-1] = 0;
				}
			}
			for (int i=0; i<num.length; i++)
				result += num[i];
			return result;
		}
		if (memory.containsKey(line.replace(" ", "")))
			return memory.get(line.replace(" ", ""));
		return 0;
	}
	
	public double calculateOperation(String va1, String va2, String operator) {
		System.out.println(va1 + " " + operator + " " + va2);
		double v1 = Double.parseDouble(va1);
		double v2 = Double.parseDouble(va2);
		if (operator.equalsIgnoreCase("plus"))
			return v1 + v2;
		if (operator.equalsIgnoreCase("minus"))
			return v1 - v2;
		if (operator.equalsIgnoreCase("multiply"))
			return v1 * v2;
		if (operator.equalsIgnoreCase("divide"))
			return v1 / v2;
		if (operator.equalsIgnoreCase("power"))
			return Math.pow(v1,v2);
		if (operator.equalsIgnoreCase("log"))
			return v1 * Math.log10(v2);
		if (operator.equalsIgnoreCase("root"))
			return (v1==2)? Math.sqrt(v2) : (v1==3)? Math.cbrt(v2) : 0;
		return 0.0;
	}
	
	public HashMap<String, String> start() {
		// start the microphone or exit if the program if this is not possible
        mic = (Microphone) cm.lookup("microphone");
        mic.clear();
		if (!mic.startRecording()) {
            System.out.println("Cannot start microphone.");
            recognizer.deallocate();
            System.exit(1);
        }
		String resultText;
		HashMap<String, String> test;
		do {
			System.out.println("Start speaking.");
			Result result = recognizer.recognize();
			System.out.println("recognizer: " + result);
			resultText = result.getBestFinalResultNoFiller();
			test = parse(resultText);
		} while (test == null);
		mic.stopRecording();
		return test;
	}

	

//	public static void main(String[]args) {
//		ScientificCalculator sc = new ScientificCalculator();
//		sc.parse("square root of 9 plus 5 multiply 3 plus 7 divide 2");
//	}
}
