package com.mbrkljac.afterjavaeight.java9;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;

public class ProcessHandleInfoDemo {

	public static void main(String[] args) {

		ProcessHandle.allProcesses()
			.sorted(Comparator.comparing(processHandle -> processHandle.info().startInstant().orElse(Instant.MAX)))
			.forEach(ProcessHandleInfoDemo::printInfo);


	}

	private static void printInfo(ProcessHandle handle) {
		System.out.println("PID, Command, Total CPU duration, User");
		System.out.println(
			handle.pid() + "; " + handle.info().command().orElse("/") + "; " + handle.info().totalCpuDuration().orElse(Duration.ZERO) + "; " +
				handle.info().user().orElse("unknown"));

	}

}
