import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * BinaryTreeTest - Test class for Binary Search Tree. 
 * 
 */

public class BinaryTreeTest {
	
	BinaryTree<Integer> tree;
	
	@Before
    public void beforeEachTestMethod() {
	    tree = new NonEmptyBinaryTree<Integer>(7);
	    tree = tree.insert(3)
	    	.insert(1)
	    	.insert(5)
	    	.insert(4)
	    	.insert(11)
	    	.insert(10)
	    	.insert(15);
    }

	@Test(timeout=1000)
	public void testInsert() {
        Assert.assertEquals("7 3 1 5 4 11 10 15", tree.preOrderShow());
	}

	@Test(timeout=1000)
	public void testRemoveNodeWithNoChild() {
        Assert.assertEquals("7 3 1 5 4 11 10", tree.delete(15).preOrderShow());
	}

	@Test(timeout=1000)
	public void testRemoveNodeNotFound() {
        Assert.assertEquals("7 3 1 5 4 11 10 15", tree.delete(1000).preOrderShow());
	}

	@Test(timeout=1000)
	public void testRemoveNodeWithOneChild() {
        Assert.assertEquals("7 3 1 4 11 10 15", tree.delete(5).preOrderShow());
	}

	@Test(timeout=1000)
	public void testRemoveNodeWithTwoChildren() {
		String result = tree.delete(3).preOrderShow();

		List<String> expected = Arrays.asList("7 4 1 5 11 10 15", "7 1 5 4 11 10 15");

		Assert.assertTrue(expected.contains(result));
	}

	@Test(timeout=1000)
	public void testRemoveRootNode() {
		String result = tree.delete(7).preOrderShow();

		List<String> expected = Arrays.asList("10 3 1 5 4 11 15", "5 3 1 4 11 10 15");

		Assert.assertTrue(expected.contains(result));
	}

//	@Before
//	public void beforeEachTestMethod() {
//	    tree = new NonEmptyBinaryTree<Integer>(10);
//	    tree = tree.insert(8)
//	    	.insert(5)
//	    	.insert(3)
//	    	.insert(4)
//	    	.insert(7)
//	    	.insert(6)
//	    	.insert(20)
//	    	.insert(25);
//    }
//
//	@Test(timeout = 1000)
//	public void testInsert() {
//		Assert.assertEquals("10 8 5 3 4 7 6 20 25", tree.preOrderShow());
//	}
//
//	@Test(timeout = 1000)
//	public void testInsertLeft() {
//		tree = tree.insert(2);
//		Assert.assertEquals("10 8 5 3 2 4 7 6 20 25", tree.preOrderShow());
//	}
//
//	@Test(timeout = 1000)
//	public void testInsertDuplicate() {
//		tree = tree.insert(7);
//		Assert.assertEquals("10 8 5 3 4 7 6 20 25", tree.preOrderShow());
//	}
//
//	@Test(timeout = 1000)
//	public void testRemoveNodeWithNoChild() {
//		tree = tree.delete(25);
//		Assert.assertEquals("10 8 5 3 4 7 6 20", tree.preOrderShow());
//	}
//
//	@Test(timeout = 1000)
//	public void testRemoveNodeWithOneChildLeft() {
//		tree = tree.delete(7);
//		Assert.assertEquals("10 8 5 3 4 6 20 25", tree.preOrderShow());
//	}
//
//	@Test(timeout = 1000)
//	public void testRemoveNodeWithOneChildRight() {
//		tree = tree.delete(3);
//		Assert.assertEquals("10 8 5 4 7 6 20 25", tree.preOrderShow());
//	}
//
//	@Test(timeout = 1000)
//	public void testRemoveNodeNotFound() {
//		tree = tree.delete(1000);
//		Assert.assertEquals("10 8 5 3 4 7 6 20 25", tree.preOrderShow());
//	}
//
//	@Test(timeout = 1000)
//	public void testRemoveNodeWithTwoChildren() {
//		tree = tree.delete(5);
//		Assert.assertTrue(tree.preOrderShow().equals("10 8 4 3 7 6 20 25") || tree.preOrderShow().equals("10 8 6 3 4 7 20 25"));
//	}
//
//	@Test(timeout = 1000)
//	public void testRemoveRootNode() {
//		System.out.println(tree.preOrderShow());
//		tree = tree.delete(10);
////		Assert.assertEquals("8 5 3 4 7 6 20 25", tree.preOrderShow());
//		Assert.assertEquals("20 8 5 3 4 7 6 25", tree.preOrderShow());
//
////		Assert.assertTrue(tree.preOrderShow().equals("8 5 3 4 7 6 20 25") || tree.preOrderShow().equals("20 8 5 3 4 7 6 25"));
//	}
//
//	@Test(timeout = 1000)
//	public void testRemoveAllNodes() {
//		tree = tree.delete(10).delete(8).delete(5).delete(3).delete(4).delete(7).delete(6).delete(20).delete(25);
//		System.out.println(tree.preOrderShow());
//		Assert.assertTrue(tree.isEmpty());
//	}
}
