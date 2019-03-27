import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import st.Parser;

public class Task3_TDD_1 {

	private Parser parser;
	private List<Integer> expected;

	@Before
	public void set_up() {
		parser = new Parser();
		expected = asList();
	}

	@Test
	public void test_parse_var_1() {
		expected = asList(1);
		parser.add("a", "a", Parser.STRING);
		parser.parse("--a=1");
		assertEquals(parser.getIntegerList("a"), expected);

		parser.add("a", "a", Parser.BOOLEAN);
		parser.parse("--a=1");
		assertEquals(parser.getIntegerList("a"), expected);

		parser.add("a", "a", Parser.CHAR);
		parser.parse("--a=1");
		assertEquals(parser.getIntegerList("a"), expected);

		parser.add("a", "a", Parser.INTEGER);
		parser.parse("--a=1");
		assertEquals(parser.getIntegerList("a"), expected);

		parser.parse("--a=1214312");
		expected = asList(1214312);
		assertEquals(parser.getIntegerList("a"), expected);

		parser.parse("--a=9999999999");
		expected = asList();
		assertEquals(parser.getIntegerList("a"), expected);
	}

	@Test
	public void test_parse_var_2() {
		parser.add("a", "a", Parser.STRING);
		parser.parse("--a=abc");
		assertEquals(parser.getIntegerList("a"), expected);

		parser.add("a", "a", Parser.BOOLEAN);
		parser.parse("--a=true");
		assertEquals(parser.getIntegerList("a"), asList(1));

		parser.add("a", "a", Parser.CHAR);
		parser.parse("--a=a");
		assertEquals(parser.getIntegerList("a"), expected);

		expected = asList(1);
		parser.add("a", "a", Parser.STRING);
		parser.parse("--a=ab1c");
		assertEquals(parser.getIntegerList("a"), expected);
	}

	@Test
	public void test_parse_var_3() {
		parser.add("a", "a", Parser.STRING);
		parser.parse("--a=-1");
		expected = asList(-1);
		assertEquals(parser.getIntegerList("a"), expected);

		parser.parse("--a=-1234");
		expected = asList(-1234);
		assertEquals(parser.getIntegerList("a"), expected);

		parser.parse("--a=-999999999");
		expected = asList();
		assertEquals(parser.getIntegerList("a"), expected);
	}

	@Test
	public void test_parse_op_1() {
		expected = asList(1, 2);
		parser.add("a", "a", Parser.STRING);
		parser.parse("--a=1,2");
		assertEquals(parser.getIntegerList("a"), expected);

		parser.parse("--a=1 2");
		assertEquals(parser.getIntegerList("a"), expected);

		parser.parse("--a=1abc2");
		assertEquals(parser.getIntegerList("a"), expected);

		expected = asList(1, 2, 3);
		parser.parse("--a=1,2,3");
		assertEquals(parser.getIntegerList("a"), expected);

		expected = asList(111, 222, 333);
		parser.parse("--a=111,222,333");
		assertEquals(parser.getIntegerList("a"), expected);

		parser.parse("--a=,111,222,333,");
		assertEquals(parser.getIntegerList("a"), expected);

		parser.parse("--a=((111,222,333))");
		assertEquals(parser.getIntegerList("a"), expected);
	}

	@Test
	public void test_parse_op_2() {
		expected = asList(1, -1);
		parser.add("a", "a", Parser.STRING);
		parser.parse("--a=1,-1");
		assertEquals(parser.getIntegerList("a"), expected);

		parser.parse("--a=1 -1");
		assertEquals(parser.getIntegerList("a"), expected);

		parser.parse("--a=1abc-1");
		assertEquals(parser.getIntegerList("a"), expected);

		expected = asList(1, -2, 3);
		parser.parse("--a=1,-2,3");
		assertEquals(parser.getIntegerList("a"), expected);

		expected = asList(111, -222, 333);
		parser.parse("--a=((111,-222,333))");
		assertEquals(parser.getIntegerList("a"), expected);
	}

	@Test
	public void test_parse_op_3() {
		expected = asList(1, 2);
		parser.add("a", "a", Parser.STRING);
		parser.parse("--a=1-2");
		assertEquals(parser.getIntegerList("a"), expected);

		parser.parse("--a=2-1");
		assertEquals(parser.getIntegerList("a"), expected);

		parser.parse("--a=4-1");
		expected = asList(1, 2, 3, 4);
		assertEquals(parser.getIntegerList("a"), expected);

		parser.parse("--a=-3--1");
		expected = asList(-3, -2, -1);
		assertEquals(parser.getIntegerList("a"), expected);

		parser.parse("--a=-1--3");
		assertEquals(parser.getIntegerList("a"), expected);

		parser.parse("--a=-3-1");
		expected = asList(-3, -2, -1, 0, 1);
		assertEquals(parser.getIntegerList("a"), expected);

		parser.parse("--a=1--3");
		assertEquals(parser.getIntegerList("a"), expected);

	}

	@Test
	public void test_parse_op_4() {
		parser.add("a", "a", Parser.STRING);

		expected = asList(1, 2, 3);
		parser.parse("--a=1-2,3");
		assertEquals(parser.getIntegerList("a"), expected);

		expected = asList(0, 1, 2);
		parser.parse("--a=1-2,0");
		assertEquals(parser.getIntegerList("a"), expected);

		parser.parse("--a=-3-1, 1-2");
		expected = asList(-3, -2, -1, 0, 1, 1, 2);
		assertEquals(parser.getIntegerList("a"), expected);

		parser.parse("--a=-3-1!!!1-2");
		assertEquals(parser.getIntegerList("a"), expected);

	}

	@Test
	public void test_parse_op_5() {
		parser.add("a", "a", Parser.STRING);
		expected = asList(1, 1, 1);
		parser.parse("--a=1=1=1");
		assertEquals(parser.getIntegerList("a"), expected);

		expected = asList(0, 0, 1, 1, 2, 2);
		parser.parse("--a=0-2,0-2");
		assertEquals(parser.getIntegerList("a"), expected);

		expected = asList(-1, -1, -1);
		parser.parse("--a=-1=-1=-1");
		assertEquals(parser.getIntegerList("a"), expected);
	}

	@Test
	public void test_parse_op_6() {
		parser.add("a", "a", Parser.STRING);
		parser.parse("--a=3,2,1");
		assertEquals(parser.getIntegerList("a"), asList(1, 2, 3));

		parser.parse("--a=2,3,1");
		assertEquals(parser.getIntegerList("a"), expected);

		parser.parse("--a=-1,-2,-3");
		assertEquals(parser.getIntegerList("a"), asList(-3, -2, -1));

		parser.parse("--a=-2,-3,-1");
		assertEquals(parser.getIntegerList("a"), expected);
	}

	@Test
	public void test_parse_op_7() {
		parser.add("a", "a", Parser.STRING);
		parser.parse("--a=1,2");
		assertEquals(parser.getIntegerList("a"), asList(1, 2));
		assertEquals(parser.getInteger("a"), 1);
		assertEquals(parser.getString("a"), "1,2");
		assertEquals(parser.getChar("a"), '1');
		assertEquals(parser.getBoolean("a"), true);

		parser.parse("--a=111,222,333");
		assertEquals(parser.getIntegerList("a"), asList(111, 222, 333));
		assertEquals(parser.getInteger("a"), 111);
		assertEquals(parser.getString("a"), "111,222,333");
		assertEquals(parser.getChar("a"), '1');
		assertEquals(parser.getBoolean("a"), true);

		parser.parse("--a=-3-1, 1-2");
		assertEquals(parser.getIntegerList("a"), asList(-3, -2, -1, 0, 1, 1, 2));
		assertEquals(parser.getInteger("a"), -3);
		assertEquals(parser.getString("a"), "-3-1, 1-2");
		assertEquals(parser.getChar("a"), '-');
		assertEquals(parser.getBoolean("a"), true);
	}

	@Test
	public void test_parse_pre_1() {
		parser.add("a", "a", Parser.STRING);
		parser.parse("--a 1");
		assertEquals(parser.getIntegerList("a"), asList(1));

		parser.add("a", "ab", Parser.CHAR);
		parser.parse("-ab 1");
		assertEquals(parser.getIntegerList("a"), asList(1));

		parser.add("a", "a", Parser.STRING);
		parser.add("b", "b", Parser.STRING);
		parser.parse("-a=1 -b=1");
		assertEquals(parser.getIntegerList("a"), asList(1));
		assertEquals(parser.getIntegerList("a"), asList(1));
	}

	@Test
	public void test_parse_pre_2() {
		parser.add("a", "a", Parser.STRING);
		parser.parse("--a='1'");
		assertEquals(parser.getIntegerList("a"), asList(1));

		parser.parse("-a=\"1\"");
		assertEquals(parser.getIntegerList("a"), asList(1));

		String test = "--a='\"1\"2'";
		parser.parse(test);
		assertEquals(parser.getIntegerList("a"), asList(1, 2));
	}

	@Test
	public void test_parse_pre_3() {
		parser.add("a", "a", Parser.STRING);
		parser.parse("--a=-1-");
		assertEquals(parser.getIntegerList("a"), asList());

		parser.parse("--a=1,2-");
		assertEquals(parser.getIntegerList("a"), asList());

		parser.parse("--a=1,2-ghs");
		assertEquals(parser.getIntegerList("a"), asList());

	}

}
