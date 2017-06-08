package evm.dmc.python.function;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service("Python_ScaleDataset")
@PropertySource("classpath:jep.properties")
public class ScaleDataset extends AbstractPythonFunction {
	private final static String name = "Python_ScaleDataset";
	private final static Integer ARGS_COUNT = 1; // TODO must also accept
													// attributes like list of
													// attributes which need to
													// have in rest

	@Value("${jep.dm_get_scaled}")
	String scale;

	@Value("${jep.dm_get_scaled_desc}")
	String description;

	public ScaleDataset() {
		super();
	}

	@PostConstruct
	public void init() {
		super.setFunction(scale);
		// Data result = super.fw.getData(PyVar.class);
		// super.setResult(result);
		// TODO
	}

	@Override
	public Integer getArgsCount() {

		return ARGS_COUNT;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

}
