package st;

import static java.util.Arrays.asList;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Parser {
	public static final int INTEGER = 1;
	public static final int BOOLEAN = 2;
	public static final int STRING = 3;
	public static final int CHAR = 4;

	private OptionMap optionMap;

	public Parser() {
		optionMap = new OptionMap();
	}

	public void add(String option_name, String shortcut, int value_type) {
		optionMap.store(option_name, shortcut, value_type);
	}

	public void add(String option_name, int value_type) {
		optionMap.store(option_name, "", value_type);
	}

	public int getInteger(String option) {
		String value = getString(option);
		int type = getType(option);
		int result;
		switch (type) {
		case INTEGER:
			try {
				result = Integer.parseInt(value);
			} catch (Exception e) {
				try {
					new BigInteger(value);
				} catch (Exception e1) {
					result = 0;
				}
				result = 0;
			}
			break;
		case BOOLEAN:
			if (getBoolean(option) == false) {
				result = 0;
			} else {
				result = 1;
			}
			break;
		case STRING:
			int power = 1;
			result = 0;
			char c;
			for (int i = value.length() - 1; i >= 0; --i) {
				c = value.charAt(i);
				if (!Character.isDigit(c))
					return 0;
				result = result + power * (c - '0');
				power *= 10;
			}
			break;
		case CHAR:
			result = (int) getChar(option);
			break;
		default:
			result = 0;
		}
		return result;
	}

	public boolean getBoolean(String option) {
		String value = getString(option);
		boolean result;
		if (value.toLowerCase().equals("false") || value.equals("0") || value.equals("")) {
			result = false;
		} else {
			result = true;
		}
		return result;
	}

	public String getString(String option) {
		String result = optionMap.getValue(option);
		return result;
	}

	public char getChar(String option) {
		String value = getString(option);
		char result;
		if (value.equals("")) {
			result = '\0';
		} else {
			result = value.charAt(0);
		}
		return result;
	}

	public List<Integer> getIntegerList(String option) {
		String value = getString(option);
		int type = getType(option);
		List<Integer> result = new ArrayList<Integer>();
		switch (type) {
		case INTEGER:
			try {
				result.add(Integer.parseInt(value));
			} catch (Exception e) {
				try {
					new BigInteger(value);
				} catch (Exception e1) {

				}
			}
			break;
		case BOOLEAN:
			if (getBoolean(option) == false) {
				result = asList(0);
			} else {
				result = asList(1);
			}
			break;
		case STRING:
			try {
				result.add(Integer.parseInt(value));
			} catch (Exception e) {
				boolean noNumber = true;
				for (char c : value.toCharArray()) {
					if (Character.isDigit(c)) {
						noNumber = false;
					}
				}
				if (noNumber) {
					break;
				}
				String[] splited = value.split("[^\\d-]");
				for (String s : splited) {
					if (s.length() != 0) {
						if (s.contains("-")) {
							if (s.charAt(s.length() - 1) == '-') {
								result = asList();
								break;
							}

							int count = 0;
							for (char c : s.toCharArray()) {
								if (c == '-') {
									count++;
								}
							}

							switch (count) {
							case (1):
								if (s.charAt(0) == '-') {
									try {
										result.add(Integer.parseInt(s));
									} catch (NumberFormatException error) {

									}
								} else {
									String[] intervalSplit = s.split("-");
									int first = Integer.parseInt(intervalSplit[0]);
									int second = Integer.parseInt(intervalSplit[1]);
									for (int i = Math.min(first, second); i <= Math.max(first, second); i++) {
										result.add(i);
									}
								}
								break;
							case (2):
								int first = 0;
								int second = 0;

								if (s.charAt(0) == '-') {
									s = s.substring(1);
									String[] intervalSplit = s.split("-");
									first = -1 * Integer.parseInt(intervalSplit[0]);
									second = Integer.parseInt(intervalSplit[1]);
								} else {
									String[] intervalSplit = s.split("-");
									first = Integer.parseInt(intervalSplit[0]);
									second = -1 * Integer.parseInt(intervalSplit[2]);
								}
								for (int i = Math.min(first, second); i <= Math.max(first, second); i++) {
									result.add(i);
								}
								break;

							case (3):
								String[] intervalSplit = s.split("-");
								first = -1 * Integer.parseInt(intervalSplit[1]);
								second = -1 * Integer.parseInt(intervalSplit[3]);
								for (int i = Math.min(first, second); i <= Math.max(first, second); i++) {
									result.add(i);
								}
								break;
							}
						} else {
							result.add(Integer.parseInt(s));
						}
					}

				}

			}
			break;
		case CHAR:
			if (Character.isDigit(getChar(option))) {
				result.add(getChar(option) - '0');
			}
			break;
		}

		Collections.sort(result);
		return result;
	}

	public int parse(String command_line_options) {
		if (command_line_options == null) {
			return -1;
		}
		int length = command_line_options.length();
		if (length == 0) {
			return -2;
		}

		int char_index = 0;
		while (char_index < length) {
			char current_char = command_line_options.charAt(char_index);

			while (char_index < length) {
				current_char = command_line_options.charAt(char_index);
				if (current_char != ' ') {
					break;
				}
				char_index++;
			}

			boolean isShortcut = true;
			String option_name = "";
			String option_value = "";
			if (current_char == '-') {
				char_index++;
				current_char = command_line_options.charAt(char_index);
				if (current_char == '-') {
					isShortcut = false;
					char_index++;
				}
			} else {
				return -3;
			}
			current_char = command_line_options.charAt(char_index);

			while (char_index < length) {
				current_char = command_line_options.charAt(char_index);
				if (Character.isLetterOrDigit(current_char) || current_char == '_') {
					option_name += current_char;
					char_index++;
				} else {
					break;
				}
			}

			boolean hasSpace = false;
			if (current_char == ' ') {
				hasSpace = true;
				while (char_index < length) {
					current_char = command_line_options.charAt(char_index);
					if (current_char != ' ') {
						break;
					}
					char_index++;
				}
			}

			if (current_char == '=') {
				char_index++;
				current_char = command_line_options.charAt(char_index);
			}
			if ((current_char == '-' && hasSpace == true) || char_index == length) {
				if (getType(option_name) == BOOLEAN) {
					option_value = "true";
					if (isShortcut) {
						optionMap.setValueWithOptioShortcut(option_name, option_value);
					} else {
						optionMap.setValueWithOptionName(option_name, option_value);
					}
				} else {
					return -3;
				}
				continue;
			} else {
				char end_symbol = ' ';
				current_char = command_line_options.charAt(char_index);
				if (current_char == '\'' || current_char == '\"') {
					end_symbol = current_char;
					char_index++;
				}
				while (char_index < length) {
					current_char = command_line_options.charAt(char_index);
					if (current_char != end_symbol) {
						option_value = option_value + current_char;
						char_index++;
					} else {
						break;
					}
				}
			}

			if (isShortcut) {
				optionMap.setValueWithOptioShortcut(option_name, option_value);
			} else {
				optionMap.setValueWithOptionName(option_name, option_value);
			}
			char_index++;
		}
		return 0;
	}

	private int getType(String option) {
		int type = optionMap.getType(option);
		return type;
	}

	@Override
	public String toString() {
		return optionMap.toString();
	}

}
