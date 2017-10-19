package evm.dmc.web.testing.project;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import evm.dmc.web.testing.hello.HelloController;
import evm.dmc.web.testing.project.ProjectPropertiesController;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ProjectPropertiesController.class, secure = false)
public class ProjectPropertiesControllerTest {

	@Test
	@Ignore
	public final void testNewProject() {
		fail("Not yet implemented"); // TODO
	}

}