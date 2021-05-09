package click.escuela.admin.core.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

//@RestController(value = "/teacher")
public class TeacherController {

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> get() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}

	@PostMapping(value = "")
	public ResponseEntity<?> create() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<?> update() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}
}
