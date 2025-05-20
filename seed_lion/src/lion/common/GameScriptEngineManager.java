package lion.common;

import java.io.Reader;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lion.netty4.message.IGameServer;

public class GameScriptEngineManager {

	private static Logger logger = LoggerFactory.getLogger(GameScriptEngineManager.class);

	private ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
	private ScriptEngine scriptEngine = scriptEngineManager.getEngineByExtension("js");

	static class SingletonHolder {
		static GameScriptEngineManager instance;
	}

	public GameScriptEngineManager(IGameServer gameServer){
		scriptEngine.put("gameServer", gameServer);
	}
	
	public static GameScriptEngineManager getInstance(IGameServer gameServer) {
		if (SingletonHolder.instance == null) {
			SingletonHolder.instance = new GameScriptEngineManager(gameServer);
		}
		return SingletonHolder.instance;
	}

	public void compile(Reader reader) throws ScriptException {
		logger.info("compile reader");
		if (scriptEngine instanceof Compilable) {
			Compilable compilable = (Compilable) scriptEngine;
			CompiledScript compiledScript = compilable.compile(reader);
			compiledScript.eval();
		}
	}

	public void compile(String jsContent) throws ScriptException {
		logger.info("compile String");
		// scriptEngine.eval(jsContent);
		if (scriptEngine instanceof Compilable) {
			Compilable compilable = (Compilable) scriptEngine;
			compilable.compile(jsContent);
		}
	}

	/**
	 * 动态执行
	 * 
	 * @param jsContent
	 * @throws ScriptException
	 */
	public String dynamicExecute(String jsContent) throws ScriptException {
		logger.info("script run:{}", jsContent);
		scriptEngine.eval(jsContent);
		String output = "";
		Object outputObj = scriptEngine.get("output");
		if (outputObj instanceof String) {
			output = (String) outputObj;
		} else if (outputObj instanceof Number) {
			Number outputNumber = (Number) outputObj;
			output = outputNumber.toString();
		}
		return output;
	}

	/**
	 * 调用某个方法
	 * 
	 * @param methodName
	 * @throws NoSuchMethodException
	 * @throws ScriptException
	 */
	public void callMethod(String methodName) throws NoSuchMethodException, ScriptException {
		logger.info("callMethod={}", methodName);
		scriptEngine.put("x", 4);
		if (scriptEngine instanceof Invocable) {
			Invocable invocable = (Invocable) scriptEngine;
			invocable.invokeFunction(methodName);
		}
		String output = "";
		Object outputObj = scriptEngine.get("output");
		if (outputObj instanceof String) {
			output = (String) outputObj;
		} else if (outputObj instanceof Number) {
			Number outputNumber = (Number) outputObj;
			output = outputNumber.toString();
		}
		System.out.println(output);
	}

}
