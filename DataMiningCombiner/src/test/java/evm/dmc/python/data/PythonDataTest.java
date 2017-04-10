package evm.dmc.python.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import evm.dmc.core.Framework;
import evm.dmc.core.data.Data;
import evm.dmc.python.DMCPythonConfig;
import evm.dmc.python.PythonFW;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DMCPythonConfig.class)
@TestPropertySource("classpath:jeptest.properties")
public class PythonDataTest {

	@Autowired
	@PythonFW
	private Framework framework;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public final void testPyString() {
		String strData = "test_test/test";
		Data data = framework.getData(PyString.class);
		assertNotNull(data);
		data.setData(strData);
		// PyString encapsulates contained data in quotes on returning by
		// getData
		assertEquals("\"" + strData + "\"", data.getData());
	}

	@Test
	public final void testJepVariable() {
		String someVar = "valInPyth";
		Data data = framework.getData(PyVar.class);
		assertNotNull(data);
		data.setData(someVar);

		assertEquals(someVar, data.getData());
	}

}
