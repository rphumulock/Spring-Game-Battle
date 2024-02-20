package demosecurity.dao;

import demosecurity.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Integer> {

	// that's it ... no need to write any code LOL!

    // add a method to sort by last name
    public List<Game> findAll();

    @Query(value = "SELECT * FROM games ORDER BY RAND() LIMIT 2;", nativeQuery = true)
    List<Game> findBattle();

}
