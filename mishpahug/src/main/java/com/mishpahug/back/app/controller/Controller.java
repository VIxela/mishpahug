package main.java.com.mishpahug.back.app.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE })
@RestController
public class Controller {
	
	
	private static final String SUCCESSFUL_RETRIEVAL = "Success";

	@ApiResponses(value = { @ApiResponse(code = 200, message = SUCCESSFUL_RETRIEVAL, response = String.class) })
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public ResponseEntity<?> getStaticFieldsForProfileForm() {	
		return new ResponseEntity<String> ("all right", HttpStatus.OK);
	}
}
