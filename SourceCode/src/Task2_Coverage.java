import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import st.OptionMap;
import st.Parser;

public class Task2_Coverage {
	private Parser parser;
	private OptionMap map;

	@Before
	public void set_up() {
		parser = new Parser();
		map = new OptionMap();
	}

	@Test
	public void test_add_pre_1() {
		parser.add("a", "a", Parser.STRING);
		parser.parse("--a=a");
		assertEquals(parser.getString("a"), "a");

		parser.add("a_b12", Parser.STRING);
		parser.parse("--a_b12=a_b12");
		assertEquals(parser.getString("a_b12"), "a_b12");

		parser.add("_12", Parser.STRING);
		parser.parse("--_12=ab");
		assertEquals(parser.getString("_12"), "ab");

		parser.add("ab", "a_b12", Parser.STRING);
		parser.parse("-a_b12=a_b12");
		assertEquals(parser.getString("a_b12"), "a_b12");

		parser.add("ab", "_12", Parser.STRING);
		parser.parse("-_12=ab");
		assertEquals(parser.getString("_12"), "ab");

	}

	@Test(expected = RuntimeException.class)
	public void test_add_pre_1_exception() {
		parser.add("a_b12*", Parser.STRING);
	}

	@Test(expected = RuntimeException.class)
	public void test_add_pre_1_exception2() {
		parser.add("ab", "a_b12*", Parser.STRING);
	}

	@Test(expected = RuntimeException.class)
	public void test_add_pre_2_exception() {
		parser.add("12", Parser.STRING);
	}

	@Test(expected = RuntimeException.class)
	public void test_add_pre_2_exception2() {
		parser.add("ab", "12", Parser.STRING);
	}

	@Test
	public void test_add_post_1() {
		parser.add("a", "a", Parser.STRING);
		parser.parse("--a=a");
		assertEquals(parser.getString("a"), "a");

		parser.add("a", "a", Parser.BOOLEAN);
		parser.parse("--a");
		assertEquals(parser.getBoolean("a"), true);

		parser.add("a", "ab", Parser.CHAR);
		parser.parse("-ab=a");
		assertEquals(parser.getChar("a"), 'a');

		parser.add("b", Parser.STRING);
		parser.parse("--b=b");
		assertEquals(parser.getString("b"), "b");

		parser.add("b", Parser.BOOLEAN);
		parser.parse("--b");
		assertEquals(parser.getBoolean("b"), true);
	}

	@Test
	public void test_add_post_2() {
		parser.add("a", "a", Parser.STRING);
		parser.add("A", "a", Parser.STRING);
		parser.parse("--a=a");
		parser.parse("--A=b");
		assertEquals(parser.getString("a"), "a");
		assertEquals(parser.getString("A"), "b");

		parser.add("c", "c", Parser.STRING);
		parser.add("d", "C", Parser.STRING);
		parser.parse("-c=c");
		parser.parse("-C=b");
		assertEquals(parser.getString("c"), "c");
		assertEquals(parser.getString("C"), "b");

		parser.add("a", Parser.BOOLEAN);
		parser.add("A", Parser.BOOLEAN);
		parser.parse("--a=true");
		parser.parse("--A=0");
		assertEquals(parser.getBoolean("a"), true);
		assertEquals(parser.getBoolean("A"), false);

		parser.parse("--D=true");
		assertEquals(parser.getBoolean("D"), false);
		assertEquals(parser.getString("D"), "");
	}

	@Test
	public void test_add_post_3() {
		parser.add("option", "o", Parser.STRING);
		parser.add("o", "option", Parser.STRING);
		parser.parse("-o=a");
		parser.parse("--o=b");
		assertEquals(parser.getString("option"), "a");
		assertEquals(parser.getString("o"), "b");
	}

	@Test(expected = RuntimeException.class)
	public void test_add_var_1_exception() {
		parser.add("", "o", Parser.STRING);
	}

	@Test(expected = RuntimeException.class)
	public void test_add_var_1_exception2() {
		parser.add("", Parser.STRING);
	}

	@Test
	public void test_add_var_1() {
		parser.add("_", "o", Parser.STRING);
		parser.parse("--_=a");
		assertEquals(parser.getString("_"), "a");

		parser.add("abcdf", "o", Parser.STRING);
		parser.parse("--abcdf=a");
		assertEquals(parser.getString("abcdf"), "a");

		parser.add("abcdf123", "o", Parser.STRING);
		parser.parse("--abcdf123=a");
		assertEquals(parser.getString("abcdf123"), "a");

	}

	@Test
	public void test_add_var_1_2() {
		parser.add("_", Parser.STRING);
		parser.parse("--_=a");
		assertEquals(parser.getString("_"), "a");

		parser.add("abcdf", Parser.STRING);
		parser.parse("--abcdf=a");
		assertEquals(parser.getString("abcdf"), "a");

		parser.add("abcdf123", Parser.STRING);
		parser.parse("--abcdf123=a");
		assertEquals(parser.getString("abcdf123"), "a");

	}

	@Test
	public void test_add_var_2() {
		parser.add("a", "_", Parser.STRING);
		parser.parse("-_=a");
		assertEquals(parser.getString("a"), "a");

		parser.add("abcdf", "abcd", Parser.STRING);
		parser.parse("-abcd=a");
		assertEquals(parser.getString("abcdf"), "a");

		parser.add("abcdf123", "abcdf12", Parser.STRING);
		parser.parse("-abcdf12=a");
		assertEquals(parser.getString("abcdf123"), "a");

	}

	@Test
	public void test_add_var_3() {
		parser.add("a", "a", Parser.STRING);
		parser.parse("--a=a");
		assertEquals(parser.getString("a"), "a");

		parser.add("a", "a", Parser.BOOLEAN);
		parser.parse("--a");
		assertEquals(parser.getBoolean("a"), true);

		parser.add("a", "a", Parser.CHAR);
		parser.parse("--a=c");
		assertEquals(parser.getChar("a"), 'c');

		parser.add("a", "a", Parser.INTEGER);
		parser.parse("--a=1");
		assertEquals(parser.getInteger("a"), 1);

		parser.add("b", Parser.STRING);
		parser.parse("--b=a");
		assertEquals(parser.getString("b"), "a");

		parser.add("b", Parser.BOOLEAN);
		parser.parse("--b");
		assertEquals(parser.getBoolean("b"), true);

		parser.add("b", Parser.CHAR);
		parser.parse("--b=c");
		assertEquals(parser.getChar("b"), 'c');

		parser.add("b", Parser.INTEGER);
		parser.parse("--b=1");
		assertEquals(parser.getInteger("b"), 1);

	}

	@Test
	public void test_parse_pre_1() {
		parser.add("a", "a", Parser.STRING);

		parser.parse("--a=a");
		assertEquals(parser.getString("a"), "a");

		parser.parse("--a=1");
		assertEquals(parser.getString("a"), "1");

		parser.parse("--a=@@@@@");
		assertEquals(parser.getString("a"), "@@@@@");

		parser.add("a", "a", Parser.BOOLEAN);
		parser.parse("--a=0");
		assertEquals(parser.getBoolean("a"), false);

		parser.parse("--a=false");
		assertEquals(parser.getBoolean("a"), false);

		parser.parse("-a=false");
		assertEquals(parser.getBoolean("a"), false);

		parser.parse("--a=abc");
		assertEquals(parser.getBoolean("a"), true);

		parser.parse("--a=true");
		assertEquals(parser.getBoolean("a"), true);

		parser.parse("--a");
		assertEquals(parser.getBoolean("a"), true);

		parser.parse("-a");
		assertEquals(parser.getBoolean("a"), true);
	}

	@Test
	public void test_parse_pre_2() {
		parser.add("a", "a", Parser.STRING);
		assertEquals(parser.parse(" "), -3);
		assertEquals(parser.parse("a a"), -3);
		assertEquals(parser.parse("---a a"), -3);
		assertEquals(parser.parse("@ a"), -3);
		assertEquals(parser.parse("-a "), -3);
		assertEquals(parser.parse("--a "), -3);

		parser.parse("--a a");
		assertEquals(parser.getString("a"), "a");

		parser.add("a", "a", Parser.BOOLEAN);
		parser.parse("--a 0");
		assertEquals(parser.getBoolean("a"), false);

		parser.add("a", "ab", Parser.CHAR);
		parser.parse("-ab a");
		assertEquals(parser.getChar("a"), 'a');

		parser.add("a", "ab", Parser.INTEGER);
		parser.parse("-ab 1");
		assertEquals(parser.getInteger("a"), 1);

		parser.add("a", "a", Parser.BOOLEAN);
		parser.add("b", "b", Parser.BOOLEAN);
		parser.parse("-a -b");
		assertEquals(parser.getBoolean("a"), true);
		assertEquals(parser.getBoolean("b"), true);
	}

	@Test
	public void test_parse_pre_3() {
		parser.add("a", "a", Parser.STRING);
		assertEquals(parser.parse("\"\""), -3);
		parser.parse("--a='a'");
		assertEquals(parser.getString("a"), "a");

		parser.parse("--a 'a'");
		assertEquals(parser.getString("a"), "a");

		parser.parse("-a='a'");
		assertEquals(parser.getString("a"), "a");

		parser.parse("-a=\"a\"");
		assertEquals(parser.getString("a"), "a");

		String test = "--a='\"abc\"'";
		parser.parse(test);
		assertEquals(parser.getString("a"), "\"abc\"");

		test = "--a=\"'abc'\"";
		parser.parse(test);
		assertEquals(parser.getString("a"), "'abc'");
	}

	@Test
	public void test_parse_post_1() {
		parser.add("a", Parser.BOOLEAN);
		parser.add("b", "b", Parser.STRING);
		parser.parse("--a=true");
		assertEquals(parser.getBoolean("a"), true);
		assertEquals(parser.getString("b"), "");
		parser.parse("--b=b");
		assertEquals(parser.getString("b"), "b");

		parser.parse("--b=a --a=false");
		assertEquals(parser.getBoolean("a"), false);
		assertEquals(parser.getString("b"), "a");
	}

	@Test
	public void test_parse_var_1() {
		assertEquals(parser.parse(null), -1);
		assertEquals(parser.parse(""), -2);
		assertEquals(parser.parse("1"), -3);
		assertEquals(parser.parse("a"), -3);
		assertEquals(parser.parse("a=a"), -3);
		assertEquals(parser.parse("---a=a"), 0);
		assertEquals(parser.parse("@=a"), -3);
		assertEquals(parser.parse("-a"), -3);
		assertEquals(parser.parse("--a"), -3);
	}

	@Test
	public void test_get_pre_1() {
		parser.add("a", "a", Parser.STRING);
		assertEquals(parser.getString("a"), "");

		parser.add("a", "a", Parser.INTEGER);
		assertEquals(parser.getInteger("a"), 0);

		parser.add("a", "a", Parser.CHAR);
		assertEquals(parser.getChar("a"), '\0');

		parser.add("a", "a", Parser.BOOLEAN);
		assertEquals(parser.getBoolean("a"), false);
	}

	@Test
	public void test_get_pre_2() {
		assertEquals(parser.getString("a"), "");
		assertEquals(parser.getInteger("a"), 0);
		assertEquals(parser.getChar("a"), '\0');
		assertEquals(parser.getBoolean("a"), false);
	}

	@Test
	public void test_get_op_1() {
		parser.add("a", "a", Parser.STRING);
		parser.parse("--a=a");
		assertEquals(parser.getString("a"), "a");
		assertEquals(parser.getInteger("a"), 0);
		assertEquals(parser.getChar("a"), 'a');
		assertEquals(parser.getBoolean("a"), true);

		parser.add("a", "a", Parser.INTEGER);
		parser.parse("--a=1");
		assertEquals(parser.getInteger("a"), 1);
		assertEquals(parser.getString("a"), "1");
		assertEquals(parser.getChar("a"), '1');
		assertEquals(parser.getBoolean("a"), true);

		parser.add("a", "a", Parser.CHAR);
		parser.parse("--a=a");
		assertEquals(parser.getInteger("a"), 97);
		assertEquals(parser.getString("a"), "a");
		assertEquals(parser.getChar("a"), 'a');
		assertEquals(parser.getBoolean("a"), true);

		parser.add("a", "a", Parser.BOOLEAN);
		parser.parse("--a=true");
		assertEquals(parser.getInteger("a"), 1);
		assertEquals(parser.getString("a"), "true");
		assertEquals(parser.getChar("a"), 't');
		assertEquals(parser.getBoolean("a"), true);

	}

	@Test
	public void test_get_op_2() {
		parser.add("a", "b", Parser.STRING);
		parser.parse("--a=a");
		assertEquals(parser.getInteger("b"), 0);
		assertEquals(parser.getString("b"), "a");
		assertEquals(parser.getChar("b"), 'a');
		assertEquals(parser.getBoolean("b"), true);

		assertEquals(parser.getInteger("c"), 0);
	}

	@Test
	public void test_get_integer_post_1() {
		parser.add("a", "a", Parser.INTEGER);
		parser.parse("--a=99999999999");
		assertEquals(parser.getInteger("a"), 0);
		assertEquals(parser.getString("a"), "99999999999");
		assertEquals(parser.getChar("a"), '9');
		assertEquals(parser.getBoolean("a"), true);

		parser.parse("--a=-1");
		assertEquals(parser.getInteger("a"), -1);
		assertEquals(parser.getString("a"), "-1");
		assertEquals(parser.getChar("a"), '-');
		assertEquals(parser.getBoolean("a"), true);
	}

	@Test
	public void test_get_string_post_1() {
		parser.add("a", "a", Parser.STRING);
		parser.parse("--a=9999");
		assertEquals(parser.getInteger("a"), 9999);
		assertEquals(parser.getString("a"), "9999");
		assertEquals(parser.getChar("a"), '9');
		assertEquals(parser.getBoolean("a"), true);

		parser.parse("--a=-1");
		assertEquals(parser.getInteger("a"), 0);
		assertEquals(parser.getString("a"), "-1");
		assertEquals(parser.getChar("a"), '-');
		assertEquals(parser.getBoolean("a"), true);

		parser.parse("--a=*****");
		assertEquals(parser.getInteger("a"), 0);
		assertEquals(parser.getString("a"), "*****");
		assertEquals(parser.getChar("a"), '*');
		assertEquals(parser.getBoolean("a"), true);

		parser.parse("--a=false");
		assertEquals(parser.getBoolean("a"), false);
		assertEquals(parser.getInteger("a"), 0);
	}

	@Test
	public void test_get_char_post_1() {
		parser.add("a", "a", Parser.CHAR);
		parser.parse("--a=999");
		assertEquals(parser.getInteger("a"), 57);
		assertEquals(parser.getString("a"), "999");
		assertEquals(parser.getChar("a"), '9');

		parser.add("a", "a", Parser.CHAR);
		parser.parse("--a=*");
		assertEquals(parser.getInteger("a"), 42);
		assertEquals(parser.getString("a"), "*");
	}

	@Test
	public void test_get_boolean_post_1() {
		parser.add("a", "a", Parser.BOOLEAN);
		parser.parse("--a=0");
		assertEquals(parser.getInteger("a"), 0);
		assertEquals(parser.getString("a"), "0");
		assertEquals(parser.getChar("a"), '0');

		parser.parse("--a=1");
		assertEquals(parser.getInteger("a"), 1);
		assertEquals(parser.getString("a"), "1");
		assertEquals(parser.getChar("a"), '1');
	}

	@Test
	public void test_to_string() {
		assertEquals(parser.toString(), "OptionMap [options=\n]");
		parser.add("a", "a", Parser.BOOLEAN);
		assertEquals(parser.toString(), "OptionMap [options=\n" + "	{name=a, shortcut=a, type=2, value=}\n" + "]");
		parser.add("a", "a", Parser.STRING);
		assertEquals(parser.toString(), "OptionMap [options=\n" + "	{name=a, shortcut=a, type=3, value=}\n" + "]");
		parser.parse("--a=a");
		assertEquals(parser.toString(), "OptionMap [options=\n" + "	{name=a, shortcut=a, type=3, value=a}\n" + "]");

	}

	@Test(expected = RuntimeException.class)
	public void test_map_store_pre_1_exception() {
		map.store(null, "abc", Parser.BOOLEAN);
	}

	@Test(expected = RuntimeException.class)
	public void test_map_store_pre_1_exception2() {
		map.store("abc", null, Parser.BOOLEAN);
	}

	@Test(expected = RuntimeException.class)
	public void test_map_store_pre_1_exception3() {
		map.store("abc", "abc", -1);
	}

	@Test(expected = RuntimeException.class)
	public void test_map_store_pre_1_exception4() {
		map.store("abc", "abc", Integer.MAX_VALUE);
	}

}
