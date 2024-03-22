package org.september.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.september.core.component")
public class AllianceCoreAutoConfig {
	public AllianceCoreAutoConfig() {
		System.out.println();
	}
}
