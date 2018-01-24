package tr.org.liderahenk.script.constants;

public class ScriptConstants {

	public static final String PLUGIN_NAME = "script";

	public static final String PLUGIN_VERSION = "1.1";

	public static final class EDITORS {
		public static final String SCRIPT_DEFINITON_EDITOR = "tr.org.liderahenk.script.editors.ScriptDefinitionEditor";
	}

	public static final class TEMPLATES {
		public static final String BASH = "#!/bin/bash\n\nset -e\n\n";
		public static final String RUBY = "#!/usr/bin/env ruby\n\n";
		public static final String PERL = "#!/usr/bin/perl\nuse strict;\nuse warnings;\n\n";
		public static final String PYTHON = "#!/usr/bin/python\n# -*- coding: utf-8 -*-\n\n";
	}

}