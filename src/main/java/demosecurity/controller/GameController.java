package demosecurity.controller;

import demosecurity.entity.Game;
import demosecurity.service.GameService;
import demosecurity.utilities.ThymeleafRenderer;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Controller
@RequestMapping("/games")
public class GameController {

	private static final Logger logger = LogManager.getLogger(GameController.class);



	private GameService gameService;

	public GameController(GameService gameService) {
		this.gameService = gameService;
	}

	@Autowired
	private ThymeleafRenderer timeleafRenderer;

	private final ExecutorService executor = Executors.newSingleThreadExecutor();





	@GetMapping("/test1")
	public ResponseBodyEmitter handleSseWithEmitter() {
		ResponseBodyEmitter emitter = new ResponseBodyEmitter();
		CompletableFuture.runAsync(() -> {
			try {
				Map<String, Object> model = new HashMap<>();
				model.put("data", "Your model data here");

				String htmlContent = timeleafRenderer.renderThymeleafTemplate("gameFragment", model);

				emitter.send("event: datastar-fragment\n");
				emitter.send("id: " + Math.random() + "\n");
				emitter.send("data: selector \n");
				emitter.send("data: merge morph_element\n");
				emitter.send("data: settle 0\n");
				emitter.send("data: fragment " + htmlContent + "\n\n", MediaType.TEXT_EVENT_STREAM);
				emitter.complete();
			} catch (IOException e) {
				emitter.completeWithError(e);
			}
		});

		return emitter;
	}
	@GetMapping("/sse")
	public SseEmitter handleSse() {
		SseEmitter emitter = new SseEmitter();
		Map<String, Object> model = new HashMap<>();
		model.put("data", "Your model data here");

//		String htmlContent = timeleafRenderer.renderThymeleafTemplate("gameFragment", model);

//		StringBuilder dataBuilder = new StringBuilder();
//		dataBuilder.append(" selector \n");
//		dataBuilder.append(" merge morph_element\n");
//		dataBuilder.append(" settle 0\n");
//		dataBuilder.append(" fragment <div id=\"contact_1\">Test</div>");

		executor.execute(() -> {
			try {

				// Prepare custom event data
				SseEmitter.SseEventBuilder event = SseEmitter.event()
						.name(" datastar-fragment")
						.id(" " + UUID.randomUUID())
						.data(" selector \n merge morph_element\n settle 0\n fragment <div id=\"contact_1\">Test</div>", MediaType.TEXT_EVENT_STREAM);

				logger.debug("This is a debug message", event);
				logger.debug("\n\n");


				emitter.send(event);



				emitter.complete();
			} catch (IOException e) {
				emitter.completeWithError(e);
			}
		});
		return emitter;
	}

	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {

		// create model attribute to bind form data
		Game game = new Game();

		theModel.addAttribute("employee", game);

		return "games/employee-form";
	}


	@GetMapping("/test2")
	public DeferredResult<String> doGet(HttpServletRequest request, HttpServletResponse response) {
		DeferredResult<String> deferredResult = new DeferredResult<>();

		response.setContentType("text/event-stream");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Connection", "keep-alive");

		AsyncContext asyncContext = request.startAsync();
		asyncContext.setTimeout(0);

		Map<String, Object> model = new HashMap<>();
		model.put("data", "Your model data here");

		String htmlContent = timeleafRenderer.renderThymeleafTemplate("gameFragment", model);

		CompletableFuture.runAsync(() -> {
			try (PrintWriter writer = response.getWriter()) {
				writer.write("event: datastar-fragment\n");
				writer.write("id: " + UUID.randomUUID() + "\n");
				writer.write("data: selector \n");
				writer.write("data: merge morph_element\n");
				writer.write("data: settle 0\n");
				writer.write("data: fragment " + htmlContent + "\n\n");
				writer.flush();
				deferredResult.setResult(null);
			} catch (IOException e) {
				deferredResult.setErrorResult(e);
			}
		});

		return deferredResult;
	}

	@PostMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("employeeId") int theId,
									Model theModel) {

		// get the employee from the service
		Game game = gameService.findById(theId);

		// set employee as a model attribute to pre-populate the form
		theModel.addAttribute("employee", game);

		// send over to our form
		return "employees/employee-form";
	}

	@GetMapping("/battle")
	public String getBattle(Model theModel) {

		// get the employee from the service
		List<Game> games = gameService.findBattle();

		// set employee as a model attribute to pre-populate the form
		theModel.addAttribute("games", games);

		// send over to our form
		return "battle";
	}

	@PostMapping("/save")
	public String saveEmployee(@ModelAttribute("employee") Game game) {

		// save the employee
		gameService.save(game);

		// use a redirect to prevent duplicate submissions
		return "redirect:/employees/list";
	}

	@PostMapping("/delete")
	public String delete(@RequestParam("employeeId") int theId) {

		// delete the employee
		gameService.deleteById(theId);

		// redirect to /employees/list
		return "redirect:/employees/list";

	}
}




/*

	//						.data(" selector \n merge morph_element \n settle 0 \n fragment " + " <div id=\"contact_1\">test</div>", MediaType.TEXT_EVENT_STREAM);

//
//						.data(" selector \n")
//						.data(" merge morph_element\n")
//						.data(" settle 0\n")
//						.data(" fragment " + "<div id=\"contact_1\">test</div>" + "\n\n");


	@GetMapping("/test1")
	public DeferredResult<String> doGet(HttpServletRequest request, HttpServletResponse response) {
		DeferredResult<String> deferredResult = new DeferredResult<>();

		response.setContentType("text/event-stream");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Connection", "keep-alive");

		AsyncContext asyncContext = request.startAsync();
		asyncContext.setTimeout(0); // never time out

		// Prepare the model for the Thymeleaf template
		Map<String, Object> model = new HashMap<>();
		model.put("data", "Your model data here");

		String htmlContent = timeleafRenderer.renderThymeleafTemplate("gameFragment", model);
		// Submit the task to be executed by a Spring-managed thread
		CompletableFuture.runAsync(() -> {
			try (PrintWriter writer = response.getWriter()) {
				writer.write("event: datastar-fragment\n");
				writer.write("id: " + Math.random() + "\n");
				writer.write("data: selector \n");
				writer.write("data: merge morph_element\n");
				writer.write("data: settle 0\n");
				writer.write("data: fragment " + htmlContent + "\n\n");
				writer.flush();
				deferredResult.setResult(null); // Indicate completion
			} catch (IOException e) {
				deferredResult.setErrorResult(e);
			}
		});

		return deferredResult;
	}
 */






