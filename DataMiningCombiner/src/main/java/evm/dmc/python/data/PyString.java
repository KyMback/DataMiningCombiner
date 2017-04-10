package evm.dmc.python.data;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("Python_String")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PyString extends JepVariable {

	public PyString() {
		super();
	}

	public PyString(String variable) {
		super(variable);

	}

	@Override
	public String getData() {
		StringBuilder var = new StringBuilder("\"");
		var.append(variableName);
		var.append("\"");
		return var.toString();
	}

}
