package com.sunny.utils;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FileUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testReadFile() {
		try {
			String con = FileUtil.readFile("application.yaml");
			assertTrue(con == null);
			con = FileUtil.readFile("application.yml");
			assertTrue(con != null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test(expected=NullPointerException.class)
	public void testWriteFile() {
		try {
			FileUtil.writeFile("application.yaml", "i love you judy");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
