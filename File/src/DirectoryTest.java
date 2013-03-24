import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class DirectoryTest {
	private File testFileA, testFileB, testFileC, testFileABC;
	private Directory testDirectoryA, testDirectoryB, testDirectoryC, testDirectoryD;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		testFileA = new File("FileA", FileType.JAVA);
		testFileB = new File("FileB", FileType.PDF);
		testFileC = new File("FileC", FileType.TXT);
		testFileABC = new File("FileABC", FileType.PDF);
		testDirectoryA = new Directory("A");
		testDirectoryB = new Directory("B");
		testDirectoryC = new Directory("C");
		testDirectoryD = new Directory("D");
		testFileA.move(testDirectoryD);
		testFileB.move(testDirectoryD);
		testFileC.move(testDirectoryD);
		testFileABC.move(testDirectoryD);
		testDirectoryA.move(testDirectoryD);
		testDirectoryB.move(testDirectoryD);
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
		assertEquals(testDirectoryA.getNbItems(), 0);
		testFileA.move(testDirectoryA);
		assertEquals(testDirectoryA.getNbItems(), 1);
		testFileB.move(testDirectoryA);
		testFileC.move(testDirectoryA);
		assertEquals(testDirectoryA.getNbItems(), 3);
		
	}
	@Test
	public void testGetItemAt() {
		testFileABC.move(testDirectoryA);
		testFileC.move(testDirectoryA);
		testFileA.move(testDirectoryA);
		testFileB.move(testDirectoryA);
		testDirectoryB.move(testDirectoryA);
		
		assertEquals(testDirectoryA.getItemAt(1), testDirectoryB);
		assertEquals(testDirectoryA.getItemAt(2), testFileA);
		assertEquals(testDirectoryA.getItemAt(3), testFileABC);
		assertEquals(testDirectoryA.getItemAt(4), testFileB);
		assertEquals(testDirectoryA.getItemAt(5), testFileC);
	}
	@Test
	public void testHasAsItem() {
		testFileABC.move(testDirectoryC);
		testDirectoryA.move(testDirectoryB);
		testDirectoryB.move(testDirectoryC);
		assertTrue(testDirectoryC.hasAsItem(testFileABC));
		assertTrue(testDirectoryB.hasAsItem(testDirectoryA));
		assertTrue(testDirectoryC.hasAsItem(testDirectoryB));
		assertFalse(testDirectoryC.hasAsItem(testDirectoryA));
	}
	@Test
	public void testGetIndexOf() {
		testFileABC.move(testDirectoryA);
		testFileC.move(testDirectoryA);
		testFileA.move(testDirectoryA);
		testFileB.move(testDirectoryA);
		testDirectoryB.move(testDirectoryA);
		assertEquals(testDirectoryA.getIndexOf(testDirectoryB), 1);
		assertEquals(testDirectoryA.getIndexOf(testFileA), 2);
		assertEquals(testDirectoryA.getIndexOf(testFileABC), 3);
		assertEquals(testDirectoryA.getIndexOf(testFileB), 4);
		assertEquals(testDirectoryA.getIndexOf(testFileC), 5);
	}
	@Test
	public void testRemoveItem() {
		assertTrue(testDirectoryD.hasAsItem(testFileA));
		testDirectoryD.removeItem(testFileA);
		assertFalse(testDirectoryD.hasAsItem(testFileA));
		
		assertTrue(testDirectoryD.hasAsItem(testDirectoryA));
		testDirectoryD.removeItem(testDirectoryA);
		assertFalse(testDirectoryD.hasAsItem(testDirectoryA));
		
	}
	
	@Test
	public void testGetItem() {
		assertEquals(testDirectoryD.getItem("A"), testDirectoryA);
		assertEquals(testDirectoryD.getItem("FileA"), testFileA);
	}
	
	@Test
	public void testAddItem() {
		assertFalse(testDirectoryA.hasAsItem(testFileA));
		testDirectoryA.addItem(testFileA);
		assertTrue(testDirectoryA.hasAsItem(testFileA));
		
		assertFalse(testDirectoryA.hasAsItem(testDirectoryB));
		testDirectoryA.addItem(testDirectoryB);
		assertTrue(testDirectoryA.hasAsItem(testDirectoryB));
	}

	@Test
	public void testIsDirectOrIndirectSubdirectoryOf() {
		assertFalse(testDirectoryA.isDirectOrIndirectSubdirectoryOf(testDirectoryB));
		testDirectoryA.move(testDirectoryB);
		assertTrue(testDirectoryA.isDirectOrIndirectSubdirectoryOf(testDirectoryB));
		assertFalse(testDirectoryA.isDirectOrIndirectSubdirectoryOf(testDirectoryC));
		testDirectoryB.move(testDirectoryC);
		assertTrue(testDirectoryA.isDirectOrIndirectSubdirectoryOf(testDirectoryC));
	}


	@Test
	public void testExists() {
		assertFalse(testDirectoryA.exists("FileA"));
		testFileA.move(testDirectoryA);
		assertTrue(testDirectoryA.exists("FileA"));
		
		assertFalse(testDirectoryA.exists("B"));
		testDirectoryB.move(testDirectoryA);
		assertTrue(testDirectoryA.exists("B"));
	}


	@Test
	public void testGetName() {
		assertEquals(testDirectoryB.getName(), "B");
	}

	@Test
	public void testSetName() {
		assertEquals(testDirectoryB.getName(), "B");
		testDirectoryB.setName("directoryB");
		assertEquals(testDirectoryB.getName(), "directoryB");
	}


	@Test
	public void testGetDirectory() {
		testFileA.move(testDirectoryA);
		testDirectoryA.move(testDirectoryB);
		assertEquals(testFileA.getDirectory(), testDirectoryA);
		assertEquals(testDirectoryA.getDirectory(), testDirectoryB);
	}
	@Test
	public void testGetRoot() {
		testFileA.move(testDirectoryA);
		Directory testDirectoryX = new Directory("X");
		testDirectoryX.move(testDirectoryA);
		assertEquals(testDirectoryX.getRoot(), testDirectoryD);
		assertEquals(testFileA.getRoot(), testDirectoryD);
	}

	@Test
	public void testMakeRoot() {
		assertEquals(testDirectoryA.getRoot(), testDirectoryD);
		testDirectoryA.makeRoot();
		assertEquals(testDirectoryA.getRoot(), testDirectoryA);
	}

	@Test
	public void testPrecedes() {
		assertTrue(testFileA.precedes("FileB"));
		assertFalse(testFileB.precedes("FileB"));
		assertFalse(testFileB.precedes("FileA"));
	}

	@Test
	public void testMove() {
		assertFalse(testDirectoryA.hasAsItem(testFileA));
		testFileA.move(testDirectoryA);
		assertTrue(testDirectoryA.hasAsItem(testFileA));
		
		assertFalse(testDirectoryA.hasAsItem(testDirectoryB));
		testDirectoryB.move(testDirectoryA);
		assertTrue(testDirectoryA.hasAsItem(testDirectoryB));
	}

	@Test
	public void testIsTerminated() {
		assertFalse(testFileA.isTerminated());
		assertTrue(testFileA.canMoveTo(null));
		testFileA.terminate();
		assertTrue(testFileA.isTerminated());
	}
/*
	@Test
	public void testTerminate() {
		fail("Not yet implemented");
	}*/

}
