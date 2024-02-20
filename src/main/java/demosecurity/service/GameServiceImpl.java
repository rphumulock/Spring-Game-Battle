package demosecurity.service;

import demosecurity.dao.GameRepository;
import demosecurity.entity.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

	private GameRepository gameRepository;
	
	@Autowired
	public GameServiceImpl(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	@Override
	public List<Game> findAll() {
		return gameRepository.findAll();
	}

	@Override
	public List<Game> findBattle() {
		return gameRepository.findBattle();
	}

	@Override
	public Optional<Game> findGame() {
		return Optional.empty();
	}

	@Override
	public Game findById(int theId) {
		Optional<Game> result = gameRepository.findById(theId);
		
		Game game = null;
		
		if (result.isPresent()) {
			game = result.get();
		}
		else {
			// we didn't find the employee
			throw new RuntimeException("Did not find employee id - " + theId);
		}
		
		return game;
	}

	@Override
	public void save(Game game) {
		gameRepository.save(game);
	}

	@Override
	public void deleteById(int theId) {
		gameRepository.deleteById(theId);
	}

}






