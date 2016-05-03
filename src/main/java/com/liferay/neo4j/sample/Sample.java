package com.liferay.neo4j.sample;

import com.liferay.neo4j.result.StringResult;
import org.neo4j.procedure.Procedure;

import java.util.stream.Stream;

/**
 * @author mate.thurzo
 */
public class Sample {

	@Procedure
	public Stream<StringResult> HelloWorld() {
		return Stream.of(new StringResult("Hello World!"));
	}

}
