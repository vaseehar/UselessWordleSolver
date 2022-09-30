package com.wordle.contoller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WordleContoller {
	String randomString;
	Map<Integer, Character> greenMap;
	List<String> possibleWordsList;

	@GetMapping(value = "/getGuess1", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map getGuess1() throws IOException {
		Random random = new Random();
		possibleWordsList = Files.readAllLines(Paths.get("path to dictionary5_nyt.txt"));
		randomString = possibleWordsList.get(random.nextInt(possibleWordsList.size()));
		greenMap = new HashMap<Integer, Character>();
		Map<String, String> wordMap = new HashMap<>();
		//randomString = "sport";
		wordMap.put("Guess", randomString);
		return wordMap;
	}

	@PostMapping(value = "/getNextGuess", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map getNextGuess(@RequestParam(name = "pattern1") String pattern1) throws IOException {
		Map<Integer, Character> blackMap = new HashMap<Integer, Character>();
		Map<Integer, Character> yellowMap = new HashMap<Integer, Character>();
		Random random = new Random();
		for (int i = 0; i < 5; i++) {
			if (pattern1.charAt(i) == 'b') {
				blackMap.put(i, randomString.charAt(i));
				continue;
			}
			if (pattern1.charAt(i) == 'y') {
				yellowMap.put(i, randomString.charAt(i));
				continue;
			}
			if (pattern1.charAt(i) == 'g') {
				greenMap.put(i, randomString.charAt(i));
				continue;
			}
		}
		Iterator<String> iterator = possibleWordsList.iterator();
		labelWhile: while (iterator.hasNext()) {
			String word = iterator.next();
			for (int i = 0; i < 5; i++) {
				if (yellowMap.get(i) != null
						&& (word.charAt(i) == yellowMap.get(i) || !word.contains(String.valueOf(yellowMap.get(i))))) {
					iterator.remove();
					continue labelWhile;
				}
				if (greenMap.get(i) != null && word.charAt(i) != greenMap.get(i)) {
					iterator.remove();
					continue labelWhile;
				}
				if ((!greenMap.containsValue(word.charAt(i)) && !yellowMap.containsValue(word.charAt(i))
						&& blackMap.containsValue(word.charAt(i)))
						|| (greenMap.containsValue(word.charAt(i)) && blackMap.get(i) != null
								&& blackMap.get(i) == word.charAt(i))) {
					iterator.remove();
					continue labelWhile;
				}
			}
		}
		String guess = "";
		boolean isEntireYNotPresent = true;
		boolean isEntireYPresent = true;
		while (isEntireYNotPresent) {
			guess = possibleWordsList.get(random.nextInt(possibleWordsList.size()));
			int removalCount = 0;
			StringBuilder sb = new StringBuilder(guess);
			String trimmedGuess;
			for (Map.Entry<Integer, Character> green : greenMap.entrySet()) {
				sb.deleteCharAt(green.getKey() - removalCount);
				removalCount++;
			}
			trimmedGuess = sb.toString();

			for (Map.Entry<Integer, Character> yellow : yellowMap.entrySet()) {
				if (!trimmedGuess.contains(yellow.getValue().toString())) {
					isEntireYPresent = false;
					break;
				}
			}
			isEntireYNotPresent = !isEntireYPresent;
		}
		randomString = guess;
		Map<String, String> guessMap = new HashMap<>();
		guessMap.put("guess", guess);
		return guessMap;
	}
}
