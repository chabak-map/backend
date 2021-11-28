package com.sikhye.chabak.utils;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRegex {
	public static boolean isRegexEmail(String target) {
		String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(target);
		return matcher.find();
	}

	public static boolean isRegexPhone(String target) {
		String regex = "^01(?:0|1)-(?:\\d{3}|\\d{4})-\\d{4}$";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(target);
		return matcher.find();
	}

	public static boolean isRegexPassword(String target) {
		String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(target);
		return matcher.find();
	}


	public static boolean isEmpty(Object target) {
		if (target == null) return true;
		if ((target instanceof String) && (((String) target).trim().length() == 0)) {
			return true;
		}
		if (target instanceof Map) {
			return ((Map<?, ?>) target).isEmpty();
		}
		if (target instanceof Map) {
			return ((Map<?, ?>) target).isEmpty();
		}
		if (target instanceof List) {
			return ((List<?>) target).isEmpty();
		}
		if (target instanceof Object[]) {
			return (((Object[]) target).length == 0);
		}
		return false;
	}
}
