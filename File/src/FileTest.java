import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class FileTest {
	private File testFileA, testFileB;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@Before
	public void setUp() throws Exception {
		testFileA = new File("FileA");
		testFileB = new File("FileB");
	}

	@Test
	public void testGetName() throws Exception{
		File testFileIllegalName = new File("!!!");
		File testFileFullName = new File("Ab_-0");
		assertEquals(testFileA.getName(), "FileA");
		assertEquals(testFileIllegalName.getName(), "X");
		assertEquals(testFileFullName.getName(), "Ab_-0");
		assertEquals(new File("O").getName(), "O");
	}

	@Test
	public void testSetName() throws Exception {
		testFileA.setName("abc_-0");
		assertEquals(testFileA.getName(), "abc_-0");
	}

	@Test
	public void testIsValidSize() {
		assertFalse(File.isValidSize(-1));
		assertTrue(File.isValidSize(Integer.MAX_VALUE));
		assertTrue(File.isValidSize(300));
	}

	@Test
	public void testGetSize() {
		assertEquals(testFileA.getSize(), 0);
	}

	@Test (expected = NotWritableException.class)
	public void testSetSizeFail() throws Exception {
		File fileNotWritable = new File("abc", 10, false);
		File fileWritable = new File("abczezeze", 20, true);
		fileWritable.setSize(30);
		assertEquals(fileWritable.getSize(), 30);
		fileNotWritable.setSize(100);
		fail("Exception expected");
	}

	@Test
	public void testGetCreationTime() throws NotWritableException {
		assertEquals((new Date()).compareTo((new File("abc")).getCreationTime()), 0);
	}

	@Test
	public void testIsWritable() {
		testFileA.setWritable(true);
		assertTrue(testFileA.isWritable());
		testFileB.setWritable(false);
		assertFalse(testFileB.isWritable());
	}

}
