package com.mbrkljac.afterjavaeight.java9;

public class ProcessHandleDestroyDemo {

	public static void main(String[] args) {

		ProcessHandle lenaHandle = ProcessHandle.allProcesses()
			.filter(ph -> ph.info().command().map(command -> command.contains("Microsoft Word")).orElse(false))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("No Lena here"));

		System.out.println(lenaHandle.info());

		lenaHandle.onExit().thenAccept(hr -> System.out.println("Lena was killed by Java!"));

		boolean shutdown = lenaHandle.destroy();

		lenaHandle.onExit().join();

		System.out.println("Shutdown: " + shutdown);

	}

}
