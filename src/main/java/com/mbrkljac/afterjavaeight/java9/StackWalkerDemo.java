package com.mbrkljac.afterjavaeight.java9;

import java.lang.StackWalker.StackFrame;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StackWalkerDemo {

	public static void main(String[] args) {
		SpringApplication.run(StackWalkerDemo.class, args);

		StackWalker walker = StackWalker.getInstance();
		walker.forEach(System.out::println);

		List<StackFrame> filteredFrames = walker.walk(stackFrameStream -> stackFrameStream
			.filter(stackFrame -> stackFrame.toStackTraceElement().getClassName().contains("StackWalkerDemo"))
			.collect(Collectors.toList())
		);

		//reason for having stream confined to lambda (Illegal state exception is thrown if the stream is used outside this lambda),
		// is to ensure we have stable and valid stacktrace at the given point.

		System.out.println("Filtered frames: " + filteredFrames);

	}
}
