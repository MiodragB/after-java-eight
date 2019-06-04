package com.mbrkljac.afterjavaeight.java11;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;

public class HttpClientDemo {

	public static void main(String[] args) throws Exception {

		getHttpV1();
		getHttpV2();

	}

	private static void getHttpV1() throws java.io.IOException, InterruptedException {
		HttpClient httpClient = HttpClient.newHttpClient();

		HttpRequest httpRequest = HttpRequest.newBuilder(URI.create("https://www.hybrid-it.rs")).GET().build();

		HttpResponse<String> httpResponse = httpClient.send(httpRequest, BodyHandlers.ofString());

		System.out.println("HTTP 1.1: ");
		System.out.println(httpResponse.version());
	}

	private static void getHttpV2() {
		HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_2).build();

		HttpRequest httpRequest = HttpRequest.newBuilder(URI.create("https://www.google.com")).GET().build();

		CompletableFuture<HttpResponse<String>> responseCompletableFuture = httpClient.sendAsync(httpRequest, BodyHandlers.ofString());

		System.out.println("HTTP 2: ");
		responseCompletableFuture.thenAccept(res -> System.out.println(res.version()));
		responseCompletableFuture.join();
	}

}
