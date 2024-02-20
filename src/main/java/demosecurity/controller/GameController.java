package demosecurity.controller;

import demosecurity.entity.Game;
import demosecurity.service.GameService;
import demosecurity.utilities.ThymeleafRenderer;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Controller
@RequestMapping("/games")
public class GameController {

	private GameService gameService;

	@Autowired
	private ThymeleafRenderer timeleafRenderer;

	public GameController(GameService gameService) {
		this.gameService = gameService;
	}
	@GetMapping(path = "/stream-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter streamSseMvc() {
		SseEmitter emitter = new SseEmitter();
		ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
		sseMvcExecutor.execute(() -> {
			try {
				SseEmitter.SseEventBuilder event = SseEmitter.event()
						.name(" datastar-fragment\n")
						.id(UUID.randomUUID() + "\n")
						.data(" merge morph_element\n")
						.data(" selector \n")
						.data(" settle 0\n")
						.data(" fragment <div id=\"contact_1\">Hi</div>\n");
				emitter.send(event);
			} catch (Exception ex) {
				emitter.completeWithError(ex);
			}
		});
		return emitter;
	}

	@GetMapping("/test1")
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/event-stream");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Connection", "keep-alive");

		AsyncContext asyncContext = request.startAsync();
		asyncContext.setTimeout(0); // never time out

		// Prepare the model for the Thymeleaf template
		Map<String, Object> model = new HashMap<>();
		model.put("data", "Your model data here");

		// Render the Thymeleaf template
		String htmlContent = timeleafRenderer.renderThymeleafTemplate("gameFragment", model);

		// Start a new thread to send events to the client
		new Thread(() -> {
			try (PrintWriter writer = response.getWriter()) {
				writer.write("event: datastar-fragment\n");
				writer.write("id: " + Math.random() + "\n");
				writer.write("data: selector \n");
				writer.write("data: merge morph_element\n");
				writer.write("data: settle 0\n");
				writer.write("data: fragment " + htmlContent + "\n\n");
				writer.flush();
				asyncContext.complete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}

	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {

		// create model attribute to bind form data
		Game game = new Game();

		theModel.addAttribute("employee", game);

		return "games/employee-form";
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









