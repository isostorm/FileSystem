import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class DirectoryTest {
	private File testFileA, testFileB, testFileC, testFileABC;
	private Directory testDirectoryA, testDirectoryB, testDirectoryC;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		testFileA = new File("FileA", FileType.JAVA);
		testFileB = new File("FileB", FileType.PDF);
		testFileC = new File("FileC", FileType.TXT);
		testFileABC = new File("FileC", FileType.PDF);
		testDirectoryA = new Directory("A");
		testDirectoryB = new Directory("B");
		testDirectoryC = new Directory("C");
	}

	@Test
	public void testCanMoveTo() {
		assertFalse(testDirectoryA.canMoveTo(testDirectoryA));
		assertTrue(testFileA.canMoveTo(testDirectoryA));
		assertTrue(testFileB.canMoveTo(testDirectoryA));
		assertTrue(testFileC.canMoveTo(testDirectoryA));
		assertTrue(testFileABC.canMoveTo(testDirectoryA));
		testDirectoryC.move(testDirectoryB);
		testDirectoryA.move(testDirectoryC);
		assertTrue(testDirectoryA.canMoveTo(testDirectoryB));
		assertFalse(testDirectoryB.canMoveTo(testDirectoryA));
		assertTrue(testDirectoryC.isDirectOrIndirectSubdirectoryOf(testDirectoryB));
		
	}



	@Test
	public void testGetNbItems() {
		testFileA.move(testDirectoryA);
		testFileB.move(testDirectoryA);/*
		testFileC.move(testDirectoryA);*/
		assertEquals(testDirectoryA.getNbItems(), 3);
	}
/*
	@Test
	public void testGetItemAt() {
		fail("Not yet implemented");
	}

	@Test
	public void testHasAsItem() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetIndexOf() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveItem() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetItem() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddItem() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsDirectOrIndirectSubdirectoryOf() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsDirectOrIndirectSubdirectoryOfRecursive() {
		fail("Not yet implemented");
	}

	@Test
	public void testExists() {
		fail("Not yet implemented");
	}

	@Test
	public void testCanHaveAsFile() {
		fail("Not yet implemented");
	}

	@Test
	public void testDiskItemStringBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testDiskItemString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetName() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCreationTime() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetLastModificationTime() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetLastModificationTime() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetCurrentModificationTime() {
		fail("Not yet implemented");
	}

	@Test
	public void testHasOverlappingUsePeriod() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsWritable() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetWritable() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDirectory() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRoot() {
		fail("Not yet implemented");
	}

	@Test
	public void testMakeRoot() {
		fail("Not yet implemented");
	}

	@Test
	public void testPrecedes() {
		fail("Not yet implemented");
	}

	@Test
	public void testMove() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsTerminated() {
		fail("Not yet implemented");
	}

	@Test
	public void testTerminate() {
		fail("Not yet implemented");
	}*/

}
