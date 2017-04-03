package intervalTree;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class IntervalTreeTest {
    @Test
    public void intervalTreeTest_IntegerSupported() {
        IntervalTree<Integer, String> tree = new IntervalTree<>(() -> 0);
        tree.addInterval(0, 10, "0-10");
        tree.addInterval(new Interval<>(10, 20, "10-20"));
        tree.addInterval(20, 30, "20-30");
        tree.addInterval(30, 40, "30-40");
        tree.addInterval(-20, 0, "-20 to 20");

        assertEquals("0-10", tree.get(5).get(0));
        assertEquals("0-10", tree.get(5, 6).get(0));

        Interval<Integer, String> expectedInterval = new Interval<>(0, 10, "0-10");

        assertEquals(expectedInterval, tree.getIntervals(5).get(0));
        assertEquals(expectedInterval, tree.getIntervals(5, 6).get(0));

        assertEquals("20-30", tree.get(25).get(0));
        assertEquals("-20 to 20", tree.get(-15).get(0));

        tree.addInterval(0, 100, "0-100");

        List<String> expected = new ArrayList<>();
        expected.add("0-100");
        expected.add("30-40");
        assertEquals(expected, tree.get(35));
    }

    @Test
    public void intervalTreeTest_ByteSupported() {
        IntervalTree<Byte, String> tree = new IntervalTree<>(() -> (byte)0);
        tree.addInterval((byte)0, (byte)10, "0-10");
        tree.addInterval((byte)10, (byte)20, "10-20");
        tree.addInterval((byte)20, (byte)30, "20-30");
        tree.addInterval((byte)30, (byte)40, "30-40");
        tree.addInterval((byte)-20, (byte)0, "-20 to 20");

        assertEquals("0-10", tree.get((byte)5).get(0));
        assertEquals("20-30", tree.get((byte)25).get(0));
        assertEquals("-20 to 20", tree.get((byte)-15).get(0));

        tree.addInterval((byte)0, (byte)100, "0-100");

        List<String> expected = new ArrayList<>();
        expected.add("0-100");
        expected.add("30-40");
        assertEquals(expected, tree.get((byte)35));
    }

    @Test
    public void intervalTreeTest_ShortSupported() {
        IntervalTree<Short, String> tree = new IntervalTree<>(() -> (short)0);
        tree.addInterval((short)0, (short)10, "0-10");
        tree.addInterval((short)10, (short)20, "10-20");
        tree.addInterval((short)20, (short)30, "20-30");
        tree.addInterval((short)30, (short)40, "30-40");
        tree.addInterval((short)-20, (short)0, "-20 to 20");


        assertEquals("0-10", tree.get((short)5).get(0));
        assertEquals("20-30", tree.get((short)25).get(0));
        assertEquals("-20 to 20", tree.get((short)-15).get(0));



        tree.addInterval((short)0, (short)100, "0-100");

        List<String> expected = new ArrayList<>();
        expected.add("0-100");
        expected.add("30-40");
        assertEquals(expected, tree.get((short)35));
    }

    @Test
    public void intervalTreeTest_DoubleSupported() {
        IntervalTree<Double, String> tree = new IntervalTree<>(() -> 0.0);
        tree.addInterval(0.0, 10.0, "0-10");
        tree.addInterval(10.0, 20.0, "10-20");
        tree.addInterval(20.0, 30.0, "20-30");
        tree.addInterval(30.0, 40.0, "30-40");
        tree.addInterval(-20.0, 0.0, "-20 to 20");

        assertEquals("0-10", tree.get(5.0).get(0));
        assertEquals("20-30", tree.get(25.0).get(0));
        assertEquals("-20 to 20", tree.get(-15.0).get(0));

        tree.addInterval(0.0, 100.0, "0-100");

        List<String> expected= new ArrayList<>();
        expected.add("0-100");
        expected.add("30-40");
        assertEquals(expected, tree.get(35.0));
    }

    @Test
    public void intervalTreeTest_FloatSupported() {
        IntervalTree<Float, String> tree = new IntervalTree<>(() -> 0.0F);
        tree.addInterval(0.0F, 10.0F, "0-10");
        tree.addInterval(10.0F, 20.0F, "10-20");
        tree.addInterval(20.0F, 30.0F, "20-30");
        tree.addInterval(30.0F, 40.0F, "30-40");
        tree.addInterval(-20.0F, 0.0F, "-20 to 0");

        assertEquals("0-10", tree.get(5.0F).get(0));
        assertEquals("20-30", tree.get(25.0F).get(0));
        assertEquals("-20 to 0", tree.get(-15.0F).get(0));

        tree.addInterval(0.0F, 100.0F, "0-100");

        List<String> expected= new ArrayList<>();
        expected.add("0-100");
        expected.add("30-40");
        assertEquals(expected, tree.get(35.0F));
    }

    @Test
    public void intervalTreeTest_BigIntegerSupported() {
        IntervalTree<BigInteger, String> tree = new IntervalTree<>(() -> BigInteger.valueOf(0));
        tree.addInterval(BigInteger.valueOf(0), BigInteger.valueOf(10), "0-10");
        tree.addInterval(BigInteger.valueOf(10), BigInteger.valueOf(20), "10-20");
        tree.addInterval(BigInteger.valueOf(20), BigInteger.valueOf(30), "20-30");
        tree.addInterval(BigInteger.valueOf(30), BigInteger.valueOf(40), "30-40");
        tree.addInterval(BigInteger.valueOf(40).negate(), BigInteger.valueOf(30).negate(), "negative 30-40");

        assertEquals("0-10", tree.get(BigInteger.valueOf(5)).get(0));
        assertEquals("20-30", tree.get(BigInteger.valueOf(25)).get(0));
        assertEquals("negative 30-40", tree.get(BigInteger.valueOf(35).negate()).get(0));

        tree.addInterval(BigInteger.valueOf(0), BigInteger.valueOf(100), "0-100");


        List<String> expected= new ArrayList<>();
        expected.add("0-100");
        expected.add("30-40");
        assertEquals(expected, tree.get(BigInteger.valueOf(35)));
    }

    @Test
    public void intervalTreeTest_BigDecimalSupported() {
        IntervalTree<BigDecimal, String> tree = new IntervalTree<>(() -> BigDecimal.valueOf(0.0));
        tree.addInterval(BigDecimal.valueOf(0), BigDecimal.valueOf(10), "0-10");
        tree.addInterval(BigDecimal.valueOf(10), BigDecimal.valueOf(20), "10-20");
        tree.addInterval(BigDecimal.valueOf(20), BigDecimal.valueOf(30), "20-30");
        tree.addInterval(BigDecimal.valueOf(30), BigDecimal.valueOf(40), "30-40");
        tree.addInterval(BigDecimal.valueOf(40).negate(), BigDecimal.valueOf(30).negate(), "negative 30-40");

        assertEquals("0-10", tree.get(BigDecimal.valueOf(5)).get(0));
        assertEquals("20-30", tree.get(BigDecimal.valueOf(25)).get(0));
        assertEquals("negative 30-40", tree.get(BigDecimal.valueOf(35).negate()).get(0));

        tree.addInterval(BigDecimal.valueOf(0), BigDecimal.valueOf(100), "0-100");

        List<String> expected= new ArrayList<>();
        expected.add("0-100");
        expected.add("30-40");
        assertEquals(expected, tree.get(BigDecimal.valueOf(35)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void intervalTreeTest_InvertedRange_ThrowsIllegalArgument() {
        IntervalTree<Integer, String> tree = new IntervalTree<>(() -> 0);
        tree.addInterval(20, 10, "20 to 10");
        assertEquals("20 to 10", tree.get(15).get(0));
    }

    @Test
    public void intervalTreeTest_ValueNotInTree() {
        IntervalTree<Integer, String> tree = new IntervalTree<>(() -> 0);
        tree.addInterval(0, 10, "0-10");
        tree.addInterval(10, 20, "10-20");
        tree.addInterval(20, 30, "20-30");
        tree.addInterval(30, 40, "30-40");
        tree.addInterval(0, 100, "0-100");

        List<String> expected = new ArrayList<>();
        assertEquals(expected, tree.get(135));
    }

    @Test
    public void intervalTreeTest_RangeOfSizeOne() {
        IntervalTree<Integer, String> tree = new IntervalTree<>(() -> 0);
        tree.addInterval(0, 10, "0-10");
        tree.addInterval(10, 20, "10-20");
        tree.addInterval(20, 30, "20-30");
        tree.addInterval(30, 40, "30-40");
        tree.addInterval(0, 100, "0-100");
        tree.addInterval(32, 32, "32");

        List<String> expected = new ArrayList<>();
        expected.add("0-100");
        expected.add("30-40");
        expected.add("32");
        assertEquals(expected, tree.get(32));
    }

    @SuppressWarnings("unchecked")
    @Test(expected=ClassCastException.class)
    public void intervalTreeTest_MixedDataTypesNotSupported() {
        IntervalTree tree = new IntervalTree<>(() -> 0);
        tree.addInterval(0, 10, "0-10");
        tree.addInterval(BigDecimal.valueOf(10.0), BigDecimal.valueOf(20.0), "10-20");
        tree.addInterval(BigInteger.valueOf(20), BigInteger.valueOf(30), "20-30");
        tree.addInterval(30, 40, "30-40");
        tree.addInterval(0.0, 100.0, "0-100");

        List<String> expected = new ArrayList<>();
        assertEquals(expected, tree.get(135));
    }
}
