package com.sysfera.godiet.shell

/*
 * Copyright 2003-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.codehaus.groovy.tools.shell.Shell

import groovy.lang.Binding
import groovy.lang.Closure

import java.io.File
import java.rmi.RemoteException;
import java.util.List

import jline.History
import jline.Terminal

import org.codehaus.groovy.runtime.InvokerHelper
import org.codehaus.groovy.runtime.StackTraceUtils
import org.codehaus.groovy.tools.shell.BufferManager
import org.codehaus.groovy.tools.shell.ExitNotification
import org.codehaus.groovy.tools.shell.IO
import org.codehaus.groovy.tools.shell.InteractiveShellRunner
import org.codehaus.groovy.tools.shell.Interpreter
import org.codehaus.groovy.tools.shell.ParseCode
import org.codehaus.groovy.tools.shell.Parser
import org.codehaus.groovy.tools.shell.Shell
import org.codehaus.groovy.tools.shell.util.MessageSource
import org.codehaus.groovy.tools.shell.util.Preferences
import org.codehaus.groovy.tools.shell.util.XmlCommandRegistrar
import org.fusesource.jansi.Ansi
import org.fusesource.jansi.AnsiConsole
import org.fusesource.jansi.AnsiRenderer

import com.sysfera.godiet.common.services.GoDietService;

/**
 * An interactive shell for evaluating Groovy code from the command-line (aka. groovysh).
 *
 * @version $Id: Groovysh.groovy 20201 2010-06-03 02:23:08Z roshandawrani $
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
class GoDietSh
extends Shell {
	static {
		// Install the system adapters
		AnsiConsole.systemInstall()

		// Register jline ansi detector
	}

	private static final MessageSource messages = new MessageSource(GoDietSh.class)

	//Newbie with Groovy
	private GoDietService godiet;
	private boolean connected = false;

	final BufferManager buffers = new BufferManager()

	final Parser parser

	final Interpreter interp

	final List imports = []

	InteractiveShellRunner runner

	History history

	GoDietSh(final ClassLoader classLoader, final Binding binding, final IO  io, final Closure registrar) {
		super(io)

		assert classLoader
		assert binding
		assert registrar

		parser = new Parser()

		interp = new Interpreter(classLoader, binding)
		registrar.call(this)


		log.debug("Godiet shell started")

	}
	public 	GoDietService getGoDiet() {
		return godiet
	}
	public void setGoDiet(GoDietService d)
	{
		if(d != null ) {
			connected = true;
			this.godiet = d;
		}
	}


	private static Closure createDefaultRegistrar() {
		return { shell ->
			def r = new XmlCommandRegistrar(shell, classLoader)
			r.register(getClass().getResource('/commands.xml'))
		}
	}

	GoDietSh(final ClassLoader classLoader, final Binding binding, final IO io) {
		this(classLoader, binding, io, createDefaultRegistrar())
	}

	GoDietSh(final Binding binding, final IO io) {
		this(Thread.currentThread().contextClassLoader, binding, io)
	}

	GoDietSh(final IO io) {
		this(new Binding(), io)
	}

	GoDietSh() {
		this(new IO())
	}

	//
	// Execution
	//

	/**
	 * Execute a single line, where the line may be a command or Groovy code (complete or incomplete).
	 */
	Object execute(final String line) {
		def result
		try{
			assert line != null

			// Ignore empty lines
			if (line.trim().size() == 0) {
				return null
			}

			maybeRecordInput(line)



			// First try normal command execution
			if (isExecutable(line)) {
				result = executeCommand(line)

				// For commands, only set the last result when its non-null/true
				if (result) {
					lastResult = result
				}

				return result
			}

			// Otherwise treat the line as Groovy
			def current = []
			current += buffers.current()

			// Append the line to the current buffer
			current << line

			// Attempt to parse the current buffer
			def status = parser.parse(imports + current)

			switch (status.code) {
				case ParseCode.COMPLETE:
					log.debug("Evaluating buffer...")

					if (io.verbose) {
						displayBuffer(buffer)
					}

				// Evaluate the current buffer w/imports and dummy statement
					def buff = imports + ['true']+ current

					lastResult = result = interp.evaluate(buff)
					buffers.clearSelected()
					break

				case ParseCode.INCOMPLETE:
				// Save the current buffer so user can build up complex muli-line code blocks
					buffers.updateSelected(current)
					break

				case ParseCode.ERROR:
					throw status.cause

				default:
				// Should never happen
					throw new Error("Invalid parse status: $status.code")
			}
		}catch(RemoteException e)
		{
			io.err.println("Server connection failed. Bye.")
			throw new ExitNotification(-1);
		}
		return result
	}

	protected Object executeCommand(final String line) {
		return super.execute(line)
	}

	/**
	 * Display the given buffer.
	 */
	private void displayBuffer(final List buffer) {
		assert buffer

		buffer.eachWithIndex { line, index ->
			def lineNum = formatLineNumber(index)

			io.out.println(" ${lineNum}@|bold >|@ $line")
		}
	}

	//
	// Prompt
	//





	private String renderPrompt() {
		//def lineNum = formatLineNumber(buffers.current().size())
		def localnode
		if(connected)
		{
			try{
				localnode = godiet.getConfigurationService().serverNodeLabel
			}catch(Exception e)
			{
				io.err.print("Server connection failed. Bye.")
				
				throw new ExitNotification(-1)
			}
		}
		return AnsiRenderer.render("@|bold godiet@${localnode}:>|@ ")
	}

	/**
	 * Format the given number suitable for rendering as a line number column.
	 */
	private String formatLineNumber(final int num) {
		assert num >= 0

		// Make a %03d-like string for the line number
		return num.toString().padLeft(3, '0')
	}

	//
	// User Profile Scripts
	//

	File getUserStateDirectory() {
		def userHome = new File(System.getProperty('user.home'))
		def dir = new File(userHome, '.godiet')
		return dir.canonicalFile
	}

	private void loadUserScript(final String filename) {
		assert filename

		def file = new File(userStateDirectory, filename)

		if (file.exists()) {
			def command = registry['load']

			if (command) {
				log.debug("Loading user-script: $file")

				// Disable the result hook for profile scripts
				def previousHook = resultHook
				resultHook = { result -> /* nothing */}

				try {
					command.load(file.toURI().toURL())
				}
				finally {
					// Restore the result hook
					resultHook = previousHook
				}
			}
			else {
				log.error("Unable to load user-script, missing 'load' command")
			}
		}
	}

	//
	// Recording
	//

	private void maybeRecordInput(final String line) {
		def record = registry['record']

		if (record != null) {
			record.recordInput(line)
		}
	}

	private void maybeRecordResult(final Object result) {
		def record = registry['record']

		if (record != null) {
			record.recordResult(result)
		}
	}

	private void maybeRecordError(Throwable cause) {
		def record = registry['record']

		if (record != null) {
			boolean sanitize = Preferences.sanitizeStackTrace

			if (sanitize) {
				cause = StackTraceUtils.deepSanitize(cause);
			}

			record.recordError(cause)
		}
	}

	//
	// Hooks
	//

	final Closure defaultResultHook = { result ->
		boolean showLastResult = !io.quiet && (io.verbose || Preferences.showLastResult)

		if (showLastResult) {
			// Need to use String.valueOf() here to avoid icky exceptions causes by GString coercion
			//	io.out.println("@|bold ===>|@ ${String.valueOf(result)}")
		}
	}

	Closure resultHook = defaultResultHook

	private void setLastResult(final Object result) {
		if (resultHook == null) {
			throw new IllegalStateException("Result hook is not set")
		}

		resultHook.call((Object)result)

		interp.context['_'] = result

		maybeRecordResult(result)
	}

	private Object getLastResult() {
		return interp.context['_']
	}

	final Closure defaultErrorHook = { Throwable cause ->
		assert cause != null

		//io.err.println("@|bold,red ERROR|@ ${cause.class.name}:")
		io.err.println("@|bold,red ${cause.message}|@")

		maybeRecordError(cause)

		if (log.debug) {
			// If we have debug enabled then skip the fancy bits below
			log.debug(cause)
		}
		//		else {
		//			boolean sanitize = Preferences.sanitizeStackTrace
		//
		//			// Sanitize the stack trace unless we are inverbose mode, or the user has request otherwise
		//			if (!io.verbose && sanitize) {
		//				cause = StackTraceUtils.deepSanitize(cause);
		//			}
		//
		//			def trace = cause.stackTrace
		//
		//			def buff = new StringBuffer()
		//
		//			for (e in trace) {
		//				buff << "        @|bold at|@ ${e.className}.${e.methodName} (@|bold "
		//
		//				buff << (e.nativeMethod ? 'Native Method' :
		//						(e.fileName != null && e.lineNumber != -1 ? "${e.fileName}:${e.lineNumber}" :
		//						(e.fileName != null ? e.fileName : 'Unknown Source')))
		//
		//				buff << '|@)'
		//
		//				io.err.println(buff)
		//
		//				buff.setLength(0) // Reset the buffer
		//
		//				// Stop the trace once we find the root of the evaluated script
		//				if (e.className == Interpreter.SCRIPT_FILENAME && e.methodName == 'run') {
		//					io.err.println('        @|bold ...|@')
		//					break
		//				}
		//			}
		//		}
	}

	Closure errorHook = defaultErrorHook

	private void displayError(final Throwable cause) {
		if (errorHook == null) {
			throw new IllegalStateException("Error hook is not set")
		}

		errorHook.call(cause)
	}

	//
	// Interactive Shell
	//

	int run(final String[] args) {
		String commandLine = null

		if (args != null && args.length > 0) {
			commandLine = args.join(' ')
		}

		return run(commandLine as String)
	}

	int run(final String commandLine) {
		def term = Terminal.terminal

		if (log.debug) {
			log.debug("Terminal ($term)")
			log.debug("    Supported:  $term.supported")
			log.debug("    ECHO:       $term.echo (enabled: $term.echoEnabled)")
			log.debug("    H x W:      $term.terminalHeight x $term.terminalWidth")
			log.debug("    ANSI:       ${term.isANSISupported()}")

			if (term instanceof jline.WindowsTerminal) {
				log.debug("    Direct:     ${term.directConsole}")
			}
		}

		def code
		boolean shell = true
		try {
			loadUserScript('groovysh.profile')

			// if args were passed in, just execute as a command
			// (but cygwin gives an empty string, so ignore that)
			if (commandLine != null && commandLine.trim().size() > 0) {
				// Run the given commands
				def commands = commandLine.split('--')
				commands.each {
					if(it == 'noshell')
					{
						shell =false
					}else{
						execute(it)
					}
				}

			}
			if(shell) {
				loadUserScript('groovysh.rc')

				// Setup the interactive runner
				runner = new InteractiveShellRunner(this, this.&renderPrompt as Closure)

				// Setup the history
				runner.history = history = new History()
				runner.historyFile = new File(userStateDirectory, 'godietsh.history')

				// Setup the error handler
				runner.errorHandler = this.&displayError

				//
				// TODO: See if we want to add any more language specific completions, like for println for example?
				//

				// Display the welcome banner
				if (!io.quiet) {
					def width = term.terminalWidth

					// If we can't tell, or have something bogus then use a reasonable default
					if (width < 1) {
						width = 80
					}

					io.out.println(messages.format('startup_banner.0', InvokerHelper.version, System.properties['java.version']))
					io.out.println(messages['startup_banner.1'])
					io.out.println('-' * (width - 1))
				}

				// And let 'er rip... :-)
				runner.run()
			}

			code = 0
		}
		catch (ExitNotification n) {
			log.debug("Exiting w/code: ${n.code}")

			code = n.code
		}
		catch (Throwable t) {
			io.err.println(messages.format('info.fatal', t))
			t.printStackTrace(io.err)

			code = 1
		}

		assert code != null // This should never happen

		return code
	}



}
