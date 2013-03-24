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
		testFileA = new File("FileA", FileType.JAVA);
		testFileB = new File("FileB", FileType.JAVA);
	}

	@Test
	public void testGetName() throws Exception{
		File testFileIllegalName = new File("!!!", FileType.JAVA);
		File testFileFullName = new File("Ab_-0", FileType.JAVA);
		assertEquals(testFileA.getName(), "FileA");
		assertEquals(testFileIllegalName.getName(), "X");
		assertEquals(testFileFullName.getName(), "Ab_-0");
		assertEquals(new File("O", FileType.JAVA).getName(), "O");
	}

	@Test
	public void testSetName() throws Exception {
		testFileA.setName("abc_-0");
		assertEquals(testFileA.getName(), "abc_-0");
	}

	@Test
	public void testIsValidSize() {
		assertFalse(File.canHaveAsSize(-1));
		assertTrue(File.canHaveAsSize(Integer.MAX_VALUE));
		assertTrue(File.canHaveAsSize(300));
	}

	@Test
	public void testGetSize() {
		assertEquals(testFileA.getSize(), 0);
	}

	@Test (expected = NotWritableException.class)
	public void testSetSizeFail() throws Exception {
		File fileNotWritable = new File("abc", 10, false, FileType.JAVA);
		File fileWritable = new File("abczezeze", 20, true, FileType.JAVA);
		fileWritable.enlarge(20);
		assertEquals(fileWritable.getSize(), 30);
		fileNotWritable.enlarge(80);
		fail("Exception expected");
	}

	@Test
	public void testGetCreationTime() throws NotWritableException {
		assertEquals((new Date()).compareTo((new File("abc", FileType.TXT)).getCreationTime()), 0);
	}

	@Test
	public void testIsWritable() {
		testFileA.setWritable(true);
		assertTrue(testFileA.isWritable());
		testFileB.setWritable(false);
		assertFalse(testFileB.isWritable());
	}

}
