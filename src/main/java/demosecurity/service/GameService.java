package demosecurity.service;

import demosecurity.entity.Game;

import java.util.List;
import java.util.Optional;


public interface GameService {

	List<Game> findAll();

	Optional<Game> findGame();

	List<Game> findBattle();

	Game findById(int theId);
	
	void save(Game game);
	
	void deleteById(int theId);
	
}
