package vit.mtech.test;

/* package whatever; // don't place package name! */

import java.util.*;
import java.lang.*;
import java.io.*;

/* Name of the class has to be "Main" only if the class is public. */
class SubExpr
{
	public static void main (String[] args) throws java.lang.Exception
	{
		String expression = "FILTER (?simProperty1 < (?origProperty1 + 150) && ?simProperty1 > (?origProperty1 – 150))";
		
		System.out.println(getSubExpression(expression, "?simProperty1", '(', ')'));
		System.out.println(getSubExpression(expression, "x2", '(', ')'));
		System.out.println(getSubExpression(expression, "x3", '(', ')'));
		System.out.println(getSubExpression(expression, "x4", '(', ')'));
		System.out.println(getSubExpression(expression, "x5", '(', ')'));
		System.out.println(getSubExpression(expression, "x6", '(', ')'));
		System.out.println(getSubExpression(expression, "x7", '(', ')'));
		System.out.println(getSubExpression(expression, "x8", '(', ')'));
	}
	
	public static String getSubExpression(String expression, String search, char open, char close) {	
		int idx = expression.indexOf(open + search);
		
		if (idx == -1)
			return ""; //No match.
		
		int depth = 0;
		
		StringBuilder builder = new StringBuilder();
		
		for (int i = idx; i < expression.length(); i++) {
			char c = expression.charAt(i);
			
			if (c == open) depth++;
			if (c == close) depth--;
			
			builder.append(expression.charAt(i));
			
			if (depth < 1) break;
		}
		
		return builder.toString();
	}
}