package blackJack.fxui;

import java.io.FileNotFoundException;

import blackJack.model.BlackJack;

public interface GameManager {

	void save(String filename, BlackJack game) throws FileNotFoundException;

	BlackJack load(String filename) throws FileNotFoundException;
}