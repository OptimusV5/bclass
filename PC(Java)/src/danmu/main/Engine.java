package danmu.main;

public class Engine {
	
	private volatile boolean run = false;
	
	private Engine() {}
	
	public static Engine getInstance() {
		return EngineHolder.engine;
	}
	
	public boolean isRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}

	public void start() {
		this.run = true;
		RequestMessage.init();
	}
	
	public void stop() {
		this.run = false;
		RequestMessage.stopRequest();
	}
	
	private static class EngineHolder {
		public static Engine engine = new Engine();
	}
	
	
}
